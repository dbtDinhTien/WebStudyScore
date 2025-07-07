/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.controllers;

import com.myapp.pojo.StudentClassSubject;
import com.myapp.pojo.User;
import com.myapp.services.StudentClassSubjectService;
import com.myapp.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiStuClassSubjectController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private StudentClassSubjectService stuClassSubService;
    // api/stuClassSubjects/{id}

    @DeleteMapping("/stuClassSubjects/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        this.stuClassSubService.deleleStudentClassSubject(id);
    }

    @GetMapping("/stuClassSubjects")
    public ResponseEntity<List<StudentClassSubject>> listStudentClassSubjects(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.stuClassSubService.getStudentClassSubjects(params), HttpStatus.OK);
    }

    @GetMapping("/stuClassSubjects/{id}")
    public ResponseEntity<StudentClassSubject> retrieve(@PathVariable(value = "id") int id) {
        return new ResponseEntity<>(this.stuClassSubService.getStudentClassSubjectById(id), HttpStatus.OK);
    }

    //Lấy môn học của học sinh 
    @GetMapping("/stuClassSubjects/student/{studentId}")
    public ResponseEntity<List<StudentClassSubject>> listClassSubjectsByStudentId(@PathVariable(value = "studentId") int studentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User author = userService.getUserByEmail(email);
        if (author == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (author.getRole().equals("ROLE_STUDENT") && !author.getId().equals(studentId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); 
        }

        return new ResponseEntity<>(this.stuClassSubService.getStudentClassSubjectsByStudentId(studentId), HttpStatus.OK);
    }

    //Lấy danh sách sinh viên theo classSubjectId
    @GetMapping("/stuClassSubjects/classSubject/{classSubjectId}")
    public ResponseEntity<List<StudentClassSubject>> listClassSubjectsByClassSubjectId(@PathVariable(value = "classSubjectId") int classSubjectId) {
        return new ResponseEntity<>(this.stuClassSubService.getStuClassSubjectByClassSubjectId(classSubjectId), HttpStatus.OK);
    }

    @GetMapping("/stuClassSubjects/export/{classSubjectId}")
    public void exportStudentList(@PathVariable(value = "classSubjectId") int classSubjectId, HttpServletResponse response) throws IOException {
        List<StudentClassSubject> list = stuClassSubService.getStuClassSubjectByClassSubjectId(classSubjectId);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students");

        // Header
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("STT");
        header.createCell(1).setCellValue("Mã số sinh viên");
        header.createCell(2).setCellValue("Họ và tên sinh viên");
        header.createCell(3).setCellValue("Điểm giữa kì");
        header.createCell(4).setCellValue("Điểm cuối kì");
        header.createCell(5).setCellValue("Điểm thêm 1");
        header.createCell(6).setCellValue("Điểm thêm 2");
        header.createCell(7).setCellValue("Điểm thêm 3");

        int rowIdx = 1;
        for (StudentClassSubject scs : list) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(rowIdx-1);
            row.createCell(1).setCellValue(scs.getStudentId().getStudentCode());
            row.createCell(2).setCellValue(scs.getStudentId().getFirstName() + " " + scs.getStudentId().getLastName());
            row.createCell(3).setCellValue(""); 
            row.createCell(4).setCellValue(""); 
            row.createCell(5).setCellValue(""); 
            row.createCell(6).setCellValue(""); 
            row.createCell(7).setCellValue(""); 
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=students_class_" + classSubjectId + ".xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
