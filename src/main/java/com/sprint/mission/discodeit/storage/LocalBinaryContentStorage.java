package com.sprint.mission.discodeit.storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
@Component
@ConditionalOnProperty(value = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {
  private final Path root;
  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Paths.get(rootPath).toAbsolutePath().normalize();
  }
  @PostConstruct
  public void init() throws IOException {
    Files.createDirectories(root);
  }
  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
  @Override
  public UUID put(UUID id, byte[] data) {
    Path path = resolvePath(id);
    try (OutputStream os = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
      os.write(data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return id;
  }
  @Override
  public InputStream get(UUID id) {
    Path path = resolvePath(id);
    try {
      return Files.newInputStream(path, StandardOpenOption.READ);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  @Override
  public ResponseEntity<?> download(com.sprint.mission.discodeit.dto.data.BinaryContentDto dto) {
    Path path = resolvePath(dto.id());
    try {
      InputStreamResource resource = new InputStreamResource(Files.newInputStream(path));
      MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
      if (dto.contentType() != null) {
        mediaType = MediaType.parseMediaType(dto.contentType());
      }
      long contentLength = Files.size(path);
      String fileName = dto.fileName();
      return ResponseEntity.ok()
          .contentType(mediaType)
          .contentLength(contentLength)
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
          .body(resource);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
