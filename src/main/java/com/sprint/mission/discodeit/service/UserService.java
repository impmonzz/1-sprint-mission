package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.LoginRequestDto;
import com.sprint.mission.discodeit.dto.UserDto;
import java.util.List;
import java.util.UUID;

public interface UserService {
  UserDto createUser(UserDto userDto);
  UserDto findUserById(UUID userId);
  List<UserDto> findAllUsers();
  UserDto updateUser(UUID userId, UserDto userDto);
  void deleteUser(UUID userId);
  UserDto login(LoginRequestDto loginRequestDto);
}
