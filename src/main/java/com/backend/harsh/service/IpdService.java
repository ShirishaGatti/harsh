package com.backend.harsh.service;

import com.backend.harsh.entities.Ipd;
import java.util.List;

public interface IpdService {
    Ipd addIpd(Ipd ipd);
    Ipd getIpdById(Long id);
    Ipd updateIpd(Long id, Ipd ipd);
    void deleteIpd(Long id);
    List<Ipd> getAllIpdRecords();
}
