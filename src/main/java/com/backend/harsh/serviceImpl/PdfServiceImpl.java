package com.backend.harsh.serviceImpl;

import com.backend.harsh.entities.ConsumedItem;
import com.backend.harsh.entities.Ipd;
import com.backend.harsh.entities.SelectedItems;
import com.backend.harsh.repository.ConsumedItemRepository;
import com.backend.harsh.service.PDFService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfServiceImpl implements PDFService {

    @Autowired
    private ConsumedItemRepository consumedItemRepository;

    @Override
    public ByteArrayInputStream generateBill(Long ipdId, String billDate) {
        List<ConsumedItem> consumedItems = consumedItemRepository.findByIpdId(ipdId);

        if (consumedItems.isEmpty()) {
            throw new IllegalArgumentException("No consumed items found for the given IPD");
        }

        Ipd ipd = consumedItems.get(0).getIpd();
        String patientName = ipd.getPatient().getName();
        String patientNo = ipd.getPatient().getId().toString();
        String admissionDate = ipd.getAdmissionDate().toString();
        String dischargeDate = ipd.getDischargeDate() != null ? ipd.getDischargeDate().toString() : "N/A";

        double totalAmount = consumedItems.stream()
                .mapToDouble(ConsumedItem::getTotalCost)
                .sum();

        Document doc = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(doc, out);
            doc.open();

            Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font subHeaderFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font boldFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font regularFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

            PdfPTable headerTable = new PdfPTable(3);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{2, 6, 2});

            PdfPCell regCell = new PdfPCell(new Paragraph("Reg. No: " + patientNo, regularFont));
            regCell.setBorder(Rectangle.NO_BORDER);
            regCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            headerTable.addCell(regCell);

            PdfPCell clinicCell = new PdfPCell(new Paragraph("Harsh Clinic", headerFont));
            clinicCell.setBorder(Rectangle.NO_BORDER);
            clinicCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerTable.addCell(clinicCell);

            PdfPCell phoneCell = new PdfPCell(new Paragraph("Phone: 9860840343", regularFont));
            phoneCell.setBorder(Rectangle.NO_BORDER);
            phoneCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            headerTable.addCell(phoneCell);
            doc.add(headerTable);

            doc.add(new Paragraph("Dr. Vanita Vasant Myakal", subHeaderFont));
            doc.add(new Paragraph("M.B.B.S., M.D. (Community Medicine)", regularFont));
            doc.add(new Paragraph("\n"));

            PdfPTable invoiceTable = new PdfPTable(3);
            invoiceTable.setWidthPercentage(100);
            invoiceTable.setWidths(new float[]{3, 3, 3});

            invoiceTable.addCell(createCell("Bill No: " + ipdId, regularFont, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
            invoiceTable.addCell(createCell("Invoice", headerFont, Element.ALIGN_CENTER, Rectangle.NO_BORDER));
            invoiceTable.addCell(createCell("Date: " + billDate, regularFont, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            doc.add(invoiceTable);
            doc.add(new Paragraph("\n"));

            doc.add(new Paragraph("Patient Name: " + patientName, regularFont));
            doc.add(new Paragraph("Patient No: " + patientNo, regularFont));
            doc.add(new Paragraph("Admission Date: " + admissionDate, regularFont));
            doc.add(new Paragraph("Discharge Date: " + dischargeDate, regularFont));
            doc.add(new Paragraph("\n"));

            PdfPTable itemsTable = new PdfPTable(5);
            itemsTable.setWidthPercentage(100);
            itemsTable.setWidths(new float[]{1, 4, 1, 1, 1});

            itemsTable.addCell(createCell("S. No", boldFont, Element.ALIGN_CENTER, Rectangle.BOX));
            itemsTable.addCell(createCell("Item Name", boldFont, Element.ALIGN_CENTER, Rectangle.BOX));
            itemsTable.addCell(createCell("Qty", boldFont, Element.ALIGN_CENTER, Rectangle.BOX));
            itemsTable.addCell(createCell("Rate", boldFont, Element.ALIGN_CENTER, Rectangle.BOX));
            itemsTable.addCell(createCell("Amount", boldFont, Element.ALIGN_CENTER, Rectangle.BOX));

            int counter = 1;
            for (ConsumedItem item : consumedItems) {
                int serialNumber = counter++;
                double itemTotalCost = 0.0;  // Initialize total cost for each item

                for (SelectedItems selectedItem : item.getSelectedItems()) {
                    double amount = selectedItem.getQuantity() * selectedItem.getPrice();
                    itemTotalCost += amount; // Accumulate total cost

                    itemsTable.addCell(createCell(String.valueOf(serialNumber), regularFont, Element.ALIGN_CENTER, Rectangle.BOX));
                    itemsTable.addCell(createCell(selectedItem.getItemName(), regularFont, Element.ALIGN_CENTER, Rectangle.BOX));
                    itemsTable.addCell(createCell(String.valueOf(selectedItem.getQuantity()), regularFont, Element.ALIGN_CENTER, Rectangle.BOX));
                    itemsTable.addCell(createCell(String.valueOf(selectedItem.getPrice()), regularFont, Element.ALIGN_CENTER, Rectangle.BOX));
                    itemsTable.addCell(createCell(String.valueOf(amount), regularFont, Element.ALIGN_CENTER, Rectangle.BOX));
                }

                // Display item-level total cost (for debugging)
                System.out.println("Total cost for item " + item.getId() + ": " + itemTotalCost);
            }

            // Corrected total amount calculation (outside loop)
             totalAmount = consumedItems.stream()
                .flatMap(ci -> ci.getSelectedItems().stream())  
                .mapToDouble(si -> si.getQuantity() * si.getPrice())  
                .sum();


            doc.add(itemsTable);
//            totalTable.addCell(totalValueCell);
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setWidths(new float[]{9, 1});

            PdfPCell totalLabelCell = new PdfPCell(new Paragraph("Total Amount:", boldFont));
            totalLabelCell.setBorder(Rectangle.BOX);
            totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(totalLabelCell);

            PdfPCell totalValueCell = new PdfPCell(new Paragraph(String.format("₹ %.2f", totalAmount), boldFont));
            totalValueCell.setBorder(Rectangle.BOX);
            totalValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalTable.addCell(totalValueCell);

            doc.add(totalTable);



            PdfPTable footerTable = new PdfPTable(2);
            footerTable.setWidthPercentage(100);
            footerTable.setWidths(new float[]{1, 1});
            doc.add(new Paragraph("\n\n\n\n\n"));
            footerTable.addCell(createCell("Guardian / Care of Sign", regularFont, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
            footerTable.addCell(createCell("Signature for Admin", regularFont, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            doc.add(footerTable);

            doc.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private PdfPCell createCell(String content, Font font, int alignment, int border) {
        PdfPCell cell = new PdfPCell(new Paragraph(content, font));
        cell.setBorder(border);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }
}
