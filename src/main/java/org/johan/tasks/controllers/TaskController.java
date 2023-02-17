package org.johan.tasks.controllers;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import java.util.List;
import java.util.UUID;
import org.johan.tasks.domains.CreateTaskRequest;
import org.johan.tasks.domains.TaskResponse;
import org.johan.tasks.domains.UpdateTaskRequest;
import org.johan.tasks.services.TaskService;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/tasks")
public class TaskController {

  private final TaskService service;

  public TaskController(TaskService service) {
    this.service = service;
  }

  @Post
  public TaskResponse addTask(Authentication authentication, @Body CreateTaskRequest request) {
    return service.addTask(authentication, request);
  }

  @Get
  public List<TaskResponse> getAllTask(Authentication authentication) {
    return service.getAllTask(authentication);
  }

  @Get("/{id}")
  public TaskResponse getTaskById(Authentication authentication, UUID id) {
    return service.getTaskById(authentication, id);
  }

  @Get("/{?completed*}")
  public List<TaskResponse> getTaskByCompleted(
      Authentication authentication, @QueryValue("completed") Boolean completed) {
    if (completed == null) {
      completed = false;
    }
    completed = completed == null ? false : completed;
    return service.getTaskByCompleted(authentication, completed);
  }

  @Get("/{?skip*}{?limit*}")
  public List<TaskResponse> getPaginateTask(
      Authentication authentication,
      @QueryValue(value = "skip", defaultValue = "0") Integer skip,
      @QueryValue(value = "limit", defaultValue = "10") Integer limit) {
    return service.getPaginateTask(authentication, skip, limit);
  }

  @Put("/{id}")
  public TaskResponse updateTask(Authentication authentication, UUID id, @Body UpdateTaskRequest request) {
    return service.updateTask(authentication, id, request);
  }

  @Delete("/{id}")
  public void deleteTask(Authentication authentication, UUID id) {
    service.deleteTask(authentication, id);
  }
}
