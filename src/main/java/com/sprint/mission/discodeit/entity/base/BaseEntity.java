package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import java.time.Instant;
import java.util.UUID;

@Getter
@MappedSuperclass
public abstract class BaseEntity {
  @Id
  @GeneratedValue
  @Column(nullable = false, updatable = false)
  protected UUID id;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  protected Instant createdAt;

  public BaseEntity() {}

  public BaseEntity(UUID id, Instant createdAt) {
    this.id = id;
    this.createdAt = createdAt;
  }
}
