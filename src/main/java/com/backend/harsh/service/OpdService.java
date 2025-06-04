package com.backend.harsh.service;

import com.backend.harsh.entities.Opd;
import java.util.List;

public interface OpdService {
    Opd addOpd(Opd opd);
    Opd getOpdById(Long id);
    Opd updateOpd(Long id, Opd opd);
    void deleteOpd(Long id);
    List<Opd> getAllOpdRecords();
}
