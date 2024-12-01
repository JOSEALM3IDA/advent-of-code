#!/bin/bash


if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <problem_name>"
    exit 1
fi

ROOT_PACKAGE="pt.josealm3ida.aoc"
MAIN_CLASS="${ROOT_PACKAGE}.$1.Main"
SRC_DIR="src"
BIN_DIR="bin"

rm -rf $BIN_DIR

mkdir -p $BIN_DIR

echo "Compiling..."
javac -d $BIN_DIR $(find $SRC_DIR -name "*.java")
if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

echo "Running..."
java -cp $BIN_DIR $MAIN_CLASS
if [ $? -ne 0 ]; then
    echo "Program execution failed."
    exit 1
fi
