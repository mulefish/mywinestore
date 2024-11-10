import psycopg2
import pandas as pd
from sklearn.preprocessing import OneHotEncoder, MinMaxScaler
import numpy as np
import json  # for serializing vectors as JSON

# Connect to PostgreSQL database
connection = psycopg2.connect(
    host="localhost",
    database="test_db",
    user="postgres",
    password="topsecret"
)
cursor = connection.cursor()

# Query to select all data from wines table
query = "SELECT id, type, variety, year, region, price, topnote, bottomnote FROM wines limit 10;"
df_wines = pd.read_sql(query, connection)

# Display the data for reference
print("Wine Data from Database:")
# print(df_wines.head())

# One-hot encode categorical features: 'type', 'variety', 'region', 'topnote', and 'bottomnote'
onehot_encoder = OneHotEncoder(sparse_output=False, handle_unknown='ignore')
encoded_categorical = onehot_encoder.fit_transform(df_wines[['type', 'variety', 'region', 'topnote', 'bottomnote']])

# Normalize numerical features: 'year' and 'price'
scaler = MinMaxScaler()
normalized_numerical = scaler.fit_transform(df_wines[['year', 'price']])

# Combine the encoded categorical and normalized numerical features into a single vector for each wine
wine_vectors = np.hstack([encoded_categorical, normalized_numerical])

# Prepend the ID to each vector for later insertion
wine_ids = df_wines['id'].values.reshape(-1, 1)  # Reshape to column vector
wine_vectors_with_ids = np.hstack([wine_ids, wine_vectors])


for vector in wine_vectors_with_ids:
    print(vector)


# # Define the insert query, inserting ID and vector as a JSON blob
# insert_query = """
#     INSERT INTO wine_vectors (id, vector) VALUES (%s, %s)
# """
#
# # Insert each vector as a single JSON object
# for vector in wine_vectors_with_ids:
#     wine_id = int(vector[0])  # Extract the wine ID
#     wine_vector = vector[1:]  # Extract the vector without the ID
#     # Serialize vector as JSON
#     wine_vector_json = json.dumps(wine_vector.tolist())
#     try:
#         cursor.execute(insert_query, (wine_id, wine_vector_json))
#     except Exception as e:
#         print(f"Error inserting vector for wine ID {wine_id}: {e}")
#
# # Commit the transaction and close the connection
# connection.commit()
# print("Wine vectors inserted into wine_vectors table.")
#
# cursor.close()
# connection.close()
# print("The end")
