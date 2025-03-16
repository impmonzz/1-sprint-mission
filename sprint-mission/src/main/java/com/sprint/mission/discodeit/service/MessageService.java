package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import java.util.List;
import java.util.UUID;

public interface MessageService {
  MessageDto createMessage(MessageCreateRequest request);
  MessageDto updateMessage(UUID messageId, MessageDto messageDto);
  MessageDto findMessageById(UUID messageId);
  List<MessageDto> findMessagesByChannelId(UUID channelId);
  void deleteMessage(UUID messageId);
}
