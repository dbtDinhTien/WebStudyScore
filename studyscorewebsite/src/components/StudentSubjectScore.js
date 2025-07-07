import { useContext, useEffect, useState } from "react";
import { MyUserContext } from "../configs/Contexts";
import { authApis, endpoints } from "../configs/Apis";
import { Table, Spinner } from "react-bootstrap";

const StudentSubjectsScores = () => {
  const user = useContext(MyUserContext);
  const [subjects, setSubjects] = useState([]);
  const [scoresMap, setScoresMap] = useState({});
  const [loadingScores, setLoadingScores] = useState(false);

  useEffect(() => {
    const loadSubjects = async () => {
      try {
        let res = await authApis().get(endpoints["student-classSubjects"](user.id));
        setSubjects(res.data);
      } catch (err) {
        console.error("❌ Lỗi load subjects:", err);
      }
    };
    if (user) loadSubjects();
  }, [user]);

  useEffect(() => {
    const loadScores = async () => {
      setLoadingScores(true);
      try {
        let promises = subjects.map(s =>
          authApis().get(endpoints["score-by-stuClassSubjectId"](s.id))
        );
        let results = await Promise.all(promises);

        let newScoresMap = {};
        results.forEach((res, idx) => {
          if (res.data && Object.keys(res.data).length > 0) {
            newScoresMap[subjects[idx].id] = res.data;
          } else {
            newScoresMap[subjects[idx].id] = null;
          }
        });
        setScoresMap(newScoresMap);
      } catch (err) {
        console.error("❌ Lỗi load scores:", err);
      }
      setLoadingScores(false);
    };

    if (subjects.length > 0) loadScores();
  }, [subjects]);

  return (
    <>
      <h2 className="text-center mt-3 fw-bold display-5">📘 DANH SÁCH MÔN HỌC VÀ ĐIỂM SINH VIÊN</h2>
      {loadingScores && <Spinner animation="border" />}
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>STT</th>
            <th>Tên môn học</th>
            <th>Lớp học</th>
            <th>Điểm</th>
          </tr>
        </thead>
        <tbody>
          {subjects.map((s, idx) => {
            const score = scoresMap[s.id];
            return (
              <tr key={s.id}>
                <td>{idx + 1}</td>
                <td>{s.classSubjectId?.subjectId?.subjectName || "N/A"}</td>
                <td>{s.classSubjectId?.classId?.name || "N/A"}</td>
                <td>
                  <div>Giữa kỳ: {score?.midtermScore ?? "Không có"}</div>
                  <div>Cuối kỳ: {score?.finalScore ?? "Không có"}</div>
                  <div>Điểm cộng 1: {score?.extraScore1 ?? "Không có"}</div>
                  <div>Điểm cộng 2: {score?.extraScore2 ?? "Không có"}</div>
                  <div>Điểm cộng 3: {score?.extraScore3 ?? "Không có"}</div>
                </td>
              </tr>
            );
          })}
        </tbody>
      </Table>
    </>
  );
};

export default StudentSubjectsScores;
