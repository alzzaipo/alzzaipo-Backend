package com.alzzaipo.ipo.application.port.out;

import com.alzzaipo.ipo.application.port.out.dto.UpdateListedIpoCommand;

public interface UpdateListedIpoPort {

    void updateListedIpo(UpdateListedIpoCommand command);
}
