#!/bin/bash

for i in $(seq 63 63); do export NO=$i; awk 'BEGIN { count = 1 } /^(INSERT)/ { print count" "$0;count+=1 } /^[-]{2}/ { print $0 } END { }' ch$NO.sql | sed -E 's/^([0-9]+)\s(.*VALUES.*,\s)([0-9]+),(.*)/\2\1,\4/g' > ch$NO.x.sql; done
