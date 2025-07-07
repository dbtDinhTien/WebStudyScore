/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.myapp.repositories;

import com.myapp.pojo.Score;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface ScoreRepository {
    List<Score> getScores(Map<String, String> params);
    Score getScoreById(int id);
    Score addOrUpdateScore(Score sc);
    void deleleScore(int id);
    long countScores(Map<String, String> params);
    List<Score> getScoresByClassSubjectId(int classSubjectId);
    void saveAll(List<Score> scores);
    List<Score> getScoresByStudentId(int studentId);
    Score getScoreByStuClassSubjectId(int stuClassSubjectId);
}
