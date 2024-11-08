package winestore.controller;

import winestore.model.Wine;
import winestore.repository.WineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wines")
public class WineController {

    @Autowired
    private WineRepository wineRepository;

    @GetMapping
    public List<Wine> getAllWines() {
        return wineRepository.findAll();
    }

    @PostMapping
    public Wine createWine(@RequestBody Wine wine) {
        return wineRepository.save(wine);
    }

    @GetMapping("/{id}")
    public Wine getWineById(@PathVariable Long id) {
        return wineRepository.findById(id).orElseThrow(() -> new RuntimeException("Wine not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteWine(@PathVariable Long id) {
        wineRepository.deleteById(id);
    }
}
