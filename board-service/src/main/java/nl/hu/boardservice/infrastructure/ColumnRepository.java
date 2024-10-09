package nl.hu.boardservice.infrastructure;

import nl.hu.boardservice.domain.data.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface ColumnRepository extends JpaRepository<Column, UUID> {
    Optional<Column> findById(UUID uuid);

    @Query("SELECT c FROM Column c JOIN c.tasks t WHERE t.id = ?1")
    Optional<Column> findColumnByTasksId(UUID uuid);
}
