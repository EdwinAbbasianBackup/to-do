package nl.hu.userservice.presentation;

import nl.hu.userservice.application.UserService;
import nl.hu.userservice.presentation.dto.UserCreateDTO;
import nl.hu.userservice.presentation.dto.UserDTO;
import nl.hu.userservice.presentation.dto.UserLoginDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Captor
    private ArgumentCaptor<UserCreateDTO> userCreateDTOCaptor;

    @Test
    void testRegister() {
        // Given
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername("testUsername");
        userCreateDTO.setPassword("testPassword");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID().toString());
        userDTO.setUsername(userCreateDTO.getUsername());

        Mockito.when(userService.register(userCreateDTO)).thenReturn(userDTO);

        // When
        ResponseEntity<UserDTO> result = userController.register(userCreateDTO);

        // Then
        Mockito.verify(userService).register(userCreateDTOCaptor.capture());
        UserCreateDTO capturedDTO = userCreateDTOCaptor.getValue();
        assertEquals(userCreateDTO.getUsername(), capturedDTO.getUsername());
        assertEquals(userCreateDTO.getPassword(), capturedDTO.getPassword());
        assertEquals(ResponseEntity.ok(userDTO), result);
    }

    @Test
    void testLogin() {
        // Given
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername("testUsername");
        userLoginDTO.setPassword("testPassword");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID().toString());
        userDTO.setUsername(userLoginDTO.getUsername());

        Mockito.when(userService.login(userLoginDTO)).thenReturn(userDTO);

        // When
        ResponseEntity<UserDTO> result = userController.login(userLoginDTO);

        // Then
        Mockito.verify(userService).login(userLoginDTO);
        assertEquals(ResponseEntity.ok(userDTO), result);
    }

    @Test
    void testGet() {
        // Given
        String uuid = UUID.randomUUID().toString();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(uuid);
        userDTO.setUsername("testUsername");

        Mockito.when(userService.findUserById(uuid)).thenReturn(userDTO);

        // When
        ResponseEntity<UserDTO> result = userController.get(uuid);

        // Then
        Mockito.verify(userService).findUserById(uuid);
        Mockito.verifyNoMoreInteractions(userService);
        assertEquals(ResponseEntity.ok(userDTO), result);
    }

    @Test
    void testGetAllUsers() {
        // Given
        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(UUID.randomUUID().toString());
        userDTO1.setUsername("user1");
        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(UUID.randomUUID().toString());
        userDTO2.setUsername("user2");

        List<UserDTO> userDTOs = Arrays.asList(userDTO1, userDTO2);
        Mockito.when(userService.getAll()).thenReturn(userDTOs);

        // When
        ResponseEntity<List<UserDTO>> result = userController.getAll();

        // Then
        Mockito.verify(userService).getAll();
        Mockito.verifyNoMoreInteractions(userService);
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
        assertEquals(ResponseEntity.ok(userDTOs), result);
    }
}
