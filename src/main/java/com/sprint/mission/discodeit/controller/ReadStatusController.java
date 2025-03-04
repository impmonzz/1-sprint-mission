package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    // DELETE /api/readStatuses/{readStatusId}
    @DeleteMapping("/{readStatusId}")
    public ResponseEntity<Void> deleteReadStatus(@PathVariable UUID readStatusId) {
        readStatusService.deleteReadStatus(readStatusId);
        return ResponseEntity.ok().build();
    }

    // GET /api/readStatuses/{userId}/channel/{channelId}
    @GetMapping("/{userId}/channel/{channelId}")
    public ResponseEntity<ReadStatusDto> getReadStatus(@PathVariable UUID userId, @PathVariable UUID channelId) {
        ReadStatusDto readStatus = readStatusService.findLastReadMessage(userId, channelId);
        return ResponseEntity.ok(readStatus);
    }

    // POST /api/readStatuses
    @PostMapping(consumes = "application/json")
    public ResponseEntity<ReadStatusDto> createReadStatus(@RequestBody ReadStatusDto readStatusDto) {
        ReadStatusDto created = readStatusService.createReadStatus(
                readStatusDto.getUserId(), readStatusDto.getChannelId(), readStatusDto.getLastReadAt()
        );
        return ResponseEntity.ok(created);
    }

    // PATCH /api/readStatuses/{readStatusId}
    @PatchMapping(path="/{readStatusId}", consumes = "application/json")
    public ResponseEntity<ReadStatusDto> updateReadStatus(@PathVariable UUID readStatusId,
                                                          @RequestBody ReadStatusUpdateRequest updateRequest) {
        readStatusService.updateReadStatus(readStatusId, updateRequest.getLastReadAt());
        ReadStatusDto updated = readStatusService.findReadStatusById(readStatusId);
        return ResponseEntity.ok(updated);
    }
}
