package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // GET /api/messages/channel/{channelId}
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<MessageDto>> getMessagesByChannel(@PathVariable UUID channelId) {
        List<MessageDto> messages = messageService.findMessagesByChannelId(channelId);
        return ResponseEntity.ok(messages);
    }

    // GET /api/messages/{messageId}
    @GetMapping("/{messageId}")
    public ResponseEntity<MessageDto> getMessage(@PathVariable UUID messageId) {
        MessageDto message = messageService.findMessageById(messageId);
        return ResponseEntity.ok(message);
    }

    // PUT /api/messages/{messageId}
    @PutMapping(path="/{messageId}", consumes = "application/json")
    public ResponseEntity<MessageDto> updateMessage(@PathVariable UUID messageId,
                                                    @RequestBody MessageDto messageDto) {
        MessageDto updated = messageService.updateMessage(messageId, messageDto);
        return ResponseEntity.ok(updated);
    }

    // POST /api/messages
    @PostMapping(consumes = "application/json")
    public ResponseEntity<MessageDto> createMessage(@RequestBody MessageCreateRequest request) {
        MessageDto created = messageService.createMessage(request);
        return ResponseEntity.ok(created);
    }

    // DELETE /api/messages/{messageId}
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }
}
