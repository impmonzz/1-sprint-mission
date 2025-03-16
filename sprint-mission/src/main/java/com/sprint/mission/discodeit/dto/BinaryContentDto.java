package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinaryContentDto {
    private UUID id;
    private String fileName;
    private Long size;
    private String contentType;

    @Schema(description = "Base64", format = "")
    private String bytes;
}
