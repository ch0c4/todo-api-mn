package org.johan.users.mappers;

import org.johan.users.domains.User;
import org.johan.users.domains.UserResponse;

public final class UserMapper {
  private UserMapper() {}

  public static UserResponse toResponse(User user) {
    return UserResponse.builder()
        .id(user.getId().toString())
        .name(user.getName())
        .age(user.getAge())
        .email(user.getEmail())
        .avatar(user.getAvatar() != null ? user.getAvatar() : null)
        .build();
  }
}
