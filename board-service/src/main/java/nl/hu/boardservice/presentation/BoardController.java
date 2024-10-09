package nl.hu.boardservice.presentation;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hu.boardservice.application.BoardService;
import nl.hu.boardservice.domain.data.Board;
import nl.hu.boardservice.presentation.dto.board.BoardDTO;
import nl.hu.boardservice.presentation.dto.board.BoardPatchDTO;
import nl.hu.boardservice.presentation.dto.builder.BoardMapper;
import nl.hu.boardservice.presentation.dto.column.ColumnPostDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController("BoardControllerV1")
@RequestMapping("/api/v1/boards")
public class BoardController {
    private final BoardService boardService;
    private final BoardMapper boardMapper;

    @GetMapping("/board/{UUID}")
    @Operation(summary = "Finds a board.")
    public ResponseEntity<BoardDTO> get(@PathVariable String UUID) {
        return ResponseEntity.ok(boardService.findById(UUID));
    }

    @GetMapping("/user/{UUID}")
    @Operation(summary = "Finds all boards from a user.")
    public ResponseEntity<List<BoardDTO>> getBoardsFromUser(@PathVariable String UUID) {
        return ResponseEntity.ok(boardService.findAllBoardsFromUser(UUID));
    }

    @PostMapping("/user/{userID}/board/{boardName}")
    @Operation(summary = "Create a new board for a owner.")
    public ResponseEntity<BoardDTO> create(@PathVariable String userID,
                                           @PathVariable String boardName) {
        Board board = boardService.create(boardName, userID);
        return ResponseEntity.ok(boardMapper.toDTO(board));
    }

    @DeleteMapping("/{UUID}")
    @Operation(summary = "Delete a board.")
    public ResponseEntity<?> delete(@PathVariable String UUID) {
        boardService.delete(UUID);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/{UUID}")
    @Operation(summary = "Edit a board name.")
    public ResponseEntity<BoardDTO> update(@PathVariable String UUID,
                                           @RequestBody @Valid BoardPatchDTO dto) {

        Board updatedBoard = boardService.update(UUID, dto.name);
        return ResponseEntity.ok(boardMapper.toDTO(updatedBoard));
    }

    @PutMapping("/{boardId}/column")
    @Operation(summary = "Create a column in a specific board.")
    public ResponseEntity<BoardDTO> createColumn(@PathVariable String boardId,
                                                 @RequestBody @Valid ColumnPostDTO columnPostDTO) {

        BoardDTO boardDTO = boardService.addColumn(boardId, columnPostDTO.name);

        return ResponseEntity.ok(boardDTO);
    }

    @DeleteMapping("/column/{columnId}")
    public ResponseEntity<?> removeColumn(@PathVariable String columnId){
        boardService.removeColumnFromBoard(UUID.fromString(columnId));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/board/{boardId}/user/{userId}")
    @Operation(summary = "Adds a normal user to a board.")
    public ResponseEntity<BoardDTO> addUser(@PathVariable String boardId,
                                                 @PathVariable String userId) {

        boardService.AddUserToBoard(boardId, userId);

        return ResponseEntity.ok().build();
    }

}

