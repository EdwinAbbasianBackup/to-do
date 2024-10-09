package nl.hu.userservice.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import nl.hu.userservice.application.exception.AlreadyExistException;
import nl.hu.userservice.application.exception.NoDataFoundException;
import nl.hu.userservice.infrastructure.UserRepository;
import nl.hu.userservice.infrastructure.data.User;
import nl.hu.userservice.presentation.dto.UserCreateDTO;
import nl.hu.userservice.presentation.dto.UserDTO;
import nl.hu.userservice.presentation.dto.UserLoginDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDTO register(UserCreateDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new AlreadyExistException("Username already exists");
        }
        User user = userRepository.save(new User(userDTO.getUsername(), userDTO.getPassword()));

        return fromUser(user);
    }

    public UserDTO findUserById(String id) {
        return fromUser(userRepository
                        .findById(UUID.fromString(id))
                        .orElseThrow(() -> new NoDataFoundException("User not found")));
    }


    public UserDTO login(UserLoginDTO userLogin) {
        User user = userRepository
                .findByUsernameAndPassword(userLogin.getUsername(), userLogin.getPassword())
                .orElseThrow(() ->  new NoDataFoundException("User not found"));

        return fromUser(user);
    }

    private UserDTO fromUser(User user) {
        return UserDTO.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getUsername())
                .build();
    }

    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId().toString())
                        .username(user.getUsername())
                        .build())
                .collect(Collectors.toList());
    }

}
