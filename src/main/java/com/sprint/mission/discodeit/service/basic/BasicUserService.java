package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;

  @Transactional
  @Override
  public User create(UserCreateRequest userCreateRequest) {
    if (userRepository.existsByEmail(userCreateRequest.email()) ||
        userRepository.existsByUsername(userCreateRequest.username())) {
      throw new RuntimeException("User with given email or username already exists");
    }
    User user = new User(userCreateRequest.username(), userCreateRequest.email(), userCreateRequest.password());
    user = userRepository.save(user);
    // 프로필 파일 업로드 처리가 필요한 경우, 별도의 API로 처리하거나 이후 추가합니다.
    UserStatus status = new UserStatus(user, Instant.now());
    userStatusRepository.save(status);
    return user;
  }

  @Override
  public UserDto find(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found: " + userId));
    return userMapper.toDto(user);
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll().stream()
        .map(userMapper::toDto)
        .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public User update(UUID userId, UserUpdateRequest userUpdateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found: " + userId));
    user.update(userUpdateRequest.newUsername(), userUpdateRequest.newEmail(), userUpdateRequest.newPassword());
    // 프로필 업데이트 처리 필요 시, 별도의 로직 추가
    return userRepository.save(user);
  }

  @Override
  public void delete(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new RuntimeException("User not found: " + userId);
    }
    userRepository.deleteById(userId);
  }
}
