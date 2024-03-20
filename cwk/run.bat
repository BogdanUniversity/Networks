@echo off

rem Delete previously compiled classes
del server\*.class client\*.class

rem Compile Java classes
javac server\Server.java client\Client.java server\Executor.java server\CourseworkProtocol.java


