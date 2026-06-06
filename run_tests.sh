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
res_file="$test_dir/sum/result.md"

succesful_arr=(0 0 0 0 0 0)
sum_arr=(0 0 0 0 0 0)

first_expected=("" "" "" "" "" "")
first_actual=("" "" "" "" "" "")

echo "Creating result file..."
> "$res_file" 
echo "Created result file."


for test_file_dir in "$test_dir"/*; do
    if [ ! -d "$test_file_dir" ] || [[ "$test_file_dir" == *"sum"* ]]; then
        continue
    fi

    type_file="$test_file_dir/type.txt"
    part_file="$test_file_dir/part.txt"
    input_file="$test_file_dir/commands.txt"
    expected_output="$test_file_dir/expected-output.txt"

    type_val=$(tr -d '[:space:]' < "$type_file")
    part_val=$(tr -d '[:space:]' < "$part_file")

    case "$type_val" in
        "happy") type_add=0 ;;
        "sad")   type_add=1 ;;
        "edge")  type_add=2 ;;
        *)       type_add=0 ;;
    esac

    if [ "$part_val" == "2" ]; then
        part_add=3
    else
        part_add=0
    fi

    arr_idx=$((type_add + part_add))
    
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
            ((succesful_arr[arr_idx]++))
        else
            echo "Test failed"

            # Capture the FIRST failure expected/actual output
            if [ -z "${first_expected[arr_idx]}" ]; then
                first_expected[arr_idx]=$(cat "$expected_output")
                first_actual[arr_idx]=$(cat "$temp_file")
            fi
        fi

        ((sum_arr[arr_idx]++))
    fi
done

rm -f "$temp_file"

# ==============================================================================
# REPORT GENERATION (MARKDOWN + HTML DETAILS)
# ==============================================================================

echo "Generating Visual Markdown Report..."

# 1. Helper function to generate a subcategory dropdown
generate_subcat() {
    local idx=$1
    local title=$2
    local succ=${succesful_arr[$idx]}
    local tot=${sum_arr[$idx]}
    local exp="${first_expected[$idx]}"
    local act="${first_actual[$idx]}"

    local icon="❌"
    local body=""

    # Determine status icon and body content
    if [ "$tot" -eq 0 ]; then
        icon="⚪"
        body="> *No tests run in this category.*"
    elif [ "$succ" -eq "$tot" ]; then
        icon="✅"
        body="> *All tests passed successfully!*"
    else
        # If tests failed, we inject the expected/actual into a code block
        body="\`\`\`text
Expected output:
$exp

Actual output:
$act
\`\`\`"
    fi

    # Append this subcategory as an HTML <details> block using Here-Doc
    cat << EOF >> "$res_file"
  <details>
  <summary><b>$title</b> &nbsp; | &nbsp; $succ/$tot &nbsp; $icon</summary>

$body

  </details>
  <br>
EOF
}

# 2. Build the final file using Here-Docs
> "$res_file" # Ensure file is completely empty before writing

# Write the main header and open the NODE section
cat << EOF >> "$res_file"
# 🧪 Graph Builder Test Report

<details open>
<summary><b><big>📦 NODE</big></b></summary>
<br>

EOF

# Generate Node Subcategories (Indices 0, 1, 2)
generate_subcat 0 "Happy"
generate_subcat 1 "Sad"
generate_subcat 2 "Edge"

# Close the NODE section and open the EDGE section
cat << EOF >> "$res_file"
</details>

<details open>
<summary><b><big>🔗 EDGE</big></b></summary>
<br>

EOF

# Generate Edge Subcategories (Indices 3, 4, 5)
generate_subcat 3 "Happy"
generate_subcat 4 "Sad"
generate_subcat 5 "Edge"

# Close the EDGE section
cat << EOF >> "$res_file"
</details>
EOF

echo "Tests completed. Results in $res_file"
