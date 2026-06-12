package com.fintrack.expense_tracker.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.fintrack.expense_tracker.model.Expense;
import com.fintrack.expense_tracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public ByteArrayInputStream generateCSVReport(Long userId, String category) {
        List<Expense> expenses;
        if (category == null || "all".equalsIgnoreCase(category)) {
            expenses = expenseRepository.findByUserIdOrderByDateDesc(userId);
        } else {
            expenses = expenseRepository.findByUserIdAndCategoryOrderByDateDesc(userId, category);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ID,Merchant Name,Billing Date,Category,Amount (INR),Tax Paid (INR)\n");

        for (Expense exp : expenses) {
            sb.append(exp.getId()).append(",")
              .append("\"").append(exp.getMerchant().replace("\"", "\"\"")).append("\",")
              .append(exp.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE)).append(",")
              .append(exp.getCategory()).append(",")
              .append(exp.getAmount()).append(",")
              .append(exp.getTax() != null ? exp.getTax() : 0.0).append("\n");
        }

        return new ByteArrayInputStream(sb.toString().getBytes());
    }

    public ByteArrayInputStream generatePDFReport(Long userId, String category) {
        List<Expense> expenses;
        if (category == null || "all".equalsIgnoreCase(category)) {
            expenses = expenseRepository.findByUserIdOrderByDateDesc(userId);
        } else {
            expenses = expenseRepository.findByUserIdAndCategoryOrderByDateDesc(userId, category);
        }

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Headers Styling
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, new Color(139, 92, 246));
            Font subFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
            Font tableBodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);

            Paragraph title = new Paragraph("VESTA AI EXPENSE REPORT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph subtitle = new Paragraph("Tax-Compliant Financial Intelligence Expense Summary Statement", subFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(25);
            document.add(subtitle);

            // Statement Metadata
            Paragraph meta = new Paragraph();
            meta.add(new Paragraph("Account Holder ID: " + userId));
            meta.add(new Paragraph("Category Filter: " + (category == null ? "ALL" : category.toUpperCase())));
            meta.add(new Paragraph("Statement Generated On: " + java.time.LocalDate.now().toString()));
            meta.setSpacingAfter(20);
            document.add(meta);

            // Table setup
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.0f, 2.5f, 1.5f, 1.5f, 1.5f, 1.0f});

            String[] headers = {"ID", "Merchant", "Date", "Category", "Amount", "Tax"};
            Color headerBg = new Color(30, 41, 59); // dark slate

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Paragraph(header, tableHeaderFont));
                cell.setBackgroundColor(headerBg);
                cell.setPadding(8);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            double totalAmt = 0.0;
            double totalTax = 0.0;

            for (Expense exp : expenses) {
                table.addCell(new PdfPCell(new Paragraph(exp.getId().toString(), tableBodyFont)));
                table.addCell(new PdfPCell(new Paragraph(exp.getMerchant(), tableBodyFont)));
                table.addCell(new PdfPCell(new Paragraph(exp.getDate().toString(), tableBodyFont)));
                table.addCell(new PdfPCell(new Paragraph(exp.getCategory(), tableBodyFont)));
                
                double amt = exp.getAmount();
                double tax = exp.getTax() != null ? exp.getTax() : 0.0;
                totalAmt += amt;
                totalTax += tax;

                table.addCell(new PdfPCell(new Paragraph("₹" + String.format("%.2f", amt), tableBodyFont)));
                table.addCell(new PdfPCell(new Paragraph("₹" + String.format("%.2f", tax), tableBodyFont)));
            }

            document.add(table);

            // Totals layout
            Paragraph totals = new Paragraph();
            totals.setSpacingBefore(20);
            totals.setAlignment(Element.ALIGN_RIGHT);
            totals.add(new Paragraph("Pre-Tax Spending: ₹" + String.format("%.2f", (totalAmt - totalTax))));
            totals.add(new Paragraph("Tax Aggregates: ₹" + String.format("%.2f", totalTax)));
            totals.add(new Paragraph("Combined Total: ₹" + String.format("%.2f", totalAmt), 
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(139, 92, 246))));
            document.add(totals);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error occurred generating PDF statement report", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
