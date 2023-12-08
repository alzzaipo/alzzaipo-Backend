package com.alzzaipo.member.adapter.out.persistence.member.custom;

import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;

public interface MemberRepositoryCustom {

	MemberJpaEntity findEntityById(Long id) throws CustomException;
}
