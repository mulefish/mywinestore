//package winestore;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class WineService {
//
//    private final WineRepository wineRepository;
//
//    @Autowired
//    public WineService(WineRepository wineRepository) {
//        this.wineRepository = wineRepository;
//    }
//
//    public List<Wine> getTop10Wines() {
//        return wineRepository.findTop10By();
//    }
//}

package winestore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Logger;

@Service
public class WineService {

    private static final Logger logger = Logger.getLogger(WineService.class.getName());
    private final WineRepository wineRepository;

    @Autowired
    public WineService(WineRepository wineRepository) {
        this.wineRepository = wineRepository;
        logger.info("WineService instantiated with WineRepository");
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


}
