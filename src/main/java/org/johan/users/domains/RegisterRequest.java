package org.johan.users.domains;

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
public class RegisterRequest {

  @NotNull @NotBlank private String name;
  @NotNull @NotBlank private String email;
  @NotNull @NotBlank private String password;
  @NotNull private Integer age;
}
