package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "channels") // "user" 예약어 충돌을 피하기 위해 테이블명을 변경
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

    @Id
    @GeneratedValue
    private UUID id;

    private String type; // "PUBLIC" 또는 "PRIVATE"
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    // participantIds 저장 – 간단하게 ElementCollection 사용
    @ElementCollection
    private List<UUID> participantIds;

    private Instant lastMessageAt;
}
