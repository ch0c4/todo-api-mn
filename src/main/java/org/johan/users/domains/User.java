package org.johan.users.domains;

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
public class User {

  private UUID id;
  private String name;
  private String email;
  private String password;
  private Integer age;
  private String avatar;
}
