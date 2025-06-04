package com.backend.harsh.serviceImpl;

import com.backend.harsh.entities.Opd;
import com.backend.harsh.repository.OpdRepository;
import com.backend.harsh.service.OpdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpdServiceImpl implements OpdService {

    @Autowired
    private final OpdRepository opdRepository;

    public OpdServiceImpl(OpdRepository opdRepository) {
        this.opdRepository = opdRepository;
    }

    @Override
    public Opd addOpd(Opd opd) {
        return opdRepository.save(opd);
    }

    @Override
    public Opd getOpdById(Long id) {
        return opdRepository.findById(id).orElse(null);
    }

    @Override
    public Opd updateOpd(Long id, Opd opd) {
        if (opdRepository.existsById(id)) {
            opd.setId(id);
            return opdRepository.save(opd);
        }
        return null;
    }

    @Override
    public void deleteOpd(Long id) {
        if (opdRepository.existsById(id)) {
            opdRepository.deleteById(id);
        }
    }

    @Override
    public List<Opd> getAllOpdRecords() {
        return opdRepository.findAll();
    }
}
