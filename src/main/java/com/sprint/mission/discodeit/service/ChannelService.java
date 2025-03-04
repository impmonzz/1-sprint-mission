package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
  ChannelDto createChannel(ChannelDto channelDto);
  ChannelDto findChannelById(UUID channelId);
  List<ChannelDto> findAllChannelsByUserId(UUID userId);
  void deleteChannel(UUID channelId);
  ChannelDto updateChannel(UUID channelId, ChannelDto channelDto);
}
