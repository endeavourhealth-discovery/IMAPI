#!/bin/sh

for sqlScript in "$@"
do
	mysql -uroot -ppassword < "$sqlScript"
done

