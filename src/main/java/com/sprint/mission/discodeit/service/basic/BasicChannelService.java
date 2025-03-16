package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasicChannelService implements ChannelService {
  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  private final ChannelMapper channelMapper;
  public BasicChannelService(ChannelRepository channelRepository, ReadStatusRepository readStatusRepository, UserRepository userRepository, MessageRepository messageRepository, ChannelMapper channelMapper) {
    this.channelRepository = channelRepository;
    this.readStatusRepository = readStatusRepository;
    this.userRepository = userRepository;
    this.messageRepository = messageRepository;
    this.channelMapper = channelMapper;
  }
  @Transactional
  @Override
  public Channel create(PublicChannelCreateRequest request) {
    Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());
    return channelRepository.save(channel);
  }
  @Transactional
  @Override
  public Channel create(PrivateChannelCreateRequest request) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    channel = channelRepository.save(channel);
    for (UUID userId : request.participantIds()) {
      User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
      ReadStatus rs = new ReadStatus(user, channel, Instant.now());
      readStatusRepository.save(rs);
    }
    return channel;
  }
  @Transactional(readOnly = true)
  @Override
  public ChannelDto find(UUID channelId) {
    Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("Channel not found: " + channelId));
    return channelMapper.toDto(channel);
  }
  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<ReadStatus> statuses = readStatusRepository.findAllByUserId(userId);
    List<Channel> channels = statuses.stream().map(ReadStatus::getChannel).distinct().collect(Collectors.toList());
    return channels.stream().map(channelMapper::toDto).collect(Collectors.toList());
  }
  @Transactional
  @Override
  public Channel update(UUID channelId, PublicChannelUpdateRequest request) {
    Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("Channel not found: " + channelId));
    if (channel.getType() == ChannelType.PRIVATE) {
      throw new RuntimeException("Private channel cannot be updated");
    }
    channel.updateChannelInfo(request.newName(), request.newDescription());
    return channelRepository.save(channel);
  }
  @Transactional
  @Override
  public void delete(UUID channelId) {
    Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("Channel not found: " + channelId));
    messageRepository.deleteAll(messageRepository.findAllByChannelId(channel.getId()));
    readStatusRepository.deleteAll(readStatusRepository.findAllByChannel(channel));
    channelRepository.deleteById(channelId);
  }
}
