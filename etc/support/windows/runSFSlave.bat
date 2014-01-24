REM ----------------------------
REM 1 SFHOME
REM 2 SFCLASSPATH
REM 3 WORKSPACE
REM 4 LOCAL WORKSPACE
REM 5 JAVA_HOME
REM 6 SF_OPTS
REM 7 SFDEFAULTINI
REM 8 HOST
REM ---------------------------

if %3 EQU %4 (
  echo Local worksapce and the actual workspace are the same.
) else (
  echo Let's copy the workspace to the local workspace.
  xcopy %3 %4 /E /V /I /F /H /R /Y
)

if not "%~5" == "" (
   echo "Setting JAVA_HOME to %~5"
   setx JAVA_HOME %~5
   set JAVA_HOME=%~5
)

if not "%~7" == "" (
   echo "Setting SFDEFAULTINI to %~7"
   set SFDEFAULTINI=-Dorg.smartfrog.iniFile=%~7
)

set SFHOME=%1
set SFUSERCLASSPATH=%~2
set SF_OPTS=%~6
REM Probably not a good idea...
set  SFJVM=%JAVA_HOME%\bin\java.exe
setx SFJVM %JAVA_HOME%\bin\java.exe

cd /D %~4

echo "Current working directory: %CD%"
echo "Script goes berserk! It's killing all the java.exe processes!"
echo "TODO: Make it behave."
taskkill /IM java.exe /F

echo "Starting daemon on %COMPUTERNAME%, host:%8"
set TEMP=C:\tmp
setx SFSERVERHOSTNAME %8
set SFSERVERHOSTNAME=%8
setx SFRMIHOSTNAME %8
set SFRMIHOSTNAME=%8
setx HOSTNAME %8
set HOSTNAME=%8
setx HOST %8
set HOST=%8
setx LOCALHOST %8
set LOCALHOST=%8

call %SFHOME%\bin\sfDaemon.bat -J "-Dorg.smartfrog.sfcore.prim.sfProcessHost=%8" -J "-Dhost=%8" -J "-Dhostname=%8" -J "-Dlocalhost=%8" -J "-Dcomputername=%8" -J "-Djava.rmi.server.hostname=%8" -J "-Djava.net.preferIPv4Stack=true" -J "-Dorg.smartfrog.sfcore.processcompound.sfRootLocatorBindAddress=%8" -d -headless
