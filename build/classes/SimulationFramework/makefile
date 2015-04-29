# windows makefile for Comp 282 simulation framework distribution.
# To use with linux change del command line  to rm -r *.class Doc 
# Mike Barnes 2/6/2013

all:	
	del *.class Doc /q
	javac AnimatePanel.java
	javac SimFrame.java
	javadoc -author -version -private -overview UML/overview.html -link http://docs.oracle.com/javase/7/docs/api/ -sourcepath ./;./UML -d Doc *.java


