You can add or modify rooms by editing .room files here. They are simple text 
files, so you can edit them with Notepad. An example can be found in test.room.
Any character not listed here that isn't a space, newline or EOF character will cause a crash if located in a .room file!
Be careful, case matters!

All rooms must have N, E, S, and W doors. The map generator doesn't know what to do if a room can't have doors in a
direction and I haven't bothered to fix that yet, so just don't try it.

Spaces will be ignored by the loader.

x = wall
. = floor
N, E, S, W = door (the letter used determines which direction the door leads)