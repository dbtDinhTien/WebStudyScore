import React, { useState } from "react";
import { authApis, endpoints } from "../configs/Apis";
import { Button, Form, Table } from "react-bootstrap";

const StudentScoreSearch = () => {
  const [keyword, setKeyword] = useState("");
  const [students, setStudents] = useState([]);
  const [selectedStudent, setSelectedStudent] = useState(null);
  const [scores, setScores] = useState([]);

  const searchStudents = async () => {
    try {
      let res = await authApis().get(endpoints["search-students"](keyword));
      setStudents(res.data);
    } catch (err) {
      console.error("Lỗi tìm sinh viên", err);
    }
  };

  const getStudentScores = async (student) => {
    try {
      setSelectedStudent(student);
      let res = await authApis().get(endpoints["student-classSubjects"](student.id));
      let data = res.data;
        console.log("Data student-classSubjects:", data);  // in ra data lấy được

      let scores = [];

      for (let item of data) {
        const scoreRes = await authApis().get(
          endpoints["score-by-stuClassSubjectId"](item.id)
        );

      scores.push({
  subjectName: item.classSubjectId.subjectId.subjectName,
  ...scoreRes.data,
});

      }

      setScores(scores);
    } catch (err) {
      console.error("Lỗi lấy điểm sinh viên", err);
    }
  };

  return (
    <div className="container mt-3">
      <h3 className="fw-bold">TÌM KIẾM SINH VIÊN ĐỂ TRA CỨU ĐIỂM</h3>
      <Form.Group>
        <Form.Control
          type="text"
          placeholder="Nhập tên hoặc MSSV"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
      </Form.Group>
      <Button className="mt-2" onClick={searchStudents}>
        Tìm
      </Button>

      {students.length > 0 && (
        <Table striped bordered hover className="mt-3">
          <thead>
            <tr>
              <th>Họ tên</th>
              <th>Email</th>
              <th>Xem điểm</th>
            </tr>
          </thead>
          <tbody>
            {students.map((s) => (
              <tr key={s.id}>
                <td>{s.firstName}{s.lastName} </td>
                <td>{s.email}</td>
                <td>
                  <Button onClick={() => getStudentScores(s)}>Xem điểm</Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}

      {selectedStudent && scores.length > 0 && (
        <>
          <h4 className="mt-4 fw-bold">CHI TIẾT ĐIỂM CỦA SINH VIÊN: {selectedStudent.firstName} {selectedStudent.lastName} </h4>
          <Table striped bordered hover className="mt-2">
            <thead>
              <tr>
               <th>Môn học</th>
            <th>Điểm giữa kỳ</th>
            <th>Điểm cuối kỳ</th>
            <th>Điểm cộng 1</th>
            <th>Điểm cộng 2</th>
            <th>Điểm cộng 3</th>
              </tr>
            </thead>
            <tbody>
              {scores.map((s, idx) => (
                <tr key={idx}>
                <td>{s.subjectName}</td>
                <td>{s.midtermScore}</td>
                <td>{s.finalScore}</td>
                <td>{s.extraScore1}</td>
                <td>{s.extraScore2}</td>
                <td>{s.extraScore3}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </>
      )}
    </div>
  );
};

export default StudentScoreSearch;
