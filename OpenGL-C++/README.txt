How to compile with ATOM in WIN32:
* Add to your system GL.dll.
* You need MinGW and insert into the system variables the path 
to the created bin folder, then:
	* Add into libraries: Glut.a.
	* Add into includes: Glut.h, Gl.h (if it doesn't exist).
* Go to setting in Atom and select your compile package, then fill the
compiler option with: -g -lglut32 -lopengl32.
TADA!