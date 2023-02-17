package org.johan.tasks.domains;

import io.micronaut.serde.annotation.Serdeable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Serdeable
public class CreateTaskRequest {

  @NotNull @NotBlank private String description;
}
