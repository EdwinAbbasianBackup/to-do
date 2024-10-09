package nl.hu.boardservice.presentation.dto.builder;

import nl.hu.boardservice.domain.data.Board;
import nl.hu.boardservice.presentation.dto.board.BoardDTO;
import nl.hu.boardservice.presentation.dto.column.ColumnDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

class BoardMapperTest {

    private final BoardMapper boardMapper = Mappers.getMapper(BoardMapper.class);
    @Test
    void testToDTO() {
        // Given
        Board board = Board.builder()
                .name("Sample Board")
                .owner("Sample Owner")
                .build();

        // When
        BoardDTO boardDTO = boardMapper.toDTO(board);

        // Then
        assertThat(boardDTO.getName()).isEqualTo(board.getName());
        assertThat(boardDTO.getOwner()).isEqualTo(board.getOwner());
    }

    @Test
    void testToEntity() {
        // Given
        ColumnDTO columnDTO = ColumnDTO.builder()
                .name("Sample DTO Column")
                .tasks(new HashSet<>())
                .build();
        List<ColumnDTO> columnDTOList = List.of(columnDTO);
        BoardDTO boardDTO = BoardDTO.builder()
                .name("Sample DTO Board")
                .owner("Sample DTO Owner")
                .columns(columnDTOList)
                .build();

        // When
        Board board = boardMapper.toEntity(boardDTO);

        // Then
        assertThat(board.getName()).isEqualTo(boardDTO.getName());
        assertThat(board.getOwner()).isEqualTo(boardDTO.getOwner());
    }

    @Test
    void testToEntityList() {
        // Given
        ColumnDTO columnDTO = ColumnDTO.builder()
                .name("Sample DTO Column")
                .tasks(new HashSet<>())
                .build();
        List<ColumnDTO> columnDTOList = List.of(columnDTO);

        BoardDTO boardDTO1 = BoardDTO.builder()
                .name("Sample DTO Board 1")
                .owner("Sample DTO Owner 1")
                .columns(columnDTOList)
                .build();

        BoardDTO boardDTO2 = BoardDTO.builder()
                .name("Sample DTO Board 2")
                .owner("Sample DTO Owner 2")
                .columns(columnDTOList)
                .build();

        List<BoardDTO> boardDTOList = List.of(boardDTO1, boardDTO2);

        // When
        List<Board> boardList = boardMapper.toEntityList(boardDTOList);

        // Then
        assertThat(boardList).isNotEmpty();
        assertThat(boardList.get(0).getName()).isEqualTo(boardDTO1.getName());
        assertThat(boardList.get(1).getName()).isEqualTo(boardDTO2.getName());
    }

    @Test
    void testToDTOList() {
        // Given
        Board board1 = Board.builder()
                .name("Sample Board 1")
                .owner("Sample Owner 1")
                .build();

        Board board2 = Board.builder()
                .name("Sample Board 2")
                .owner("Sample Owner 2")
                .build();

        List<Board> boardList = List.of(board1, board2);

        // When
        List<BoardDTO> boardDTOList = boardMapper.toDTOList(boardList);

        // Then
        assertThat(boardDTOList).isNotEmpty();
        assertThat(boardDTOList.get(0).getName()).isEqualTo(board1.getName());
        assertThat(boardDTOList.get(1).getName()).isEqualTo(board2.getName());
    }
}
