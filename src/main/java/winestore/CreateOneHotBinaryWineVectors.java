package winestore;

import java.sql.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateOneHotBinaryWineVectors {

    private static final List<String> types = Arrays.asList("Red", "Fortified", "Rose", "Sparkling", "White", "Dessert");
    private static final List<String> varieties = Arrays.asList("Zinfandel", "Sauvignon Blanc", "Chardonnay", "Cabernet Sauvignon", "Pinot Noir", "Syrah", "Merlot", "Riesling", "Malbec");
    private static final List<String> topnotes = Arrays.asList("rich", "oak", "bold", "intense", "spicy", "vanilla", "light", "complex", "fruity", "plummy", "dark fruit", "robust", "smooth", "ripe", "herbaceous", "smoky", "woody", "aromatic", "earthy", "sweet", "bubbly", "leathery");
    private static final List<String> regions = Arrays.asList("Mendoza", "Champagne", "Mosel", "Napa Valley", "Sonoma", "Barossa Valley", "Rioja", "Bordeaux", "Tuscany");
    private static final List<String> bottomnotes = Arrays.asList("rich", "oak", "bold", "intense", "spicy", "vanilla", "light", "complex", "fruity", "plummy", "dark fruit", "robust", "smooth", "ripe", "herbaceous", "smoky", "woody", "aromatic", "earthy", "sweet", "bubbly", "citrus", "floral", "leathery");

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/test_db";
        String user = "postgres";
        String password = "topsecret";

        // Initialize Jackson ObjectMapper for JSON serialization
        ObjectMapper mapper = new ObjectMapper();

        // Generate column names for each feature
        List<String> columnNames = new ArrayList<>();
        columnNames.addAll(generateColumnNames("type", types));
        columnNames.addAll(generateColumnNames("variety", varieties));
        columnNames.addAll(generateColumnNames("topnote", topnotes));
        columnNames.addAll(generateColumnNames("region", regions));
        columnNames.addAll(generateColumnNames("bottomnote", bottomnotes));

        // Print the array of column names
        System.out.println("One-Hot Encoded Column Names: " + columnNames);

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement stmt = connection.createStatement()) {

            // Drop and create table
            stmt.execute("DROP TABLE IF EXISTS wine_vectors;");
            stmt.execute("CREATE TABLE wine_vectors (id SERIAL PRIMARY KEY, vector JSON);");

            // Query wine data
            String query = "SELECT id, type, variety, topnote, region, bottomnote FROM wines;";
            ResultSet rs = stmt.executeQuery(query);

            // Prepare statement for inserting vectors
            String insertQuery = "INSERT INTO wine_vectors (vector) VALUES (?::json);";
            PreparedStatement insertStmt = connection.prepareStatement(insertQuery);

            // Process each wine row
            while (rs.next()) {
                int wineId = rs.getInt("id");
                String type = rs.getString("type");
                String variety = rs.getString("variety");
                String topnote = rs.getString("topnote");
                String region = rs.getString("region");
                String bottomnote = rs.getString("bottomnote");

                // Create the binary vector for each feature
                int[] typeVector = oneHotEncode(type, types);
                int[] varietyVector = oneHotEncode(variety, varieties);
                int[] topnoteVector = oneHotEncode(topnote, topnotes);
                int[] regionVector = oneHotEncode(region, regions);
                int[] bottomnoteVector = oneHotEncode(bottomnote, bottomnotes);

                // Combine all feature vectors into a single vector
                int[] wineVector = combineVectors(typeVector, varietyVector, topnoteVector, regionVector, bottomnoteVector);

                // Convert to JSON format for storage
                String vectorJson = mapper.writeValueAsString(wineVector);

                // Insert the JSON-encoded vector into wine_vectors
                insertStmt.setString(1, vectorJson);
                insertStmt.executeUpdate();
            }

            System.out.println("Wine vectors inserted into wine_vectors table.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // One-hot encoding function
    public static int[] oneHotEncode(String value, List<String> uniqueValues) {
        int[] vector = new int[uniqueValues.size()];
        int index = uniqueValues.indexOf(value);
        if (index >= 0) {
            vector[index] = 1;
        }
        return vector;
    }

    // Combines multiple vectors
    public static int[] combineVectors(int[]... vectors) {
        int length = Arrays.stream(vectors).mapToInt(v -> v.length).sum();
        int[] combined = new int[length];
        int pos = 0;
        for (int[] vector : vectors) {
            System.arraycopy(vector, 0, combined, pos, vector.length);
            pos += vector.length;
        }
        return combined;
    }

    // Generates column names for one-hot encoded values
    public static List<String> generateColumnNames(String prefix, List<String> values) {
        List<String> columnNames = new ArrayList<>();
        for (String value : values) {
            columnNames.add(prefix + "_" + value.replaceAll("\\s+", "_"));
        }
        return columnNames;
    }
}
