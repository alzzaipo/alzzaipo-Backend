package com.alzzaipo.hexagonal.ipo.application.port.in;

import java.util.List;

public interface GetIpoAgentListQuery {

    List<String> getIpoAgentList(int stockCode);
}
