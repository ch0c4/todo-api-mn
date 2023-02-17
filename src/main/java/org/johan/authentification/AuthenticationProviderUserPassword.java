package org.johan.authentification;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Singleton;
import org.johan.users.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink.OverflowStrategy;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

  private final UserRepository repository;

  public AuthenticationProviderUserPassword(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public Publisher<AuthenticationResponse> authenticate(
      HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> request) {
    return Flux.create(
        emitter -> {
          var user = repository.findByEmail((String) request.getIdentity());
          if (user == null) {
            emitter.error(AuthenticationResponse.exception());
            return;
          }

          if (!BCrypt.checkpw((String) request.getSecret(), user.getPassword())) {
            emitter.error(AuthenticationResponse.exception());
            return;
          }
          emitter.next(AuthenticationResponse.success(user.getEmail()));
          emitter.complete();
        },
        OverflowStrategy.ERROR);
  }
}
