GCC = gcc
CONSERVATIVE_FLAGS = -std=c99 -Wall -Wextra -pedantic
DEBUGGING_FLAGS = -g -O0
CFLAGS = $(CONSERVATIVE_FLAGS) $(DEBUGGING_FLAGS)

render_tone: render_tone.o wave.o io.o
	$(GCC) -o render_tone render_tone.o wave.o io.o -lm

rener_tone.o: render_tone.c wave.h io.h
	$(GCC) $(CFLAGS) -c render_tone.c -lm

render_song: render_song.o wave.o io.o
	$(GCC) -o render_song render_song.o wave.o io.o -lm

render_song.o: render_song.c wave.h io.h
	$(GCC) $(CFLAGS) -c render_song.c -lm

render_echo: render_echo.o wave.o io.o
	$(GCC) -o render_echo render_echo.o wave.o io.o -lm

render_echo.o: render_echo.c wave.h io.h
	$(GCC) $(CFLAGS) -c render_echo.c -lm

wave.o: wave.c wave.h
	$(GCC) -c wave.c $(CFLAGS) -lm

io.o: io.c io.h
	$(GCC) -c io.c $(CFLAGS)

clean:
	rm -f *.o render_tone render_song render_echo
