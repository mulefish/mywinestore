package winestore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Logger;

@Service
public class WineService {

    private static final Logger logger = Logger.getLogger(WineService.class.getName());
    private final WineRepository wineRepository;
    private final WineVectorRepository wineVectorRepository;

    @Autowired
    public WineService(WineRepository wineRepository, WineVectorRepository wineVectorRepository) {
        this.wineRepository = wineRepository;
        this.wineVectorRepository = wineVectorRepository;
        logger.info("WineService instantiated with WineRepository and WineVectorRepository");
    }

    public List<Wine> getTop10Wines() {
        logger.info("getTop10Wines called");
        List<Wine> top10Wines = wineRepository.findTop10By();
        if (top10Wines != null) {
            logger.info("Retrieved " + top10Wines.size() + " wines from repository");
            top10Wines.forEach(wine -> logger.info("Wine: " + wine.toString()));
        } else {
            logger.warning("No wines retrieved; result is null");
        }
        return top10Wines;
    }

    public List<WineVector> searchWineVectors(String criteria) {
        logger.info("searchWineVectors called with criteria: " + criteria);
        List<WineVector> vectors = wineVectorRepository.findByCriteria(criteria);
        return vectors;
    }
}
