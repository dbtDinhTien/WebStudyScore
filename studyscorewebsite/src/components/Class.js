import React, { useEffect, useState } from "react";
import { Table, Container, Alert, Spinner } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis"; // giả sử endpoint classes đã có

const Class = () => {
  const [classes, setClasses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadClasses = async () => {
      try {
        setLoading(true);
        const res = await authApis().get(endpoints['classes']); // endpoint classes
        console.log("Classes fetched:", res.data);
        setClasses(res.data);
      } catch (err) {
        console.error("Lỗi khi tải lớp học:", err);
        setError("Không thể tải dữ liệu lớp học.");
      } finally {
        setLoading(false);
      }
    };

    loadClasses();
  }, []);

  return (
    <Container>
      <h3 className="text-center mt-3 fw-bold display-5">DANH SÁCH LỚP HỌC</h3>

      {loading && <Spinner animation="border" />}

      {error && <Alert variant="danger">{error}</Alert>}

      {!loading && !error && (
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>Mã lớp</th>
              <th>Tên lớp</th>
              <th>Giáo viên chủ nhiệm</th>
            </tr>
          </thead>
          <tbody>
            {classes.map((cls) => (
              <tr key={cls.id}>
                <td>{cls.code}</td>
                <td>{cls.name}</td>
                <td>{cls.lecturerId ? `${cls.lecturerId.firstName} ${cls.lecturerId.lastName}` : "Chưa có"}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </Container>
  );
};

export default Class;
