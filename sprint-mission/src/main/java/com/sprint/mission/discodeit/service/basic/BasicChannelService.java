package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    @Override
    public ChannelDto createChannel(ChannelDto channelDto) {
        Channel channel = new Channel();
        channel.setType(channelDto.getType());
        channel.setName(channelDto.getName());
        channel.setDescription(channelDto.getDescription());
        channel.setParticipantIds(channelDto.getParticipantIds());
        channel.setCreatedAt(Instant.now());
        channel.setUpdatedAt(Instant.now());
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
        // 예시: 모든 채널 중, participantIds에 userId가 포함된 채널만 반환
        return channelRepository.findAll().stream()
                .filter(c -> c.getParticipantIds() != null && c.getParticipantIds().contains(userId))
                .map(c -> new ChannelDto(
                        c.getId(),
                        c.getType(),
                        c.getName(),
                        c.getDescription(),
                        c.getParticipantIds(),
                        c.getLastMessageAt()
                ))
                .collect(Collectors.toList());
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
