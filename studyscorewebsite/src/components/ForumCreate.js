import { useState } from "react";
import { Button, Card, Form, Alert } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";

const ForumCreate = () => {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    if (!title.trim() || !content.trim()) {
      setError("Tiêu đề và nội dung không được để trống.");
      return;
    }

   try {
  const res = await authApis().post(endpoints["post-add"], {
    title,
    content,
  });
  alert("✅ Đăng bài thành công!");
  navigate(`/forum/${res.data.id}`);
} catch (err) {
  console.error("Lỗi khi đăng bài viết:", err);
  setError("Đăng bài thất bại. Vui lòng thử lại.");
}

  };

  return (
    <div className="container mt-4">
      <Card>
        <Card.Body>
          <Card.Title>📝 Tạo bài viết mới</Card.Title>

          {error && <Alert variant="danger">{error}</Alert>}

          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Tiêu đề</Form.Label>
              <Form.Control
                type="text"
                placeholder="Nhập tiêu đề bài viết..."
                value={title}
                onChange={(e) => setTitle(e.target.value)}
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Nội dung</Form.Label>
              <Form.Control
                as="textarea"
                rows={6}
                placeholder="Viết nội dung bài viết..."
                value={content}
                onChange={(e) => setContent(e.target.value)}
              />
            </Form.Group>

            <div className="d-flex gap-2">
              <Button type="submit" variant="primary">
                ➕ Đăng bài
              </Button>
              <Button variant="secondary" onClick={() => navigate("/forum")}>
                ↩️ Quay lại
              </Button>
            </div>
          </Form>
        </Card.Body>
      </Card>
    </div>
  );
};

export default ForumCreate;
