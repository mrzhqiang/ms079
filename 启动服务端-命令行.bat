@echo off
@title MapleStory_079
set PATH=%PATH%;JAVA_HOME%\bin
set JRE_HOME=%JAVA_HOME%\jre
set CLASSPATH=%CLASSPATH%;.;./*;./lib/*
java -server -Dwzpath=wz server.Start
pause
