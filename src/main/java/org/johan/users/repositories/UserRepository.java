package org.johan.users.repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import jakarta.inject.Singleton;
import java.util.Optional;
import java.util.UUID;
import org.bson.Document;
import org.johan.users.domains.User;

@Singleton
public class UserRepository {

  private final MongoClient client;

  public UserRepository(MongoClient client) {
    this.client = client;
  }

  public void save(User user) {
    getCollection().insertOne(user);
  }

  public User findById(UUID id) {
    return getCollection().find(Filters.eq("_id", id)).first();
  }

  public User findByEmail(String email) {
    return getCollection().find(Filters.eq("email", email)).first();
  }

  private MongoCollection<User> getCollection() {
    return client.getDatabase("todo").getCollection("user", User.class);
  }

  public void update(User user) {
    getCollection().findOneAndUpdate(Filters.eq("_id", user.getId()), new Document("$set", user));
  }

  public void remove(User user) {
    getCollection().findOneAndDelete(Filters.eq("_id", user.getId()));
  }
}
