import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";
import { Button, Card, Form, Alert } from "react-bootstrap";

const ForumEdit = () => {
  const { postId } = useParams();
  const navigate = useNavigate();
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadPost = async () => {
      try {
        const res = await authApis().get(endpoints["post-detail"](postId));
        setTitle(res.data.title);
        setContent(res.data.content);
      } catch (err) {
        setError("Không tải được bài viết.");
      }
    };

    loadPost();
  }, [postId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    if (!title.trim() || !content.trim()) {
      setError("Tiêu đề và nội dung không được để trống.");
      return;
    }

    try {
      await authApis().put(endpoints["post-edit"](postId), { title, content });
      alert("✅ Cập nhật bài viết thành công!");
      navigate(`/forum/${postId}`);
    } catch (err) {
      if (err.response) {
        console.error("Lỗi khi cập nhật bài viết:");
        console.error("👉 Status:", err.response.status);
        console.error("👉 Data:", err.response.data);
        console.error("👉 Headers:", err.response.headers);
      } else if (err.request) {
        console.error("Không nhận được phản hồi từ server:", err.request);
      } else {
        console.error("Lỗi khác:", err.message);
      }
      setError("Cập nhật thất bại. Vui lòng thử lại.");
    }
  };

  return (
    <div className="container mt-4">
      <Card>
        <Card.Body>
          <Card.Title>✏️ Chỉnh sửa bài viết</Card.Title>

          {error && <Alert variant="danger">{error}</Alert>}

          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Tiêu đề</Form.Label>
              <Form.Control
                type="text"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Nội dung</Form.Label>
              <Form.Control
                as="textarea"
                rows={6}
                value={content}
                onChange={(e) => setContent(e.target.value)}
              />
            </Form.Group>

            <div className="d-flex gap-2">
              <Button type="submit" variant="primary">
                💾 Lưu
              </Button>
              <Button
                variant="secondary"
                onClick={() => navigate(`/forum/${postId}`)}
              >
                ↩️ Quay lại
              </Button>
            </div>
          </Form>
        </Card.Body>
      </Card>
    </div>
  );
};

export default ForumEdit;
