REM ----------------------------
REM 1 HOST
REM 2 SFHOME
REM 3 SFCLASSPATH
REM 4 WORKSPACE
REM 5 Local workspace
REM 6 JAVA_HOME
REM ---------------------------

set SFHOME=%2
set SFUSERCLASSPATH=%~3
if not "%~6" == "" (
   echo "Setting JAVA_HOME to %~6"
   setx JAVA_HOME %~6
   set JAVA_HOME=%~6
)

REM Probably not a good idea...
set  SFJVM=%JAVA_HOME%\bin\java.exe
setx SFJVM %JAVA_HOME%\bin\java.exe
set TEMP=C:\tmp
call %SFHOME%\bin\sfStopDaemon.bat %~1 -headless
