package winestore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Vectorize {

    private static final String URL = "jdbc:postgresql://localhost:5432/test_db";
    private static final String USER = "postgres"; // Change this to your DB user
    private static final String PASSWORD = "topsecret"; // Change this to your DB password

    public static void main(String[] args) {
        try {
            // 1. Establish a database connection
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // 2. Create a statement object
            Statement statement = connection.createStatement();

            // 3. Execute a SQL SELECT query
            String sql = "SELECT * FROM wines";
            ResultSet resultSet = statement.executeQuery(sql);

            // 4. Process the result set
            while (resultSet.next()) {
                // Replace these column names with your actual database column names
                long id = resultSet.getLong("id");
                String type = resultSet.getString("type");
                String variety = resultSet.getString("variety");
                int year = resultSet.getInt("year");
                String region = resultSet.getString("region");
                int price = resultSet.getInt("price");
                String topnote = resultSet.getString("topnote");
                String bottomnote = resultSet.getString("bottomnote");

                // Print each wine record
                System.out.println("ID: " + id + ", Type: " + type + ", Variety: " + variety +
                        ", Year: " + year + ", Region: " + region + ", Price: $" + price +
                        ", Top Note: " + topnote + ", Bottom Note: " + bottomnote);
            }

            // 5. Close the resources
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
