package com.alzzaipo.hexagonal.ipo.application.service;

import com.alzzaipo.hexagonal.ipo.adapter.out.persistence.IpoPersistenceAdapter;
import com.alzzaipo.hexagonal.ipo.application.port.in.GetIpoListQuery;
import com.alzzaipo.hexagonal.ipo.domain.Ipo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetIpoListService implements GetIpoListQuery {

    private final IpoPersistenceAdapter ipoPersistenceAdapter;

    @Override
    public List<Ipo> getIpoList() {
        return ipoPersistenceAdapter.findIpoList();
    }
}
