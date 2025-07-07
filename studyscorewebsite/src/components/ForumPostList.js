import { useEffect, useState } from "react";
import { Card, Spinner, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";

const ForumPostListCards = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPosts = async () => {
      setLoading(true);
      try {
        const res = await authApis().get(endpoints["post-all"]);
        setPosts(res.data);
      } catch (err) {
        alert("Lỗi tải danh sách bài viết");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchPosts();
  }, []);

  if (loading) {
    return (
      <div className="text-center mt-5">
        <Spinner animation="border" />
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className=" mt-3 fw-bold display-6">📢 DIỄN ĐÀN TRAO ĐỔI</h1>
        <Button variant="success" onClick={() => navigate("/forum/add")}>
          ➕ Thêm bài viết
        </Button>
      </div>

      {posts.length === 0 ? (
        <p className="text-center">Chưa có bài viết nào.</p>
      ) : (
        <div className="d-flex flex-column gap-4">
          {posts.map((post) => (
            <Card key={post.id} className="shadow-sm">
              <Card.Body
                style={{ cursor: "pointer" }}
                onClick={() => navigate(`/forum/${post.id}`)}
              >
                <Card.Title className="fs-4 fw-bold text-primary">
                  {post.title}
                </Card.Title>
                <Card.Subtitle className="mb-2 text-muted">
                  {post.authorId?.name || post.authorId?.email || "Ẩn danh"} •{" "}
                  {new Date(post.createdAt).toLocaleString("vi-VN", {
                    day: "2-digit",
                    month: "2-digit",
                    year: "numeric",
                    hour: "2-digit",
                    minute: "2-digit",
                  })}
                </Card.Subtitle>
                <Card.Text className="mt-2" style={{ whiteSpace: "pre-line" }}>
                  {post.content.length > 200
                    ? post.content.slice(0, 200) + "…"
                    : post.content}
                </Card.Text>
                <div className="d-flex justify-content-end mt-3">
                  <Button
                    variant="outline-primary"
                    size="sm"
                    onClick={(e) => {
                      e.stopPropagation();
                      navigate(`/forum/${post.id}`);
                    }}
                  >
                    Xem chi tiết
                  </Button>
                </div>
              </Card.Body>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};

export default ForumPostListCards;
