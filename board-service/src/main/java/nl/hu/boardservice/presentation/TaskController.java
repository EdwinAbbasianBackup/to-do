package nl.hu.boardservice.presentation;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hu.boardservice.application.TaskService;
import nl.hu.boardservice.domain.data.Task;
import nl.hu.boardservice.presentation.dto.builder.TaskMapper;
import nl.hu.boardservice.presentation.dto.task.TaskDTO;
import nl.hu.boardservice.presentation.dto.task.TaskPatchDTO;
import nl.hu.boardservice.presentation.dto.task.TaskPostDTO;
import nl.hu.boardservice.presentation.dto.user.PatchUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;


@Slf4j
@AllArgsConstructor
@RestController("TaskControllerV1")
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    /**
     * Creates a new task. this task contains a title, description and is added with a status of OPEN.
     *
     * @param taskDTO the task to be created.
     * @return the created task.
     */
    @PostMapping("/task/{columnUuid}")
    @Operation(summary = "Create a new task.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task successfully created",
                content = @Content(schema = @Schema(implementation = TaskDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid task input", content = @Content)
    })
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Parameter(description = "Task to be created") TaskPostDTO taskDTO,
                                              @PathVariable String columnUuid) {
        if (taskDTO.getTitle() == null) {
            return ResponseEntity.badRequest().build();
        }

        Task task = taskService.create(taskMapper.toEntity(taskDTO), columnUuid);
        return ResponseEntity.ok(taskMapper.toDTO(task));
    }

    @PutMapping("/task/{taskId}/column/{columnId}")
    @Operation(summary = "Switch a task to another column.")
    public ResponseEntity<TaskDTO> switchTaskToOtherColumn(@PathVariable String taskId, @PathVariable String columnId) {
        Task task = taskService.switchTaskToOtherColumn(taskId, columnId);
        return ResponseEntity.ok(taskMapper.toDTO(task));
    }


    /**
     * Update an existing task.
     *
     * @param taskDTO the task details to be updated.
     * @param uuid    the task identifier.
     * @return the updated task.
     */
    @PatchMapping("/{uuid}")
    @Operation(summary = "Update an existing task.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task successfully updated",
                content = @Content(schema = @Schema(implementation = TaskDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid task input", content = @Content),
    })
    public ResponseEntity<TaskDTO> updateTask(@RequestBody @Parameter(description = "Task to be updated") TaskPatchDTO taskDTO, @PathVariable String uuid) {
        if (taskDTO.getTitle() == null) {
            return ResponseEntity.badRequest().build();
        }
        Task task = taskService.updateTask(taskMapper.toEntity(taskDTO), uuid);
        return ResponseEntity.ok(taskMapper.toDTO(task));
    }

    /**
     * Deletes a task by its uuid.
     *
     * @param uuid the task identifier.
     * @return response entity without content.
     */
    @DeleteMapping("/columns/{columnId}/task/{uuid}")
    @Operation(summary = "Delete a task with a given uuid.")
    public ResponseEntity<?> deleteTaskById(@PathVariable String columnId, @PathVariable String uuid) {
        log.info("Deleting task with uuid {}", uuid);
        taskService.deleteTaskById(columnId, uuid);
        return ResponseEntity.noContent().build();
    }

    /**
     * Fetches all tasks.
     *
     * @return list of all tasks.
     */
    @GetMapping
    @Operation(summary = "Retrieve all tasks.")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        log.info("Retrieving all tasks");
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    /**
     * Assigns a task to a user.
     *
     * @param uuid the task identifier.
     * @param userId the user identifier.
     * @return response entity without content.
     */
    @PatchMapping("/{uuid}/assigned")
    @Operation(summary = "Assign a task to a user.")
    public ResponseEntity<?> assignTask(@PathVariable String uuid, @RequestBody PatchUserDTO userId) {
        log.info("Assigning task with id {} to user with id {}", uuid, userId);
        taskService.assignTask(uuid, userId.userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Unassigned a task from a user.
     * @param uuid the task identifier.
     * @param userId the user identifier.
     * @return response entity without content.
     */
    @PatchMapping("/{uuid}/unassigned")
    @Operation(summary = "unassigned a task from a user.")
    public ResponseEntity<?> unassignedTask(@PathVariable String uuid, @RequestBody PatchUserDTO userId) {
        log.info("Unassigning task with id {} from user with id {}", uuid, userId);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        taskService.unassignedTask(uuid, userId.userId);
        return ResponseEntity.ok().build();
    }
}
