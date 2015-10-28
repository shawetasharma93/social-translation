#!/bin/sh
git pull --rebase origin master
git push origin master
while [ $? -ne 0 ]
do
    git pull --rebase origin master
    git push origin master
done
git push --tags origin master
