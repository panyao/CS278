----------------------------------------------------------------------------------
Assignment 3  Integration Test Result
----------------------------------------------------------------------------------
The create file, update file and remove file tests are all passed. 

The create folder test failed. When you create a folder in either server or client, the folder will synchronize as a file instead of folder on the other side. And any changes inside the folder will not synchronize.