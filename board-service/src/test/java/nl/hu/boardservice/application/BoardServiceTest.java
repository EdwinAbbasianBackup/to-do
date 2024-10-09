package nl.hu.boardservice.application;

import nl.hu.boardservice.application.exception.NoDataFoundException;
import nl.hu.boardservice.application.external.UserServiceHTTP;
import nl.hu.boardservice.domain.data.Board;
import nl.hu.boardservice.domain.data.Column;
import nl.hu.boardservice.infrastructure.BoardRepository;
import nl.hu.boardservice.presentation.dto.board.BoardDTO;
import nl.hu.boardservice.presentation.dto.builder.BoardMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BoardServiceTest {

    @Mock
    BoardRepository boardRepository;
    @Mock
    ColumnService columnService;
    @Mock
    UserServiceHTTP userServiceHTTP;
    @Mock
    BoardMapper boardMapper;

    @InjectMocks
    BoardService boardService;

    @Captor
    ArgumentCaptor<Board> boardCaptor;

    @Test
    void testFindAllBoardsFromUserSuccessful() {
        // Given
        String ownerUuid = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        UserServiceHTTP.UserResponse userResponse = new UserServiceHTTP.UserResponse();
        userResponse.id = ownerUuid;
        Mockito.when(userServiceHTTP.getUserById(ownerUuid)).thenReturn(userResponse);

        List<Board> boards = new ArrayList<>();
        Board board1 = new Board();
        Board board2 = new Board();
        boards.add(board1);
        boards.add(board2);

        BoardDTO boardDTO1 = new BoardDTO();
        BoardDTO boardDTO2 = new BoardDTO();
        Mockito.when(boardRepository.findBoardsByUser(ownerUuid)).thenReturn(boards);
        Mockito.when(boardMapper.toDTOList(boards)).thenReturn(Arrays.asList(boardDTO1, boardDTO2));

        // When
        List<BoardDTO> boardDTOs = boardService.findAllBoardsFromUser(ownerUuid);

        // Then
        Mockito.verify(userServiceHTTP).getUserById(ownerUuid);
        Mockito.verify(boardRepository).findBoardsByUser(ownerUuid);
        Mockito.verify(boardMapper).toDTOList(boards);
        assertNotNull(boardDTOs);
        assertEquals(boards.size(), boardDTOs.size());
    }

    @Test
    void testFindAllBoardsFromUserUnsuccessful() {
        // Given
        String ownerUuid = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        Mockito.when(userServiceHTTP.getUserById(ownerUuid)).thenReturn(null);

        Exception exception = assertThrows(NoDataFoundException.class, () -> {
            // When
            boardService.findAllBoardsFromUser(ownerUuid);
        });

        // Then
        Mockito.verify(userServiceHTTP).getUserById(ownerUuid);
        Mockito.verifyNoMoreInteractions(boardRepository);
        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void testCreate() {
        // Given
        String name = "BoardName";
        String ownerUuid = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        UserServiceHTTP.UserResponse userResponse = new UserServiceHTTP.UserResponse();
        userResponse.id = ownerUuid;
        Board board = new Board();

        Mockito.when(userServiceHTTP.getUserById(ownerUuid)).thenReturn(userResponse);
        Mockito.when(boardRepository.save(boardCaptor.capture())).thenReturn(board);
        // When
        Board result = boardService.create(name, ownerUuid);

        // Then
        Mockito.verify(userServiceHTTP).getUserById(ownerUuid);
        Board capturedBoard = boardCaptor.getValue();
        assertNotNull(capturedBoard);
        assertEquals(name, capturedBoard.getName());
        assertEquals(ownerUuid, capturedBoard.getOwner());
        assertNotNull(result);
    }

    @Test
    void testAddUserToBoard() {
        // Given
        String boardUuid = "f47ac10b-58cc-4372-a567-0e02b2c3d555";
        String ownerUuid = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        UserServiceHTTP.UserResponse userResponse = new UserServiceHTTP.UserResponse();
        userResponse.id = ownerUuid;
        Mockito.when(userServiceHTTP.getUserById(ownerUuid)).thenReturn(userResponse);
        Board board = new Board();
        Mockito.when(boardRepository.findById(UUID.fromString(boardUuid))).thenReturn(Optional.of(board));

        // When
        boardService.AddUserToBoard(boardUuid, ownerUuid);

        // Then
        Mockito.verify(userServiceHTTP).getUserById(ownerUuid);
        Mockito.verify(boardRepository).findById(UUID.fromString(boardUuid));
        assertTrue(board.getAssignees().contains(ownerUuid));
    }

    @Test
    void testUpdate() {
        // Given
        String id = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        String newName = "New Name";

        Board board = new Board();
        Mockito.when(boardRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(board));
        Mockito.when(boardRepository.save(board)).thenReturn(board);

        // When
        Board result = boardService.update(id, newName);

        // Then
        Mockito.verify(boardRepository).findById(UUID.fromString(id));
        Mockito.verify(boardRepository).save(board);
        assertEquals(newName, result.getName());
    }

    @Test
    void testDelete() {
        // Given
        String uuidOfBoard = "f47ac10b-58cc-4372-a567-0e02b2c3d479";

        Board board = new Board();
        Mockito.when(boardRepository.findById(UUID.fromString(uuidOfBoard))).thenReturn(Optional.of(board));

        // When
        boardService.delete(uuidOfBoard);

        // Then
        Mockito.verify(boardRepository).findById(UUID.fromString(uuidOfBoard));
        Mockito.verify(boardRepository).delete(board);
    }

    @Test
    void testAddColumn() {
        // Given
        String boardUuid = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        String columnName = "Test Column";

        Board board = new Board();
        Column column = new Column();
        Mockito.when(boardRepository.findById(UUID.fromString(boardUuid))).thenReturn(Optional.of(board));
        Mockito.when(columnService.create(columnName)).thenReturn(column);

        BoardDTO boardDTO = new BoardDTO();
        Mockito.when(boardMapper.toDTO(board)).thenReturn(boardDTO);

        // When
        BoardDTO result = boardService.addColumn(boardUuid, columnName);

        // Then
        Mockito.verify(boardRepository).findById(UUID.fromString(boardUuid));
        Mockito.verify(columnService).create(columnName);
        Mockito.verify(boardMapper).toDTO(board);
        assertNotNull(result);
        assertTrue(board.getColumns().contains(column));
    }

    @Test
    void testFindById() {
        // Given
        String boardUuid = "f47ac10b-58cc-4372-a567-0e02b2c3d479";

        Board board = new Board();
        Mockito.when(boardRepository.findById(UUID.fromString(boardUuid))).thenReturn(Optional.of(board));

        BoardDTO boardDTO = new BoardDTO();
        Mockito.when(boardMapper.toDTO(board)).thenReturn(boardDTO);

        // When
        BoardDTO result = boardService.findById(boardUuid);

        // Then
        Mockito.verify(boardRepository).findById(UUID.fromString(boardUuid));
        Mockito.verify(boardMapper).toDTO(board);
        assertEquals(boardDTO, result);
    }
}