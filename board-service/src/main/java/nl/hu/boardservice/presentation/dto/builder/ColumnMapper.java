package nl.hu.boardservice.presentation.dto.builder;

import nl.hu.boardservice.domain.data.Column;
import nl.hu.boardservice.domain.data.Task;
import nl.hu.boardservice.presentation.dto.column.ColumnDTO;
import nl.hu.boardservice.presentation.dto.task.TaskDTO;
import nl.hu.boardservice.presentation.dto.task.TaskPatchDTO;
import nl.hu.boardservice.presentation.dto.task.TaskPostDTO;
import nl.hu.boardservice.presentation.dto.task.TaskPutDTO;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = TagMapper.class)
public interface ColumnMapper {

    ColumnDTO toDTO(Column column);

    Column toEntity(ColumnDTO columnDTO);

    List<Column> toEntityList(List<ColumnDTO> columnDTOList);

    List<ColumnDTO> toDTOList(List<Column> columnList);
}
