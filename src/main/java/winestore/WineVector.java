package winestore;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.*;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "wine_vectors")
public class WineVector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "json")
    private String vector; // Stores JSON-encoded vector data as a string

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVector() {
        return vector;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }

    // Method to get the vector as a list of integers
    public List<Integer> getVectorAsList() {
        // Use a JSON library like Jackson to parse JSON string to List<Integer>
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(vector, new TypeReference<List<Integer>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}