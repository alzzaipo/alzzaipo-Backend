package com.alzzaipo.hexagonal.ipo.application.port.out;

import com.alzzaipo.hexagonal.ipo.application.port.out.dto.UpdateListedIpoCommand;

public interface UpdateListedIpoPort {

    void updateListedIpo(UpdateListedIpoCommand command);
}
