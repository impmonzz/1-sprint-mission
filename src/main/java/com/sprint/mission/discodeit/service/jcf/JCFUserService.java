package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.LoginRequestDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class JCFUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        throw new UnsupportedOperationException("createUser not implemented in JCFUserService");
    }

    @Override
    public UserDto findUserById(UUID userId) {
        throw new UnsupportedOperationException("findUserById not implemented in JCFUserService");
    }

    @Override
    public List<UserDto> findAllUsers() {
        throw new UnsupportedOperationException("findAllUsers not implemented in JCFUserService");
    }

    @Override
    public UserDto updateUser(UUID userId, UserDto userDto) {
        throw new UnsupportedOperationException("updateUser not implemented in JCFUserService");
    }

    @Override
    public void deleteUser(UUID userId) {
        throw new UnsupportedOperationException("deleteUser not implemented in JCFUserService");
    }

    @Override
    public UserDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User with username " + loginRequestDto.getUsername() + " not found"));
        if (!user.getPassword().equals(loginRequestDto.getPassword())) {
            throw new RuntimeException("Wrong password");
        }
        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getProfileId()
        );
    }
}
