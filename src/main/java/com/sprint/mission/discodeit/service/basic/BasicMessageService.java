package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public MessageDto createMessage(MessageCreateRequest request) {
        Message message = new Message();
        message.setContent(request.getContent());
        message.setChannelId(request.getChannelId());
        message.setAuthorId(request.getAuthorId());
        message.setCreatedAt(Instant.now());
        message.setUpdatedAt(Instant.now());
        Message saved = messageRepository.save(message);
        return new MessageDto(
                saved.getId(),
                saved.getContent(),
                saved.getCreatedAt(),
                saved.getUpdatedAt(),
                saved.getChannelId(),
                saved.getAuthorId()
        );
    }

    @Override
    public MessageDto updateMessage(UUID messageId, MessageDto messageDto) {
        Message existing = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        existing.setContent(messageDto.getContent());
        existing.setUpdatedAt(Instant.now());
        Message updated = messageRepository.save(existing);
        return new MessageDto(
                updated.getId(),
                updated.getContent(),
                updated.getCreatedAt(),
                updated.getUpdatedAt(),
                updated.getChannelId(),
                updated.getAuthorId()
        );
    }

    @Override
    public MessageDto findMessageById(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        return new MessageDto(
                message.getId(),
                message.getContent(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getChannelId(),
                message.getAuthorId()
        );
    }

    @Override
    public List<MessageDto> findMessagesByChannelId(UUID channelId) {
        List<Message> messages = messageRepository.findByChannelId(channelId);
        return messages.stream()
                .map(m -> new MessageDto(
                        m.getId(),
                        m.getContent(),
                        m.getCreatedAt(),
                        m.getUpdatedAt(),
                        m.getChannelId(),
                        m.getAuthorId()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMessage(UUID messageId) {
        messageRepository.deleteById(messageId);
    }
}
