REM ----------------------------
REM 1 HOST
REM 2 SFHOME
REM 3 SFCLASSPATH
REM ---------------------------

set SFHOME=%2
set SFUSERCLASSPATH=%~3

call %SFHOME%\bin\sfStopDaemon.bat %1