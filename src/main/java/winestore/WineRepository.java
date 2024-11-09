package winestore;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WineRepository extends JpaRepository<Wine, Long> {
    List<Wine> findTop10By();
}
