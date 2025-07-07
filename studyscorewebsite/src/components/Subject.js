import React, { useEffect, useState } from "react";
import { Table, Container, Alert, Spinner } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";

const Subject = () => {
  const [subjects, setSubjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadSubjects = async () => {
      try {
        setLoading(true);
        const res = await authApis().get(endpoints['subjects']);
        console.log("Subjects fetched:", res.data); // ✅ Hiển thị dữ liệu trong log
        setSubjects(res.data);
      } catch (err) {
        console.error("Lỗi khi tải môn học:", err); // ✅ In lỗi rõ ràng
        setError("Không thể tải dữ liệu môn học.");
      } finally {
        setLoading(false);
      }
    };

    loadSubjects();
  }, []);

  return (
    <Container>
      <h3 className="text-center mt-3 fw-bold display-5">DANH SÁCH MÔN HỌC</h3>

      {loading && <Spinner animation="border" />}

      {error && <Alert variant="danger">{error}</Alert>}

        

      {!loading && !error && (
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>Hình ảnh</th>
              <th>Mã môn</th>
              <th>Tên môn học</th>
              <th>Số tín chỉ</th>
              <th>Mô tả</th>
              
            </tr>
          </thead>
          <tbody>
            {subjects.map((subject) => (
              <tr key={subject.id}>
                <td>
                    <img src={subject.imageUrl} alt={subject.subjectName} width="100" />
                </td>
                <td>{subject.subjectCode}</td>       
                <td>{subject.subjectName}</td>       
                <td>{subject.credits}</td>  
                <td>{subject.description} </td> 
                 
                       
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </Container>
  );
};

export default Subject;
