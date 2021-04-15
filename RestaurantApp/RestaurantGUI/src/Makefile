all: compile.o

run: run.o

compile.o: setup.o MainWindow.o CustomerPage.o ManagerPage.o

setup.o:
	javac .\dbSetup.java

MainWindow.o:
	javac .\MainWindow.java

CustomerPage.o:
	javac -Xlint .\CustomerPage.java

ManagerPage.o:
	javac .\ManagerPage.java

run.o:
	java -cp ".;postgresql-42.2.8.jar" MainWindow

clean:
	del *.class *.o