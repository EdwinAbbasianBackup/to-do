package nl.hu.boardservice.infrastructure;

import nl.hu.boardservice.domain.data.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface BoardRepository extends JpaRepository<Board, UUID> {

    void deleteById(UUID uuid);

    @Query("SELECT b FROM Board b WHERE b.owner = :owner OR :owner MEMBER OF b.assignees")
    List<Board> findBoardsByUser(@Param("owner") String owner);

    @Query("SELECT b FROM Board b JOIN b.columns c WHERE c.id = ?1")
    Optional<Board> findBoardByColumnId(UUID columnID);

}
