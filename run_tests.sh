#!/bin/bash

#set -e

test_dir="./test_files" 
program="./app.jar"
temp_file="/tmp/temp.txt" 

if [ ! -d "$test_dir" ]; then
    echo "Test files do not exist!"
    exit 1
fi


echo "Generating App cache..."

java -XX:ArchiveClassesAtExit=app.jsa -cp "$program" com.szofttech.test.TestWrapper < /dev/null > /dev/null || true

echo "Generated App cache"

mkdir -p "$test_dir/sum" 
res_file="$test_dir/sum/result.txt"

echo "Creating result file..."
> "$res_file" 
echo "Created result file."

if [ ! -d "$temp_file" ]; then
    touch "$temp_file"
fi

for test_file_dir in "$test_dir"/*; do
    if [ ! -d "$test_file_dir" ] || [[ "$test_file_dir" == *"sum"* ]]; then
        continue
    fi

    ls "$test_file_dir"

    input_file="$test_file_dir/commands.txt"
    expected_output="$test_file_dir/expected-output.txt"
    
    if [[ -f "$input_file" && -f "$expected_output" ]]; then
        is_test_successful=true


        echo "Running Java..."

        java -XX:SharedArchiveFile=app.jsa -cp "$program" com.szofttech.test.TestWrapper < "$input_file" > "$temp_file"

        echo "Java finished."
        
        while IFS= read -r line; do
            [ -z "$line" ] && continue
            
            if ! grep -qF "$line" "$temp_file"; then
                is_test_successful=false
                break
            fi
        done < "$expected_output"

        if [[ "$is_test_successful" == "true" ]]; then
            echo "Test passed"
            
            echo "success!" >> "$res_file"
        else
            echo "Test failed"

            echo "expected output:\n" >> "$res_file"
            cat "$expected_output" >> "$res_file"
            echo "actual output:\n" >> "$res_file"
            cat "$temp_file" >> "$res_file" 
        fi

        echo "+-----+" >> "$res_file"
    fi
done

rm -f "$temp_file"
echo "Tests completed. Results in $res_file"
cat $res_file