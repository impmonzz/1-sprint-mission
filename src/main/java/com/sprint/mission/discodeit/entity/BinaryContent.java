package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinaryContent {

    @Id
    @GeneratedValue
    private UUID id;

    @Lob
    private byte[] bytes;

    private String fileName;
    private Long size;
    private String contentType;
    private Instant createdAt;
}
