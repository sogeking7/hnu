#!/bin/bash

# Set variables for easy configuration
OUTPUT_DIR="../src/app/ml"
OPENAPI_DIR="http://10.131.0.19:8011/openapi.json"

# Clean previous generation
echo "Cleaning previous API client..."
rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR"

echo "Generating TypeScript services from OpenAPI schema..."

npx @openapitools/openapi-generator-cli generate \
    -i "$OPENAPI_DIR" \
    -g typescript-angular \
    -o "$OUTPUT_DIR" \
    --api-package=api \
    --invoker-package=invoker \
    --model-package=model \
    --skip-validate-spec \
    --additional-properties=basePath=https://b32aff9354e5.ngrok-free.app,dateLibrary=java8,fileenumPropertyNaming=original,fileNaming=kebab-case

# Clean up unnecessary files
echo "Cleaning up generated files..."
rm -rf "$OUTPUT_DIR/.gitignore"
rm -rf "$OUTPUT_DIR/.openapi-generator-ignore"
rm -rf "$OUTPUT_DIR/.openapi-generator"
rm -rf "openapitools.json"

echo "✅ API client generated successfully at $OUTPUT_DIR"
