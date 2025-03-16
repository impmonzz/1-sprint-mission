package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Transactional
  @Override
  public Message create(MessageCreateRequest request, List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID channelId = request.channelId();
    UUID authorId = request.authorId();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("Channel not found: " + channelId));
    User author = userRepository.findById(authorId)
        .orElseThrow(() -> new NoSuchElementException("Author not found: " + authorId));
    Message message = new Message(request.content(), channel, author);
    List<BinaryContent> attachments = new ArrayList<>();
    if (binaryContentCreateRequests != null) {
      for (BinaryContentCreateRequest att : binaryContentCreateRequests) {
        BinaryContent bc = new BinaryContent(att.fileName(), (long) att.bytes().length, att.contentType());
        bc = binaryContentRepository.save(bc);
        attachments.add(bc);
      }
      message.setAttachments(attachments);
    }
    return messageRepository.save(message);
  }

  @Override
  public Message find(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("Message not found: " + messageId));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId);
  }

  @Transactional
  @Override
  public Message update(UUID messageId, MessageUpdateRequest request) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("Message not found: " + messageId));
    message.updateContent(request.newContent());
    return messageRepository.save(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("Message not found: " + messageId));
    if (message.getAttachments() != null) {
      for (BinaryContent bc : message.getAttachments()) {
        binaryContentRepository.deleteById(bc.getId());
      }
    }
    messageRepository.deleteById(messageId);
  }
}
