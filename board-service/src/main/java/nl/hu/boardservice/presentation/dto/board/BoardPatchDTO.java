package nl.hu.boardservice.presentation.dto.board;

import jakarta.validation.constraints.Size;

public class BoardPatchDTO {
    @Size(min = 1, max = 50)
    public String name;
}
