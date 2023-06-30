JAVAC=/usr/bin/javac
.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin
DOCDIR=doc


default:
	$(JAVAC) -d $(BINDIR) $(SRCDIR)/*.java $<

clean:
	rm $(BINDIR)/*.class
	
run: #Running the simulation with the medium terrain
	@java -cp $(BINDIR) Flow "medsample_in.txt"
	
run2: #Running the simulation with the large terrain
	@java -cp $(BINDIR) Flow "largesample_in.txt"
