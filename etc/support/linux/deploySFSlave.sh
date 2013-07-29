# $1 - HOSTNAME
# $2 - SFHOME
# $3 - SFCLASSPATH
# $4 - component name
# $5 - sf script path
# $6 - WORKSPACE
# $7 - Local Workspace
# $8 - JAVA_HOME


source ~/.bash_profile;

export =$2;
export SFUSERCLASSPATH=$3;

if [ $8 ]; then
   export JAVA_HOME=$8;
   export PATH=$JAVA_HOME/bin:$PATH;
fi

cd $7;

echo "Current working directory: $PWD"
echo "Starting daemon on $HOSTNAME"

$SFHOME/bin/sfStart $1 $4 $5 -headless