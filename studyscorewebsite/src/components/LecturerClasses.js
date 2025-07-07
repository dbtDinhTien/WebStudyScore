import { useContext, useEffect, useState } from "react";
import { Table, Form, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import { MyUserContext } from "../configs/Contexts";
import { authApis, endpoints } from "../configs/Apis";

const LecturerClasses = () => {
  const user = useContext(MyUserContext);
  const [classes, setClasses] = useState([]);
  const [keyword, setKeyword] = useState("");

  useEffect(() => {
    const loadClasses = async () => {
      if (user && user.id) {
        try {
          const res = await authApis().get(endpoints['lecturer-classes'](user.id));
          console.log("Data lớp học phụ trách:", res.data);
          setClasses(res.data);
        } catch (err) {
          console.error("Lỗi khi tải lớp học phụ trách:", err);
        }
      }
    };

    loadClasses();
  }, [user]);

  const filteredClasses = classes.filter(c =>
    c.classId?.name.toLowerCase().includes(keyword.toLowerCase())
  );

  return (
    <>
      <h2 className="text-center mt-3 fw-bold display-5">📚 LỚP HỌC PHỤ TRÁCH</h2>

      <Form.Control
        type="text"
        placeholder="Tìm kiếm theo tên lớp..."
        className="mb-3"
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
      />

      <Table striped bordered hover>
        <thead>
          <tr>
            <th>STT</th>
            <th>Lớp</th>
            <th>Môn học</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {filteredClasses.map((c, index) => (
            <tr key={c.id}>
              <td>{index + 1}</td>
              <td>{c.classId?.name}</td>
              <td>{c.subjectId?.subjectName}</td>
              <td>
                <Link to={`/lecturer/students/${c.id}`}>
                  <Button variant="info" size="sm">👥 Xem sinh viên</Button>
                </Link>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </>
  );
};

export default LecturerClasses;
