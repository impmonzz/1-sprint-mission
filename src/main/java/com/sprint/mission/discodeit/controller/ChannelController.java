package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    // GET /api/channels/{channelId}
    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelDto> getChannel(@PathVariable UUID channelId) {
        ChannelDto channel = channelService.findChannelById(channelId);
        return ResponseEntity.ok(channel);
    }

    // GET /api/channels?userId=...
    @GetMapping
    public ResponseEntity<List<ChannelDto>> getChannelsByUser(@RequestParam UUID userId) {
        List<ChannelDto> channels = channelService.findAllChannelsByUserId(userId);
        return ResponseEntity.ok(channels);
    }

    // POST /api/channels/private
    @PostMapping(path="/private", consumes = "application/json")
    public ResponseEntity<ChannelDto> createPrivateChannel(@RequestBody ChannelDto channelDto) {
        channelDto.setType("PRIVATE");
        ChannelDto created = channelService.createChannel(channelDto);
        return ResponseEntity.ok(created);  // 200 OK
    }

    // POST /api/channels/public
    @PostMapping(path="/public", consumes = "application/json")
    public ResponseEntity<ChannelDto> createPublicChannel(@RequestBody ChannelDto channelDto) {
        channelDto.setType("PUBLIC");
        ChannelDto created = channelService.createChannel(channelDto);
        return ResponseEntity.ok(created);
    }

    // DELETE /api/channels/{channelId}
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.ok().build();
    }

    // PATCH /api/channels/{channelId}
    @PatchMapping(path="/{channelId}", consumes = "application/json")
    public ResponseEntity<ChannelDto> updateChannel(@PathVariable UUID channelId, @RequestBody ChannelDto channelDto) {
        ChannelDto updated = channelService.updateChannel(channelId, channelDto);
        return ResponseEntity.ok(updated);
    }
}
