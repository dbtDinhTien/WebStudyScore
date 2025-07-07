import { useContext } from "react";
import { Button, Container, Nav, Navbar } from "react-bootstrap";
import { Link } from "react-router-dom";
import { MyDispatcherContext, MyUserContext } from "../../configs/Contexts";
import { Modal } from "react-bootstrap";
import { useState } from "react";


const Header = () => {
  const user = useContext(MyUserContext);
  const dispatch = useContext(MyDispatcherContext);
  const [showProfile, setShowProfile] = useState(false);
const handleClose = () => setShowProfile(false);
const handleShow = () => setShowProfile(true);


  return (
    <Navbar expand="lg" bg="light" variant="light" className="mb-4 shadow-sm">
      <Container>
        <Navbar.Brand className="fw-bold fs-4">📚 STUDYSCORE</Navbar.Brand>
        <Navbar.Toggle aria-controls="main-navbar" />
        <Navbar.Collapse id="main-navbar">
          
          {/* Menu bên trái */}
          <Nav className="me-auto">
            <Link to="/" className="nav-link">🏠 Trang chủ</Link>

            {user?.role === "ROLE_ADMIN" && (
              <>
                <Link to="/subject" className="nav-link">📘 Môn học</Link>
                <Link to="/classes" className="nav-link">🏫 Lớp học</Link>
                <Link to="/classsubject" className="nav-link">🎓 Môn học theo lớp</Link>
                <Link to="/studentscore" className="nav-link">📝 Điểm sinh viên</Link>
              </>
            )}

            {user?.role === "ROLE_LECTURER" && (
              <>
                <Link to="/lecturer/classes" className="nav-link">📚 Lớp phụ trách</Link>
                <Link to="/lecturer/class-subjects" className="nav-link">👨‍🏫 Lớp môn học giảng dạy</Link>

                <Link to="/lecturer/input-scores" className="nav-link">📝 Nhập điểm</Link>
                <Link to="/lecturer/search-student" className="nav-link">🔍 Tra cứu điểm</Link>
                <Link to="/lecturer/forum" className="nav-link">💬 Diễn đàn</Link>
                <Link to="/lecturer/chat" className="nav-link">📬 Nhắn tin</Link>
              </>
            )}

            {user?.role === "ROLE_STUDENT" && (
              <>
                <Link to="/student/subjects" className="nav-link">📘 Môn học</Link>
                <Link to="/student/grades" className="nav-link">📊 Điểm chi tiết</Link>
                <Link to="/lecturer/forum" className="nav-link">💬 Diễn đàn</Link>
                <Link to="/student/chat" className="nav-link">📬 Nhắn tin</Link>
              </>
            )}
          </Nav>

          {/* Avatar + Logout bên phải */}
          <Nav className="ms-auto align-items-center">
            {user === null ? (
              <>
                <Link to="/login" className="nav-link text-danger fw-semibold">Đăng nhập</Link>
                <Link to="/register" className="nav-link text-success fw-semibold">Đăng ký</Link>
              </>
            ) : (
              <>
                <div className="d-flex align-items-center">
                  <img
                    src={user.avatarUrl}
                    alt="avatar"
                    width="35"
                    height="35"
                    className="rounded-circle me-2"
                  />
                  <span
                    onClick={handleShow}
                    className="me-3 fw-semibold text-dark text-decoration-none"
                    style={{ cursor: "pointer" }}
                  >
                    {user.firstName} {user.lastName}
                  </span>

                  <Button
                    variant="outline-danger"
                    size="sm"
                    
                    onClick={() => {
    handleClose(); // đóng modal (nếu đang mở)
    dispatch({ type: "logout" }); // đăng xuất
  }}
                  >
                    Đăng xuất
                  </Button>
                </div>
              </>
            )}
          </Nav>

        </Navbar.Collapse>
        {user && (
  <Modal show={showProfile} onHide={handleClose} centered>
    <Modal.Header closeButton>
      <Modal.Title>Thông tin cá nhân</Modal.Title>
    </Modal.Header>
    <Modal.Body>
      <div className="text-center">
        {user.avatarUrl && (
          <img
            src={user.avatarUrl}
            alt="avatar"
            width="80"
            height="80"
            className="rounded-circle mb-3"
          />
        )}
        <h5>{user.firstName} {user.lastName}</h5>
        <p><strong>Email:</strong> {user.email}</p>
        <p><strong>Vai trò:</strong> {user.role}</p>
      </div>
    </Modal.Body>
  </Modal>
)}


      </Container>
    </Navbar>
  );
};

export default Header;
