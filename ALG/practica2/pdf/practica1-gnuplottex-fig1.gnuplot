set terminal pdf color
set output 'practica1-gnuplottex-fig1.pdf'
f(x)=3.31988e-008*x-8.77114e-005
plot "./datos/maximominimo.dat" title 'Orden Empírico', f(x) title 'adjustment'
