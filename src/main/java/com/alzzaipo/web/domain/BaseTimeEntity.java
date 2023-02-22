package com.alzzaipo.web.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass   // 자식 엔티티가 해당 클래스를 상속할 경우 필드들을 칼럼으로 인식하도록 함
@EntityListeners(AuditingEntityListener.class)  // Auditing 기능을 포함시킴
public class BaseTimeEntity {

    @CreatedDate    // 엔티티가 생성되어 저장될 때 시간이 자동으로 저장됨
    private LocalDateTime createdDate;

    @LastModifiedDate   // 조회한 엔티티의 값을 변경할 때 시간이 자동으로 저장됨
    private LocalDateTime modifiedDate;
}
