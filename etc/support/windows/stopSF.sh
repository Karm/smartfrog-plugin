#!/bin/bash

# $1 - host name
# $2 - remote script
# $3 - SFHOME
# $4 - SFCLASSPATH
# $5 - WORKSPACE
# $6 - Local Workspace

ssh -x hudson@$1  $2  $1 $3 "\"$4\"" "\"$5\"" "\"$6\"";
