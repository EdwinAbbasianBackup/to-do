package nl.hu.boardservice.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hu.boardservice.application.ColumnService;
import nl.hu.boardservice.domain.data.Column;
import nl.hu.boardservice.presentation.dto.builder.ColumnMapper;
import nl.hu.boardservice.presentation.dto.column.ColumnDTO;
import nl.hu.boardservice.presentation.dto.column.ColumnPatchDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController("ColumnControllerV1")
@RequestMapping("/api/v1/column")
public class ColumnController {
    private final ColumnService columnService;
    private final ColumnMapper columnMapper;


    /**
     * Finds a column. This column contains an id, title and tasks.
     *
     * @param UUID the column to be found.
     * @return the found column.
     */
    @GetMapping("/{UUID}")
    @Operation(summary = "Finds a column.")
    public ResponseEntity<ColumnDTO> get(@PathVariable String UUID){
        ColumnDTO columnDTO = columnService.findByIdDto(UUID);
        return ResponseEntity.ok(columnDTO);
    }


    /**
     * Edits a column name.
     *
     * @param UUID the column rename.
     * @return the renamed column.
     */
    @PatchMapping("/{UUID}")
    @Operation(summary = "Edit a column name.")
    public ResponseEntity<ColumnDTO> update(@PathVariable String UUID,
                                            @RequestBody @Parameter(description = "New column name") ColumnPatchDTO dto) {
        Column updatedColumn = columnService.update(UUID, dto.name);
        return ResponseEntity.ok(columnMapper.toDTO(updatedColumn));
    }
}
