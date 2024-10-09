package nl.hu.boardservice.presentation.dto.builder;

import nl.hu.boardservice.domain.data.Board;
import nl.hu.boardservice.domain.data.Column;
import nl.hu.boardservice.presentation.dto.board.BoardDTO;
import nl.hu.boardservice.presentation.dto.column.ColumnDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = TagMapper.class)
public interface BoardMapper {

    BoardDTO toDTO(Board board);

    Board toEntity(BoardDTO boardDTO);

    List<Board> toEntityList(List<BoardDTO> boardDToList);

    List<BoardDTO> toDTOList(List<Board> boardList);
}
