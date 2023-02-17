package org.johan.tasks.domains;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@Serdeable
public class TaskResponse {

  private String id;
  private String description;
  private Boolean completed;
}
