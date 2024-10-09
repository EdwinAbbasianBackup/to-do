package nl.hu.boardservice.presentation.dto.builder;

import nl.hu.boardservice.domain.data.Tag;
import nl.hu.boardservice.domain.data.Task;
import nl.hu.boardservice.presentation.dto.tag.TagDTO;
import nl.hu.boardservice.presentation.dto.task.TaskDTO;
import nl.hu.boardservice.presentation.dto.task.TaskPatchDTO;
import nl.hu.boardservice.presentation.dto.task.TaskPostDTO;
import nl.hu.boardservice.presentation.dto.task.TaskPutDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class TaskMapperTest {
    @Autowired
    private TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);


    @Test
    void testToDTO() {
        // Given
        UUID uuid = UUID.randomUUID();
        UUID uuidAssignee = UUID.randomUUID();
        List <String> assignees = List.of(uuidAssignee.toString());
        Tag tag = new Tag();
        List<Tag> tagList = List.of(tag);
        Task task = new Task();
        task.setId(uuid);
        task.setTitle("testTitle");
        task.setDescription("testDescription");
        task.setTags(tagList);

        // When
        TaskDTO taskDTO = taskMapper.toDTO(task);

        // Then
        assertThat(taskDTO.getId()).isEqualTo(task.getId().toString());
        assertThat(taskDTO.getTitle()).isEqualTo(task.getTitle());
        assertThat(taskDTO.getDescription()).isEqualTo(task.getDescription());
    }

    @Test
    void testToEntityFromDTO() {
        // Given
        TagDTO tag = new TagDTO();
        List<TagDTO> tagList = List.of(tag);
        TaskDTO taskDTO = TaskDTO.builder()
                .id("f47ac10b-58cc-4372-a567-0e02b2c3d479")
                .title("testTitle")
                .description("testDescription")
                .tags(tagList)
                .build();

        // When
        Task task = taskMapper.toEntity(taskDTO);

        // Then
        if(task.getId() != null) {
            assertThat(task.getId().toString()).isEqualTo(taskDTO.getId());
        }
        assertThat(task.getTitle()).isEqualTo(taskDTO.getTitle());
        assertThat(task.getDescription()).isEqualTo(taskDTO.getDescription());

    }

    @Test
    void testToEntityFromPostDTO() {
        // Given
        TaskPostDTO taskPostDTO = TaskPostDTO.builder()
                .title("Sample Title")
                .description("Sample Description")
                .build();

        // When
        Task task = taskMapper.toEntity(taskPostDTO);

        // Then
        assertThat(task.getTitle()).isEqualTo(taskPostDTO.getTitle());
        assertThat(task.getDescription()).isEqualTo(taskPostDTO.getDescription());

        assertThat(task.getTags()).isEmpty();
        assertThat(task.getAssignees()).isEmpty();
    }

    @Test
    void testToEntityFromPutDTO() {
        // Given
        Set<TagDTO> labels = new HashSet<>();
        labels.add(new TagDTO("Bug"));
        labels.add(new TagDTO("Frontend"));

        HashMap<String, String> assignees = new HashMap<>();
        assignees.put("1", "Alice");
        assignees.put("2", "Bob");

        TaskPutDTO taskPutDTO = TaskPutDTO.builder()
                .title("Sample Title")
                .description("Sample Description")
                .labels(labels)
                .assignees(assignees)
                .build();

        // When
        Task task = taskMapper.toEntity(taskPutDTO);

        // Then
        assertThat(task.getTitle()).isEqualTo(taskPutDTO.getTitle());
        assertThat(task.getDescription()).isEqualTo(taskPutDTO.getDescription());
        assertThat(task.getAssignees()).isEqualTo(taskPutDTO.getAssignees());

    }

    @Test
    void testToEntityFromPatchDTO() {
        // Given
        TaskPatchDTO taskPatchDTO = TaskPatchDTO.builder()
                .title("Updated Title")
                .description("Updated Description")
                .build();

        Task task = Task.builder()
                .title("Old Title")
                .description("Old Description")
                .build();

        // When
        taskMapper.updateModel(task, taskPatchDTO);

        // Then
        assertThat(task.getTitle()).isEqualTo(taskPatchDTO.getTitle());
        assertThat(task.getDescription()).isEqualTo(taskPatchDTO.getDescription());
    }

    @Test
    void testToEntityList() {
        // Given
        List<TagDTO> labels = new ArrayList<>();
        labels.add(new TagDTO("Bug"));
        labels.add(new TagDTO("Frontend"));

        TaskDTO taskDTO1 = TaskDTO.builder()
                .id("1")
                .title("Title1")
                .description("Description1")
                .tags(labels)
                .build();

        TaskDTO taskDTO2 = TaskDTO.builder()
                .id("2")
                .title("Title2")
                .description("Description2")
                .tags(labels)
                .build();

        List<TaskDTO> taskDTOList = Arrays.asList(taskDTO1, taskDTO2);

        // When
        List<Task> taskList = taskMapper.toEntityList(taskDTOList);

        // Then
        assertThat(taskList).hasSize(2);
        assertThat(taskList.get(0).getTitle()).isEqualTo(taskDTO1.getTitle());
        assertThat(taskList.get(0).getDescription()).isEqualTo(taskDTO1.getDescription());
        assertThat(taskList.get(1).getTitle()).isEqualTo(taskDTO2.getTitle());
        assertThat(taskList.get(1).getDescription()).isEqualTo(taskDTO2.getDescription());

    }

    @Test
    void testToDTOList() {
        // Given
        Task task1 = Task.builder()
                .title("Title1")
                .description("Description1")
                .build();

        Task task2 = Task.builder()
                .title("Title2")
                .description("Description2")
                .build();

        List<Task> taskList = Arrays.asList(task1, task2);

        // When
        List<TaskDTO> taskDTOList = taskMapper.toDTOList(taskList);

        // Then
        assertThat(taskDTOList).hasSize(2);
        assertThat(taskDTOList.get(0).getTitle()).isEqualTo(task1.getTitle());
        assertThat(taskDTOList.get(0).getDescription()).isEqualTo(task1.getDescription());
        assertThat(taskDTOList.get(1).getTitle()).isEqualTo(task2.getTitle());
        assertThat(taskDTOList.get(1).getDescription()).isEqualTo(task2.getDescription());

    }
}