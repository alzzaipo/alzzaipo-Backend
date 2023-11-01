package com.alzzaipo.hexagonal.ipo.application.port.in;


import com.alzzaipo.hexagonal.ipo.domain.Ipo;

import java.util.List;

public interface GetIpoListQuery {
    List<Ipo> getIpoList();
}
