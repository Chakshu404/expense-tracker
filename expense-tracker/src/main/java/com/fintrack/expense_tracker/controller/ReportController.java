package com.fintrack.expense_tracker.controller;

import com.fintrack.expense_tracker.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/csv")
    public ResponseEntity<InputStreamResource> downloadCSVReport(
            @RequestParam Long userId,
            @RequestParam(value = "category", required = false) String category) {
        ByteArrayInputStream csvStream = reportService.generateCSVReport(userId, category);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Vesta_Expense_Report.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(csvStream));
    }

    @GetMapping("/pdf")
    public ResponseEntity<InputStreamResource> downloadPDFReport(
            @RequestParam Long userId,
            @RequestParam(value = "category", required = false) String category) {
        ByteArrayInputStream pdfStream = reportService.generatePDFReport(userId, category);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Vesta_Expense_Statement.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }
}
