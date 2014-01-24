#!/bin/bash
# kill them all
# 2 - hostname
# 1 - killerscript or clear script, whatever...

ssh -x MSDOMAIN_hudson@$2 $1
