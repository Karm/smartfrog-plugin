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

ssh -x hudson@$1  'net use "*" /delete /y; net use h: \\\\smb.mw.lab.eng.bos.redhat.com\\qa_services jboss42 /user:hudson /PERSISTENT:NO /y;net use t: \\\\smb.mw.lab.eng.bos.redhat.com\\qa_tools jboss42 /PERSISTENT:NO  /y;' $2 $3 "\"$4\"" "\"$5\"" "\"$6\"" "\"$7\"" "\"$8\"" "\"$9\""
