#include <iostream>

#include <GL/gl.h>
#include <GL/glut.h>

#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>

#include "../header/window.h"
#include "../header/camera.h"

using namespace std;

bool keys_ascii[256] = {0};

Window my_screen(50, 50, 500, 500);
Camera camera(0, 0, 500, 500, 0, 1000);

void clean(){ glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT ); }

/* Proyección */
void projection(){
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glFrustum(-5, 5, -5, 5, camera.getFrontPlane(), camera.getBackPlane());
}

// Transformaciones de vista.
void change_observer(){
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	glTranslatef(0.f, 0.f, -camera.getFrontPlane());
	glRotatef(camera.getRotationX(), 0.f, 1.f, 0.f);
	glRotatef(camera.getRotationY(), 1.f, 0.f, 0.f);
}

// Dibuja un eje de coordenadas
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

// Dibujamos los objetos en escena.
void draw_objects(){
	float x_r = (float)(keys_ascii[102] - keys_ascii[100]) * 0.02f;
	float y_r = (float)(keys_ascii[101] - keys_ascii[103]) * 0.02f;
	camera.addXRotation(x_r);
	camera.addYRotation(y_r);

	draw_axis(100.f);
	glFlush();
	glutPostRedisplay();
}

// Dibujamos la escena
void draw_scene(void){
	clean();
	change_observer();
	draw_objects();
	glutSwapBuffers();
}

// Events.
void change_window_size(int _w,int _h){
	my_screen.resize(_w, _h);
	projection();
	glViewport(0, 0, _w, _h);
	glutPostRedisplay();
}

void normal_keys(unsigned char key, int x, int y){
	switch (toupper(key)) {
		case 'Q':
			exit(0);
		break;
	}
}

void key_pressed(int key, int x, int y){
	keys_ascii[key] = true;
}

void key_unpressed(int key, int x, int y){
	keys_ascii[key] = false;
}

// Init
void init(void){
	glClearColor(1.f, 1.f, 1.f, 1.f); // RGB(255, 255, 255, 255);
	projection();
	glViewport(0, 0, my_screen.getWidth(), my_screen.getHeight());
}

// Main Program
int main(int argc, char **argv){
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH);
	glutInitWindowPosition(my_screen.getX(), my_screen.getY());
	glutInitWindowSize(my_screen.getWidth(), my_screen.getHeight());
	glutCreateWindow("Ejercicio de entrega - Lukas Häring");
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
