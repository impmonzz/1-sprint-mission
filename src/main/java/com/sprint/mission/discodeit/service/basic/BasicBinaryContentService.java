package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  public BasicBinaryContentService(BinaryContentRepository binaryContentRepository, BinaryContentStorage binaryContentStorage) {
    this.binaryContentRepository = binaryContentRepository;
    this.binaryContentStorage = binaryContentStorage;
  }

  @Transactional
  @Override
  public BinaryContent create(String fileName, Long size, String contentType, byte[] data) {
    BinaryContent bc = new BinaryContent(fileName, size, contentType);
    bc = binaryContentRepository.save(bc);
    binaryContentStorage.put(bc.getId(), data);
    return bc;
  }

  @Override
  public BinaryContent find(UUID id) {
    return binaryContentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("BinaryContent not found: " + id));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
    return binaryContentRepository.findAllByIdIn(ids);
  }

  @Override
  public ResponseEntity<?> download(UUID id) {
    BinaryContent bc = binaryContentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("BinaryContent not found: " + id));
    BinaryContentDto dto = new BinaryContentDto(bc.getId(), bc.getFileName(), bc.getSize(), bc.getContentType());
    return binaryContentStorage.download(dto);
  }

  @Override
  public void delete(UUID id) {
    if (!binaryContentRepository.existsById(id)) {
      throw new NoSuchElementException("BinaryContent not found: " + id);
    }
    binaryContentRepository.deleteById(id);
  }
}
