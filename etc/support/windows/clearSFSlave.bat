REM ---------------------------------------------------------------------------------
REM Remove malicious system properties
REM ---------------------------------------------------------------------------------
REG delete HKCU\Environment /F /V SFUSERCLASSPATH
REG delete HKCU\Environment /F /V CLASSPATH
REG delete HKCU\Environment /F /V SFUSERHOME
REG delete HKCU\Environment /F /V SFUSERHOME1
REG delete HKCU\Environment /F /V SFLIBRARYPATH
REG delete HKCU\Environment /F /V SFHOME
REG delete HKCU\Environment /F /V SFHOME1
REG delete HKCU\Environment /F /V SFJVM

echo "Script goes berserk! It's killing all the java.exe processes!"
echo "TODO: Make it behave."
taskkill /IM java.exe /F

