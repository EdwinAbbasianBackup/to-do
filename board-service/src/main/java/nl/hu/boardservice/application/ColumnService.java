package nl.hu.boardservice.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.hu.boardservice.application.exception.NoDataFoundException;
import nl.hu.boardservice.domain.data.Column;
import nl.hu.boardservice.domain.data.Task;
import nl.hu.boardservice.infrastructure.ColumnRepository;
import nl.hu.boardservice.presentation.dto.builder.ColumnMapper;
import nl.hu.boardservice.presentation.dto.column.ColumnDTO;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ColumnService {
    private final ColumnRepository columnRepository;
    private final ColumnMapper columnMapper;

    public Column create(String columnName) {
        Column column = Column.builder()
                .name(columnName)
                .build();

        return columnRepository.save(column);
    }

    public Column update(String columnId, String toBeUpdatedColumnName) {
        Column existingColumn = columnRepository
                .findById(UUID.fromString(columnId))
                .orElseThrow(() -> new NoDataFoundException("Column not found"));

        existingColumn.setName(toBeUpdatedColumnName);

        return columnRepository.save(existingColumn);
    }


    public void delete(String columnId) {
        Column column = columnRepository
                .findById(UUID.fromString(columnId))
                .orElseThrow(() -> new NoDataFoundException("Column not found"));

        columnRepository.deleteById(column.getId());
    }

    @Transactional
    public void addTaskToColumn(Task task, String columnUuid) {
        Column column = columnRepository
                .findById(UUID.fromString(columnUuid))
                .orElseThrow(() -> new NoDataFoundException("Column not found"));

        column.addTask(task);
    }

    @Transactional
    public void removeTaskFromColumn(Task task, String columnUuid) {
        Column column = columnRepository
                .findById(UUID.fromString(columnUuid))
                .orElseThrow(() -> new NoDataFoundException("Column not found"));

        column.removeTask(task);
    }

    public ColumnDTO findByIdDto(String columnUuid) {
        Column column = columnRepository
                .findById(UUID.fromString(columnUuid))
                .orElseThrow(() -> new NoDataFoundException("Column not found"));

        return columnMapper.toDTO(column);
    }

    public Column findById(String columnUuid) {
        return columnRepository
                .findById(UUID.fromString(columnUuid))
                .orElseThrow(() -> new NoDataFoundException("Column not found"));
    }

    public Column findColumnByTaskID(String taskId) {
        return columnRepository.findColumnByTasksId(UUID.fromString(taskId))
                .orElseThrow(() -> new NoDataFoundException(
                       """
                       No column found with task id: %s
                       """.formatted(taskId)));
    }


}
