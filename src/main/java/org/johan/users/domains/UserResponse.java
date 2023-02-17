package org.johan.users.domains;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@Serdeable
public class UserResponse {

  private String id;
  private String name;
  private String email;
  private Integer age;
  private String avatar;
}
