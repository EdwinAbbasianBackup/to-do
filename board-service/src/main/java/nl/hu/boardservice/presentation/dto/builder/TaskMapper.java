package nl.hu.boardservice.presentation.dto.builder;

import nl.hu.boardservice.domain.data.Task;
import nl.hu.boardservice.presentation.dto.task.TaskDTO;
import nl.hu.boardservice.presentation.dto.task.TaskPatchDTO;
import nl.hu.boardservice.presentation.dto.task.TaskPostDTO;
import nl.hu.boardservice.presentation.dto.task.TaskPutDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = TagMapper.class)
public interface TaskMapper {

    TaskDTO toDTO(Task task);

    Task toEntity(TaskDTO taskDTO);

    Task toEntity(TaskPostDTO taskPostDTO);

    Task toEntity(TaskPutDTO taskPutDTO);

    Task toEntity(TaskPatchDTO taskPatchDTO);

    List<Task> toEntityList(List<TaskDTO> taskDTOList);

    List<TaskDTO> toDTOList(List<Task> taskList);

    @InheritConfiguration
    void updateModel(@MappingTarget Task task, TaskPatchDTO dto);
}