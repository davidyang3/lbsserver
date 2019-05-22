root=`dirname \`pwd\``
APPLICATION=$root/bin/lbsserver.jar
ps ux | grep $APPLICATION | grep -v grep | grep -v stop.sh | cut -c 9-15 | xargs kill -s 9

for var in $@
do
    ps ux | grep "$var" | grep -v grep | grep -v stop.sh | cut -c 9-15 | xargs kill -s 9
done

echo "kill $@ done"