package nl.hu.boardservice.presentation.dto.task;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskPatchDTO {
    private String title;
    private String description;
}

