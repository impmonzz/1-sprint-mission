package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.Instant;
import java.util.UUID;

@Getter
@MappedSuperclass
public abstract class BaseUpdatableEntity extends BaseEntity {
  @LastModifiedDate
  @Column(nullable = false)
  protected Instant updatedAt;

  public BaseUpdatableEntity() {
    super();
  }

  public BaseUpdatableEntity(UUID id, Instant createdAt, Instant updatedAt) {
    super(id, createdAt);
    this.updatedAt = updatedAt;
  }
}
