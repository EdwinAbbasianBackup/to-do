package nl.hu.boardservice.application;

import lombok.AllArgsConstructor;
import nl.hu.boardservice.application.external.UserServiceHTTP;
import nl.hu.boardservice.domain.data.Task;
import nl.hu.boardservice.infrastructure.TaskRepository;
import nl.hu.boardservice.application.exception.NoDataFoundException;
import nl.hu.boardservice.presentation.dto.builder.TaskMapper;
import nl.hu.boardservice.presentation.dto.task.TaskDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserServiceHTTP userServiceHTTP;
    private final ColumnService columnService;
    private final TaskMapper taskMapper;


    public Task create(Task newTask, String columnUuid) {
        Task task = new Task();
        task.setTitle(newTask.getTitle());
        task.setDescription(newTask.getDescription());

        columnService.addTaskToColumn(task, columnUuid);

        return taskRepository.save(task);
    }

    @Transactional
    public Task switchTaskToOtherColumn(String taskId, String columnId) {
        Task task = taskRepository
                .findById(UUID.fromString(taskId))
                .orElseThrow(() -> new NoDataFoundException("Task not found"));

        columnService.removeTaskFromColumn(task, columnService.findColumnByTaskID(taskId).getId().toString());
        columnService.addTaskToColumn(task, columnId);

        return taskRepository.save(task);
    }

    public Task save(String title, String description) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Task task, String id) {
        Task existingTask = taskRepository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new NoDataFoundException("Task not found"));

        if (task.getTitle() != null) {
            existingTask.setTitle(task.getTitle());
        }

        if (task.getDescription() != null) {
            existingTask.setDescription(task.getDescription());
        }
        return existingTask;
    }

    @Transactional
    public void deleteTaskById(String columnUuid, String taskUuid) {
        Task existingTask = taskRepository
                .findById(UUID.fromString(taskUuid))
                .orElseThrow(() -> new NoDataFoundException("Task not found"));

        columnService.removeTaskFromColumn(existingTask, columnUuid);
    }

    @Transactional
    public void assignTask(String taskId, String userId) {
        Task existingTask = getTaskById(taskId).orElseThrow(() -> new NoDataFoundException("Task not found"));
        final var user = userServiceHTTP.getUserById(userId);

        if (user == null) {
            throw new NoDataFoundException("User not found");
        }

        existingTask.addAssignee(user.id, user.username);

    }

    @Transactional
    public void unassignedTask(String taskId, String userId) {
        Task existingTask = getTaskById(taskId).orElseThrow(() -> new NoDataFoundException("Task not found"));
        final var user = userServiceHTTP.getUserById(userId);

        if (user == null) {
            throw new NoDataFoundException("User not found");
        }

        existingTask.removeAssignee(user.id);
    }


    public List<TaskDTO> getAllTasks() {
        return taskMapper.toDTOList(taskRepository.findAll());
    }

    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(UUID.fromString(id));
    }




}
