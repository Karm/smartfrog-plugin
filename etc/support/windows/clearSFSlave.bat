REM ---------------------------------------------------------------------------------
REM Remove malicious system properties
REM ---------------------------------------------------------------------------------
REG delete HKCU\Environment /F /V /S SFUSERCLASSPATH
REG delete HKCU\Environment /F /V /S CLASSPATH
REG delete HKCU\Environment /F /V /S SFUSERHOME
REG delete HKCU\Environment /F /V /S SFUSERHOME1
REG delete HKCU\Environment /F /V /S SFLIBRARYPATH
REG delete HKCU\Environment /F /V /S SFHOME
REG delete HKCU\Environment /F /V /S SFHOME1
