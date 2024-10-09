package nl.hu.boardservice.presentation.dto.builder;

import nl.hu.boardservice.domain.data.Column;
import nl.hu.boardservice.presentation.dto.column.ColumnDTO;
import nl.hu.boardservice.presentation.dto.tag.TagDTO;
import nl.hu.boardservice.presentation.dto.task.TaskDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ColumnMapperTest {
    @Autowired
    private ColumnMapper columnMapper = Mappers.getMapper(ColumnMapper.class);

    @Test
    void testToDTO() {
        // Given
        Column column = new Column();
        column.setName("Sample Column");

        // When
        ColumnDTO columnDTO = columnMapper.toDTO(column);

        // Then
        assertThat(columnDTO.getName()).isEqualTo(column.getName());
    }

    @Test
    void testToEntity() {
        List<TagDTO> labels = new ArrayList<>();
        labels.add(new TagDTO("Bug"));
        labels.add(new TagDTO("Frontend"));
        // Given
        ColumnDTO columnDTO = new ColumnDTO();
        columnDTO.setName("Sample");
        TaskDTO task = new TaskDTO();
        task.setTags(labels);
        Set<TaskDTO> tasks = Set.of(task);
        columnDTO.setTasks(tasks);

        // When
        Column column = columnMapper.toEntity(columnDTO);

        // Then
        assertThat(column.getName()).isEqualTo(columnDTO.getName());
    }

    @Test
    void testToEntityList() {
        // Given
        List<TagDTO> labels = new ArrayList<>();
        labels.add(new TagDTO("Bug"));
        labels.add(new TagDTO("Frontend"));

        TaskDTO task = new TaskDTO();
        task.setTags(labels);
        Set<TaskDTO> tasks = Set.of(task);

        ColumnDTO columnDTO1 = new ColumnDTO();
        columnDTO1.setName("1");
        columnDTO1.setTasks(tasks);

        ColumnDTO columnDTO2 = new ColumnDTO();
        columnDTO2.setName("2");
        columnDTO2.setTasks(tasks);

        List<ColumnDTO> columnDTOList = List.of(columnDTO1, columnDTO2);

        // When
        List<Column> columnList = columnMapper.toEntityList(columnDTOList);

        // Then
        assertThat(columnList).isNotEmpty();
        assertThat(columnList.get(0).getName()).isEqualTo(columnDTO1.getName());
        assertThat(columnList.get(1).getName()).isEqualTo(columnDTO2.getName());
    }

    @Test
    void testToDTOList() {
        // Given
        Column column1 = new Column();
        column1.setName("1");

        Column column2 = new Column();
        column2.setName("2");

        List<Column> columnList = List.of(column1, column2);

        // When
        List<ColumnDTO> columnDTOList = columnMapper.toDTOList(columnList);

        // Then
        assertThat(columnDTOList).isNotEmpty();
        assertThat(columnDTOList.get(0).getName()).isEqualTo(column1.getName());
        assertThat(columnDTOList.get(1).getName()).isEqualTo(column2.getName());
    }
}