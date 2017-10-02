#include <iostream>

#include <GL/gl.h>
#include <GL/glut.h>

#include <vector>
#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>

// Main Includes
#include <cmath>
#include "../header/window.h"
#include "../header/camera.h"
// 3D includes
#include "../header/object3d.h"
#include "../header/cube.h"
#include "../header/tetrahedron.h"

using namespace std;

#define PI 3.14159265358979323846  /* PI */

// Boolean keycontrol array.
bool keys_ascii[256] = {0};
vector<Object3D*> objects;
Object3D* currentVisible;

// Prevents from redrawing more times than needed.
bool is_being_draw = false;

// Initialization of Window and Camera.
Window my_screen(50, 50, 500, 500);
Camera camera(0, 0, 500, 500, 300, 10000);

// Cleaning method.
void clean(){ glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT ); }

// Projection.
void projection(){
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glFrustum(-5, 5, -5, 5, camera.getFrontPlane(), camera.getBackPlane());
}

// Viewer transformation.
void change_observer(){
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	glTranslatef(0.f, 0.f, -2*camera.getFrontPlane());
	glRotatef(camera.getRotation().getX(), 0.f, 1.f, 0.f);
	glRotatef(camera.getRotation().getY(), 1.f, 0.f, 0.f);
}

// Draws a coordinate axis (NOT USED)
void draw_axis(int size){
	glBegin(GL_LINES);
		// Color Rojo (Eje X)
		glColor3f(1.f, 0.f, 0.f);
		glVertex3f(-size, 0.f, 0.f);
		glVertex3f(+size, 0.f, 0.f);

		// Color verde (Eje Y)
		glColor3f(0.f, 1.f, 0.f);
		glVertex3f(0.f, -size, 0.f);
		glVertex3f(0.f, +size, 0.f);
		// eje Z, color azul
		glColor3f(0.f, 0.f, 1.f);
		glVertex3f(0.f, 0.f, -size);
		glVertex3f(0.f, 0.f, +size);
	glEnd();
}

// Drawing Method
void draw_objects(){
	is_being_draw = true;
	float x_r = (float)(keys_ascii[102] - keys_ascii[100]) * 0.02f;
	float y_r = (float)(keys_ascii[101] - keys_ascii[103]) * 0.02f;
	camera.getRotation().addX(x_r);
	camera.getRotation().addY(y_r);
	currentVisible->draw(camera);
	if(x_r == 0.0f && y_r == 0.0f){
		is_being_draw = false;
	}else{
		glutPostRedisplay();
	}
}

// Drawing scene
void draw_scene(void){
	clean();
	change_observer();
	draw_objects();
	glutSwapBuffers();
}


// Resize Sceene (Callback from resizing screen).
void resize_scene(int _w, int _h){
	camera.resize(_w, _h);
	projection();
	glViewport(0, 0, _w, _h);
}

// Key Event
void key_special_pressed(int key, int x, int y){ if(!is_being_draw || !keys_ascii[key]){ glutPostRedisplay(); keys_ascii[key] = true; } }
void key_special_unpressed(int key, int x, int y){ keys_ascii[key] = false; }
void key_pressed(unsigned char key, int x, int y){
	keys_ascii[key] = false;

	int ds = key - '0';
	if(ds >= 0 && ds <= objects.size()){
		currentVisible = objects[ds-1];
	}else{
		GLenum type = GL_FILL;
		bool is_chess = false;
		switch(key){
			case 'p': type = GL_POINT; 	 break;
			case 'l': type = GL_LINE; break;
			case 'f': type = GL_FILL; break;
			case 'c': type = GL_FILL; is_chess = true;	break;
		}
		currentVisible->setDrawType(type);
		currentVisible->setChess(is_chess);
	}
	glutPostRedisplay();
}

// Init
void init(void){
	// Adding objects to scene
	Tetrahedron* tetrahedron = new Tetrahedron();
	objects.push_back(tetrahedron);
	currentVisible = tetrahedron;

	Cube* cube = new Cube();
	objects.push_back(cube);


	Object3D* ant = new Object3D("../models/ant.ply");
	ant->setScale(0.5f);
	objects.push_back(ant);

	Object3D* shark = new Object3D("../models/shark.ply");
	shark->setScale(1.5f);
	objects.push_back(shark);

	Object3D* cat = new Object3D("../models/cat.ply");
	cat->setScale(20.f);
	objects.push_back(cat);

	glClearColor(1.f, 1.f, 1.f, 1.f); // RGB(255, 255, 255, 255) [White];
	projection();
	glViewport(0, 0, my_screen.getWidth(), my_screen.getHeight());
}

// Main Program
int main(int argc, char **argv){
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH);
	glutInitWindowPosition(my_screen.getX(), my_screen.getY());
	glutInitWindowSize(my_screen.getWidth(), my_screen.getHeight());
	glutCreateWindow("Ejercicio de entrega - Lukas Haring");
	glEnable(GL_DEPTH_TEST);

	//Events functions.
	glutDisplayFunc(draw_scene);
	glutReshapeFunc(resize_scene);
	glutSpecialFunc(key_special_pressed);
	glutSpecialUpFunc(key_special_unpressed);
	glutKeyboardUpFunc(key_pressed);

	init();
	glutMainLoop();
	return 0;
}
