package com.sprint.mission.discodeit.mapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class ChannelMapper {
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;
  public ChannelDto toDto(Channel entity) {
    List<UserDto> participantDtos = readStatusRepository.findAllByChannel(entity)
        .stream()
        .map(rs -> userMapper.toDto(rs.getUser()))
        .collect(Collectors.toList());
    return new ChannelDto(entity.getId(), entity.getType().name(), entity.getName(), entity.getDescription(), participantDtos);
  }
}
