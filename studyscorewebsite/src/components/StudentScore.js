import React, { useEffect, useState } from "react";
import { Container, Table, Spinner, Alert, Button } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";

const StudentScores = () => {
  const [scores, setScores] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState(null);

  useEffect(() => {
    const fetchScores = async () => {
      try {
        let res = await authApis().get(endpoints.scores);
        setScores(res.data);
      } catch (ex) {
        console.error(ex);
        console.error("Chi tiết lỗi:", ex.response?.data || ex.message);
  console.error("Mã lỗi:", ex.response?.status);
        setErr("Lỗi khi tải điểm số sinh viên!");
      } finally {
        setLoading(false);
      }
    };

    fetchScores();
  }, []);

  if (loading) return <Spinner animation="border" />;
  if (err) return <Alert variant="danger">{err}</Alert>;

  return (
    <Container>
      <h3 className="my-4">Danh sách điểm số sinh viên</h3>
      <Table bordered striped hover responsive>
        <thead>
          <tr>
            <th>STT</th>
            <th>Sinh viên</th>
            <th>Môn học</th>
            <th>Điểm giữa kỳ</th>
            <th>Điểm cuối kỳ</th>
            <th>Điểm cộng 1</th>
            <th>Điểm cộng 2</th>
            <th>Điểm cộng 3</th>
            <th>Trạng thái khóa</th>
            
          </tr>
        </thead>
        <tbody>
          {scores.length === 0 ? (
            <tr>
              <td colSpan="10" className="text-center">Không có dữ liệu</td>
            </tr>
          ) : (
            scores.map((s, idx) => (
              <tr key={s.id}>
                <td>{idx + 1}</td>
                <td>{s.studentId?.lastName} {s.studentId?.firstName}</td>
                <td>{s.classSubjectId?.subjectId?.subjectName}</td>
                <td>{s.midtermScore}</td>
                <td>{s.finalScore}</td>
                <td>{s.extraScore1}</td>
                <td>{s.extraScore2}</td>
                <td>{s.extraScore3}</td>
                <td>
                {s.lockStatus === "locked" ? "🔒 Đã khóa" : "🔓 Chưa khóa"}
                </td>

                
              </tr>
            ))
          )}
        </tbody>
      </Table>
    </Container>
  );
};

export default StudentScores;
