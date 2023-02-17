package org.johan.tasks.mappers;

import org.johan.tasks.domains.Task;
import org.johan.tasks.domains.TaskResponse;

public final class TaskMapper {
  private TaskMapper() {}

  public static TaskResponse toResponse(Task task) {
    return TaskResponse.builder()
        .id(task.getId().toString())
        .description(task.getDescription())
        .completed(task.getCompleted())
        .build();
  }
}
