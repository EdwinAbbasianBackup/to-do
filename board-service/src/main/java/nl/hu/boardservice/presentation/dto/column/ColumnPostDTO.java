package nl.hu.boardservice.presentation.dto.column;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnPostDTO {
    @Size(min = 1, max = 25)
    public String name;
}
