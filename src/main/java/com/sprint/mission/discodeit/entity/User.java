package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User extends BaseUpdatableEntity {
  @Column(nullable = false, length = 50, unique = true)
  private String username;
  @Column(nullable = false, length = 100, unique = true)
  private String email;
  @Column(nullable = false, length = 60)
  private String password;
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "status_id")
  private UserStatus status;

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public void update(String newUsername, String newEmail, String newPassword) {
    this.username = newUsername;
    this.email = newEmail;
    this.password = newPassword;
  }

  public void setProfile(BinaryContent profile) {
    this.profile = profile;
  }
}
