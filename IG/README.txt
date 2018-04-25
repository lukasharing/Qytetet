The usage is simple, just press 1-9 to show one model, each model has its
own properties, by simply pressing "c", "p", "l", "f":
	"c" -> chess mode,
	"p" -> points mode,
	"f" -> fill mode,
	"l" -> line mode.

How to compile it?
As Windows developer compilation method is explained above,
as we don't use makefiles, I haven't provided one, so in other
platforms you will have to "make it by hand", just compile the file
that is in the source file and dont forget to link each .h file that is
in the include file (Sorry for that).

How to compile OpenGL & GLUT with ATOM editor in WIN32:
* Add to your system GL.dll (Maybe Glut.dll).
* You need MinGW!!. Then insert into the system variables MinGW bin path's.
	* Add into libraries: Glut.a.
	* Add into includes: Glut.h, Gl.h (if it doesn't exist).
* Go to setting in Atom and select your compile package
(You have to download the extension), then fill the
compiler option with: -g -lglut32 -lopengl32.
press F5 to compile.
and, TADA!

Written by Lukas Häring García.