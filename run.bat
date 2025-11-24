@echo off
REM JavaFX POS tətbiqini run etmək üçün bat faylı

cd /d "C:\Users\nijat.gurbanli\Desktop\pos-system"

REM JavaFX SDK yerləşdiyi yolu göstərin
set JAVAFX_LIB=C:\Users\nijat.gurbanli\Downloads\javafx-sdk-21.0.9\lib

REM Tətbiqin JAR faylının yolu
set JAR_FILE=target\pos-system-1.0-SNAPSHOT.jar

REM Run əmrini işə salır
java --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml -jar "%JAR_FILE%"

pause
