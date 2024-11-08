import os

def find_package_statements(root_dir):
    for dirpath, _, filenames in os.walk(root_dir):
        for filename in filenames:
            if filename.endswith('.java'):
                file_path = os.path.join(dirpath, filename)
                with open(file_path, 'r') as file:
                    for line in file:
                        if 'package' in line:
                            chomp_line = line.strip()
                            print(f"{filename} ::: {dirpath} |||| >>>> {chomp_line}")
root_directory = '.'
print("+" * 80 )
find_package_statements(root_directory)
print("-" * 80 )