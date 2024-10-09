package nl.hu.userservice.application;

import nl.hu.userservice.infrastructure.UserRepository;
import nl.hu.userservice.infrastructure.data.User;
import nl.hu.userservice.presentation.dto.UserCreateDTO;
import nl.hu.userservice.presentation.dto.UserDTO;
import nl.hu.userservice.presentation.dto.UserLoginDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;


    @Test
    void testRegisterNewUserSuccessful() {
        // Given
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername("testUser");
        userCreateDTO.setPassword("testPass");

        User user = new User(userCreateDTO.getUsername(), userCreateDTO.getPassword());
        Mockito.when(userRepository.existsByUsername(userCreateDTO.getUsername())).thenReturn(false);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        UserDTO userDTO = userService.register(userCreateDTO);

        // Then
        assertNotNull(userDTO);
        assertEquals(user.getUsername(), userDTO.getUsername());
    }

    @Test
    void testFindUserByIdSuccessful() {
        // Given
        String userId = UUID.randomUUID().toString();
        User user = new User("testUser", "testPass");
        Mockito.when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(user));

        // When
        UserDTO userDTO = userService.findUserById(userId);

        // Then
        assertNotNull(userDTO);
        assertEquals(user.getUsername(), userDTO.getUsername());
    }

    @Test
    void testLoginSuccessful() {
        // Given
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername("testUser");
        userLoginDTO.setPassword("testPass");

        User user = new User(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        Mockito.when(userRepository.findByUsernameAndPassword(userLoginDTO.getUsername(), userLoginDTO.getPassword()))
                .thenReturn(Optional.of(user));

        // When
        UserDTO userDTO = userService.login(userLoginDTO);

        // Then
        assertNotNull(userDTO);
        assertEquals(user.getUsername(), userDTO.getUsername());
    }

}