package nl.hu.boardservice.presentation.dto.column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.hu.boardservice.presentation.dto.task.TaskDTO;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnDTO {
    private String id;
    private String name;
    private Set<TaskDTO> tasks;
}
