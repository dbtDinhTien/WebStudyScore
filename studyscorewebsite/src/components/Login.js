import { useContext, useState } from "react";
import { Alert, Button, Form } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import MySpinner from "./layout/MySpinner";
import Apis, { authApis, endpoints } from "../configs/Apis";
import cookie from 'react-cookies'
import { MyDispatcherContext } from "../configs/Contexts";

const Login = () => {
  const info = [
    { label: "Email", field: "email", type: "email" },
    { label: "Mật khẩu", field: "password", type: "password" }
  ];


  const [user, setUser] = useState({});
  const [msg, setMsg] = useState();
  const [loading, setLoading] = useState(false);
  const nav = useNavigate();
  const dispatch = useContext(MyDispatcherContext);

//   const validate = () => {
//     // Kiểm tra các trường bắt buộc không được để trống
//     for (let i of info) {
//       if (!user[i.field] || user[i.field].trim() === "") {
//         setMsg(`Vui lòng nhập ${i.label}`);
//         return false;
//       }
//     }

//     // Kiểm tra mật khẩu và xác nhận mật khẩu
//     if (user.password !== user.confirm) {
//       setMsg("Mật khẩu không khớp!");
//       return false;
//     }

//     return true;
//   };

const validate = () => {
  if (!user.email || user.email.trim() === "") {
    setMsg("Vui lòng nhập Email");
    return false;
  }

  if (!user.password || user.password.trim() === "") {
    setMsg("Vui lòng nhập Mật khẩu");
    return false;
  }

  return true;
};





  const login = async (e) => {
    e.preventDefault();
    
    if(!validate()) return; 

      try {
        setLoading(true);
        console.info(user); 
        let res= await Apis.post(endpoints['login'],{...user} ); 
        cookie.save('token', res.data.token); 
        let u= await authApis().get(endpoints['current-user']); 
        console.info(u.data); 
        dispatch({
                "type": "login",
                "payload": u.data
            });
             
  
      nav("/"); // 
    

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
    

  return (
    <>
      <h1 className="text-center text-danger mt-2">ĐĂNG NHẬP NGƯỜI DÙNG</h1>
      {msg && <Alert variant="danger" className="mt-1">{msg}</Alert>}

      <Form onSubmit={login}>
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
          {loading ? (
            <MySpinner />
          ) : (
            <Button type="submit" variant="danger">
              Đăng Nhập
            </Button>
          )}
        </Form.Group>
      </Form>
    </>
  );
};

export default Login;
