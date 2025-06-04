package com.backend.harsh.service;

import com.backend.harsh.entities.Bill;
import java.util.List;

public interface BillService {
    Bill addBill(Bill bill);
    Bill getBillById(Long id);
    List<Bill> getAllBills();
    void deleteBill(Long id);
}
