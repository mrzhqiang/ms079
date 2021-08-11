@echo off
@title MapleStory_079
set path=%JAVA_HOME%\bin;%SystemRoot%\system32;%SystemRoot%
set JRE_HOME=%JAVA_HOME%\jre
set CLASSPATH=.;target/ms079.jar
java -server -Dwzpath=wz gui.ZlhssMS
pause
