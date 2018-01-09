#include <iostream>
#include <GL/gl.h>
#include <GL/glu.h>
#include <GL/glut.h>
#include <vector>
#include <typeinfo>
// Library
#include "../libs/JpegLoader.h"
#include "../libs/FpsTimer.h"

// Main Includes
#include <cmath>
#include "../header/camera.h"


// // // 3D includes
#include "../header/object3d.h"
#include "../header/hierarchical_object.h"
#include "../header/plane.h"
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
Bomb* mario_bomb;
Object3D* currentVisible = mario_bomb;
Revolution* pawn;
Plane* plane;
FpsTimer* fpsTimer;

// Prevents from redrawing more times than needed.
bool cam_blocked = false;
bool scan_method = true;

int initial_time = 20;
int time_transition = initial_time;
long int delta = 0;

// Initialization of Window and camera->
std::vector<Camera*> cameras;
int camera_index = 0;
Camera* camera;
Camera helper;

// MOUSE METHOD
void mouse(int button, int state, int x, int y){
	if(!cam_blocked){
		//float mx = 2.f * x / camera->getWidth() - 1.f;
		//float my = 2.f * y / camera->getHeight() - 1.f;
		switch (button) {
			case 0:
				if(scan_method){
					unsigned char res[4];
					GLint viewport[4];
					glGetIntegerv(GL_VIEWPORT, viewport);
					glReadPixels(x, viewport[3] - y, 1,1,GL_RGBA, GL_UNSIGNED_BYTE, &res);

					bool found = false;
					int i;
					for(i = 0; i < objects.size() && !found; ++i){
						if(objects[i]->sameIntColor(res)){
							found = true;
						}
					}
					if(i < objects.size()){
						std::cout << typeid(*objects[i-1]).name() << std::endl;
					}
				}
			break;
			case 3: // Wheel Up
				camera->zoom(0.98);
			break;
			case 4: // Wheel down
				camera->zoom(1/0.98);
			break;
			default: break;
		}
	}
}

// Drawing Method
const float r_light = 10.f;
void draw_objects(){
	float x_r = (float)(keys_ascii['d'] - keys_ascii['a']) * 0.4f;
	float y_r = (float)(keys_ascii['w'] - keys_ascii['s']) * 0.4f;
	if(mario_bomb != currentVisible){
		camera->getPosition().addX(x_r);
		camera->getPosition().addY(y_r);
	}else{
		mario_bomb->getVelocity().add(-x_r, 0.0, y_r);
		cameras[2]->setLookAt(mario_bomb->getPosition());
		cameras[2]->getPosition().add(-x_r, 0.0, y_r);
	}

	if(scan_method){
		glDisable(GL_DITHER);
		glDisable(GL_LIGHTING);
		glDisable(GL_TEXTURE_2D);
	}else{
		glEnable(GL_DITHER);
		glEnable(GL_LIGHTING);
	}
	plane->draw(delta, scan_method);
  currentVisible->draw(delta, scan_method);
}

// Draw cameras
void draw_cameras(){
	std::vector<Camera*>::iterator it;
	for(it = cameras.begin(); it != cameras.end(); ++it){
		if(*it != camera){
			Vector3D* vc = &(*it)->getPosition();
			glPushMatrix();
			glTranslatef(vc->getX(), vc->getY(), vc->getZ());
			glColor3ub(255, 255, 255);
			glutSolidSphere(1.0, 50, 50);
			glPopMatrix();
		}
	}
}

// Draw Text
void draw_text(float x, float y, void* font, char* string) {
    glRasterPos2f(x,y);

    for(char* c = string; *c != '\0'; c++) {
        glutBitmapCharacter(font, *c);
    }
}

// Draw GUI
void draw_gui(){
	draw_text(-18.0, +18.0, GLUT_BITMAP_HELVETICA_18, fpsTimer->getFps());
}

// Drawing scene
void draw_scene(void){
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
  fpsTimer->timeFrame();
	glPushMatrix();
	camera->projection();
	draw_objects();
	draw_cameras();
	glPopMatrix();

	if(cam_blocked){
		camera = cameras[(++camera_index) % cameras.size()];
		cam_blocked = false;
	}


	draw_gui();
	glutPostRedisplay();
	glutSwapBuffers();
}

//Resize Sceene (Callback from resizing screen).
void resize_scene(int _w, int _h){ camera->resize(_w, _h); }

// Key Event
void key_special_pressed(int key, int x, int y){
	keys_ascii[key] = true;
	switch(key){
		case GLUT_KEY_RIGHT:
			cam_blocked = true;
		break;
	}
}
void key_special_unpressed(int key, int x, int y){ keys_ascii[key] = false; }
void key_unpressed(unsigned char key, int x, int y){
	keys_ascii[key] = false;

	int ds = key - '0';
	if(ds >= 0 && ds <= objects.size()){
		currentVisible = objects[ds-1];
  }else{
		bool modify = false;
		GLenum type;
		switch(key){
			case 'q': scan_method = !scan_method; break;
			case 'p': type = GL_POINT; break;
			case 'l': type = GL_LINE; break;
			case 'f': type = GL_FILL; break;
			default: type = GL_FILL; break;
		}
    if(!modify){
      currentVisible->setDrawType(type);
    }
	}
}

void key_pressed(unsigned char key, int x, int y){
	keys_ascii[key] = true;
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
	plane = new Plane(10.0);
	plane->setVisibility(true, true);

	Tetrahedron* tetrahedron = new Tetrahedron();
	tetrahedron->setColor(0.10, 0.50, 0.50);
	objects.push_back(tetrahedron);
	currentVisible = tetrahedron;

	Cube* cube = new Cube();
	cube->setColor(0.3, 0.4, 0.2);
	cube->setTexture(idTex);
	objects.push_back(cube);

	pawn = new Revolution("../models/peon.ply", 4, 1);
	objects.push_back(pawn);

  /* Revolution from points */
  const int number = 6;
  float points[number * 3] = {
    .5f, .5f, 0.f,
    1.f, 1.f, 0.f,
    1.5f, .5f, 0.f,
    1.5f, -.5f, 0.f,
    1.f, -1.f, 0.f,
    .5f, -.5f, 0.f,
  };
  int slices = 4;
  Revolution* rev = new Revolution(points, number, slices, 0);
	rev->setColor(0.8, 0.4, 0.0);
  rev->setCover(6 * slices);
	objects.push_back(rev);

  mario_bomb = new Bomb();
	objects.push_back(mario_bomb);

	Object3D* obj = new Object3D("../models/beethoven.ply");
	obj->setColor(0.1, 0.8, 0.1);
	objects.push_back(obj);


	glClearColor(0.0f, 0.0f, 0.0f, 1.f);
	GLfloat ca1[4] = { 1.0, 1.0, 1.0, 1.0 };
	glEnable(GL_LIGHTING);
	glLightfv(GL_LIGHT0, GL_AMBIENT, ca1);
	glEnable (GL_LIGHT0);
	//float ca2[4] = { 0.0, 1.0, 1.0, 1.0 };
	//glLightfv(GL_LIGHT1, GL_AMBIENT, ca2);
	//glEnable (GL_LIGHT1);
	glEnable(GL_DEPTH_TEST);
	glShadeModel (GL_SMOOTH);
	glLightModeli( GL_LIGHT_MODEL_LOCAL_VIEWER, GL_TRUE );

	glViewport(0, 0, camera->getWidth(), camera->getHeight());
}

// Main Program
int main(int argc, char *argv[]){
	fpsTimer = new FpsTimer(30);

	Camera* new_cam;
	new_cam = new Camera(500, 500, 10.0, 100.0, 43.0, true);
	new_cam->getPosition().set(-10.0, 20.0, 0.0);
	new_cam->getTopVector().set(1.0, 0.0, 0.0);
	cameras.push_back(new_cam);

	new_cam = new Camera(500, 500, 10.0, 100.0, 43.0, true);
	new_cam->getPosition().set(10.0, 10.0, +20.0);
	new_cam->getTopVector().set(1.0, 1.0, 0.0);
	cameras.push_back(new_cam);

	new_cam = new Camera(500, 500, 10.0, 100.0, 43.0, true);
	new_cam->getPosition().set(-20.0, 30.0, -20.0);
	new_cam->getTopVector().set(0.0, 0.0, 1.0);
	cameras.push_back(new_cam);
	camera = new_cam;

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH);
	glutInitWindowPosition(0, 0);
	glutInitWindowSize(camera->getWidth(), camera->getHeight());
	glutCreateWindow("Ejercicio de entrega - Lukas Haring");

	//Events functions.
	glutDisplayFunc(draw_scene);
	glutReshapeFunc(resize_scene);
	glutSpecialFunc(key_special_pressed);
	// MOUSE FUNCTION
	glutMouseFunc(mouse);
	//glutMotionFunc(moving_mouse);
	glutSpecialUpFunc(key_special_unpressed);
	glutKeyboardUpFunc(key_unpressed);
	glutKeyboardFunc(key_pressed);

	init();
	glutMainLoop();
	return 0;
}
