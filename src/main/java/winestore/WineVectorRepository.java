package winestore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WineVectorRepository extends JpaRepository<WineVector, Long> {

    // Custom query to find vectors by criteria (modify as needed for your use case)
    @Query("SELECT w FROM WineVector w WHERE w.vector LIKE %:criteria%")
    List<WineVector> findByCriteria(String criteria);
}
