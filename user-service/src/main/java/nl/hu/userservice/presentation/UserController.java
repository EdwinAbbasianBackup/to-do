package nl.hu.userservice.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hu.userservice.application.UserService;
import nl.hu.userservice.presentation.dto.UserCreateDTO;
import nl.hu.userservice.presentation.dto.UserDTO;
import nl.hu.userservice.presentation.dto.UserLoginDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController("UserControllerV1")
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;


    /**
     * Creates a new task. this task contains a title, description and is added with a status of OPEN.
     *
//     * @param UserCreateDTO the USER to be created.
     * @return the created task.
     */
    @PostMapping
    @Operation(summary = "Register a new User.")
    public ResponseEntity<UserDTO> register(@RequestBody @Parameter(description = "Task to be created") UserCreateDTO userDTO) {
        if (userDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        UserDTO user = userService.register(userDTO);
        return ResponseEntity.ok(user);
    }


    /**
     * Creates a new task. this task contains a title, description and is added with a status of OPEN.
     *
//     * @param UserLoginDTO the user to login.
     * @return the created task.
     */
    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<UserDTO> login(@RequestBody @Parameter(description = "Task to be created") UserLoginDTO userLogin) {
        UserDTO user = userService.login(userLogin);
        return ResponseEntity.ok(user);
    }


    /**
     * Creates a new task. this task contains a title, description and is added with a status of OPEN.
     *
//     * @param taskDTO the task to be created.
     * @return the created task.
     */
    @GetMapping("/{uuid}")
    @Operation(summary = "Get a user")
    public ResponseEntity<UserDTO> get(@PathVariable @Parameter(description = "User to get") String uuid) {
        UserDTO user = userService.findUserById(uuid);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll(){
        return ResponseEntity.ok(userService.getAll());
    }
}
