package nl.hu.boardservice.presentation;

import nl.hu.boardservice.application.external.UserServiceHTTP;
import nl.hu.boardservice.presentation.dto.board.BoardDTO;
import nl.hu.boardservice.presentation.dto.board.BoardPatchDTO;
import nl.hu.boardservice.presentation.dto.column.ColumnDTO;
import nl.hu.boardservice.presentation.dto.column.ColumnPostDTO;
import org.apache.hc.client5.http.classic.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoardControllerIT {
    private final static String userId = "f47ac10b-58cc-4372-a567-0e02b2c3d479";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private UserServiceHTTP userServiceHTTP;

    private RestTemplate patchRestTemplate;

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
    void givenBoardIsCreatedWhenBoardIsDeletedThenBoardIsNotAvailable() {
        // Given
        final var rand = new Random();
        final var boardName = "board-" + rand.nextInt(100000);
        final var boardCreateUri = "http://localhost:" + port + "/api/v1/boards/user/" + userId + "/board/" + boardName;
        var boardCreationResponse = restTemplate.postForEntity(boardCreateUri, null, BoardDTO.class);
        assertThat(boardCreationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        var boardId = boardCreationResponse.getBody().getId();

        // When
        var deleteUri = "http://localhost:" + port + "/api/v1/boards/" + boardId;
        var boardDeletionResponse = restTemplate.exchange(deleteUri, HttpMethod.DELETE, null, Void.class);

        // Then
        assertThat(boardDeletionResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        var boardRetrievalResponse = restTemplate.getForEntity(deleteUri, BoardDTO.class);
        assertThat(boardRetrievalResponse.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    }
    @Test
    void whenBoardIsCreatedThenItCanBeRetrieved() {
        //Given
        final var rand = new Random();
        final var boardName = "board-" + rand.nextInt(100000);

        final var boardCreateUri = "http://localhost:" + port + "/api/v1/boards/user/" + userId + "/board/" + boardName;

        //When
        ResponseEntity<BoardDTO> boardCreationResponse = restTemplate.postForEntity(boardCreateUri, null, BoardDTO.class);

        //Then
        assertThat(boardCreationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(boardCreationResponse.getBody()).isNotNull();
        assertThat(boardCreationResponse.getBody().getName()).isEqualTo(boardName);

        final var boardRetrieveUri = "http://localhost:" + port + "/api/v1/boards/board/" + boardCreationResponse.getBody().getId();
        ResponseEntity<BoardDTO> boardRetrievalResponse = restTemplate.getForEntity(boardRetrieveUri, BoardDTO.class);

        assertThat(boardRetrievalResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(boardRetrievalResponse.getBody()).isNotNull();
        assertThat(boardRetrievalResponse.getBody().getName()).isEqualTo(boardName);
    }

    @Test
    void givenBoardIsCreatedWhenBoardIsUpdatedThenUpdatedBoardIsAvailable() {
        // Given
        final var rand = new Random();

        final var originalBoardName = "board-" + rand.nextInt(100000);
        final var updatedBoardName = "board-" + rand.nextInt(100000);

        final var boardCreateUri = "http://localhost:" + port + "/api/v1/boards/user/" + userId + "/board/" + originalBoardName;
        var boardCreationResponse = restTemplate.postForEntity(boardCreateUri, null, BoardDTO.class);
        assertThat(boardCreationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        var boardId = boardCreationResponse.getBody().getId();

        // When
        BoardPatchDTO boardPatchDTO = new BoardPatchDTO();
        boardPatchDTO.name = updatedBoardName;
        var updateUri = "http://localhost:" + port + "/api/v1/boards/" + boardId;
        var boardUpdateResponse = patchRestTemplate.exchange(updateUri, HttpMethod.PATCH, new HttpEntity<>(boardPatchDTO), BoardDTO.class);

        // Then
        assertThat(boardUpdateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(boardUpdateResponse.getBody().getName()).isEqualTo(updatedBoardName);
    }



    @Test
    void givenBoardIsAvailableWhenColumnIsCreatedThenColumnIsAvailable() {
        // Given
        final var rand = new Random();
        final var boardName = "board-" + rand.nextInt(100000);
        final var boardCreateUri = "http://localhost:" + port + "/api/v1/boards/user/" + userId + "/board/" + boardName;
        var boardCreationResponse = restTemplate.postForEntity(boardCreateUri, null, BoardDTO.class);
        assertThat(boardCreationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        var boardId = boardCreationResponse.getBody().getId();

        ColumnPostDTO columnPostDTO = new ColumnPostDTO();
        columnPostDTO.name = "New Column";

        // When
        var addColumnUri = "http://localhost:" + port + "/api/v1/boards/" + boardId + "/column";
        restTemplate.put(addColumnUri, columnPostDTO, BoardDTO.class);

        // Then
        var boardRetrievalResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/boards/board/" + boardId, BoardDTO.class);
        var columnNames = boardRetrievalResponse.getBody().getColumns().stream().map(ColumnDTO::getName).collect(Collectors.toList());
        assertThat(columnNames).contains(columnPostDTO.name);
    }

    @Test
    void whenBoardsAreQueriedForUserThenCorrectBoardsAreReturned() {
        // Setup
        final var boardName1 = "board-1";
        final var boardName2 = "board-2";
        restTemplate.postForEntity("http://localhost:" + port + "/api/v1/boards/user/" + userId + "/board/" + boardName1, null, BoardDTO.class);
        restTemplate.postForEntity("http://localhost:" + port + "/api/v1/boards/user/" + userId + "/board/" + boardName2, null, BoardDTO.class);

        // When
        ResponseEntity<List<BoardDTO>> response = restTemplate.exchange("http://localhost:" + port + "/api/v1/boards/user/" + userId, HttpMethod.GET, null, new ParameterizedTypeReference<List<BoardDTO>>() {});

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<BoardDTO> boards = response.getBody();
        assertThat(boards).hasSize(2);
        assertThat(boards.stream().map(BoardDTO::getName)).contains(boardName1, boardName2);
    }

    @Test
    void whenInvalidUpdateIsSentThenReturnBadRequest() {
        // Given
        final var boardName = "board-test";
        ResponseEntity<BoardDTO> boardCreationResponse = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/boards/user/" + userId + "/board/" + boardName, null, BoardDTO.class);
        var boardId = boardCreationResponse.getBody().getId();

        BoardPatchDTO boardPatchDTO = new BoardPatchDTO();
        boardPatchDTO.name = "";

        // When
        ResponseEntity<BoardDTO> response = restTemplate.exchange("http://localhost:" + port + "/api/v1/boards/" + boardId, HttpMethod.PATCH, new HttpEntity<>(boardPatchDTO), BoardDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
