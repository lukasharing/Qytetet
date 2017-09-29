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
#include "../header/tetraedro.h"

using namespace std;

#define PI 3.14159265358979323846  /* pi */

// Boolean keycontrol array.
bool keys_ascii[256] = {0};
vector<Object3D*> objetos;

const float r2d = 180.f / PI;

// Initialization of Window and Camera.
Window my_screen(50, 50, 500, 500);
Camera camera(0, 0, 500, 500, 5, 500);

// Cleaning method
void clean(){ glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT ); }

// Projection
void projection(){
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glFrustum(-5, 5, -5, 5, camera.getFrontPlane(), camera.getBackPlane());
}

// View transformations
void change_observer(){
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	glTranslatef(0.f, 0.f, -2*camera.getFrontPlane());
	glRotatef(camera.getRotacionX(), 0.f, 1.f, 0.f);
	glRotatef(camera.getRotacionY(), 1.f, 0.f, 0.f);
}

// Draws a coordinate axis
void draw_axis(int size){
	glBegin(GL_LINES);
		glColor3f(1.f, 0.f, 0.f); // Color Rojo (Eje X)
		glVertex3f(-size, 0.f, 0.f);
		glVertex3f(+size, 0.f, 0.f);

		glColor3f(0.f, 1.f, 0.f); // Color verde (Eje Y)
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
	float x_r = (float)(keys_ascii[102] - keys_ascii[100]) * 0.02f;
	float y_r = (float)(keys_ascii[101] - keys_ascii[103]) * 0.02f;
	camera.addXRotacion(x_r);
	camera.addYRotacion(y_r);

	for(int i = 0; i < objetos.size(); i++){
		if(objetos[i]->getName() == "miCuboMenosFavorito"){
			objetos[i]->getRotation().addX(0.0005f);
			objetos[i]->getRotation().addY(0.0005f);
		}
		objetos[i]->draw(0.0, 0.0, -2*camera.getFrontPlane());
	}

	//draw_axis(2.f);
}

// Drawing scene
void draw_scene(void){
	clean();
	change_observer();
	draw_objects();
	glutSwapBuffers();
	glutPostRedisplay();
}

// Events.
void change_window_size(int _w,int _h){
	my_screen.resize(_w, _h);
	projection();
	glViewport(0, 0, _w, _h);
}

// Key Event
void key_pressed(int key, int x, int y){ keys_ascii[key] = true; }
void key_unpressed(int key, int x, int y){ keys_ascii[key] = false; }

// Init
void init(void){
	// Adding objects to scene
	Cube* cubo_scalado = new Cube();
	cubo_scalado->setName("miCuboMenosFavorito");
	cubo_scalado->setScale(1.0f);
	cubo_scalado->setColor(0xff33ff);
	objetos.push_back(cubo_scalado);

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
	glutReshapeFunc(change_window_size);

	glutSpecialFunc(key_pressed);
	glutSpecialUpFunc(key_unpressed);

	init();
	glutMainLoop();
	return 0;
}
