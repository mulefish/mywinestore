import psycopg2
import pandas as pd
from sklearn.preprocessing import OneHotEncoder, MinMaxScaler
import numpy as np

# Connect to PostgreSQL database
connection = psycopg2.connect(
    host="localhost",
    database="test_db",
    user="postgres",
    password="topsecret"
)
cursor = connection.cursor()

# Query to select all data from wines table
query = "SELECT id, type, variety, year, region, price, topnote, bottomnote FROM wines;"
df_wines = pd.read_sql(query, connection)

# Display the data for reference
print("Wine Data from Database:")
print(df_wines.head())

# One-hot encode categorical features: 'type', 'variety', 'region', 'topnote', and 'bottomnote'
onehot_encoder = OneHotEncoder(sparse_output=False, handle_unknown='ignore')
encoded_categorical = onehot_encoder.fit_transform(df_wines[['type', 'variety', 'region', 'topnote', 'bottomnote']])

# Display the shape of encoded categorical data for debugging
print("Shape of encoded categorical data:", encoded_categorical.shape)

# Normalize numerical features: 'year' and 'price'
scaler = MinMaxScaler()
normalized_numerical = scaler.fit_transform(df_wines[['year', 'price']])

# Combine the encoded categorical and normalized numerical features into a single vector for each wine
wine_vectors = np.hstack([encoded_categorical, normalized_numerical])

# Display the shape of combined vectors for debugging
print("Shape of combined wine vectors:", wine_vectors.shape)

# Prepend the ID to each vector
wine_ids = df_wines['id'].values.reshape(-1, 1)  # Reshape to column vector
wine_vectors_with_ids = np.hstack([wine_ids, wine_vectors])

# Check the length of each vector and the number of placeholders
vector_length = wine_vectors_with_ids.shape[1]
num_placeholders = 37  # Update this if your table has fewer or more columns

print(f"Vector length: {vector_length}, Number of placeholders: {num_placeholders}")
if vector_length != num_placeholders:
    print(f"Error: Vector length ({vector_length}) does not match the number of placeholders ({num_placeholders}).")
else:
    print("Vector length matches placeholders.")

# If vector length matches placeholders, proceed with insertion
if vector_length == num_placeholders:
    insert_query = """
    INSERT INTO wine_vectors (
        id,
        type_red, type_white, type_rose, type_sparkling, type_dessert, type_fortified,
        variety_cabernet_sauvignon, variety_pinot_noir, variety_chardonnay, variety_merlot,
        variety_sauvignon_blanc, variety_syrah, variety_malbec, variety_zinfandel, variety_riesling,
        region_bordeaux, region_napa_valley, region_tuscany, region_rioja, region_barossa_valley,
        region_champagne, region_mosel, region_mendoza, region_sonoma,
        topnote_rich, topnote_bold, topnote_earthy, topnote_woody, topnote_fruity, topnote_spicy,
        topnote_intense, topnote_oak, topnote_vanilla, topnote_citrus, topnote_light,
        bottomnote_herbaceous, bottomnote_sweet, bottomnote_smooth, bottomnote_smoky, bottomnote_bubbly,
        bottomnote_floral, bottomnote_plummy, bottomnote_complex, bottomnote_aromatic,
        normalized_year, normalized_price
    ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s,
              %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s,
              %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);
"""



# Insert vectors into wine_vectors table


    # Execute the insert statement for each vector
    for vector in wine_vectors_with_ids:
        try:
            cursor.execute(insert_query, tuple(vector))
        except Exception as e:
            print(f"Error inserting vector {vector}: {e}")

    # Commit the transaction and close the connection
    connection.commit()
    print("Wine vectors inserted into wine_vectors table.")
else:
    print("Vector length mismatch. Skipping insertion.")

cursor.close()
connection.close()
print("The end")