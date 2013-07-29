REM ----------------------------
REM 1 SFHOME
REM 2 SFCLASSPATH
REM 3 WORKSPACE
REM 4 LOCAL WORKSPACE
REM 5 JAVA_HOME
REM 6 SF_OPTS
REM 7 SFDEFAULTINI
REM ---------------------------

if %3 EQU %4 (
  echo Local worksapce and the actual workspace are the same.
) else (
  echo Let's copy the workspace to the local workspace.
  xcopy %3 %4 /E /V /I /F /H /R /Y
)

if not "%~5" == "" (
   echo "Setting JAVA_HOME to %~5"
   set JAVA_HOME=%~5
)

if not "%~7" == "" (
   echo "Setting SFDEFAULTINI to %~7"
   set SFDEFAULTINI=-Dorg.smartfrog.iniFile=%~7
)

set SFHOME=%1
set SFUSERCLASSPATH=%~2
set SF_OPTS=%~6


cd /D %~4

echo "Current working directory: %CD%"
echo "Starting daemon on %COMPUTERNAME%"

call %SFHOME%\bin\sfDaemon.bat -d -headless