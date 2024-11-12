package com.Non_academicWebsite.ExcelConverter;


import com.Non_academicWebsite.Entity.Forms.Forms;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelConverterService {
    public void exportToExcel(HttpServletResponse response, List<? extends Forms> dataList) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Staff Data");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("UserId");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Department");
        header.createCell(3).setCellValue("Faculty");
        header.createCell(4).setCellValue("Form Type");
        header.createCell(5).setCellValue("Status");
        header.createCell(6).setCellValue("Leave At");
        header.createCell(7).setCellValue("Leave Days");

        int rowCount = 1;

        for (Forms data : dataList) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(data.getUser().getId());
            row.createCell(1).setCellValue(data.getUser().getFirst_name());
            row.createCell(2).setCellValue(data.getUser().getDepartment());
            row.createCell(3).setCellValue(data.getUser().getFaculty());
            row.createCell(4).setCellValue(data.getFormType());
            row.createCell(5).setCellValue(data.getStatus());
            row.createCell(6).setCellValue(data.getLeaveAt());
            row.createCell(7).setCellValue(data.getLeaveDays());

        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=staff_data.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}

