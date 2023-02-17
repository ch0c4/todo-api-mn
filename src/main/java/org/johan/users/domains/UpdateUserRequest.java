package org.johan.users.domains;

import io.micronaut.serde.annotation.Serdeable;
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
public class UpdateUserRequest {

  @NotNull private Integer age;
}
