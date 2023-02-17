package org.johan.tasks.domains;

import io.micronaut.serde.annotation.Serdeable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Serdeable
public class Task {

  private UUID id;
  private String description;
  private Boolean completed;
  private UUID userId;
}
