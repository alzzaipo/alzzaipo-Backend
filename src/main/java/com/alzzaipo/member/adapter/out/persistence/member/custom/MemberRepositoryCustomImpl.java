package com.alzzaipo.member.adapter.out.persistence.member.custom;

import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

	private final EntityManager entityManager;

	@Override
	public MemberJpaEntity findEntityById(Long id) throws CustomException {
		TypedQuery<MemberJpaEntity> query = entityManager.createQuery(
			"SELECT m FROM MemberJpaEntity m WHERE m.id = :id", MemberJpaEntity.class);
		query.setParameter("id", id);

		List<MemberJpaEntity> resultList = query.getResultList();

		if (resultList.isEmpty()) {
			throw new CustomException(HttpStatus.NOT_FOUND, "회원 조회 실패");
		}
		return resultList.get(0);
	}
}
