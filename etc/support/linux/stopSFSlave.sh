# $1 - HOSTNAME
# $2 - SFHOME
# $3 - SFCLASSPATH
# $4 - WORKSPACE
# $5 - Local Workspace
# $6 - JAVA_HOME

source ~/.bash_profile;

export SFHOME=$2;
export SFUSERCLASSPATH=$3;

if [ $6 ]; then
   export JAVA_HOME=$6;
fi

export PATH=$JAVA_HOME/bin:$PATH;

cd $5;

echo "Current working directory: $PWD"
echo "Starting daemon on $HOSTNAME"

$SFHOME/bin/sfStopDaemon $1