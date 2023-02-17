package org.johan.tasks.repositories;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;
import org.bson.Document;
import org.johan.tasks.domains.Task;

@Singleton
public class TaskRepository {

  private final MongoClient client;

  public TaskRepository(MongoClient client) {
    this.client = client;
  }

  public void save(Task task) {
    getCollection().insertOne(task);
  }

  private MongoCollection<Task> getCollection() {
    return client.getDatabase("todo").getCollection("task", Task.class);
  }

  public List<Task> getTasksByUser(UUID userId) {
    var tasks = getCollection().find(Filters.eq("userId", userId));
    return getTaskList(tasks);
  }

  public Task findByIdAndUserId(UUID id, UUID userId) {
    return getCollection()
        .find(Filters.and(Filters.eq("_id", id), Filters.eq("userId", userId)))
        .first();
  }

  public List<Task> findByCompletedAndUserId(Boolean completed, UUID userId) {
    var tasks =
        getCollection()
            .find(Filters.and(Filters.eq("completed", completed), Filters.eq("userId", userId)));
    return getTaskList(tasks);
  }

  public List<Task> getPaginateTasks(UUID userId, Integer skip, Integer limit) {
    var tasks = getCollection().find(Filters.eq("userId", userId)).skip(skip).limit(limit);
    return getTaskList(tasks);
  }

  private static List<Task> getTaskList(FindIterable<Task> tasks) {
    return StreamSupport.stream(tasks.spliterator(), false).toList();
  }

  public void update(Task task) {
    getCollection().findOneAndUpdate(Filters.eq("_id", task.getId()), new Document("$set", task));
  }

  public void remove(UUID id, UUID userId) {
    getCollection()
        .findOneAndDelete(Filters.and(Filters.eq("_id", id), Filters.eq("userId", userId)));
  }
}
