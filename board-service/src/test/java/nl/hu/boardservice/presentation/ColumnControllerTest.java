package nl.hu.boardservice.presentation;

import nl.hu.boardservice.application.ColumnService;
import nl.hu.boardservice.domain.data.Column;
import nl.hu.boardservice.presentation.dto.builder.ColumnMapper;
import nl.hu.boardservice.presentation.dto.column.ColumnDTO;
import nl.hu.boardservice.presentation.dto.column.ColumnPatchDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ColumnControllerTest {

    @Mock
    private ColumnService columnService;

    @Mock
    private ColumnMapper columnMapper;

    @InjectMocks
    private ColumnController columnController;

    @Test
    void testGetColumn() {
        // Given
        String UUID = "someUUID";
        ColumnDTO expectedColumnDTO = new ColumnDTO();
        Mockito.when(columnService.findByIdDto(UUID)).thenReturn(expectedColumnDTO);

        // When
        ResponseEntity<ColumnDTO> result = columnController.get(UUID);

        // Then
        Mockito.verify(columnService).findByIdDto(UUID);
        Mockito.verifyNoMoreInteractions(columnService);
        assertEquals(ResponseEntity.ok(expectedColumnDTO), result);
    }

    @Test
    void testUpdateColumnName() {
        // Given
        String UUID = "someUUID";
        String newTitle = "New Title";
        ColumnPatchDTO dto = new ColumnPatchDTO();
        dto.name = newTitle;
        Column updatedColumn = new Column();
        ColumnDTO expectedColumnDTO = new ColumnDTO();
        Mockito.when(columnService.update(UUID, newTitle)).thenReturn(updatedColumn);
        Mockito.when(columnMapper.toDTO(updatedColumn)).thenReturn(expectedColumnDTO);

        // When
        ResponseEntity<ColumnDTO> result = columnController.update(UUID, dto);

        // Then
        Mockito.verify(columnService).update(UUID, newTitle);
        Mockito.verifyNoMoreInteractions(columnService);
        assertEquals(ResponseEntity.ok(expectedColumnDTO), result);
    }
}
