import React, { useEffect, useState } from "react";
import { authApis, endpoints } from "./APIs"; // giả sử file APIs.js nằm cùng thư mục

function ScoreStatistics({ classSubjectId }) {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!classSubjectId) return;

    setLoading(true);
    setError(null);

    authApis()
      .get(endpoints['score-statistics'](classSubjectId))
      .then((res) => {
        setStats(res.data);
      })
      .catch((err) => {
        if (err.response && err.response.status === 204) {
          setStats(null);
        } else {
          setError("Lấy dữ liệu thống kê điểm thất bại");
        }
      })
      .finally(() => {
        setLoading(false);
      });
  }, [classSubjectId]);

  if (loading) return <p>Đang tải thống kê điểm...</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;
  if (!stats) return <p>Chưa có dữ liệu thống kê cho lớp môn học này.</p>;

  return (
    <div>
      <h3>Thống kê điểm lớp môn học ID: {classSubjectId}</h3>
      <ul>
        <li>Điểm giữa kì trung bình: {stats.averageMidtermScore.toFixed(2)}</li>
        <li>Điểm cuối kì trung bình: {stats.averageFinalScore.toFixed(2)}</li>
        <li>Điểm giữa kì cao nhất: {stats.maxMidtermScore.toFixed(2)}</li>
        <li>Điểm cuối kì cao nhất: {stats.maxFinalScore.toFixed(2)}</li>
        <li>Điểm giữa kì thấp nhất: {stats.minMidtermScore.toFixed(2)}</li>
        <li>Điểm cuối kì thấp nhất: {stats.minFinalScore.toFixed(2)}</li>
      </ul>
    </div>
  );
}

export default ScoreStatistics;
