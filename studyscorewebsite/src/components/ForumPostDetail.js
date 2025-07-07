import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";
import { Button, Card, Form, Modal } from "react-bootstrap";

const formatDate = (dateString) => {
  const d = new Date(dateString);
  return d.toLocaleString("vi-VN", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const ForumPostDetail = () => {
  const { postId } = useParams();
  const navigate = useNavigate();
  const [post, setPost] = useState(null);
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");
  const [editId, setEditId] = useState(null);
  const [currentUser, setCurrentUser] = useState(null);

  // --- modal sửa bài ---
  const [showEditModal, setShowEditModal] = useState(false);
  const [editTitle, setEditTitle] = useState("");
  const [editContent, setEditContent] = useState("");

  useEffect(() => {
    const loadPost = async () => {
      const res = await authApis().get(endpoints["post-detail"](postId));
      setPost(res.data);
    };

    const loadComments = async () => {
      const res = await authApis().get(endpoints["get-comments"](postId));
      setComments(res.data);
    };

    const loadCurrentUser = async () => {
      const res = await authApis().get(endpoints["current-user"]);
      setCurrentUser(res.data);
    };

    loadPost();
    loadComments();
    loadCurrentUser();
  }, [postId]);

  const handleAddComment = async (e) => {
    e.preventDefault();
    if (!newComment.trim()) return;

    try {
      if (editId) {
        const res = await authApis().put(endpoints["comment-edit"](editId), {
          content: newComment,
        });
        setComments((prev) =>
          prev.map((c) => (c.id === editId ? res.data : c))
        );
        setEditId(null);
      } else {
        const res = await authApis().post(endpoints["comment-add"], {
          content: newComment,
          postId: post.id,
        });
        setComments([...comments, res.data]);
      }
      setNewComment("");
    } catch (err) {
      console.error("Lỗi khi gửi bình luận:", err);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Bạn có chắc chắn xoá bình luận này?")) return;
    try {
      await authApis().delete(endpoints["comment-delete"](id));
      setComments(comments.filter((c) => c.id !== id));
    } catch (err) {
      console.error("Xoá thất bại:", err);
    }
  };

  const handleEdit = (comment) => {
    setNewComment(comment.content);
    setEditId(comment.id);
  };

  const handleDeletePost = async () => {
    if (!window.confirm("Bạn có chắc chắn muốn xoá bài viết này?")) return;
    try {
      await authApis().delete(endpoints["post-delete"](post.id));
      navigate("/lecturer/forum");
    } catch (err) {
      console.error("Lỗi khi xoá bài viết:", err);
    }
  };

  // Mở modal sửa bài và set giá trị ban đầu
  const handleShowEditModal = () => {
    setEditTitle(post.title);
    setEditContent(post.content);
    setShowEditModal(true);
  };

  // Đóng modal mà không lưu
  const handleCloseEditModal = () => {
    setShowEditModal(false);
  };

  // Lưu sửa bài viết
  const handleSaveEditPost = async () => {
    try {
      const res = await authApis().put(endpoints["post-edit"](post.id), {
        title: editTitle,
        content: editContent,
      });
      setPost(res.data); // cập nhật post mới
      setShowEditModal(false); // đóng modal
    } catch (err) {
      console.error("Lỗi khi cập nhật bài viết:", err);
      alert("Cập nhật bài viết thất bại!");
    }
  };

  if (!post) return <p>Đang tải bài viết...</p>;

  return (
    <div className="container mt-4">
      <Card>
        <Card.Body>
          <Card.Title>{post.title}</Card.Title>
          <Card.Text>{post.content}</Card.Text>
          <Card.Subtitle className="text-muted mb-2">
            Đăng lúc: {formatDate(post.createdAt)}
          </Card.Subtitle>
          <div className="d-flex gap-2">
            <Button variant="secondary" onClick={() => navigate("/lecturer/forum")}>
              ↩️ Quay lại
            </Button>
            {currentUser?.id === post.authorId?.id && (
              <>
                <Button variant="warning" onClick={handleShowEditModal}>
                  ✏️ Sửa bài
                </Button>
                <Button variant="danger" onClick={handleDeletePost}>
                  🗑️ Xoá bài
                </Button>
              </>
            )}
          </div>
        </Card.Body>
      </Card>

      {/* Modal sửa bài viết */}
      <Modal show={showEditModal} onHide={handleCloseEditModal}>
        <Modal.Header closeButton>
          <Modal.Title>Sửa bài viết</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group className="mb-3">
            <Form.Label>Tiêu đề</Form.Label>
            <Form.Control
              type="text"
              value={editTitle}
              onChange={(e) => setEditTitle(e.target.value)}
            />
          </Form.Group>
          <Form.Group>
            <Form.Label>Nội dung</Form.Label>
            <Form.Control
              as="textarea"
              rows={5}
              value={editContent}
              onChange={(e) => setEditContent(e.target.value)}
            />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseEditModal}>
            Huỷ
          </Button>
          <Button variant="primary" onClick={handleSaveEditPost}>
            Lưu
          </Button>
        </Modal.Footer>
      </Modal>

      <div className="mt-4">
        <h5>Bình luận ({comments.length})</h5>
        {comments.map((c) => (
          <Card key={c.id} className="mb-2">
            <Card.Body>
              <Card.Text>{c.content}</Card.Text>
              <small className="text-muted">
                {c.authorId?.firstName} {c.authorId?.lastName} • {formatDate(c.createdAt)}
              </small>

              {currentUser?.id === c.authorId?.id && (
                <div className="mt-2">
                  <Button variant="warning" size="sm" onClick={() => handleEdit(c)}>
                    ✏️ Sửa
                  </Button>{" "}
                  <Button variant="danger" size="sm" onClick={() => handleDelete(c.id)}>
                    ❌ Xoá
                  </Button>
                </div>
              )}
            </Card.Body>
          </Card>
        ))}

        <Form onSubmit={handleAddComment} className="mt-3">
          <Form.Group className="mb-2">
            <Form.Control
              type="text"
              placeholder="Nhập bình luận..."
              value={newComment}
              onChange={(e) => setNewComment(e.target.value)}
            />
          </Form.Group>
          <Button type="submit">{editId ? "Cập nhật" : "Gửi bình luận"}</Button>
        </Form>
      </div>
    </div>
  );
};

export default ForumPostDetail;
