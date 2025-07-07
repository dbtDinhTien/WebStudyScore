import React, { useEffect, useState } from "react";
import { Container, Table, Spinner, Alert } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";

const ClassSubjects = () => {
  const [data, setData] = useState([]);  // Mảng đối tượng { class, subject, lecturer }
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
  const fetchData = async () => {
    try {
      setLoading(true);

      // Lấy danh sách classSubjects (môn học theo lớp, có cả lớp, môn, giảng viên)
      const resClassSubjects = await authApis().get(endpoints.classSubjects);
      const classSubjects = resClassSubjects.data;

      // Map dữ liệu sang mảng để render
      const combinedData = classSubjects.map(cs => ({
        className: cs.classId.name,
        subjectName: cs.subjectId ? cs.subjectId.subjectName : "Không xác định môn học",
        lecturerName: cs.lecturerId
          ? `${cs.lecturerId.firstName} ${cs.lecturerId.lastName}`
          : "Chưa có giảng viên",
      }));

      setData(combinedData);
    } catch (err) {
      setError("Lỗi khi tải dữ liệu");
    } finally {
      setLoading(false);
    }
  };

  fetchData();
}, []);



  if (loading) return <Spinner animation="border" />;

  if (error) return <Alert variant="danger">{error}</Alert>;

  return (
    <Container>
      <h3 className="text-center mt-3 fw-bold display-5">DANH SÁCH MÔN HỌC THEO LỚP HỌC</h3>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Lớp</th>
            <th>Môn học</th>
            <th>Giảng viên chủ nhiệm</th>
          </tr>
        </thead>
        <tbody>
          {data.length > 0 ? (
            data.map((item, idx) => (
              <tr key={idx}>
                <td>{item.className}</td>
                <td>{item.subjectName}</td>
                <td>{item.lecturerName}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={3} className="text-center">
                Không có dữ liệu
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </Container>
  );
};

export default ClassSubjects;
