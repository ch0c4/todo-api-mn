package org.johan.tasks.services;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.johan.tasks.domains.CreateTaskRequest;
import org.johan.tasks.domains.Task;
import org.johan.tasks.domains.TaskResponse;
import org.johan.tasks.domains.UpdateTaskRequest;
import org.johan.tasks.mappers.TaskMapper;
import org.johan.tasks.repositories.TaskRepository;
import org.johan.users.domains.User;
import org.johan.users.repositories.UserRepository;

@Singleton
public class TaskService {

  private final TaskRepository repository;
  private final UserRepository userRepository;

  public TaskService(TaskRepository repository, UserRepository userRepository) {
    this.repository = repository;
    this.userRepository = userRepository;
  }

  private User getUser(String email) {
    return userRepository.findByEmail(email);
  }

  public TaskResponse addTask(Authentication authentication, CreateTaskRequest request) {
    var user = getUser(authentication.getName());
    var newTask =
        Task.builder()
            .id(UUID.randomUUID())
            .description(request.getDescription())
            .completed(false)
            .userId(user.getId())
            .build();
    repository.save(newTask);
    return TaskMapper.toResponse(newTask);
  }

  public List<TaskResponse> getAllTask(Authentication authentication) {
    var user = getUser(authentication.getName());
    var tasks = repository.getTasksByUser(user.getId());
    return getTaskResponseList(tasks);
  }

  public TaskResponse getTaskById(Authentication authentication, UUID id) {
    var user = getUser(authentication.getName());
    var task = repository.findByIdAndUserId(id, user.getId());
    if (task == null) {
      throw new HttpStatusException(HttpStatus.NOT_FOUND, "Task not found");
    }
    return TaskMapper.toResponse(task);
  }

  public List<TaskResponse> getTaskByCompleted(Authentication authentication, Boolean completed) {
    var user = getUser(authentication.getName());
    var tasks = repository.findByCompletedAndUserId(completed, user.getId());
    if (tasks.isEmpty()) {
      return new ArrayList<>();
    }
    return getTaskResponseList(tasks);
  }

  public List<TaskResponse> getPaginateTask(
      Authentication authentication, Integer skip, Integer limit) {
    var user = getUser(authentication.getName());
    var tasks = repository.getPaginateTasks(user.getId(), skip, limit);
    if (tasks.isEmpty()) {
      return new ArrayList<>();
    }
    return getTaskResponseList(tasks);
  }

  private static List<TaskResponse> getTaskResponseList(List<Task> tasks) {
    return tasks.stream().map(TaskMapper::toResponse).toList();
  }

  public TaskResponse updateTask(
      Authentication authentication, UUID id, UpdateTaskRequest request) {
    var user = getUser(authentication.getName());
    var task = repository.findByIdAndUserId(id, user.getId());
    if (task == null) {
      throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Task not found");
    }
    if (request.getCompleted() != null) {
      task.setCompleted(request.getCompleted());
    }
    if (request.getDescription() != null) {
      task.setDescription(request.getDescription());
    }
    repository.update(task);
    return TaskMapper.toResponse(task);
  }

  public void deleteTask(Authentication authentication, UUID id) {
    var user = getUser(authentication.getName());
    repository.remove(id, user.getId());
  }
}
