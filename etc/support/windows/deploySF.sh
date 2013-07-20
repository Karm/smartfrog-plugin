#!/bin/bash

# $1 - host name
# $2 - remote script
# $3 - SFHOME
# $4 - SFCLASSPATH
# $5 - component name
# $6 - sf script path

#'net use "*" /delete /y; net use h: \\\\smb.mw.lab.eng.bos.redhat.com\\qa_services jboss42 /user:hudson /PERSISTENT:NO /y;net use t: \\\\smb.mw.lab.eng.bos.redhat.com\\qa_tools jboss42 /PERSISTENT:NO  /y;'
ssh -x hudson@$1 $2 $1 $3 "\"$4\"" $5 $6
