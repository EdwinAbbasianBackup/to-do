package nl.hu.boardservice.presentation.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.hu.boardservice.presentation.dto.tag.TagDTO;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private String id;
    private String title;
    private String description;
    private List<TagDTO> tags;
    private HashMap<String, String> assignees;
}
