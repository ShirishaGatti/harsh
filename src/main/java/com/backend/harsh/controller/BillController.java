package com.backend.harsh.controller;

import com.backend.harsh.entities.Bill;
import com.backend.harsh.service.BillService;
import com.backend.harsh.service.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bills")
@CrossOrigin(
	    origins = "http://localhost:5173",
	    allowedHeaders = "*",
	    allowCredentials = "true"
	)
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private PDFService pdfService;

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        List<Bill> bills = billService.getAllBills();
        if (bills.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(Optional.of(bills));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBill(@PathVariable("id") Long id) {
        Bill bill = billService.getBillById(id);
        if (bill == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(Optional.of(bill));
    }

    @PostMapping("/add")
    public ResponseEntity<Bill> addBill(@RequestBody Bill bill) {
        try {
            Bill newBill = billService.addBill(bill);
            return ResponseEntity.status(HttpStatus.CREATED).body(newBill);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBill(@PathVariable("id") Long id) {
        try {
            billService.deleteBill(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadBill(@PathVariable Long id) throws IOException {
        Date generationDate = new Date();
        String date = generationDate.toString();
        ByteArrayInputStream bis = pdfService.generateBill(id, date);
        byte[] billBytes = bis.readAllBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Bill_" + id + ".pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(billBytes);
    }
}
