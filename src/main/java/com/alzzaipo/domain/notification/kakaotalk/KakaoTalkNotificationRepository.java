package com.alzzaipo.domain.notification.kakaotalk;

import com.alzzaipo.domain.dto.PhoneInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface KakaoTalkNotificationRepository extends JpaRepository<KakaoTalkNotification, Long> {

    @Query("SELECT new com.alzzaipo.domain.dto.PhoneInfoDto(k.countryCode, k.phoneNumber) " +
            "FROM KakaoTalkNotification k " +
            "WHERE k.member.accountId = :accountId")
    Optional<PhoneInfoDto> findPhoneInfoDtoByAccountId(String accountId);
}
