/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.myapp.pojo.Score;
import com.myapp.pojo.StudentClassSubject;
import com.myapp.pojo.User;
import com.myapp.services.ScoreService;
import com.myapp.services.StudentClassSubjectService;
import com.myapp.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiScoreController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentClassSubjectService stuClassSubjectService;

    // api/scores/{id}
    @DeleteMapping("/scores/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        this.scoreService.deleleScore(id);
    }

    // api/scores – Danh sách các điểm 
    @GetMapping("/scores")
    public ResponseEntity<List<Score>> listScores(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(scoreService.getScores(params), HttpStatus.OK);
    }

    // api/scores/{id} – Chi tiết 
    @GetMapping("/scores/{id}")
    public ResponseEntity<Score> retrieve(@PathVariable(value = "id") int id) {
        return new ResponseEntity<>(scoreService.getScoreById(id), HttpStatus.OK);
    }

    @PutMapping("/scores/lock/{classSubjectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void lockScores(@PathVariable(value = "classSubjectId") int classSubjectId) {
        this.scoreService.lockScoresByClassSubjectId(classSubjectId);
    }

    @PatchMapping("/scores/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateScore(@PathVariable("id") int id, @RequestBody Score sc) {
        Score current = this.scoreService.getScoreById(id);
        if (!"draft".equalsIgnoreCase(current.getLockStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Điểm đã khóa không thay đổi được");
        }

        if (sc.getMidtermScore() != null) {
            current.setMidtermScore(sc.getMidtermScore());
        }
        if (sc.getFinalScore() != null) {
            current.setFinalScore(sc.getFinalScore());
        }
        if (sc.getExtraScore1() != null) {
            current.setExtraScore1(sc.getExtraScore1());
        }
        if (sc.getExtraScore2() != null) {
            current.setExtraScore2(sc.getExtraScore2());
        }
        if (sc.getExtraScore3() != null) {
            current.setExtraScore3(sc.getExtraScore3());
        }
        if (sc.getLockStatus() != null) {
            current.setLockStatus(sc.getLockStatus());
        }

        this.scoreService.addOrUpdateScore(current);
    }

    @PostMapping("/scores/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addScore(@RequestBody Score sc) {
        this.scoreService.addOrUpdateScore(sc);
    }

    // chi tiết điểm của từng môn học của học sinh
    @GetMapping("/scores/stuClassSubject/{studentClassSubjectId}")
    public ResponseEntity<Score> getScoreByStuClassSubjectId(@PathVariable(value = "studentClassSubjectId") int studentClassSubjectId) {
        return new ResponseEntity<>(scoreService.getScoreByStuClassSubjectId(studentClassSubjectId), HttpStatus.OK);
    }

    // Lấy danh sách điểm theo classSubjectId
    @GetMapping("/scores/classSubject/{classSubjectId}")
    public ResponseEntity<List<Score>> getScoresByClassSubjectId(@PathVariable(value = "classSubjectId") int classSubjectId) {
        return new ResponseEntity<>(scoreService.getScoresByClassSubjectId(classSubjectId), HttpStatus.OK);
    }

    // Lấy danh sách điểm theo studentId
     @GetMapping("/scores/student/{studentId}")
    public ResponseEntity<List<Score>> getScoresByStudentId(@PathVariable(value = "studentId") Integer studentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User author = userService.getUserByEmail(email);
        if (author == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (author.getRole().equals("ROLE_STUDENT") && !author.getId().equals(studentId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); 
        }

        return new ResponseEntity<>(scoreService.getScoresByStudentId(studentId), HttpStatus.OK);
    }

    //Nhập điểm theo lớp môn học bằng file excel
    @PostMapping("/scores/import/{classSubjectId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void importScoresFromExcel(@RequestParam("file") MultipartFile file,
            @PathVariable("classSubjectId") int classSubjectId) {
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                Cell studentCodeCell = row.getCell(1);
                if (studentCodeCell == null) {
                    // Bỏ qua dòng này vì không có mã sinh viên
                    continue;
                }
                String studentCode = row.getCell(1).getStringCellValue().trim();
                Float midtermScore = getFloatFromCell(row.getCell(3));
                Float finalScore = getFloatFromCell(row.getCell(4));
                Float extraScore1 = getFloatFromCell(row.getCell(5));
                Float extraScore2 = getFloatFromCell(row.getCell(6));
                Float extraScore3 = getFloatFromCell(row.getCell(7));

                // Tìm StudentClassSubject theo studentCode và classSubjectId
                StudentClassSubject scs = stuClassSubjectService.getByStuCodeAndClassSubjectId(studentCode, classSubjectId);
                if (scs == null) {
                    continue;
                }

                // Kiểm tra đã có điểm chưa
                Score score = scoreService.getScoreByStuClassSubjectId(scs.getId());

                // Nếu đã có và bị khóa, bỏ qua dòng này
                if (score != null && score.getLockStatus().equalsIgnoreCase("locked")) {
                    continue;
                }

                // Nếu chưa có thì tạo mới
                if (score == null) {
                    score = new Score();
                    score.setStudentClassSubjectId(scs);
                }

                // Cập nhật các điểm
                score.setMidtermScore(midtermScore);
                score.setFinalScore(finalScore);
                score.setExtraScore1(extraScore1);
                score.setExtraScore2(extraScore2);
                score.setExtraScore3(extraScore3);

                scoreService.addOrUpdateScore(score);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Import thất bại!");
        }
    }

    private Float getFloatFromCell(Cell cell) {
        try {
            if (cell == null) {
                return null;
            }
            if (cell.getCellType() == CellType.NUMERIC) {
                return (float) cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                return Float.parseFloat(cell.getStringCellValue().trim());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    //Lấy danh sách điểm theo lớp môn học bằng file excel
    @GetMapping("/scores/export-excel/{classSubjectId}")
    public void exportScoreList(@PathVariable("classSubjectId") int classSubjectId, HttpServletResponse response) throws IOException {
        List<StudentClassSubject> list = stuClassSubjectService.getStuClassSubjectByClassSubjectId(classSubjectId);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Scores");

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
        header.createCell(8).setCellValue("Trạng thái khóa");

        int rowIdx = 1;
        for (StudentClassSubject scs : list) {
            Row row = sheet.createRow(rowIdx);

            // Lấy điểm nếu có
            Score score = scoreService.getScoreByStuClassSubjectId(scs.getId());

            row.createCell(0).setCellValue(rowIdx);
            row.createCell(1).setCellValue(scs.getStudentId().getStudentCode());
            row.createCell(2).setCellValue(scs.getStudentId().getFirstName() + " " + scs.getStudentId().getLastName());
            setCellValueOrBlank(row, 3, score != null ? score.getMidtermScore() : null);
            setCellValueOrBlank(row, 4, score != null ? score.getFinalScore() : null);
            setCellValueOrBlank(row, 5, score != null ? score.getExtraScore1() : null);
            setCellValueOrBlank(row, 6, score != null ? score.getExtraScore2() : null);
            setCellValueOrBlank(row, 7, score != null ? score.getExtraScore3() : null);
            row.createCell(8).setCellValue(score != null && score.getLockStatus() != null ? score.getLockStatus() : "draft");

            rowIdx++;
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=scores_class_" + classSubjectId + ".xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private void setCellValueOrBlank(Row row, int columnIndex, Float value) {
        Cell cell = row.createCell(columnIndex);
        if (value != null) {
            cell.setCellValue(value);
        } else {
            cell.setBlank();
        }
    }

    //Lấy danh sách điểm theo lớp môn học bằng file pdf 
    @GetMapping("/scores/export-pdf/{classSubjectId}")
    public void exportScoreListAsPdf(@PathVariable("classSubjectId") int classSubjectId, HttpServletResponse response) throws IOException, DocumentException {
        List<StudentClassSubject> list = stuClassSubjectService.getStuClassSubjectByClassSubjectId(classSubjectId);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=scores_class_" + classSubjectId + ".pdf");

        Document document = new Document(PageSize.A4); // Landscape khổ ngang
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        InputStream fontStream = getClass().getResourceAsStream("/fonts/arial.ttf");
        byte[] fontBytes = IOUtils.toByteArray(fontStream);

        BaseFont baseFont = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontBytes, null);
        Font headerFont = new Font(baseFont, 12, Font.BOLD);
        Font cellFont = new Font(baseFont, 10, Font.NORMAL);

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        // Tiêu đề cột
        String[] headers = {
            "STT", "Mã số sinh viên", "Họ và tên sinh viên", "Điểm giữa kì", "Điểm cuối kì",
            "Điểm thêm 1", "Điểm thêm 2", "Điểm thêm 3", "Trạng thái khóa"
        };

        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        // Dữ liệu từng sinh viên
        int stt = 1;
        for (StudentClassSubject scs : list) {
            Score score = scoreService.getScoreByStuClassSubjectId(scs.getId());

            table.addCell(new PdfPCell(new Phrase(String.valueOf(stt++), cellFont)));
            table.addCell(new PdfPCell(new Phrase(scs.getStudentId().getStudentCode(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(scs.getStudentId().getFirstName() + " " + scs.getStudentId().getLastName(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(score != null && score.getMidtermScore() != null ? score.getMidtermScore().toString() : "", cellFont)));
            table.addCell(new PdfPCell(new Phrase(score != null && score.getFinalScore() != null ? score.getFinalScore().toString() : "", cellFont)));
            table.addCell(new PdfPCell(new Phrase(score != null && score.getExtraScore1() != null ? score.getExtraScore1().toString() : "", cellFont)));
            table.addCell(new PdfPCell(new Phrase(score != null && score.getExtraScore2() != null ? score.getExtraScore2().toString() : "", cellFont)));
            table.addCell(new PdfPCell(new Phrase(score != null && score.getExtraScore3() != null ? score.getExtraScore3().toString() : "", cellFont)));
            table.addCell(new PdfPCell(new Phrase(score != null && score.getLockStatus() != null ? score.getLockStatus() : "draft", cellFont)));
        }

        document.add(table);
        document.close();
    }
    

}
