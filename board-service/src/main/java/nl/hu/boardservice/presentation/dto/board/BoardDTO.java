package nl.hu.boardservice.presentation.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.hu.boardservice.presentation.dto.column.ColumnDTO;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private String id;
    private String name;
    private String owner;
    private Set<String> members;
    private List<ColumnDTO> columns;

}
