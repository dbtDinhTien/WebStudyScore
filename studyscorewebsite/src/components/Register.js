

import { useRef, useState } from "react";
import { Alert, Button, Form } from "react-bootstrap";
import Apis, { endpoints } from "../configs/Apis";
import MySpinner from "./layout/MySpinner";
import { useNavigate } from "react-router-dom";

const Register = () => {
  const info = [
    { label: "Tên", field: "first_name", type: "text" },
    { label: "Họ và tên lót", field: "last_name", type: "text" },
    { label: "Email", field: "email", type: "email" },
    { label: "Điện thoại", field: "phone", type: "tel" },
    { label: "Mật khẩu", field: "password", type: "password" },
    { label: "Xác nhận mật khẩu", field: "confirm", type: "password" },
  ];

  const avatar = useRef();
  const [user, setUser] = useState({});
  const [msg, setMsg] = useState();
  const [loading, setLoading] = useState(false);
  const nav = useNavigate();

  const validate = () => {
  if (!user.email || user.email.trim() === "") {
    setMsg("Vui lòng nhập Email");
    return false;
  }

  // Kiểm tra định dạng email sinh viên theo tên miền trường
  const studentEmailRegex = /^[a-zA-Z0-9._%+-]+@ou\.edu\.vn$/; // sửa lại domain theo trường bạn
  if (!studentEmailRegex.test(user.email)) {
    setMsg("Email không hợp lệ! Vui lòng dùng email sinh viên do trường cấp ( @ou.edu.vn ).");
    return false;
  }

  if (!user.password || user.password.trim() === "") {
    setMsg("Vui lòng nhập Mật khẩu");
    return false;
  }

  if (user.password !== user.confirm) {
    setMsg("Mật khẩu không khớp!");
    return false;
  }

  return true;
};

  const register = async (e) => {
    e.preventDefault();
    setMsg("");

    if (validate()) {
      let form = new FormData();
      for (let key in user)
        if (key !== "confirm") form.append(key, user[key]);

      if (avatar.current && avatar.current.files.length > 0) {
        form.append("avatar", avatar.current.files[0]);
      }

      try {
        setLoading(true);
        let res = await Apis.post(endpoints["register"], form, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });

        if (res.status === 201) nav("/login");
      } catch (ex) {
        if (ex.response) {
          console.error("Error status:", ex.response.status);
          console.error("Error data:", ex.response.data);
          setMsg(`Lỗi: ${ex.response.status} - ${JSON.stringify(ex.response.data)}`);
        } else if (ex.request) {
          console.error("No response received:", ex.request);
          setMsg("Không nhận được phản hồi từ server.");
        } else {
          console.error("Error", ex.message);
          setMsg(`Lỗi: ${ex.message}`);
        }
      } finally {
        setLoading(false);
      }
    }
  };

  return (
    <>
      <h1 className="text-center text-danger mt-2">ĐĂNG KÝ NGƯỜI DÙNG</h1>
      {msg && <Alert variant="danger" className="mt-1">{msg}</Alert>}

      <Form onSubmit={register}>
        {info.map((i) => (
          <Form.Group key={i.field} className="mb-3">
            <Form.Control
              value={user[i.field] || ""}
              onChange={(e) => setUser({ ...user, [i.field]: e.target.value })}
              type={i.type}
              placeholder={i.label}
              required
            />
          </Form.Group>
        ))}

        <Form.Group className="mb-3">
          <Form.Control ref={avatar} type="file" />
        </Form.Group>

        <Form.Group className="mb-3">
          {loading ? (
            <MySpinner />
          ) : (
            <Button type="submit" variant="danger">
              Đăng Ký
            </Button>
          )}
        </Form.Group>
      </Form>
    </>
  );
};

export default Register;
