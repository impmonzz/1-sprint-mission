package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentService {
  BinaryContentDto createBinaryContent(BinaryContentDto binaryContentDto);
  BinaryContentDto findBinaryContentById(UUID id);
  List<BinaryContentDto> findAllFiles(List<UUID> ids);
  void deleteFile(UUID id);
  Optional<BinaryContentDto> findById(UUID id);
}
