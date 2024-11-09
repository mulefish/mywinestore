package winestore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/wines")
public class WineController {

    private final WineRepository wineRepository;

    @Autowired
    public WineController(WineRepository wineRepository) {
        this.wineRepository = wineRepository;
    }

    // Get all wines (READ)
    @GetMapping
    public List<Wine> getAllWines() {
        return wineRepository.findAll();
    }

    // Get a wine by ID (READ)
    @GetMapping("/{id}")
    public Wine getWineById(@PathVariable Long id) {
        return wineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wine not found"));
    }

    // Add a new wine (CREATE)
    @PostMapping
    public Wine createWine(@RequestBody Wine wine) {
        return wineRepository.save(wine);
    }

    // Update an existing wine (UPDATE)
    @PutMapping("/{id}")
    public Wine updateWine(@PathVariable Long id, @RequestBody Wine wineDetails) {
        return wineRepository.findById(id)
                .map(wine -> {
                    wine.setType(wineDetails.getType());
                    wine.setVariety(wineDetails.getVariety());
                    wine.setYear(wineDetails.getYear());
                    wine.setRegion(wineDetails.getRegion());
                    wine.setPrice(wineDetails.getPrice());
                    wine.setTopnote(wineDetails.getTopnote());
                    wine.setBottomnote(wineDetails.getBottomnote());
                    return wineRepository.save(wine);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wine not found"));
    }

    // Delete a wine (DELETE)
    @DeleteMapping("/{id}")
    public void deleteWine(@PathVariable Long id) {
        if (wineRepository.existsById(id)) {
            wineRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wine not found");
        }
    }
}
