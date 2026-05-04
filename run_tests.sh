#!/bin/bash

#set -e

test_dir="./test_files" 
program="./app.jar"
temp_file="/tmp/temp.txt" 

if [ ! -d "$test_dir" ]; then
    echo "Test files do not exist!"
    exit 1
fi


#echo "Generating App cache..."

#java -XX:ArchiveClassesAtExit=app.jsa -jar "$program" < /dev/null > /dev/null || true

# Run the app for 5 seconds, then send SIGTERM (a graceful shutdown signal)
#timeout -s SIGTERM 5s java -XX:ArchiveClassesAtExit=app.jsa -jar "$program" || true

#echo "Generated App cache"

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

    input_file="$test_file_dir/input.txt"
    expected_output="$test_file_dir/expected_output.txt"
    
    if [[ -f "$input_file" && -f "$expected_output" ]]; then
        is_test_successful=true

        #java -XX:SharedArchiveFile=app.jsa -jar "$program" com.szofttech.test.Test < "$input_file" > "$temp_file"

        # The '2>&1' at the end forces Standard Error into Standard Output
        # The '|| true' prevents the script from crashing if Java throws an error code
        echo "Running Java with absolute path..."

        java -Xmx256m -cp "/test/app.jar" com.szofttech.test.TestWrapper < "$input_file" > "$temp_file" 2>&1 || true

        echo "Java finished."
        
        while IFS= read -r line; do
            [ -z "$line" ] && continue
            
            if ! grep -qF "$line" "$temp_file"; then
                is_test_successful=false
                break
            fi
        done < "$expected_output"

        if [[ "$is_test_successful" == "true" ]]; then
            echo "success!" >> "$res_file"
        else
            echo "failure!" >> "$res_file"
        fi
    fi
done

rm -f "$temp_file"
echo "Tests completed. Results in $res_file"
cat $res_file