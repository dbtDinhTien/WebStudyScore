import { BrowserRouter, Route, Routes } from "react-router-dom"
import Header from "./components/layout/Header"
import { Container } from "react-bootstrap"
import Home from "./components/Home"
import Footer from "./components/layout/Footer"
import 'bootstrap/dist/css/bootstrap.min.css';
import Login from "./components/Login"
import Register from "./components/Register"
import { MyDispatcherContext, MyUserContext } from "./configs/Contexts"
import MyUserReducer from "./reducers/MyUserReducer"
import { useEffect, useReducer } from "react"
import Subject from "./components/Subject"
import cookie from "react-cookies";
import { jwtDecode } from "jwt-decode";
import { authApis, endpoints } from "./configs/Apis"
import Class from "./components/Class"
import ClassSubjects from "./components/ClassSubjects"
import StudentScores from "./components/StudentScore"
import LecturerClasses from "./components/LecturerClasses"
import LecturerStudentList from "./components/LecturerStudentList"
import LecturerInputScores from "./components/LecturerInputScores"
import LecturerSelectClassSubject from "./components/LecturerSelectClassSubject"
import ForumPostList from "./components/ForumPostList"
import ForumPostDetail from "./components/ForumPostDetail"
import ForumCreate from "./components/ForumCreate"
import ForumEdit from "./components/ForumEdit"
import StudentScoreSearch from "./components/StudentScoreSearch"
import StudentSubjects from "./components/StudentSubjects"
import StudentSubjectsScores from "./components/StudentSubjectScore"
import StudentChat from "./components/StudentChat"
import LecturerChat from "./components/LecturerChat"
import LecturerClassSubject from "./components/LecturerClassSubject"

const initialUser = null;

const App = () => {

  const [user, dispatch] = useReducer(MyUserReducer, initialUser);


  useEffect(() => {
    const token = cookie.load("token");
    if (token) {
      try {
        const decoded = jwtDecode(token);
        console.log("Decoded token:", decoded);  // chỉ có email trong decoded.sub

        // Gọi API lấy user theo email
        authApis().get(endpoints['current-user'])

          .then(res => {
            console.log("User data from API:", res.data);
            dispatch({ type: "login", payload: res.data }); // Gửi đủ data user vào context
          })
          .catch(err => {
            console.error("Lỗi lấy user:", err);
            dispatch({ type: "logout" });
          });

      } catch (e) {
        dispatch({ type: "logout" });
      }
    }
  }, [dispatch]);

  return (
    <MyUserContext.Provider value={user}>
      <MyDispatcherContext.Provider value={dispatch}>
        <BrowserRouter>
        <div className="d-flex flex-column min-vh-100">
          <Header />
          <Container className="flex-grow-1 my-3">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/subject" element={<Subject />} />
              <Route path="/classes" element={<Class />} />
              <Route path="/classsubject" element={<ClassSubjects />} />
              <Route path="/studentscore" element={<StudentScores />} />
              <Route path="/lecturer/classes" element={<LecturerClasses />} />
              <Route path="/lecturer/students/:classSubjectId" element={<LecturerStudentList />} />
              <Route path="/lecturer/input-scores/:classSubjectId" element={<LecturerInputScores />} />
              <Route path="/lecturer/input-scores" element={<LecturerSelectClassSubject />} />
              <Route path="/lecturer/forum" element={<ForumPostList />} />
              <Route path="/forum/:postId" element={<ForumPostDetail />} />
              <Route path="/forum/add" element={<ForumCreate />} />
              <Route path="/forum/edit/:postId" element={<ForumEdit />} />
              <Route path="/lecturer/search-student" element={<StudentScoreSearch />} />
              <Route path="/student/subjects" element={<StudentSubjects />} />
              <Route path="/student/grades" element={<StudentSubjectsScores />} />
              <Route path="/student/chat" element={<StudentChat />} />
              <Route path="/lecturer/chat" element={<LecturerChat />} />
              <Route path="/lecturer/class-subjects" element={<LecturerClassSubject />} />

            </Routes>
          </Container>
          <Footer />
         </div>
        </BrowserRouter>
      </MyDispatcherContext.Provider>
    </MyUserContext.Provider>

  );

}

export default App; 