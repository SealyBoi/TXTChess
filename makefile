JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		CheckmateValidation/Check.java \
		CheckmateValidation/FreeSquare.java \
		CheckmateValidation/Interceptor.java \
		Pieces/Bishop.java \
		Pieces/King.java \
		Pieces/Knight.java \
		Pieces/Pawn.java \
		Pieces/Pieces.java \
		Pieces/Queen.java \
		Pieces/Rook.java \
		Main/Board.java \
		Main/Main.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) */*.class