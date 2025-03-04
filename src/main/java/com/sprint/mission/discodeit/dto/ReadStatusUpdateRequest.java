package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadStatusUpdateRequest {
    private UUID id;
    private UUID channelId;
    private UUID userId;
    private Instant lastReadAt;
}
