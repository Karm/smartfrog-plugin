# $1 - SFHOME
# $2 - SFCLASSPATH
# $3 - WORKSPACE
# $4 - Local Workspace
# $5 - JAVA_HOME
# $6 - SF_OPTS
# $7 - SFDEFAULTINI

source ~/.bash_profile;

export SFHOME=$1;
export SFUSERCLASSPATH=$2;

if [ $5 ]; then
   export JAVA_HOME=$5;
   export PATH=$JAVA_HOME/bin:$PATH;
fi

if [ $6 ]; then
   export SF_OPTS=$6;
fi

if [ $7 ]; then
   export SFDEFAULTINI=-Dorg.smartfrog.iniFile=$7;
fi

if [ $3 == $4 ]; then
   echo "Local workspace and the actual workspace are the same."
fi

cd $4;

echo "Current working directory: $PWD"
echo "Starting daemon on $HOSTNAME"

$SFHOME/bin/sfDaemon -d -headless