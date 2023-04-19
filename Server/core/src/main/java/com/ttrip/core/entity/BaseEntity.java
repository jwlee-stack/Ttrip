package com.ttrip.core.entity;

/**
 * BaseEntity
 * @MappedSuperclass : JPA Entity 클래스들이 해당 추상클래스를 상속할 경우 내부 필드를 컬럼으로 인식
 * @EntityListeners(value = AuditingEntityListener.class)
 *      : 해당 클래스에 Auditing 기능 추가
 *      : Entity lifeCycle과 관련된 이벤트 listen 기능 추가
 *
 */


import com.ttrip.core.entity.listener.Auditable;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
public class BaseEntity implements Auditable {

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
