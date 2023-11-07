package com.alzzaipo.hexagonal.ipo.application.port.out;

import com.alzzaipo.hexagonal.ipo.domain.Ipo;

import java.util.List;

public interface FindNotListedIposPort {

    List<Ipo> findNotListedIpos();
}
