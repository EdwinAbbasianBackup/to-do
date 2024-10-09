package nl.hu.boardservice.presentation;

import nl.hu.boardservice.application.BoardService;
import nl.hu.boardservice.domain.data.Board;
import nl.hu.boardservice.presentation.dto.board.BoardDTO;
import nl.hu.boardservice.presentation.dto.board.BoardPatchDTO;
import nl.hu.boardservice.presentation.dto.builder.BoardMapper;
import nl.hu.boardservice.presentation.dto.column.ColumnPostDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class BoardControllerTest {

    @Mock
    private BoardService boardService;

    @Spy
    private BoardMapper boardMapper;

    @InjectMocks
    private BoardController boardController;

    @Test
    void testGet() {
        // Given
        String uuid = UUID.randomUUID().toString();
        BoardDTO boardDTO = new BoardDTO();
        Mockito.when(boardService.findById(uuid)).thenReturn(boardDTO);

        // When
        ResponseEntity<BoardDTO> result = boardController.get(uuid);

        // Then
        Mockito.verify(boardService).findById(uuid);
        Mockito.verifyNoMoreInteractions(boardService);
        assertEquals(ResponseEntity.ok(boardDTO), result);
    }

    @Test
    void testGetBoardsFromUser() {
        // Given
        String uuid = UUID.randomUUID().toString();
        List<BoardDTO> boardDTOs = List.of(new BoardDTO());
        Mockito.when(boardService.findAllBoardsFromUser(uuid)).thenReturn(boardDTOs);

        // When
        ResponseEntity<List<BoardDTO>> result = boardController.getBoardsFromUser(uuid);

        // Then
        Mockito.verify(boardService).findAllBoardsFromUser(uuid);
        Mockito.verifyNoMoreInteractions(boardService);
        assertEquals(ResponseEntity.ok(boardDTOs), result);
    }

    @Test
    void testCreate() {
        // Given
        String userId = UUID.randomUUID().toString();
        String boardName = "Test Board";
        Board board = new Board();
        BoardDTO boardDTO = new BoardDTO();
        Mockito.when(boardService.create(boardName, userId)).thenReturn(board);
        Mockito.when(boardMapper.toDTO(board)).thenReturn(boardDTO);

        // When
        ResponseEntity<BoardDTO> result = boardController.create(userId, boardName);

        // Then
        Mockito.verify(boardService).create(boardName, userId);
        Mockito.verifyNoMoreInteractions(boardService);
        assertEquals(ResponseEntity.ok(boardDTO), result);
    }

    @Test
    void testDelete() {
        // Given
        String uuid = UUID.randomUUID().toString();

        // When
        ResponseEntity<?> result = boardController.delete(uuid);

        // Then
        Mockito.verify(boardService).delete(uuid);
        Mockito.verifyNoMoreInteractions(boardService);
        assertEquals(ResponseEntity.ok().build(), result);
    }

    @Test
    void testUpdate() {
        // Given
        String uuid = UUID.randomUUID().toString();
        BoardPatchDTO dto = new BoardPatchDTO();
        dto.name = "Updated Name";
        Board updatedBoard = new Board();
        BoardDTO updatedDTO = new BoardDTO();
        Mockito.when(boardService.update(uuid, dto.name)).thenReturn(updatedBoard);
        Mockito.when(boardMapper.toDTO(updatedBoard)).thenReturn(updatedDTO);

        // When
        ResponseEntity<BoardDTO> result = boardController.update(uuid, dto);

        // Then
        Mockito.verify(boardService).update(uuid, dto.name);
        Mockito.verifyNoMoreInteractions(boardService);
        assertEquals(ResponseEntity.ok(updatedDTO), result);
    }

    @Test
    void testCreateColumn() {
        // Given
        String boardId = UUID.randomUUID().toString();
        ColumnPostDTO columnPostDTO = new ColumnPostDTO();
        columnPostDTO.name = "Test Column";
        BoardDTO boardDTO = new BoardDTO();
        Mockito.when(boardService.addColumn(boardId, columnPostDTO.name)).thenReturn(boardDTO);
      
        // When
        ResponseEntity<BoardDTO> result = boardController.createColumn(boardId, columnPostDTO);

        // Then
        Mockito.verify(boardService).addColumn(boardId, columnPostDTO.name);
        Mockito.verifyNoMoreInteractions(boardService);
        assertEquals(ResponseEntity.ok(boardDTO), result);
    }

    @Test
    void testAddUser() {
        // Given
        String boardId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        // When
        ResponseEntity<BoardDTO> result = boardController.addUser(boardId, userId);

        // Then
        Mockito.verify(boardService).AddUserToBoard(boardId, userId);
        Mockito.verifyNoMoreInteractions(boardService);
        assertEquals(ResponseEntity.ok().build(), result);
    }
}
