#!/bin/bash

# Google Java Format Script
# Downloads google-java-format jar and formats all Java files in the project

set -e  # Exit on any error

# Configuration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAR_NAME="google-java-format-1.22.0-all-deps.jar"
JAR_PATH="$SCRIPT_DIR/$JAR_NAME"
DOWNLOAD_URL="https://github.com/google/google-java-format/releases/download/v1.22.0/$JAR_NAME"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Java is installed
check_java() {
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed or not in PATH"
        exit 1
    fi

    java_version=$(java -version 2>&1 | head -n 1)
    print_status "Using Java: $java_version"
}

# Download google-java-format jar if it doesn't exist
download_jar() {
    if [[ -f "$JAR_PATH" ]]; then
        print_status "google-java-format jar already exists at $JAR_PATH"
        return 0
    fi

    print_status "Downloading google-java-format jar..."

    if command -v curl &> /dev/null; then
        curl -L -o "$JAR_PATH" "$DOWNLOAD_URL"
    elif command -v wget &> /dev/null; then
        wget -O "$JAR_PATH" "$DOWNLOAD_URL"
    else
        print_error "Neither curl nor wget is available. Please install one of them."
        exit 1
    fi

    if [[ -f "$JAR_PATH" ]]; then
        print_success "Downloaded google-java-format jar to $JAR_PATH"
    else
        print_error "Failed to download google-java-format jar"
        exit 1
    fi
}

# Find all Java files in the project
find_java_files() {
    local project_root
    project_root=$(git rev-parse --show-toplevel 2>/dev/null || pwd)

    print_status "Scanning for Java files in: $project_root"

    # Find all .java files, excluding common build/target directories
    # Using while loop instead of mapfile for better compatibility
    java_files=()
    while IFS= read -r -d '' file; do
        java_files+=("$file")
    done < <(find "$project_root" -name "*.java" \
        -not -path "*/target/*" \
        -not -path "*/build/*" \
        -not -path "*/.gradle/*" \
        -not -path "*/node_modules/*" \
        -not -path "*/.git/*" \
        -print0)

    if [[ ${#java_files[@]} -eq 0 ]]; then
        print_warning "No Java files found in the project"
        exit 0
    fi

    print_status "Found ${#java_files[@]} Java files"
}

# Format Java files
format_files() {
    local formatted_count=0
    local failed_count=0

    print_status "Formatting Java files..."

    for file in "${java_files[@]}"; do
        echo -n "Formatting $(basename "$file")... "

        if java -jar "$JAR_PATH" --replace "$file" 2>/dev/null; then
            echo -e "${GREEN}✓${NC}"
            ((formatted_count++))
        else
            echo -e "${RED}✗${NC}"
            ((failed_count++))
            print_error "Failed to format: $file"
        fi
    done

    echo
    print_success "Formatted $formatted_count files successfully"

    if [[ $failed_count -gt 0 ]]; then
        print_warning "$failed_count files failed to format"
    fi
}

# Show git status if in a git repository
show_git_status() {
    if git rev-parse --git-dir > /dev/null 2>&1; then
        echo
        print_status "Git status after formatting:"
        git status --porcelain | grep "\.java$" || print_status "No Java files were modified"
    fi
}

# Main execution
main() {
    print_status "Starting Google Java Format process..."
    echo

    check_java
    download_jar
    find_java_files
    format_files
    show_git_status

    echo
    print_success "Google Java Format process completed!"
}

# Run the main function
main "$@"