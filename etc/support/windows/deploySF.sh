#!/bin/bash

# $1 - host name
# $2 - remote script
# $3 - SFHOME
# $4 - SFCLASSPATH
# $5 - component name
# $6 - sf script path
# $7 - WORKSPACE
# $8 - Local Workspace

ssh -x hudson@$1 $2 $1 $3 "\"$4\"" $5 $6 "\"$7\"" "\"$8\""
