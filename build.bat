@echo off
echo Cleaning...
rmdir /s /q out
mkdir out

echo Compiling...
javac -d out -sourcepath src src\main\*.java src\main\model\*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b
)

echo Creating JAR...
jar cfm GoL.jar manifest.txt -C out .

echo Done! Run with: java -jar GoL.jar
pause
