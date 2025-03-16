package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentDto createBinaryContent(BinaryContentDto binaryContentDto) {
        byte[] data = Base64.getDecoder().decode(binaryContentDto.getBytes());
        BinaryContent entity = new BinaryContent();
        entity.setBytes(data);
        entity.setFileName(binaryContentDto.getFileName());
        entity.setSize(binaryContentDto.getSize());
        entity.setContentType(binaryContentDto.getContentType());
        entity.setCreatedAt(java.time.Instant.now());
        BinaryContent saved = binaryContentRepository.save(entity);
        String encoded = Base64.getEncoder().encodeToString(saved.getBytes());
        return new BinaryContentDto(saved.getId(), saved.getFileName(), saved.getSize(), saved.getContentType(), encoded);
    }

    @Override
    public BinaryContentDto findBinaryContentById(UUID id) {
        BinaryContent entity = binaryContentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BinaryContent not found with id: " + id));
        String encoded = Base64.getEncoder().encodeToString(entity.getBytes());
        return new BinaryContentDto(entity.getId(), entity.getFileName(), entity.getSize(), entity.getContentType(), encoded);
    }

    @Override
    public List<BinaryContentDto> findAllFiles(List<UUID> ids) {
        List<BinaryContent> entities = binaryContentRepository.findAllById(ids);
        return entities.stream().map(entity -> {
            String encoded = Base64.getEncoder().encodeToString(entity.getBytes());
            return new BinaryContentDto(entity.getId(), entity.getFileName(), entity.getSize(), entity.getContentType(), encoded);
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteFile(UUID id) {
        binaryContentRepository.deleteById(id);
    }

    @Override
    public Optional<BinaryContentDto> findById(UUID id) {
        return binaryContentRepository.findById(id)
                .map(entity -> {
                    String encoded = Base64.getEncoder().encodeToString(entity.getBytes());
                    return new BinaryContentDto(entity.getId(), entity.getFileName(), entity.getSize(), entity.getContentType(), encoded);
                });
    }
}
