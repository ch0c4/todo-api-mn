package org.johan.tasks.domains;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Serdeable
public class UpdateTaskRequest {

  private String description;
  private Boolean completed;

}
