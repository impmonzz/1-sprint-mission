package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @GetMapping
    public ResponseEntity<List<BinaryContentDto>> getBinaryContents(@RequestParam List<UUID> binaryContentIds) {
        List<BinaryContentDto> contents = binaryContentService.findAllFiles(binaryContentIds);
        return ResponseEntity.ok(contents);
    }

    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentDto> getBinaryContent(@PathVariable UUID binaryContentId) {
        BinaryContentDto content = binaryContentService.findBinaryContentById(binaryContentId);
        return ResponseEntity.ok(content);
    }
}
