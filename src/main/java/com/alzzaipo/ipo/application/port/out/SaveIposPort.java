package com.alzzaipo.ipo.application.port.out;

import com.alzzaipo.ipo.domain.Ipo;
import java.util.List;

public interface SaveIposPort {

    void saveAll(List<Ipo> ipos);

}
