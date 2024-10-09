package nl.hu.boardservice.presentation;

import nl.hu.boardservice.presentation.dto.board.BoardDTO;
import nl.hu.boardservice.presentation.dto.column.ColumnPostDTO;
import nl.hu.boardservice.presentation.dto.task.TaskDTO;
import nl.hu.boardservice.presentation.dto.task.TaskPatchDTO;
import nl.hu.boardservice.presentation.dto.task.TaskPostDTO;
import nl.hu.boardservice.presentation.dto.user.PatchUserDTO;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import nl.hu.boardservice.application.external.UserServiceHTTP;
import org.apache.hc.client5.http.classic.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerIT {
    private final static String userId = "f47ac10b-58cc-4372-a567-0e02b2c3d479";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private UserServiceHTTP userServiceHTTP;

    private RestTemplate patchRestTemplate;

    private String generateRandomUuid() {
        return UUID.randomUUID().toString();
    }

    @BeforeEach
    void setup() {
        UserServiceHTTP.UserResponse mockUserResponse = new UserServiceHTTP.UserResponse();
        mockUserResponse.id = userId;
        mockUserResponse.username = "test-user";

        Mockito.when(userServiceHTTP.getUserById(Mockito.anyString())).thenReturn(mockUserResponse);

        this.patchRestTemplate = restTemplate.getRestTemplate();
        HttpClient httpClient = HttpClients.createDefault();
        this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    @Test
    void whenTaskIsCreatedThenTaskIsAvailable() {
        // Given
        final var boardCreateUri = "http://localhost:" + port + "/api/v1/boards/user/" + userId + "/board/" + "newBoard";
        var boardCreationResponse = restTemplate.postForEntity(boardCreateUri, null, BoardDTO.class);
        assertThat(boardCreationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        var boardId = boardCreationResponse.getBody().getId();
        ColumnPostDTO columnPostDTO = new ColumnPostDTO("Column Title");
        final var columnUri = "http://localhost:" + port + "/api/v1/boards/" + boardId + "/column";
        HttpEntity<ColumnPostDTO> request = new HttpEntity<>(columnPostDTO);

        ResponseEntity<BoardDTO> boardResponse = restTemplate.exchange(columnUri, HttpMethod.PUT, request, BoardDTO.class);

        String columnUuid = boardResponse.getBody().getColumns().get(boardResponse.getBody().getColumns().size() - 1).getId();

        final var taskPostDTO = new TaskPostDTO("Task Title", "Task Description");
        final var taskUri = "http://localhost:" + port + "/api/v1/tasks/task/" + columnUuid;

        // When
        ResponseEntity<TaskDTO> response = restTemplate.postForEntity(taskUri, taskPostDTO, TaskDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo(taskPostDTO.getTitle());
        assertThat(response.getBody().getDescription()).isEqualTo(taskPostDTO.getDescription());
    }

    @Test
    void whenTaskIsUpdatedThenNewTaskIsAvailable() {
        // Given
        final var boardCreateUri = "http://localhost:" + port + "/api/v1/boards/user/" + userId + "/board/" + "newBoard";
        var boardCreationResponse = restTemplate.postForEntity(boardCreateUri, null, BoardDTO.class);
        assertThat(boardCreationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        var boardId = boardCreationResponse.getBody().getId();

        ColumnPostDTO columnDTO = new ColumnPostDTO("Column Title");
        final var columnUri = "http://localhost:" + port + "/api/v1/boards/" + boardId + "/column";
        HttpEntity<ColumnPostDTO> columnRequest = new HttpEntity<>(columnDTO);
        ResponseEntity<BoardDTO> boardResponse = restTemplate.exchange(columnUri, HttpMethod.PUT, columnRequest, BoardDTO.class);
        String columnUuid = boardResponse.getBody().getColumns().get(boardResponse.getBody().getColumns().size() - 1).getId();

        TaskPostDTO taskDTO = new TaskPostDTO("InitialTitle", "Description");
        ResponseEntity<TaskDTO> taskResponse = restTemplate.postForEntity("/api/v1/tasks/task/" + columnUuid, taskDTO, TaskDTO.class);
        String taskUuid = taskResponse.getBody().getId();

        final var taskPatchDTO = new TaskPatchDTO("Updated Task Title", "Updated Task Description");
        final var taskUri = "http://localhost:" + port + "/api/v1/tasks/" + taskUuid;

        // When
        HttpEntity<TaskPatchDTO> requestUpdate = new HttpEntity<>(taskPatchDTO);
        ResponseEntity<TaskDTO> response = restTemplate.exchange(taskUri, HttpMethod.PATCH, requestUpdate, TaskDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo(taskPatchDTO.getTitle());
        assertThat(response.getBody().getDescription()).isEqualTo(taskPatchDTO.getDescription());
    }

    @Test
    void whenTaskIsDeletedThenTaskIsNotAvailable() {
        // Given
        final var boardCreateUri = "http://localhost:" + port + "/api/v1/boards/user/" + userId + "/board/" + "newBoard";
        var boardCreationResponse = restTemplate.postForEntity(boardCreateUri, null, BoardDTO.class);
        assertThat(boardCreationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        var boardId = boardCreationResponse.getBody().getId();

        ColumnPostDTO columnDTO = new ColumnPostDTO("Column Title");
        final var columnUri = "http://localhost:" + port + "/api/v1/boards/" + boardId + "/column";
        HttpEntity<ColumnPostDTO> columnRequest = new HttpEntity<>(columnDTO);
        ResponseEntity<BoardDTO> boardResponse = restTemplate.exchange(columnUri, HttpMethod.PUT, columnRequest, BoardDTO.class);
        String columnUuid = boardResponse.getBody().getColumns().get(boardResponse.getBody().getColumns().size() - 1).getId();

        TaskPostDTO taskDTO = new TaskPostDTO("Task Title", "Task Description");
        ResponseEntity<TaskDTO> taskResponse = restTemplate.postForEntity("/api/v1/tasks/task/" + columnUuid, taskDTO, TaskDTO.class);
        String taskUuid = taskResponse.getBody().getId();
        final var taskDeleteUri = "http://localhost:" + port + "/api/v1/tasks/columns/" + columnUuid + "/task/" + taskUuid;

        // When
        ResponseEntity<Void> response = restTemplate.exchange(taskDeleteUri, HttpMethod.DELETE, null, Void.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> fetchDeletedTask = restTemplate.getForEntity(taskDeleteUri, String.class);
        assertThat(fetchDeletedTask.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    }


    @Test
    void whenTaskIsAssignedToUserThenAssignmentIsReflected() {
        // Given
        final var taskUuid = generateRandomUuid();
        final var userId = new PatchUserDTO("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        final var assignUri = "http://localhost:" + port + "/api/v1/tasks/" + taskUuid + "/assigned";

        // When
        HttpEntity<PatchUserDTO> request = new HttpEntity<>(userId);
        ResponseEntity<Void> response = restTemplate.exchange(assignUri, HttpMethod.PATCH, request, Void.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void whenTaskIsUnassignedFromUserThenUnassignmentIsReflected() {
        // Given
        final var taskUuid = generateRandomUuid();
        final var userId = new PatchUserDTO("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        final var unassignUri = "http://localhost:" + port + "/api/v1/tasks/" + taskUuid + "/unassigned";

        // When
        HttpEntity<PatchUserDTO> request = new HttpEntity<>(userId);
        ResponseEntity<Void> response = restTemplate.exchange(unassignUri, HttpMethod.PATCH, request, Void.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
