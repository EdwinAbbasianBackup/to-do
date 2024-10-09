package nl.hu.boardservice.application;

import nl.hu.boardservice.application.external.UserServiceHTTP;
import nl.hu.boardservice.domain.data.Column;
import nl.hu.boardservice.domain.data.Task;
import nl.hu.boardservice.infrastructure.TaskRepository;
import nl.hu.boardservice.presentation.dto.builder.TaskMapper;
import nl.hu.boardservice.presentation.dto.task.TaskDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;
    @Mock
    UserServiceHTTP userServiceHTTP;
    @Mock
    ColumnService columnService;
    @Mock
    TaskMapper taskMapper;

    @InjectMocks
    TaskService taskService;

    @Captor
    ArgumentCaptor<Task> taskCaptor;
    @Test
    void testCreateTask() {
        // Given
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Description for test task");
        String columnUuid = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        Mockito.when(taskRepository.save(taskCaptor.capture())).thenReturn(task);

        // When
        Task result = taskService.create(task, columnUuid);

        // Then
        Task savedTask = taskCaptor.getValue();
        Mockito.verify(taskRepository).save(taskCaptor.capture());
        assertEquals(task.getTitle(), savedTask.getTitle());
        assertEquals(task.getDescription(), savedTask.getDescription());
        assertNotNull(result);
    }

    @Test
    void testSwitchTaskToOtherColumn() {
        // Given
        String taskId = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        String columnId = "f47ac10b-58cc-4372-a567-0e02b2c3d455";
        String columnToSwitchToId = "f47ac10b-58cc-4372-a567-0e02b2c3d412";
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Description for test task");
        taskService.create(task, columnId);

        Mockito.when(taskRepository.findById(UUID.fromString(taskId))).thenReturn(Optional.of(task));

        Column column = new Column();
        column.setId(UUID.fromString(columnId));
        Mockito.when(columnService.findColumnByTaskID(taskId)).thenReturn(column);

        // When
        Task result = taskService.switchTaskToOtherColumn(taskId, columnToSwitchToId);

        // Then
        assertEquals(columnId, columnService.findColumnByTaskID(taskId).getId().toString());
        Mockito.verify(taskRepository).findById(UUID.fromString(taskId));
    }

    @Test
    void testUpdateTask() {
        // Given
        String taskId = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        Task task = new Task();
        task.setTitle("Updated Task");
        task.setDescription("Updated Description");

        Task existingTask = new Task();
        Mockito.when(taskRepository.findById(UUID.fromString(taskId))).thenReturn(Optional.of(existingTask));

        // When
        Task result = taskService.updateTask(task, taskId);

        // Then
        assertEquals(task.getTitle(), result.getTitle());
        assertEquals(task.getDescription(), result.getDescription());
        Mockito.verify(taskRepository).findById(UUID.fromString(taskId));
    }


    @Test
    void testDeleteTaskById() {
        // Given
        String taskUuid = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        String columnUuid = "f47ac10b-58cc-4372-a567-0e02b2c3d455";
        Task existingTask = new Task();
        Mockito.when(taskRepository.findById(UUID.fromString(taskUuid))).thenReturn(Optional.of(existingTask));

        // When
        taskService.deleteTaskById(columnUuid, taskUuid);

        // Then
        Mockito.verify(taskRepository).findById(UUID.fromString(taskUuid));
        Mockito.verify(columnService).removeTaskFromColumn(taskCaptor.capture(), eq(columnUuid));
        assertEquals(existingTask, taskCaptor.getValue());

    }

    @Test
    void testGetAllTasks() {
        // Given
        List<Task> tasks = List.of(new Task());
        Mockito.when(taskRepository.findAll()).thenReturn(tasks);
        List<TaskDTO> taskDTOs = List.of(new TaskDTO());
        Mockito.when(taskMapper.toDTOList(tasks)).thenReturn(taskDTOs);

        // When
        List<TaskDTO> result = taskService.getAllTasks();

        // Then
        Mockito.verify(taskRepository).findAll();
        Mockito.verify(taskMapper).toDTOList(tasks);
        assertNotNull(result);
        assertEquals(taskDTOs, result);
    }

    @Test
    void testGetTaskById() {
        // Given
        String taskId = "f47ac10b-58cc-4372-a567-0e02b2c3d453";
        Task task = new Task();
        Mockito.when(taskRepository.findById(UUID.fromString(taskId))).thenReturn(Optional.of(task));

        // When
        Optional<Task> result = taskService.getTaskById(taskId);

        // Then
        Mockito.verify(taskRepository).findById(UUID.fromString(taskId));
        assertTrue(result.isPresent());
        assertEquals(task, result.get());
    }
}