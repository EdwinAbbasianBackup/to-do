package nl.hu.boardservice.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.hu.boardservice.application.exception.NoDataFoundException;
import nl.hu.boardservice.application.external.UserServiceHTTP;
import nl.hu.boardservice.domain.data.Board;
import nl.hu.boardservice.domain.data.Column;
import nl.hu.boardservice.infrastructure.BoardRepository;
import nl.hu.boardservice.infrastructure.ColumnRepository;
import nl.hu.boardservice.presentation.dto.board.BoardDTO;
import nl.hu.boardservice.presentation.dto.builder.BoardMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final ColumnRepository columnRepository;
    private final ColumnService columnService;
    private final UserServiceHTTP userServiceHTTP;
    private final BoardMapper boardMapper;


    public List<BoardDTO> findAllBoardsFromUser(String ownerUuid) {
        if (userServiceHTTP.getUserById(ownerUuid) == null)
            throw new NoDataFoundException("User not found");

        return boardMapper.toDTOList(boardRepository.findBoardsByUser(ownerUuid));
    }

    public Board create(String name, String ownerUuid) {
        if (userServiceHTTP.getUserById(ownerUuid) == null) {
            throw new NoDataFoundException("User not found");
        }

        Board board = Board.builder()
                .name(name)
                .owner(ownerUuid)
                .build();
        boardRepository.save(board);
        return board;
    }

    @Transactional
    public void AddUserToBoard(String boardUuid, String userUuid) {
        Board board = boardRepository.findById(UUID.fromString(boardUuid))
                .orElseThrow(() -> new NoDataFoundException("Board not found"));
         if (userServiceHTTP.getUserById(userUuid) == null)
            throw new NoDataFoundException("User not found");

        board.addAssignee(userUuid);
    }

    public Board update(String id, String newName) {
        Board existingBoard = boardRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NoDataFoundException("Board not found"));

        existingBoard.setName(newName);
        return boardRepository.save(existingBoard);
    }

    @Transactional
    public void delete(String uuidOfBoard) {
        Board board = boardRepository
                .findById(UUID.fromString(uuidOfBoard))
                .orElseThrow(() -> new NoDataFoundException("Board not found"));

        boardRepository.delete(board);
    }

    @Transactional
    public BoardDTO addColumn(String boardUuid, String columnName) {
        Board board = boardRepository.findById(UUID.fromString(boardUuid))
                .orElseThrow(() -> new NoDataFoundException("Board not found"));

        board.addColumn(columnService.create(columnName));

        return boardMapper.toDTO(board);
    }

    public BoardDTO findById(String boardUuid) {
        Board board = boardRepository.findById(UUID.fromString(boardUuid))
                .orElseThrow(() -> new NoDataFoundException("Board not found"));

        return boardMapper.toDTO(board);
    }

    public void removeColumnFromBoard(UUID columnId) {
        Board board = boardRepository.findBoardByColumnId(columnId).orElseThrow(() -> new NoDataFoundException("no column found"));

        // Find the Column
        Column column = columnRepository.findById(columnId)
            .orElseThrow(() -> new IllegalArgumentException("No Column found with ID: " + columnId));

        // Remove the Column from the Board
        board.removeColumn(column);

        // Save the updated Board
        boardRepository.save(board);
    }


}
