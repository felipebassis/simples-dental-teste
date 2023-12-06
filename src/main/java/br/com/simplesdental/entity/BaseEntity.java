package br.com.simplesdental.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@ToString
@MappedSuperclass
@EqualsAndHashCode
public abstract class BaseEntity {

    @CreationTimestamp
    @Column(name = "created_date", columnDefinition = "timestamp", nullable = false, updatable = false)
    private LocalDateTime createdDate;

}
