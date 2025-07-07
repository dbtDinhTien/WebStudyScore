function deleteObject(endpoint, id) {
    if (confirm("Bạn chắc chắn xóa không?") === true) {
        fetch(endpoint + id, {
            method: "delete"
        }).then(res => {
            if (res.status === 204) {
                alert("Xóa thành công!");
                location.reload();
            } else
                alert("Có lỗi xảy ra!");
        });
    }
}
function populateSubjectEditModal(button) {
    const id = button.getAttribute("data-id");
    const subjectCode = button.getAttribute("data-subjectCode");
    const subjectName = button.getAttribute("data-subjectName");
    const credits = button.getAttribute("data-credits");
    const description = button.getAttribute("data-description");
    const image = button.getAttribute("data-image");

    document.getElementById("edit-id").value = id;
    document.getElementById("edit-subjectCode").value = subjectCode;
    document.getElementById("edit-subjectName").value = subjectName;
    document.getElementById("edit-credits").value = credits;
    document.getElementById("edit-description").value = description;
    document.getElementById("current-avatar").src = image;
    document.getElementById("edit-oldImage").value = image;


    // Gán action cho form: /subjects/{id}
    document.getElementById("editSubjectForm").action = "/StudyScoreWebAPI/subjects/" + id;
}
function populateEditClassModal(button) {
    const id = button.getAttribute('data-id');
    const code = button.getAttribute('data-code');
    const name = button.getAttribute('data-name');
    const lecturerId = button.getAttribute('data-lecturer-id');

    document.getElementById('edit-id').value = id;
    document.getElementById('edit-code').value = code;
    document.getElementById('edit-name').value = name;

    const lecturerSelect = document.getElementById('edit-lecturer');
    if (lecturerSelect) {
        lecturerSelect.value = lecturerId;
    }
    
    document.getElementById('editClassForm').action = '/StudyScoreWebAPI/classes/' + id;
}
function populateEditClassSubjectModal(button) {
    const id = button.getAttribute('data-id');
    const classId = button.getAttribute('data-class-id');
    const subjectId = button.getAttribute('data-subject-id');
    const lecturerId = button.getAttribute('data-lecturer-id');

    document.getElementById('edit-cs-id').value = id;
    document.getElementById('edit-class').value = classId;
    document.getElementById('edit-subject').value = subjectId;

    const lecturerSelect = document.getElementById('edit-lecturer');
    if (lecturerId && lecturerSelect) {
        lecturerSelect.value = lecturerId;
    } else if (lecturerSelect) {
        lecturerSelect.value = ""; // trường hợp chưa phân công
    }

    // Cập nhật action của form
    document.getElementById('editClassSubjectForm').action = '/StudyScoreWebAPI/classSubjects/' + id;
}
function populateEditScoreModal(button) {
    const id = button.getAttribute('data-id');
    const studentClassSubjectId = button.getAttribute('data-student-class-subject-id'); // Gộp 2 thành 1
    const midtermScore = button.getAttribute('data-midterm-score');
    const finalScore = button.getAttribute('data-final-score');
    const extra1 = button.getAttribute('data-extra1');
    const extra2 = button.getAttribute('data-extra2');
    const extra3 = button.getAttribute('data-extra3');
    const lockStatus = button.getAttribute('data-lock-status');

    document.getElementById('edit-id').value = id;

    // Gán cho select Sinh viên - Môn học
    const studentClassSubjectSelect = document.getElementById('edit-studentClassSubjectId');
    if (studentClassSubjectSelect) {
        studentClassSubjectSelect.value = studentClassSubjectId;
    }

    // Gán giá trị điểm
    document.getElementById('edit-midterm-score').value = midtermScore;
    document.getElementById('edit-final-score').value = finalScore;
    document.getElementById('edit-extra1').value = extra1;
    document.getElementById('edit-extra2').value = extra2;
    document.getElementById('edit-extra3').value = extra3;

    // Gán trạng thái khóa cho select
    const lockStatusSelect = document.getElementById('edit-lock-status');
    if (lockStatusSelect) {
        lockStatusSelect.value = lockStatus;
    }

    // Cập nhật action của form
    document.getElementById('editScoreForm').action = '/StudyScoreWebAPI/scores/' + id;
}

function populateEditUserModal(button) {
    const id = button.getAttribute('data-id');
    const firstName = button.getAttribute('data-first-name');
    const lastName = button.getAttribute('data-last-name');
    const email = button.getAttribute('data-email');
    const studentCode = button.getAttribute('data-student-code');
    const lecturerCode = button.getAttribute('data-lecturer-code');
    const role = button.getAttribute('data-role');
    const active = button.getAttribute('data-active');
    const avatar = button.getAttribute('data-avatar');
    const classId = button.getAttribute('data-class-id');

    document.getElementById('edit-id').value = id;
    document.getElementById('edit-firstName').value = firstName;
    document.getElementById('edit-lastName').value = lastName;
    document.getElementById('edit-email').value = email;
    document.getElementById('edit-studentCode').value = studentCode;
    document.getElementById('edit-lecturerCode').value = lecturerCode;
    document.getElementById('edit-role').value = role;
    document.getElementById('edit-active').checked = active === 'true';
    document.getElementById('current-avatar').src = avatar;
    document.getElementById("edit-oldImage").value = avatar;
    document.getElementById('edit-classId').value = classId;


    document.getElementById('editScoreForm').action = '/StudyScoreWebAPI/users/' + id;
}
function populateEditStudentClassSubjectModal(button) {
    const id = button.getAttribute('data-id');
    const studentId = button.getAttribute('data-student-id');
    const classSubjectId = button.getAttribute('data-class-subject-id');

    document.getElementById('edit-scs-id').value = id;

    document.getElementById('edit-student-id').value = studentId;
    document.getElementById('edit-class-subject-id').value = classSubjectId;

    document.getElementById('editStudentClassSubjectForm').action = '/StudyScoreWebAPI/studentClassSubjects/' + id;
}
