rem   This is a batch file that you can run from the command line to
rem   compile the Comp 282 simulation framework and make all the on-line
rem   javadoc documentation files.
rem   The documentation will be placed in the Doc subdirectory.
rem    ^ character allows for multi-line batch file commands

rem   Delete all class files and documentation files
del *.class Doc /q

rem    compile the *.java files
javac AnimatePanel.java
javac SimFrame.java

rem    make the documentation


javadoc -author -version -private -overview ./UML/overview.html ^
 -link http://docs.oracle.com/javase/7/docs/api/ ^
 -sourcepath ./;./UML ^
 -d Doc ^
 *.java