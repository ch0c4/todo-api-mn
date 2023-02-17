package org.johan.users.services;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.types.files.SystemFile;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.johan.users.domains.RegisterRequest;
import org.johan.users.domains.UpdateUserRequest;
import org.johan.users.domains.User;
import org.johan.users.domains.UserResponse;
import org.johan.users.mappers.UserMapper;
import org.johan.users.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

@Slf4j
@Singleton
public class UserService {

  private final UserRepository repository;

  public UserService(UserRepository repository) {
    this.repository = repository;
  }

  public UserResponse register(RegisterRequest request) {
    var user = getUser(request.getEmail());
    if (user != null) {
      throw new HttpStatusException(HttpStatus.BAD_REQUEST, "User already exists with this email");
    }

    var newUser =
        User.builder()
            .id(UUID.randomUUID())
            .name(request.getName())
            .email(request.getEmail())
            .password(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt(12)))
            .age(request.getAge())
            .build();
    repository.save(newUser);
    return UserMapper.toResponse(newUser);
  }

  public UserResponse me(Authentication authentication) {
    var user = getUser(authentication.getName());
    return UserMapper.toResponse(user);
  }

  private User getUser(String email) {
    return repository.findByEmail(email);
  }

  public UserResponse update(Authentication authentication, UpdateUserRequest request) {
    var user = getUser(authentication.getName());
    user.setAge(request.getAge());
    repository.update(user);
    return UserMapper.toResponse(user);
  }

  public UserResponse uploadAvatar(Authentication authentication, CompletedFileUpload fileUpload) {
    if ((fileUpload.getFilename() == null || fileUpload.getFilename().equals(""))) {
      throw new HttpStatusException(HttpStatus.BAD_REQUEST, "File is empty or null");
    }
    try {

      var file = new File("src/main/resources/" + fileUpload.getFilename());
      var outputStream = new FileOutputStream(file);
      outputStream.write(fileUpload.getBytes());
      var user = getUser(authentication.getName());
      user.setAvatar(file.getPath());
      repository.update(user);
      return UserMapper.toResponse(user);
    } catch (IOException e) {
      throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in writing file");
    }
  }

  public SystemFile avatar(UUID id) {
    var user = repository.findById(id);
    if (user == null) {
      throw new HttpStatusException(HttpStatus.BAD_REQUEST, "User not found");
    }
    var path = Paths.get(user.getAvatar());
    return new SystemFile(path.toFile()).attach("avatar.png");
  }

  public void deleteAvatar(Authentication authentication) {
    var user = getUser(authentication.getName());
    if (user.getAvatar() == null) {
      throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Avatar not found");
    }
    user.setAvatar(null);
    repository.update(user);
  }

  public void deleteUser(Authentication authentication) {
    var user = getUser(authentication.getName());
    repository.remove(user);
  }
}
