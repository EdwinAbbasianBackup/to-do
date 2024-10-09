package nl.hu.boardservice.presentation;

import nl.hu.boardservice.application.TaskService;
import nl.hu.boardservice.domain.data.Task;
import nl.hu.boardservice.presentation.dto.builder.TaskMapper;
import nl.hu.boardservice.presentation.dto.task.TaskDTO;
import nl.hu.boardservice.presentation.dto.task.TaskPatchDTO;
import nl.hu.boardservice.presentation.dto.task.TaskPostDTO;
import nl.hu.boardservice.presentation.dto.user.PatchUserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class TaskControllerTest {
    @Mock
    private TaskService taskService;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskController taskController;

    @Test
    void testCreateTask() {
        // Given
        TaskPostDTO taskDTO = new TaskPostDTO();
        taskDTO.setTitle("Task Title");
        String columnUuid = "someColumnUuid";
        Task task = new Task();
        Mockito.when(taskService.create(taskMapper.toEntity(taskDTO), columnUuid)).thenReturn(task);
        Mockito.when(taskMapper.toDTO(task)).thenReturn(new TaskDTO());

        // When
        ResponseEntity<TaskDTO> result = taskController.createTask(taskDTO, columnUuid);

        // Then
        Mockito.verify(taskService).create(taskMapper.toEntity(taskDTO), columnUuid);
        Mockito.verifyNoMoreInteractions(taskService);
        assertEquals(ResponseEntity.ok(new TaskDTO()), result);
    }

    @Test
    void testSwitchTaskToOtherColumn() {
        // Given
        String taskId = "someTaskId";
        String columnId = "someColumnId";
        Task task = new Task();
        Mockito.when(taskService.switchTaskToOtherColumn(taskId, columnId)).thenReturn(task);
        Mockito.when(taskMapper.toDTO(task)).thenReturn(new TaskDTO());

        // When
        ResponseEntity<TaskDTO> result = taskController.switchTaskToOtherColumn(taskId, columnId);

        // Then
        Mockito.verify(taskService).switchTaskToOtherColumn(taskId, columnId);
        Mockito.verifyNoMoreInteractions(taskService);
        assertEquals(ResponseEntity.ok(new TaskDTO()), result);
    }

    @Test
    void testUpdateTask() {
        // Given
        TaskPatchDTO taskDTO = new TaskPatchDTO();
        taskDTO.setTitle("Updated Task Title");
        String uuid = "someTaskUuid";
        Task task = new Task();
        Mockito.when(taskService.updateTask(taskMapper.toEntity(taskDTO), uuid)).thenReturn(task);
        Mockito.when(taskMapper.toDTO(task)).thenReturn(new TaskDTO());

        // When
        ResponseEntity<TaskDTO> result = taskController.updateTask(taskDTO, uuid);

        // Then
        Mockito.verify(taskService).updateTask(taskMapper.toEntity(taskDTO), uuid);
        Mockito.verifyNoMoreInteractions(taskService);
        assertEquals(ResponseEntity.ok(new TaskDTO()), result);
    }

    @Test
    void testDeleteTaskById() {
        // Given
        String uuid = "someTaskUuid";
        String columnId = "someColumnId";

        // When
        ResponseEntity<?> result = taskController.deleteTaskById(columnId, uuid);

        // Then
        Mockito.verify(taskService).deleteTaskById(columnId, uuid);
        Mockito.verifyNoMoreInteractions(taskService);
        assertEquals(ResponseEntity.noContent().build(), result);
    }

    @Test
    void testGetAllTasks() {
        // Given
        List<TaskDTO> tasks = List.of(new TaskDTO(), new TaskDTO());
        Mockito.when(taskService.getAllTasks()).thenReturn(tasks);

        // When
        ResponseEntity<List<TaskDTO>> result = taskController.getAllTasks();

        // Then
        Mockito.verify(taskService).getAllTasks();
        Mockito.verifyNoMoreInteractions(taskService);
        assertEquals(ResponseEntity.ok(tasks), result);
    }

    @Test
    void testAssignTask() {
        // Given
        String uuid = "someTaskUuid";
        PatchUserDTO userId = new PatchUserDTO(uuid);
        userId.userId = "someUserId";

        // When
        ResponseEntity<?> result = taskController.assignTask(uuid, userId);

        // Then
        Mockito.verify(taskService).assignTask(uuid, userId.userId);
        Mockito.verifyNoMoreInteractions(taskService);
        assertEquals(ResponseEntity.ok().build(), result);
    }

    @Test
    void testUnassignedTask() {
        // Given
        String uuid = "someTaskUuid";
        PatchUserDTO userId = new PatchUserDTO(uuid);
        userId.userId = "someUserId";

        // When
        ResponseEntity<?> result = taskController.unassignedTask(uuid, userId);

        // Then
        Mockito.verify(taskService).unassignedTask(uuid, userId.userId);
        Mockito.verifyNoMoreInteractions(taskService);
        assertEquals(ResponseEntity.ok().build(), result);
    }
}