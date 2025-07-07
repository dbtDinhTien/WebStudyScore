/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.services.impl;

import com.myapp.pojo.Score;
import com.myapp.pojo.User;
import com.myapp.repositories.ScoreRepository;
import com.myapp.services.EmailService;
import com.myapp.services.ScoreService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class ScoreServiceImply implements ScoreService {

    @Autowired
    public ScoreRepository scoreRepo;

    @Autowired
    public EmailService emailService;

    @Override
    public List<Score> getScores(Map<String, String> params) {
        return this.scoreRepo.getScores(params);
    }

    @Override
    public Score getScoreById(int id) {
        return this.scoreRepo.getScoreById(id);
    }

    @Override
    public Score addOrUpdateScore(Score sc) {
        return this.scoreRepo.addOrUpdateScore(sc);
    }

    @Override
    public void deleleScore(int id) {
        this.scoreRepo.deleleScore(id);
    }

    @Override
    public long countScores(Map<String, String> params) {
        return this.scoreRepo.countScores(params);
    }

    @Override
    public List<Score> getScoresByClassSubjectId(int classSubjectId) {
        return this.scoreRepo.getScoresByClassSubjectId(classSubjectId);
    }

    @Override
    public void lockScoresByClassSubjectId(int classSubjectId) {
        List<Score> scores = scoreRepo.getScoresByClassSubjectId(classSubjectId);

        for (Score s : scores) {
            s.setLockStatus("locked");
        }

        this.scoreRepo.saveAll(scores);
        for (Score s : scores) {
            User student = s.getStudentClassSubjectId().getStudentId();
            String email = student.getEmail();
            String fullName = student.getFirstName() + " " + student.getLastName();

            String subjectName = s.getStudentClassSubjectId()
                    .getClassSubjectId()
                    .getSubjectId()
                    .getSubjectName();

            if (email != null && !email.isEmpty()) {
                String subject = "Thông báo điểm môn học";
                String body = "Chào " + fullName + ",\n\n"
                        + "Giảng viên đã khóa điểm môn học " + subjectName + " của bạn.\n"
                        + "Hãy đăng nhập hệ thống để xem chi tiết.\n\n"
                        + "Trân trọng.";

                emailService.sendEmail(email, subject, body);
            }
        }
    }

    @Override
    public List<Score> getScoresByStudentId(int studentId
    ) {
        return this.scoreRepo.getScoresByStudentId(studentId);
    }

    @Override
    public Score getScoreByStuClassSubjectId(int stuClassSubjectId
    ) {
        return this.scoreRepo.getScoreByStuClassSubjectId(stuClassSubjectId);
    }

}
