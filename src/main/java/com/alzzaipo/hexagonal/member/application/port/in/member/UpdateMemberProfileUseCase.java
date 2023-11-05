package com.alzzaipo.hexagonal.member.application.port.in.member;

import com.alzzaipo.hexagonal.member.application.port.in.dto.UpdateMemberProfileCommand;

public interface UpdateMemberProfileUseCase {

    void updateMemberProfile(UpdateMemberProfileCommand command);
}
