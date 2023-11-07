package com.alzzaipo.ipo.application.port.in;


import com.alzzaipo.ipo.domain.Ipo;

import java.util.List;

public interface GetIpoListQuery {
    List<Ipo> getIpoList();
}
