package nl.hu.boardservice.presentation.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.hu.boardservice.presentation.dto.tag.TagDTO;

import java.util.HashMap;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskPutDTO {
    private String title;
    private String description;
    private Set<TagDTO> labels;
    private HashMap<String, String> assignees;

}
