package com.backend.harsh.service;

import java.io.ByteArrayInputStream;

public interface PDFService {
    ByteArrayInputStream generateBill(Long billId, String genDate);
}
