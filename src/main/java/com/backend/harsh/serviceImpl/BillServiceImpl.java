package com.backend.harsh.serviceImpl;

import com.backend.harsh.entities.Bill;
import com.backend.harsh.repository.BillRepository;
import com.backend.harsh.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Override
    public Bill addBill(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public Bill getBillById(Long id) {
        return billRepository.findById(id).orElse(null);
    }

    @Override
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @Override
    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }
}
