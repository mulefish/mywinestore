import random
import pandas as pd

# Define sample data for wine attributes with regional notes
wine_types = ['Red', 'White', 'Rose', 'Sparkling', 'Dessert', 'Fortified']
regions = {
    'Bordeaux': ['rich', 'bold', 'earthy'],
    'Napa Valley': ['woody', 'dark fruit', 'robust'],
    'Tuscany': ['herbaceous', 'spicy', 'smooth'],
    'Rioja': ['oak', 'vanilla', 'leathery'],
    'Barossa Valley': ['spicy', 'intense', 'smoky'],
    'Champagne': ['bubbly', 'citrus', 'light'],
    'Mosel': ['sweet', 'floral', 'fruity'],
    'Mendoza': ['ripe', 'plummy', 'bold'],
    'Sonoma': ['fruity', 'complex', 'aromatic']
}
varieties = ['Cabernet Sauvignon', 'Pinot Noir', 'Chardonnay', 'Merlot', 'Sauvignon Blanc', 'Syrah', 'Malbec', 'Zinfandel', 'Riesling']

# Generate N wine entries
N = 100
wines = []
for i in range(N):
    wine_type = random.choice(wine_types)
    region = random.choice(list(regions.keys()))
    variety = random.choice(varieties)
    
    topnote, bottomnote = random.sample(regions[region], 2)

    wine = {
        "ID": i + 1,
        "Name": f"Wine {i + 1} ({variety})",
        "Type": wine_type,
        "Variety": variety,
        "Year": random.randint(1980, 2023),
        "Region": region,
        "Price": random.randint(10, 300),  # Price as a whole dollar
        "Topnote": topnote,
        "Bottomnote": bottomnote
    }
    wines.append(wine)

# Convert to DataFrame for easier manipulation or export
df_wines = pd.DataFrame(wines)

# Loop through the DataFrame and generate SQL INSERT statements
for index, row in df_wines.iterrows():
    id = row['ID']
    name = row['Name']
    t = row['Type']
    v = row['Variety']
    y = row['Year']
    r = row['Region']
    p = row['Price']
    topnote = row['Topnote']
    bottomnote = row['Bottomnote']

    x = f"INSERT INTO wines (id, type, variety, year, region, price, topnote, bottomnote) VALUES ({id}, '{t}', '{v}', {y}, '{r}', {p}, '{topnote}', '{bottomnote}');"
    print(x)
