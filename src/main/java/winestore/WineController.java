package winestore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wines")
public class WineController {

    private final WineRepository wineRepository;
    private final WineVectorRepository wineVectorRepository;

    // Predefined vectors for descriptors
    private static final Map<String, double[]> descriptorVectors = new HashMap<>() {{
        put("spicy", new double[]{0.8, 0.1, 0.1});
        put("fruity", new double[]{0.3, 0.7, 0.0});
        put("bubbly", new double[]{0.2, 0.2, 0.6});
    }};

    @Autowired
    public WineController(WineRepository wineRepository, WineVectorRepository wineVectorRepository) {
        this.wineRepository = wineRepository;
        this.wineVectorRepository = wineVectorRepository;
    }

    // Endpoint to search wines by exact topnote match
    @GetMapping("/search")
    public List<Wine> searchWinesByTopnote(@RequestParam String topnote) {
        return wineRepository.findByTopnote(topnote);
    }

    // Endpoint to search wine vectors by criteria
    @GetMapping("/vectors/findByCriteria")
    public List<WineVector> searchWineVectors(@RequestParam String criteria) {
        return wineVectorRepository.findByCriteria(criteria);
    }

    // Endpoint to find the 5 closest wines based on topnote vector or descriptor
    @GetMapping("/vectors/search")
    public List<Wine> findClosestWines(@RequestParam String topnoteVector) {
        double[] inputVector;

        // Check if topnoteVector is a descriptor (like "spicy")
        if (descriptorVectors.containsKey(topnoteVector.toLowerCase())) {
            inputVector = descriptorVectors.get(topnoteVector.toLowerCase());
        } else {
            // Parse as numeric vector if not a descriptor
            inputVector = Arrays.stream(topnoteVector.split(","))
                    .mapToDouble(Double::parseDouble)
                    .toArray();
        }

        List<Wine> allWines = wineRepository.findAll();
        return allWines.stream()
                .filter(wine -> parseVector(wine.getTopnote()).length == inputVector.length) // Ensure vector length matches
                .sorted((wine1, wine2) -> Double.compare(
                        cosineSimilarity(inputVector, parseVector(wine2.getTopnote())),
                        cosineSimilarity(inputVector, parseVector(wine1.getTopnote()))
                ))
                .limit(5)
                .collect(Collectors.toList());
    }

    // Helper function to parse topnote strings into vectors
    private double[] parseVector(String topnote) {
        return Arrays.stream(topnote.split(","))
                .map(value -> {
                    try {
                        return Double.parseDouble(value);
                    } catch (NumberFormatException e) {
                        System.err.println("Non-numeric value in topnote: " + value);
                        return null;  // Return null to handle gracefully
                    }
                })
                .filter(Objects::nonNull) // Remove any null values resulting from non-numeric entries
                .mapToDouble(Double::doubleValue)
                .toArray();
    }

    // Helper function to calculate cosine similarity
    private double cosineSimilarity(double[] vec1, double[] vec2) {
        double dotProduct = 0, magnitudeA = 0, magnitudeB = 0;
        for (int i = 0; i < vec1.length; i++) {
            dotProduct += vec1[i] * vec2[i];
            magnitudeA += vec1[i] * vec1[i];
            magnitudeB += vec2[i] * vec2[i];
        }
        return dotProduct / (Math.sqrt(magnitudeA) * Math.sqrt(magnitudeB));
    }

    // Endpoint to get the next 10 wines based on page number
    @GetMapping("/next10")
    public List<Wine> getNext10Wines(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return wineRepository.findAll(pageable).getContent();
    }

    // Get all wines (READ)
    @GetMapping
    public List<Wine> getAllWines() {
        return wineRepository.findAll();
    }

    // Get all wines (READ) - Explicit /all endpoint
    @GetMapping("/all")
    public List<Wine> getAllWinesExplicit() {
        return wineRepository.findAll();
    }

    // Get top 10 wines (READ)
    @GetMapping("/top10")
    public List<Wine> getTop10Wines() {
        return wineRepository.findTop10By();
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
