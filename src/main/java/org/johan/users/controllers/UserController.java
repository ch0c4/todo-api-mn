package org.johan.users.controllers;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.types.files.SystemFile;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import java.util.UUID;
import org.johan.users.domains.RegisterRequest;
import org.johan.users.domains.UpdateUserRequest;
import org.johan.users.domains.UserResponse;
import org.johan.users.services.UserService;

@Controller("/users")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class UserController {

  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @Post("/register")
  @Secured(SecurityRule.IS_ANONYMOUS)
  public UserResponse register(@Body RegisterRequest request) {
    return service.register(request);
  }

  @Get("/me")
  public UserResponse me(Authentication authentication) {
    return service.me(authentication);
  }

  @Put("/me")
  public UserResponse update(Authentication authentication, @Body UpdateUserRequest request) {
    return service.update(authentication, request);
  }

  @Post(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA)
  public UserResponse upload(
      Authentication authentication, @Part("file") CompletedFileUpload fileUpload) {
    return service.uploadAvatar(authentication, fileUpload);
  }

  @Get(value = "/{id}/avatar", produces = MediaType.IMAGE_PNG)
  @Secured(SecurityRule.IS_ANONYMOUS)
  public SystemFile avatar(UUID id) {
    return service.avatar(id);
  }

  @Delete("/me/avatar")
  public void deleteAvatar(Authentication authentication) {
    service.deleteAvatar(authentication);
  }

  @Delete("/me")
  public void deleteUser(Authentication authentication) {
    service.deleteUser(authentication);
  }
}
