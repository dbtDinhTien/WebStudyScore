import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";
import { Table } from "react-bootstrap";

const LecturerStudentList = () => {
  const { classSubjectId } = useParams();
  const [students, setStudents] = useState([]);

  useEffect(() => {
    const loadStudents = async () => {
      try {
        console.log(`🔎 Đang tải danh sách sinh viên với classSubjectId: ${classSubjectId}`);
        const res = await authApis().get(endpoints['students-by-classSubjectId'](classSubjectId));
        console.log("✅ Dữ liệu sinh viên nhận được:", res.data);
        setStudents(res.data);
      } catch (err) {
        console.error("❌ Lỗi khi tải danh sách sinh viên:", err);
        if (err.response) {
          console.error("📦 Response data:", err.response.data);
          console.error("🔢 Status code:", err.response.status);
          console.error("📄 Headers:", err.response.headers);
        } else if (err.request) {
          console.error("📭 Request được gửi nhưng không có phản hồi:", err.request);
        } else {
          console.error("⚠️ Lỗi khác:", err.message);
        }
      }
    };

    loadStudents();
  }, [classSubjectId]);

  return (
    <>
      <h2>👥 Danh sách sinh viên</h2>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>STT</th>
            <th>Mã số sinh viên</th>
            <th>Họ và tên</th>
            <th>Email</th>
          </tr>
        </thead>
        <tbody>
          {students.map((s, idx) => (
            <tr key={s.id}>
              <td>{idx + 1}</td>
              <td>{s.studentId?.studentCode}</td>
              <td>{s.studentId?.firstName} {s.studentId?.lastName}</td>
              <td>{s.studentId?.email}</td>
            </tr>
          ))}
        </tbody>
      </Table>
    </>
  );
};

export default LecturerStudentList;
