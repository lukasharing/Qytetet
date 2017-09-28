#ifndef WINDOW_H
#define WINDOW_H
class Window{
  private:
    int w, h; // Tamaño
    int x, y; // Coordenadas
  public:
    // Ancho de la Pantalla
    int getWidth();
    void setWidth(int);

    // Alto de la Pantalla
    int getHeight();
    void setHeight(int);

    // Tamaño
    void resize(int, int);

    // Coordenada X
    int getX();
    void setX(int);

    // Coordenada Y
    int getY();
    void setY(int);

    // Coordenadas X/Y
    void set(int, int);

    Window();
    Window(int, int, int, int);
};


int   Window::getWidth(){ return w; };
void  Window::setWidth(int _w){ w = _w; };
int   Window::getHeight(){ return h; };
void  Window::setHeight(int _h){ h = _h; };
void   Window::resize(int _w, int _h){ w = _w; h = _h; };

int   Window::getX(){ return x; };
void  Window::setX(int _x){ x = _x; };
int   Window::getY(){ return y; };
void  Window::setY(int _y){ y = _y; };
void  Window::set(int _x, int _y){ x = _x; y = _y; };

Window::Window(){ set(0,0); resize(100, 100); };
Window::Window(int _x, int _y, int _w, int _h){ set(_x, _y); resize(_w, _h); };

#endif
