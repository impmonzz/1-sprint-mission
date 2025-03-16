package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentService {
  BinaryContent create(String fileName, Long size, String contentType, byte[] data);
  BinaryContent find(UUID id);
  List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);
  ResponseEntity<?> download(UUID id);
  void delete(UUID binaryContentId);
}
