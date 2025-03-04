package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    @Override
    public ChannelDto createChannel(ChannelDto channelDto) {
        // 기본 생성자 + setter 사용
        Channel channel = new Channel();
        channel.setId(UUID.randomUUID());
        channel.setType(channelDto.getType());
        channel.setName(channelDto.getName());
        channel.setDescription(channelDto.getDescription());
        channel.setCreatedAt(Instant.now());
        channel.setUpdatedAt(Instant.now());
        channel.setParticipantIds(channelDto.getParticipantIds());
        channel.setLastMessageAt(null);
        Channel saved = channelRepository.save(channel);
        return new ChannelDto(
                saved.getId(),
                saved.getType(),
                saved.getName(),
                saved.getDescription(),
                saved.getParticipantIds(),
                saved.getLastMessageAt()
        );
    }

    @Override
    public ChannelDto findChannelById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found with id: " + channelId));
        return new ChannelDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                channel.getParticipantIds(),
                channel.getLastMessageAt()
        );
    }

    @Override
    public List<ChannelDto> findAllChannelsByUserId(UUID userId) {
        Iterable<Channel> allChannels = channelRepository.findAll();
        List<ChannelDto> result = new ArrayList<>();
        for (Channel c : allChannels) {
            if (c.getParticipantIds() != null && c.getParticipantIds().contains(userId)) {
                result.add(new ChannelDto(
                        c.getId(),
                        c.getType(),
                        c.getName(),
                        c.getDescription(),
                        c.getParticipantIds(),
                        c.getLastMessageAt()
                ));
            }
        }
        return result;
    }

    @Override
    public void deleteChannel(UUID channelId) {
        channelRepository.deleteById(channelId);
    }

    @Override
    public ChannelDto updateChannel(UUID channelId, ChannelDto channelDto) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found with id: " + channelId));
        if ("PRIVATE".equalsIgnoreCase(channel.getType())) {
            throw new RuntimeException("Private channel cannot be updated");
        }
        channel.setName(channelDto.getName());
        channel.setDescription(channelDto.getDescription());
        channel.setUpdatedAt(Instant.now());
        Channel updated = channelRepository.save(channel);
        return new ChannelDto(
                updated.getId(),
                updated.getType(),
                updated.getName(),
                updated.getDescription(),
                updated.getParticipantIds(),
                updated.getLastMessageAt()
        );
    }
}
