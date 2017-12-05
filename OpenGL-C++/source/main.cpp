#include <iostream>
#include <GL/gl.h>
#include <GL/glut.h>
#include <vector>

// Library
#include "../libs/JpegLoader.h"

// Main Includes
#include <cmath>
#include "../header/window.h"
#include "../header/camera.h"


// // // 3D includes
#include "../header/object3d.h"
#include "../header/hierarchical_object.h"
#include "../header/cube.h"
#include "../header/revolution_object.h"
#include "../header/tetrahedron.h"
#include "../header/mario/bomb.h"

// #include <pthread.h>
// void junk() {
//   int i;
//   i=pthread_getconcurrency();
// };
//using namespace std;

#define PI 3.14159265358979323846  /* PI */
//
// Boolean keycontrol array.
bool keys_ascii[256] = {0};
std::vector<Object3D*> objects;
Hierarchy* model;
Object3D* currentVisible = model;
Revolution* pawn;

// Prevents from redrawing more times than needed.
bool is_being_draw = false;
bool automatic = true;
long int time = 0;

// Initialization of Window and Camera.
Window my_screen(50, 50, 500, 500);
Camera camera(0, 0, 500, 500, 300, 10000);

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

// // Draws a coordinate axis (NOT USED)
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
const float r_light = 10.f;
void draw_objects(){
	const GLfloat posf1[4] = { cos(time / 10.0) * r_light, 1.0, sin(time / 10.0) * r_light, 1.0 } ; // (x,y,z,w)
	const GLfloat posf2[4] = { 1.0, cos(time / 10.0) * r_light, sin(time / 10.0) * r_light, 1.0 } ; // (x,y,z,w)
	glLightfv(GL_LIGHT0, GL_POSITION, posf1);
	glLightfv(GL_LIGHT1, GL_POSITION, posf2);

	is_being_draw = true;
	float x_r = (float)(keys_ascii[102] - keys_ascii[100]) * 2.f;
	float y_r = (float)(keys_ascii[101] - keys_ascii[103]) * 2.f;
	camera.getRotation().addX(x_r);
	camera.getRotation().addY(y_r);
  currentVisible->draw(time);
	if(x_r == 0.0f && y_r == 0.0f){
		is_being_draw = false;
	}

  if(automatic || is_being_draw){
    time++;
		glutPostRedisplay();
	}
}

// Drawing scene
void draw_scene(void){
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
	change_observer();
	draw_objects();
	glutSwapBuffers();
}

//Resize Sceene (Callback from resizing screen).
void resize_scene(int _w, int _h){
	camera.resize(_w, _h);
	projection();
	glViewport(0, 0, _w, _h);
}

// Key Event
void key_special_pressed(int key, int x, int y){
	if(!is_being_draw || !keys_ascii[key]){
		keys_ascii[key] = true;
		glutPostRedisplay();
	}
}
void key_special_unpressed(int key, int x, int y){ keys_ascii[key] = false; }
void key_pressed(unsigned char key, int x, int y){
	keys_ascii[key] = false;

	int ds = key - '0';
	if(ds >= 0 && ds <= objects.size()){
		currentVisible = objects[ds-1];
  }else{
		bool modify = false;
		GLenum type;
		switch(key){
			case 'p': type = GL_POINT;break;
			case 'l': type = GL_LINE; break;
			case 'f': type = GL_FILL; break;
			case 'z': model->setLibertyValue(0, -0.1f); break;
			case 'x': model->setLibertyValue(1, -0.1f); break;
			case 'c': model->setLibertyValue(2, -0.1f); break;
			case 'b': model->setLibertyValue(0, 0.1f); break;
			case 'n': model->setLibertyValue(1, 0.1f); break;
			case 'm': model->setLibertyValue(2, 0.1f); break;
			default: type = GL_FILL; break;
		}
    if(!modify){
      currentVisible->setDrawType(type);
    }
	}
	glutPostRedisplay();
}

// Init
void init(){
	// Init
	JpegLoader jpeg_opener;

	JpegLoader::ImageInfo* wood = jpeg_opener.load("../images/crate.jpg");

	GLuint idTex;
	glGenTextures(1, &idTex);
	glBindTexture(GL_TEXTURE_2D, idTex);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, wood->nWidth, wood->nHeight, 0, GL_RGB, GL_UNSIGNED_BYTE, wood->pData);

	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

	// Adding objects to scene
	Tetrahedron* tetrahedron = new Tetrahedron();
	objects.push_back(tetrahedron);
	currentVisible = tetrahedron;

	Cube* cube = new Cube();
	cube->setTexture(idTex);
	objects.push_back(cube);

	pawn = new Revolution("../models/peon.ply", 4, 2);
	pawn->setScale(3.f);
	objects.push_back(pawn);

  /* Revolution from points */
  const int number = 6;
  float points[number * 3] = {
    .5f, 0.f, .5f,
    1.f, 0.f, 1.f,
    1.5f, 0.f, .5f,
    1.5f, 0.f, -.5f,
    1.f, 0.f, -1.f,
    .5f, 0.f, -.5f
  };
  int slices = 4;
  Revolution* rev = new Revolution(points, number, slices, 2);
	rev->setScale(1.f);
  rev->setCover(6 * slices);
	objects.push_back(rev);

  Bomb* mario_bomb = new Bomb();
  mario_bomb->setScale(3.f);
	objects.push_back(model = mario_bomb);

	Object3D* obj = new Object3D("../models/beethoven.ply");
	obj->setScale(1.f);
	objects.push_back(obj);


	glClearColor(.0f, .0f, .0f, 1.f);
	glEnable(GL_LIGHTING);

	float ca1[4] = { 1.0, 0.0, 1.0, 1.0 };
	glLightfv(GL_LIGHT0, GL_AMBIENT, ca1);
	glEnable (GL_LIGHT0);
	float ca2[4] = { 0.0, 1.0, 1.0, 1.0 };
	glLightfv(GL_LIGHT1, GL_AMBIENT, ca2);
	glEnable (GL_LIGHT1);
	glEnable(GL_DEPTH_TEST);

	projection();
	glViewport(0, 0, my_screen.getWidth(), my_screen.getHeight());
}

// Main Program
int main(int argc, char *argv[]){
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH);
	glutInitWindowPosition(my_screen.getX(), my_screen.getY());
	glutInitWindowSize(my_screen.getWidth(), my_screen.getHeight());
	glutCreateWindow("Ejercicio de entrega - Lukas Haring v.01");

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
