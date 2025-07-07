import { useContext, useEffect, useState } from "react";
import { MyUserContext } from "../configs/Contexts";
import { authApis, endpoints } from "../configs/Apis";
import { Table } from "react-bootstrap";

const StudentSubjects = () => {
  const user = useContext(MyUserContext);
  const [subjects, setSubjects] = useState([]);

useEffect(() => {
  const loadSubjects = async () => {
    try {
      let res = await authApis().get(endpoints["student-classSubjects"](user.id));
      console.log("📥 Toàn bộ dữ liệu trả về từ API:", res.data);

      res.data.forEach((s, idx) => {
  console.log(`Phần tử thứ ${idx}:`, s);

  // Bây giờ lấy classSubjectId ra xem
  const cs = s.classSubjectId;

  console.log("classSubjectId:", cs);
  console.log("subjectId:", cs?.subjectId);
  console.log("subjectName:", cs?.subjectId?.subjectName);
  console.log("classId:", cs?.classId);
  console.log("className:", cs?.classId?.name);
});

      setSubjects(res.data);
    } catch (err) {
      console.error("Lỗi khi load danh sách môn học:", err);
    }
  };

  if (user !== null) loadSubjects();
}, [user]);



  return (
    <>
      <h2 className="text-center mt-3 fw-bold display-5">📘 DANH SÁCH MÔN HỌC CỦA SINH VIÊN</h2>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>STT</th>
            <th>Tên môn học</th>
            <th>Lớp học</th>
          </tr>
        </thead>
      <tbody>
  {subjects.map((s, idx) => {
    const cs = s.classSubjectId;
    return (
      <tr key={s.id}>
        <td>{idx + 1}</td>
        <td>{cs?.subjectId?.subjectName || "N/A"}</td>
        <td>{cs?.classId?.name || "N/A"}</td>
      </tr>
    );
  })}
</tbody>




      </Table>
    </>
  );
};

export default StudentSubjects;
