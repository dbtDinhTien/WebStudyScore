import { useEffect, useState, useContext } from "react";
import { authApis, endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/Contexts";
import { Link } from "react-router-dom";
import { Table, Button } from "react-bootstrap";

const LecturerSelectClassSubject = () => {
  const [classSubjects, setClassSubjects] = useState([]);
  const [lockedClassIds, setLockedClassIds] = useState([]);
  const user = useContext(MyUserContext);

  useEffect(() => {
    const fetchClassSubjects = async () => {
      try {
        if (!user?.id) {
          console.log("⚠️ Chưa có user.id, không gọi API.");
          return;
        }

        console.log("🔍 Đang lấy lớp học phụ trách cho giảng viên ID:", user.id);
        const res = await authApis().get(endpoints["lecturer-classes"](user.id));
        const data = res.data;
        console.log("✅ Dữ liệu lớp học phụ trách:", data);
        setClassSubjects(data);

        // Kiểm tra lớp đã khóa điểm
        const lockedChecks = await Promise.all(
          data.map(async (c) => {
            try {
              const scoreRes = await authApis().get(endpoints["scores-by-classSubjectId"](c.id));
              const scores = scoreRes.data;
              console.log(`📊 Lớp ${c.id} có ${scores.length} điểm:`, scores);
              const allLocked = scores.length > 0 && scores.every(s => s.lockStatus === "locked");
              return allLocked ? Number(c.id) : null;
            } catch (err) {
              console.error(`❌ Lỗi khi kiểm tra điểm lớp ${c.id}:`, err);
              return null;
            }
          })
        );

        const lockedIds = lockedChecks.filter(id => id !== null);
        console.log("🔒 Lớp đã khóa (ID dạng number):", lockedIds);
        setLockedClassIds(lockedIds);
      } catch (err) {
        console.error("❌ Lỗi khi load lớp phụ trách:", err);
        if (err.response) console.error("↪️ Chi tiết lỗi:", err.response.data);
      }
    };

    fetchClassSubjects();
  }, [user?.id]);

  const handleLockScores = async (classSubjectId) => {
    if (!window.confirm("Bạn có chắc muốn khóa điểm lớp này? Sau khi khóa sẽ không thể sửa lại!")) return;

    try {
      await authApis().put(endpoints["lock-scores"](classSubjectId));
      alert("🔒 Đã khóa điểm thành công!");
      setLockedClassIds(prev => {
        const newIds = [...prev, Number(classSubjectId)];
        console.log("🆕 lockedClassIds sau khi thêm:", newIds);
        return [...new Set(newIds)];
      });
    } catch (err) {
      console.error("❌ Lỗi khi khóa điểm:", err);
      alert("❌ Không thể khóa điểm. Vui lòng thử lại.");
    }
  };

  return (
    <>
      <h2 className=" mt-3 fw-bold display-9">HÃY CHỌN LỚP HỌC ĐỂ NHẬP ĐIỂM: </h2>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>STT</th>
            <th>Tên lớp</th>
            <th>Môn học</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {classSubjects.map((c, idx) => {
            const currentId = Number(c.id);
            const isLocked = lockedClassIds.includes(currentId);

            console.log(`🔍 classSubjectId = ${c.id}, typeof = ${typeof c.id}, isLocked = ${isLocked}`);

            return (
              <tr key={c.id}>
                <td>{idx + 1}</td>
                <td>{c.classId?.name}</td>
                <td>{c.subjectId?.subjectName}</td>
                <td>
                  {isLocked ? (
                    <Button variant="success" disabled>
                      ✅ Đã khóa
                    </Button>
                  ) : (
                    <>
                      <Link to={`/lecturer/input-scores/${c.id}`}>
                        <Button variant="primary" className="me-2">📝 Nhập điểm</Button>
                      </Link>
                      <Button variant="danger" onClick={() => handleLockScores(c.id)}>
                        🔒 Khóa điểm
                      </Button>
                    </>
                  )}
                </td>
              </tr>
            );
          })}
        </tbody>
      </Table>
    </>
  );
};

export default LecturerSelectClassSubject;
