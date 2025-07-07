-- Bảng Users: lưu thông tin tài khoản
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role varchar(20) NOT NULL,
    avatar_url VARCHAR(500),
    student_code VARCHAR(50),
    lecturer_code VARCHAR(50),
    class_id INT, 
    `active` bit(1) DEFAULT b'1'
);
-- Bảng Classes: quản lý lớp học
CREATE TABLE class (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    lecturer_id INT,
    FOREIGN KEY (lecturer_id) REFERENCES user(id)
);
ALTER TABLE user
ADD CONSTRAINT fk_user_class
FOREIGN KEY (class_id) REFERENCES class(id);
-- Bảng Subjects: thông tin môn học
CREATE TABLE subject (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject_code VARCHAR(50) UNIQUE NOT NULL,
    subject_name VARCHAR(255) NOT NULL,
    description TEXT, -- Thêm mô tả môn học
    credits INT DEFAULT 3,
    image_url VARCHAR(500)
);

-- Bảng Class_Subjects: môn học gán cho lớp
CREATE TABLE class_subject (
    id INT AUTO_INCREMENT PRIMARY KEY,
    class_id INT NOT NULL,
    subject_id INT NOT NULL,
    lecturer_id INT,
    FOREIGN KEY (class_id) REFERENCES class(id),
    FOREIGN KEY (subject_id) REFERENCES subject(id),
    FOREIGN KEY (lecturer_id) REFERENCES user(id)
);
-- Bảng student_class_subjects: môn học của sinh viên
CREATE TABLE student_class_subject (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    class_subject_id INT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES user(id),
    FOREIGN KEY (class_subject_id) REFERENCES class_subject(id),
    UNIQUE(student_id, class_subject_id) -- tránh trùng đăng ký
);

-- Bảng Scores: điểm sinh viên
CREATE TABLE score (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_class_subject_id INT NOT NULL,
    midterm_score FLOAT,
    final_score FLOAT,
    extra_score1 FLOAT,
    extra_score2 FLOAT,
    extra_score3 FLOAT,
    lock_status ENUM('draft', 'locked') DEFAULT 'draft',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_class_subject_id) REFERENCES student_class_subject(id)
);

-- Bảng Forum_Posts: bài post trên diễn đàn
CREATE TABLE forum_post (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES user(id)
);

-- Bảng Forum_Comments: comment bài viết
CREATE TABLE forum_comment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    content TEXT NOT NULL,
    author_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES forum_post(id),
    FOREIGN KEY (author_id) REFERENCES user(id)
);



-- Insert vào Users (2 admin, 2 giảng viên, 5 sinh viên)
INSERT INTO user (first_name,last_name, email, password, role, avatar_url, student_code, lecturer_code)
VALUES 
('Nguyen Van', 'A', 'A.NguyenVan@ou.edu.vn', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'ROLE_ADMIN', NULL, NULL, NULL),
('Tran Thi', 'B', 'B.TranThi@ou.edu.vn', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'ROLE_LECTURER', NULL, NULL, 'GV001'),
('Le Van', 'C', 'C.LeVan@ou.edu.vn', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'ROLE_LECTURER', NULL, NULL, 'GV002'),
('Mai Thị Hồng', 'Duy', '2251012047duy@ou.edu.vn', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'ROLE_STUDENT', 'https://res.cloudinary.com/dq5ajyj0q/image/upload/v1748637500/pngtree-account-avatar-user-abstract-circle-background-flat-color-icon-png-image_4965046_ggmoek.png', '2251012047', NULL),
('Pham Thi', 'E', 'E.PhamThi@ou.edu.vn','$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'ROLE_STUDENT', NULL, 'SV002', NULL),
('Phan Thi', 'F', 'F.PhanThi@ou.edu.vn','$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'ROLE_STUDENT', NULL, 'SV003', NULL),
('Le Tran', 'H', 'H.LeTran@ou.edu.vn', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'ROLE_STUDENT', NULL, 'SV004', NULL),
('Truong Ho', 'K', 'K.TruongHo@ou.edu.vn', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'ROLE_STUDENT', NULL, 'SV005', NULL),
('Đinh Bích', 'Tiên', '2251012132tien@ou.edu.vn', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'ROLE_STUDENT','https://res.cloudinary.com/dq5ajyj0q/image/upload/v1748637500/pngtree-account-avatar-user-abstract-circle-background-flat-color-icon-png-image_4965046_ggmoek.png', '2251012132', NULL);

-- Insert vào Classes 
INSERT INTO class (name,code,lecturer_id)
VALUES 
('Công nghệ thông tin 2021A','CNTT2021A',2),
('Công nghệ thông tin 2021 B','CNTT2021B',3);

-- Insert vào Subjects
INSERT INTO subject (subject_code, subject_name, description, credits,image_url)
VALUES 
('CT101', 'Cấu trúc dữ liệu', 'Học về cây, danh sách, đồ thị và thuật toán.', 3,'https://res.cloudinary.com/dq5ajyj0q/image/upload/v1746280157/lo-trinh-hoc-cau-truc-du-lieu-va-giai-thuat-phan-2-63723261114.6884_g5jx3u.jpg'),
('CN202', 'Mạng máy tính', 'Kiến thức căn bản về hệ thống mạng.', 3,'https://res.cloudinary.com/dq5ajyj0q/image/upload/v1746280116/mang-may-tinh-1_8c9106c5577f4ddb90c8a387203096b9_grande_ix4q11.jpg'),
('LT303', 'Lập trình hướng đối tượng', 'Lập trình Java, thiết kế OOP.', 4,'https://res.cloudinary.com/dq5ajyj0q/image/upload/v1746280233/1692946617-KYWcedi_ini1fr.jpg');

-- Gán môn cho lớp
INSERT INTO class_subject (class_id, subject_id,lecturer_id)
VALUES 
(1, 1,2),
(1, 3,2),
(2, 2,3),
(2, 3,3);

UPDATE user SET class_id = 1 WHERE student_code = '2251012047';
UPDATE user SET class_id = 1 WHERE student_code = 'SV002';
UPDATE user SET class_id = 1 WHERE student_code = 'SV003';
UPDATE user SET class_id = 2 WHERE student_code = 'SV004';
UPDATE user SET class_id = 2 WHERE student_code = 'SV005';
UPDATE user SET class_id = 2 WHERE student_code = '2251012132';

INSERT INTO student_class_subject (student_id, class_subject_id)
VALUES 
(5, 1),  -- SV002 đăng ký CTDL cho lớp CNTT2021A
(6, 1),  -- SV003
(7, 1),  -- SV004
(8, 2),  -- SV005 đăng ký LTHDT cho lớp CNTT2021A
(4, 2),  -- SV001
(4,1),
(4,3),
(9,2);

-- Điểm mẫu
INSERT INTO score (student_class_subject_id, midterm_score, final_score, extra_score1, lock_status)
VALUES 
(1, 7.5, 8.0, NULL, 'draft'),
(2, 6.0, 7.0, NULL, 'draft'),
(3, 8.5, 9.0, NULL, 'draft'),
(4, 7.0, 7.5, NULL, 'draft'),
(5, 5.5, 6.0, NULL, 'draft'),
(6, 8, 8, NULL, 'draft');

-- Forum Posts
INSERT INTO forum_post (title, content, author_id)
VALUES 
('Cách tính điểm giữa kỳ?', 'Thầy ơi cho em hỏi điểm giữa kỳ được tính như thế nào?', 5),
('Điểm final em thấp', 'Em muốn được phúc khảo điểm final ạ.', 6);

-- Forum Comments
INSERT INTO forum_comment (post_id, content, author_id)
VALUES 
(1, 'Điểm giữa kỳ lấy từ bài tập và kiểm tra giữa kỳ.', 3),
(2, 'Em gửi đơn phúc khảo lên văn phòng khoa nhé.', 4);