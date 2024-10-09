package nl.hu.boardservice.application;

import nl.hu.boardservice.application.exception.NoDataFoundException;
import nl.hu.boardservice.domain.data.Column;
import nl.hu.boardservice.infrastructure.ColumnRepository;
import nl.hu.boardservice.presentation.dto.builder.ColumnMapper;
import nl.hu.boardservice.presentation.dto.column.ColumnDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
@ExtendWith(MockitoExtension.class)
class ColumnServiceTest {
    @Mock
    ColumnRepository columnRepository;
    @Spy
    ColumnMapper columnMapper;

    @InjectMocks
    ColumnService columnService;

    @Test
    void testUpdate() {
        // Given
        String columnId = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        String updatedName = "UpdatedColumn";

        Column column = new Column();

        Mockito.when(columnRepository.save(column)).thenReturn(column);
        Mockito.when(columnRepository.findById(UUID.fromString(columnId)))
                .thenReturn(Optional.of(column));

        // When
        Column resultDTO = columnService.update(columnId, updatedName);

        // Then
        Mockito.verify(columnRepository).save(column);
        assertEquals(updatedName, column.getName());
        assertEquals(column, resultDTO);
    }

    @Test
    void testDeleteColumn() {
        // Given
        String columnId = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        Column column = new Column();

        Mockito.when(columnRepository.findById(UUID.fromString(columnId))).thenReturn(Optional.of(column));

        // When
        columnService.delete(columnId);

        // Then
        Mockito.verify(columnRepository).deleteById(column.getId());
    }

    @Test
    void testDeleteColumnNotFound() {
        // Given
        String columnId = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        Mockito.when(columnRepository.findById(UUID.fromString(columnId))).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(NoDataFoundException.class, () -> {
            columnService.delete(columnId);
        });

        // Then
        assertTrue(exception.getMessage().contains("Column not found"));
    }

    @Test
    void testFindByIdDtoSuccessful() {
        // Given
        String columnUuid = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        Column column = new Column();
        ColumnDTO columnDTO = new ColumnDTO();

        Mockito.when(columnRepository.findById(UUID.fromString(columnUuid)))
                .thenReturn(Optional.of(column));
        Mockito.when(columnMapper.toDTO(column)).thenReturn(columnDTO);

        // When
        ColumnDTO resultDTO = columnService.findByIdDto(columnUuid);

        // Then
        Mockito.verify(columnMapper).toDTO(column);
        assertEquals(columnDTO, resultDTO);
    }

    @Test
    void testFindByIdDtoUnsuccessful() {
        // Given
        String columnUuid = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        Mockito.when(columnRepository.findById(UUID.fromString(columnUuid)))
                .thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(NoDataFoundException.class, () -> {
            columnService.findByIdDto(columnUuid);
        });

        // Then
        assertTrue(exception.getMessage().contains("Column not found"));
    }
}