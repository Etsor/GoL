#!/bin/bash

echo "Cleaning..."
rm -rf out
mkdir out

echo "Compiling..."
javac -d out -sourcepath src src/main/*.java src/main/model/*.java
if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo "Creating JAR..."
jar cfm GameOfLife.jar manifest.txt -C out .

echo "Done! Run with: java -jar GameOfLife.jar"
