REM ----------------------------
REM 1 host
REM 2 SFHOME
REM 3 SFCLASSPATH
REM 4 componentName
REM 5 script path
REM 6 WORKSPACE
REM 7 LOCAL WORKSPACE
REM 8 JAVA_HOME
REM ---------------------------

set  SFHOME=%2
set  SFUSERCLASSPATH=%~3
REM probably not a good idea :-(
set JAVA_HOME=%8
setx JAVA_HOME %8
setx SFJVM %JAVA_HOME%\bin\java.exe
set  SFJVM=%JAVA_HOME%\bin\java.exe

cd /D %~7

echo "Current working directory: %CD%"
set TEMP=C:\tmp
call %SFHOME%\bin\sfStart.bat %1 %4 %5 -headless
