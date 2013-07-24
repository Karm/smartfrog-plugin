#!/bin/bash

# $1 - host name
# $2 - remote script
# $3 - SFHOME
# $4 - SFCLASSPATH
# $5 - WORKSPACE
# $6 - Local Workspace
# $7 - JAVA_HOME
# $8 - SF_OPTS
# $9 - SFDEFAULTINI

ssh -x hudson@$1  $2  $1 $3 "\"$4\"" "\"$5\"" "\"$6\"" "\"$7\"" "\"$8\"" "\"$9\"";
