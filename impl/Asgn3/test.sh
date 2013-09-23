#!/bin/bash
#Integration test

SERVER_DIR=test-server
CLIENT_DIR=test-client
SOURCE_DIR=test-data/invariant
SHORT_INTERVAL=2
LONG_INTERVAL=15

IP_ADDRESS=192.168.2.10

function isDirSame {
if [[ $(diff -r $CLIENT_DIR $SERVER_DIR) ]]
then 
  echo "FAIL"
else
  echo "PASS"
fi 
sleep SHORT_INTERVAL
}


echo initialization
mkdir $SERVER_DIR
mkdir $CLIENT_DIR

java -jar dropbox.jar $SERVER_DIR &> server_output.txt &
sleep 10  
java -jar dropbox.jar $CLIENT_DIR $IP_ADDRESS &> client_output.txt &
sleep 25

echo 'creating file in client'
echo 'test create' > $CLIENT_DIR/test1.txt
sleep 15
isDirSame 

echo 'Update file from client'
echo 'test update' >> $CLIENT_DIR/test1.txt
sleep 15
isDirSame

echo 'create file in server'
cp $SOURCE_DIR/vandy.png $SERVER_DIR/
sleep 15
isDirSame 

echo 'Update file from server'
echo 'test update' >> $SERVER_DIR/vandy.png
sleep 15
isDirSame

echo remove file from client
rm $CLIENT_DIR/test1.txt 
sleep 15
isDirSame

echo remove file from server
rm $SERVER_DIR/vandy.png 
sleep 15
isDirSame 

echo Create folder in client
mkdir $CLIENT_DIR/TestClient
sleep 10
isDirSame 

echo Create folder in server
mkdir $SERVER_DIR/TestServer
sleep 10
isDirSame 


sleep 10
pkill java
#rm -rf $SERVER_DIR
#rm -rf $CLIENT_DIR


