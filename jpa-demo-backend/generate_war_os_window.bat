@echo off
set pwd=%cd%
echo ========================================
echo -- Minify static-resources
echo ========================================
cd /d "D:\projects\jpa-demo\jpa-demo-compressor\"
call mvn package
cd /d %pwd%

cd /d "D:\project-releases\jpa-demo\compressor"
call java -jar compressor-jar-with-dependencies.jar jpa-demo-backend
cd /d %pwd%
echo ========================================
echo -- Minifying Finished
echo ========================================

echo ========================================
echo -- Start Generating jpa-demo-backend war
echo ========================================
cd /d "D:\projects\jpa-demo\jpa-demo-backend\"
call mvn clean package -Dmaven.test.skip=true -Dmaven.site.skip=true -Dmaven.javadoc.skip=true
cd /d %pwd%
echo =========================================
echo -- Generating 'jpa-demo-backend' Finished
echo =========================================
pause