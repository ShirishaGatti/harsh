package com.backend.harsh.serviceImpl;

import com.backend.harsh.entities.Ipd;
import com.backend.harsh.repository.IpdRepository;
import com.backend.harsh.service.IpdService;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IpdServiceImpl implements IpdService {

    @Autowired
    private final IpdRepository ipdRepository;

    public IpdServiceImpl(IpdRepository ipdRepository) {
        this.ipdRepository = ipdRepository;
    }

    @Override
    public Ipd addIpd(Ipd ipd) {
        return ipdRepository.save(ipd);
    }

    @Override
    public Ipd getIpdById(Long id) {
        return ipdRepository.findById(id).orElse(null);
    }

    @Override
    public Ipd updateIpd(Long id, Ipd ipd) {
        if (ipdRepository.existsById(id)) {
            ipd.setId(id);
            return ipdRepository.save(ipd);
        }
        return null;
    }

    @Override
    public void deleteIpd(Long id) {
        if (ipdRepository.existsById(id)) {
            ipdRepository.deleteById(id);
        }
    }

//    @Override
//    public List<Ipd> getAllIpdRecords() {
//        return ipdRepository.findAll();
//    }
    
    @Override
    public List<Ipd> getAllIpdRecords() {
        List<Ipd> ipds = ipdRepository.findAll();
        ipds.forEach(ipd -> {
            Hibernate.initialize(ipd.getConsumedItems());
            ipd.getConsumedItems().forEach(ci -> Hibernate.initialize(ci.getSelectedItems()));
        });
        return ipds;
    }
    
}
