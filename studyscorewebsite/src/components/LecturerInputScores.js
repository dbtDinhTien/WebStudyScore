import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { authApis, endpoints,BASE_URL } from "../configs/Apis";
import { Table, Button, Form } from "react-bootstrap";
import axios from "axios";
import cookie from "react-cookies";


const LecturerInputScores = () => {
   console.log("🌐 API URL:", process.env.REACT_APP_API_URL);
  const { classSubjectId } = useParams(); // lấy classSubjectId từ route
  const [students, setStudents] = useState([]);
  const [scores, setScores] = useState({});
  const [isClassLocked, setIsClassLocked] = useState(false);
  const [classSubjectName, setClassSubjectName] = useState("");

  

  useEffect(() => {
    // Lấy trạng thái khóa lớp
    const fetchClassLockStatus = async () => {
      try {
        const res = await authApis().get(`/classSubjects/${classSubjectId}`);
        setIsClassLocked(res.data.isLocked);
          // Khai báo biến ở đây:
    const subjectName = res.data.subjectId?.subjectName || "";

 setClassSubjectName(`${subjectName}`);      } catch (err) {
        console.error("Lỗi khi lấy trạng thái khóa lớp:", err);
        setIsClassLocked(false);
      }
    };

    fetchClassLockStatus();
  }, [classSubjectId]);


 

function downloadStudentList(classSubjectId) {
  const token = cookie.load("token");
  if (!token) {
    alert("Bạn chưa đăng nhập hoặc token không hợp lệ");
    return;
  }

  const apiUrl = process.env.REACT_APP_API_URL;
  if (!apiUrl) {
    alert("API URL chưa được cấu hình đúng!");
    return;
  }

  const url = `${apiUrl}/stuClassSubjects/export/${classSubjectId}`;

  axios
    .get(url, {
      responseType: "blob", // quan trọng để nhận file
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    .then((res) => {
      // Tạo link ẩn để tải file
      const blob = new Blob([res.data], {
        type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      });
      const downloadUrl = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = downloadUrl;
      // Tên file bạn muốn đặt, có thể lấy từ header Content-Disposition nếu server trả về
      link.download = `students_class_${classSubjectId}.xlsx`;
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(downloadUrl);
    })
    .catch((err) => {
      if (err.response && err.response.status === 401) {
        alert("Bạn không có quyền tải file này. Vui lòng đăng nhập lại.");
      } else {
        alert("Lỗi khi tải file danh sách sinh viên.");
      }
    });
}


  useEffect(() => {
   const fetchStudentsAndScores = async () => {
  try {
    // Lấy danh sách sinh viên
    const resStudents = await authApis().get(endpoints["students-by-classSubjectId"](classSubjectId));
    setStudents(resStudents.data);

    // Lấy danh sách điểm
    const resScores = await authApis().get(`/scores/classSubject/${classSubjectId}`);

    const scoreMap = {};
    for (const s of resScores.data) {
      const studentId = s.studentClassSubjectId?.studentId?.id; // lấy đúng studentId
      if (!studentId) continue;

      scoreMap[studentId] = {
        scoreId: s.id,
        midterm: s.midtermScore,
        final: s.finalScore,
        bonus1: s.extraScore1,
        bonus2: s.extraScore2,
        bonus3: s.extraScore3,
        lockStatus: s.lockStatus,
        studentId,
        studentClassSubjectId: s.studentClassSubjectId?.id, // thêm id này
        classSubjectId,
      };
    }

    // Gộp điểm draft từ localStorage như cũ
    const draftKey = `draftScores-${classSubjectId}`;
    const draft = JSON.parse(localStorage.getItem(draftKey)) || {};
    for (const studentId in draft) {
      scoreMap[studentId] = {
        ...scoreMap[studentId],
        ...draft[studentId],
      };
    }

    setScores(scoreMap);
  } catch (err) {
    console.error("Lỗi khi load sinh viên hoặc điểm:", err);
  }


    };

    fetchStudentsAndScores();
  }, [classSubjectId]);

  const handleChange = (studentId, field, value) => {
    let numericValue = parseFloat(value);
    if (scores[studentId]?.lockStatus === "LOCKED" || isClassLocked) {
      // Nếu lớp hoặc điểm đã khóa thì không cho thay đổi
      return;
    }

    if (value === "" || isNaN(numericValue)) {
      // cho phép xóa input
      setScores((prev) => ({
        ...prev,
        [studentId]: {
          ...prev[studentId],
          [field]: "",
        },
      }));
      return;
    }

    // kiểm tra giới hạn cho midterm và final (0-10)
    if ((field === "midterm" || field === "final") && (numericValue < 0 || numericValue > 10)) {
      return;
    }

    setScores((prev) => ({
      ...prev,
      [studentId]: {
        ...prev[studentId],
        studentId,
        classSubjectId,
        [field]: numericValue,
      },
    }));
  };

  const handleSave = async (studentId) => {
    const studentScores = scores[studentId];
    if (!studentScores) return;

    try {
      const payload = {
        midtermScore: Number(studentScores.midterm) || 0,
        finalScore: Number(studentScores.final) || 0,
        extraScore1: Number(studentScores.bonus1 || 0),
        extraScore2: Number(studentScores.bonus2 || 0),
        extraScore3: Number(studentScores.bonus3 || 0),
        studentClassSubjectId: studentScores.studentClassSubjectId, // Trường này nên là id số, không phải object
      };

  if (studentScores.scoreId) {
  // Cập nhật điểm nếu đã có scoreId, gọi POST thay vì PUT
  console.log(`POST to /scores/${studentScores.scoreId}`);
  await authApis().patch(endpoints['update-score'](studentScores.scoreId), payload);
} else {
  // Thêm mới điểm
  console.log("POST to", endpoints["save-score"]);
  await authApis().post(endpoints["save-score"], payload);

      const res = await authApis().post(endpoints["save-score"], payload);
        // Nếu backend trả về id mới thì cập nhật lại scoreId trong state
        if (res.data?.id) {
          setScores(prev => ({
            ...prev,
            [studentId]: {
              ...prev[studentId],
              scoreId: res.data.id,
            },
          }));
        }
      }

      // Cập nhật lại điểm trong state và bỏ cờ nháp
      setScores((prev) => ({
        ...prev,
        [studentId]: {
          ...prev[studentId],
          ...payload,
          scoreId: studentScores.scoreId || null,
          isDraft: false,
        },
      }));

      // Xóa draft trong localStorage
      const draftKey = `draftScores-${classSubjectId}`;
      const draft = JSON.parse(localStorage.getItem(draftKey)) || {};
      delete draft[studentId];
      localStorage.setItem(draftKey, JSON.stringify(draft));

      alert("✅ Lưu điểm thành công!");
    } catch (err) {
      console.error("❌ Lỗi khi lưu điểm:", err);
      alert("❌ Có lỗi khi lưu điểm.");
    }
  };

  const handleDraft = (studentId) => {
    const draftKey = `draftScores-${classSubjectId}`;
    const draft = JSON.parse(localStorage.getItem(draftKey)) || {};

    draft[studentId] = {
      ...scores[studentId],
      studentId,
      classSubjectId,
      isDraft: true,
    };

    localStorage.setItem(draftKey, JSON.stringify(draft));

    setScores(prev => ({
      ...prev,
      [studentId]: {
        ...prev[studentId],
        isDraft: true,
      }
    }));

    alert("📥 Đã lưu nháp cho sinh viên.");
  };

 const handleUploadExcel = async (e) => {
  const file = e.target.files[0];
  if (!file) return;

  const formData = new FormData();
  formData.append("file", file);

  try {
    const apiUrl = process.env.REACT_APP_API_URL;
    if (!apiUrl) throw new Error("API URL chưa cấu hình");

    const res = await axios.post(
      `${apiUrl}/scores/import/${classSubjectId}`,
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      }
    );
    console.log("Import thành công:", res.data);
    alert("Nhập điểm thành công");
  } catch (error) {
    console.error("❌ Lỗi khi nhập điểm từ Excel:");

    // Nếu là lỗi Axios, có response từ server
    if (error.response) {
      console.error("Status:", error.response.status);
      console.error("Data:", error.response.data);
      console.error("Headers:", error.response.headers);
    } else if (error.request) {
      // Request đã gửi nhưng không nhận được phản hồi
      console.error("No response received:", error.request);
    } else {
      // Lỗi khác
      console.error("Error message:", error.message);
    }

    // Toàn bộ lỗi
    console.error(error);
    
    alert("Lỗi khi nhập điểm từ Excel. Xem console để biết chi tiết.");
  }
};


  return (
    <>
      {/* <h2>📝 Nhập điểm lớp {classSubjectId}</h2> */}
<h2 className="text-center">📝 NHẬP ĐIỂM CHO LỚP: {classSubjectName}</h2>

      <Table striped bordered hover responsive>
        <thead>
          <tr>
            <th>STT</th>
            <th>Sinh viên</th>
            <th>Giữa kỳ</th>
            <th>Cuối kỳ</th>
            <th>Điểm cộng 1</th>
            <th>Điểm cộng 2</th>
            <th>Điểm cộng 3</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {students.map((s, idx) => {
            // Lấy studentId đúng chuẩn
            const studentId = typeof s.studentId === "object" && s.studentId !== null ? s.studentId.id : s.studentId;
            const isDraft = scores[studentId]?.isDraft;

            return (
              <tr key={studentId} style={{ position: "relative" }}>
                <td>{idx + 1}</td>
                <td>{s.studentId?.firstName} {s.studentId?.lastName}</td>

                {["midterm", "final", "bonus1", "bonus2", "bonus3"].map((f) => (
                  <td key={f}>
                    <Form.Control
                      type="number"
                      min={f === "midterm" || f === "final" ? 0 : undefined}
                      max={f === "midterm" || f === "final" ? 10 : undefined}
                      value={scores[studentId]?.[f] ?? ""}
                      onChange={(e) => handleChange(studentId, f, e.target.value)}
                      style={{ color: isDraft ? "red" : "inherit" }}
                      disabled={isClassLocked || scores[studentId]?.lockStatus === "LOCKED"}
                    />
                  </td>
                ))}

                <td>
                  <Button
                    variant="warning"
                    onClick={() => handleDraft(studentId)}
                    disabled={isClassLocked || scores[studentId]?.lockStatus === "LOCKED"}
                  >
                    📝 Lưu nháp
                  </Button>{" "}
                  <Button
                    variant="success"
                    onClick={() => handleSave(studentId)}
                    disabled={isClassLocked || scores[studentId]?.lockStatus === "LOCKED"}
                  >
                    💾 Lưu
                  </Button>
                  {isClassLocked && (
                    <div style={{ color: "red", fontWeight: "bold", marginTop: "10px" }}>
                      ⚠️ Lớp này đã khóa điểm, bạn không thể thay đổi điểm nữa.
                    </div>
                  )}
                </td>

                {/* Chữ NHÁP nổi bên ngoài cuối dòng */}
                {isDraft && (
                  <td
                    style={{
                      position: "absolute",
                      right: "-80px",
                      top: "50%",
                      transform: "translateY(-50%)",
                      color: "red",
                      fontWeight: "bold",
                      whiteSpace: "nowrap",
                      pointerEvents: "none",
                    }}
                  >
                    NHÁP
                  </td>
                )}
              </tr>
            );
          })}
        </tbody>

     

      </Table>
 

<div className="d-flex flex-column flex-md-row align-items-center gap-3 mb-4">
  <Button
  variant="info"
  className="mb-2 mb-md-0"
  onClick={() => downloadStudentList(classSubjectId)}
  style={{ minWidth: "220px" }}
>
  📥 Tải danh sách sinh viên
</Button>


  <Form.Group controlId="formFile" className="mb-0" style={{ minWidth: "250px" }}>
    <Form.Label className="mb-1">📤 Tải file Excel đã nhập điểm</Form.Label>
    <Form.Control
      type="file"
      accept=".xlsx"
      onChange={(e) => handleUploadExcel(e)}
      disabled={isClassLocked}
    />
  </Form.Group>
</div>
<div className="d-flex flex-column flex-md-row align-items-center gap-3">
  <Button
  variant="success"
  onClick={() => {
    const apiUrl = process.env.REACT_APP_API_URL;
    if (!apiUrl) {
      alert("API URL chưa được cấu hình đúng!");
      return;
    }
    const url = `${apiUrl}/scores/export-excel/${classSubjectId}`;
    console.log("Export Excel URL:", url);
    window.open(url, "_blank");
  }}
  disabled={students.length === 0}
>
  📊 Xuất điểm (Excel)
</Button>

<Button
  variant="danger"
  onClick={() => {
    const apiUrl = process.env.REACT_APP_API_URL;
    if (!apiUrl) {
      alert("API URL chưa được cấu hình đúng!");
      return;
    }
    const url = `${apiUrl}/scores/export-pdf/${classSubjectId}`;
    console.log("Export PDF URL:", url);
    window.open(url, "_blank");
  }}
  disabled={students.length === 0}
>
  🧾 Xuất điểm (PDF)
</Button>

</div>




    </>
  );
};

export default LecturerInputScores;
