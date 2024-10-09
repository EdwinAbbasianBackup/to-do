package nl.hu.boardservice.infrastructure;

import nl.hu.boardservice.domain.data.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findAllById(UUID uuid);

    @Query("SELECT t FROM Task t JOIN t.tags l WHERE l.name = ?1")
    Set<Task> findAllByLabels(String label);

    // WRITE query that gets all tasks
    @Query("SELECT t FROM Task t")
    List<Task> findAllTasks();


    Optional<Task> findByTitle(String title);
}
