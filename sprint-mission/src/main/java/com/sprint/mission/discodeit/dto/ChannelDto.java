package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDto {
    private UUID id;
    private String type; // "PUBLIC" 또는 "PRIVATE"
    private String name;
    private String description;
    private List<UUID> participantIds;
    private Instant lastMessageAt;
}
