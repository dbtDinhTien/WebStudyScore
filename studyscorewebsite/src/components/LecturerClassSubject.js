import { useContext, useEffect, useState } from "react";
import { Table, Form, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import { MyUserContext } from "../configs/Contexts";
import { authApis, endpoints } from "../configs/Apis";

const LecturerClassSubjects = () => {
  const user = useContext(MyUserContext);
  const [classSubjects, setClassSubjects] = useState([]);
  const [keyword, setKeyword] = useState("");

  useEffect(() => {
    const loadClassSubjects = async () => {
      if (user && user.id) {
        try {
          const res = await authApis().get(endpoints['lecturer-classSubjects'](user.id));
          console.log("Dữ liệu lớp môn học giảng viên dạy:", res.data);
          setClassSubjects(res.data);
        } catch (err) {
          console.error("Lỗi khi tải lớp môn học:", err);
        }
      }
    };

    loadClassSubjects();
  }, [user]);

  const filteredClassSubjects = classSubjects.filter(cs =>
    cs.classId?.name.toLowerCase().includes(keyword.toLowerCase()) ||
    cs.subjectId?.subjectName.toLowerCase().includes(keyword.toLowerCase())
  );

  return (
    <>
      <h2 className="text-center mt-3 fw-bold display-5">🎓 Lớp - Môn học phụ trách</h2>

      <Form.Control
        type="text"
        placeholder="Tìm kiếm theo tên lớp hoặc môn học..."
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
          {filteredClassSubjects.map((cs, index) => (
            <tr key={cs.id}>
              <td>{index + 1}</td>
              <td>{cs.classId?.name}</td>
              <td>{cs.subjectId?.subjectName}</td>
              <td>
                <Link to={`/lecturer/students/${cs.id}`}>
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

export default LecturerClassSubjects;
