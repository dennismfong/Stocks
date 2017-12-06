string=$1
if [[ $string == *.java ]]; then
v2=${string%.*}
fi
echo $v2
javac *.java
java -cp /cs/class/cs174a/public/mysql-connector-java-5.1.44-bin.jar:. $v2
