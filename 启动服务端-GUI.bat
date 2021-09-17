@echo off
@title MapleStory_079
set path=%PATH%;JAVA_HOME%\bin;%SystemRoot%\system32;%SystemRoot%
set JRE_HOME=%JAVA_HOME%\jre
set CLASSPATH=%CLASSPATH%;./*;./lib/*
java -server -Dwzpath=wz gui.GUIApplication
pause
