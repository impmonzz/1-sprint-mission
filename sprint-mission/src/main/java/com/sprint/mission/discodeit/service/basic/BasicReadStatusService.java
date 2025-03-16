package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;

    public BasicReadStatusService(ReadStatusRepository readStatusRepository) {
        this.readStatusRepository = readStatusRepository;
    }

    @Override
    public ReadStatusDto createReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        ReadStatus readStatus = new ReadStatus(userId, channelId, lastReadAt);
        ReadStatus saved = readStatusRepository.save(readStatus);
        return convertToDto(saved);
    }

    @Override
    public ReadStatusDto findReadStatusById(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReadStatus not found with id: " + id));
        return convertToDto(readStatus);
    }

    @Override
    public ReadStatusDto findLastReadMessage(UUID userId, UUID channelId) {
        for (ReadStatus rs : readStatusRepository.findAll()) {
            if (rs.getUserId().equals(userId) && rs.getChannelId().equals(channelId)) {
                return convertToDto(rs);
            }
        }
        throw new RuntimeException("ReadStatus not found for user " + userId + " and channel " + channelId);
    }

    @Override
    public void updateReadStatus(UUID id, Instant newTime) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReadStatus not found with id: " + id));
        readStatus.updateLastReadAt(newTime);
        readStatusRepository.save(readStatus);
    }

    @Override
    public void deleteReadStatus(UUID id) {
        readStatusRepository.deleteById(id);
    }

    private ReadStatusDto convertToDto(ReadStatus rs) {
        return new ReadStatusDto(rs.getId(), rs.getUserId(), rs.getChannelId(), rs.getLastReadAt());
    }
}
