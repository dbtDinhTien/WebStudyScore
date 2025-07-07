import axios from "axios"
import cookie from 'react-cookies'
const BASE_URL= process.env.REACT_APP_API_URL ||'http://localhost:8080/StudyScoreWebAPI/api/'

export const endpoints= {
    'register': '/users',               
    'login': '/login',                
    'current-user': '/secure/profile', 
    'subjects': '/subjects',
    'classes':'/classes',
    'classSubjects': '/classSubjects',
    'scores':'/scores',
    'lecturer-classes': (lecturerId) => `/classSubjects/lecturer/${lecturerId}`,
    'students-by-classSubjectId': (id) => `/stuClassSubjects/classSubject/${id}`, 
    'scores-by-classSubjectId': (id) => `/scores/classSubject/${id}`,
    'update-score': (scoreId) => `/scores/${scoreId}`, // dùng POST
    'save-score': '/scores/add', // POST
    'lock-scores': (classSubjectId) => `/scores/lock/${classSubjectId}`,
    'classSubject-detail': (id) => `classSubjects/${id}`,
    'import-scores': (classSubjectId) => `/scores/import/${classSubjectId}`,
    'post-all': '/posts/allPost',
    'post-detail': (id) => `/posts/${id}`,
    'post-add': '/posts/add',
    'post-edit': (id) => `/posts/edit/${id}`,
    'post-delete': (id) => `/posts/delete/${id}`,
    'get-comments': (postId) => `/comments/post/${postId}`,
    'chatMessages': '/chat/messages',
    'chatSend': '/chat/send',
    'lecturersSearch': (keyword) => `/lecturers/search?keyword=${keyword}`,
    'chatConversations': '/chat/conversations',
    'studentsSearch': (keyword) => `/students/search?keyword=${keyword}`,
    'lecturer-classSubjects': (lecturerId) => `/classSubjects/lecturer/${lecturerId}`,



    'comment-add': '/comments/add',
    'comment-edit': (id) => `/comments/edit/${id}`,
    'comment-delete': (id) => `/comments/${id}`,
    // Sinh viên trong lớp môn học
   'exportExcel': (classSubjectId) =>
    `/scores/export-excel/${classSubjectId}`,

  'exportPdf': (classSubjectId) =>
    `/scores/export-pdf/${classSubjectId}`,

  
'search-students': (keyword) => `/students/search?keyword=${encodeURIComponent(keyword)}`,
'student-classSubjects': (studentId) => `/stuClassSubjects/student/${studentId}`,
'score-by-stuClassSubjectId': (id) => `/scores/stuClassSubject/${id}`,

//SINH VIEN 
// Lấy danh sách môn học của sinh viên
'student-classSubjects': (studentId) => `/stuClassSubjects/student/${studentId}`,

// Lấy danh sách điểm của sinh viên
'scores-by-student': (studentId) => `/scores/student/${studentId}`,



    

}

export const authApis=() => {
    return axios.create({
        baseURL:BASE_URL,
        headers:{                                           
            'Authorization':`Bearer ${cookie.load('token')}`
        }
       
    })
}

export default axios.create({
    baseURL: BASE_URL
})