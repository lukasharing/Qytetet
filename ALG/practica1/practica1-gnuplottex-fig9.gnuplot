set terminal pdf color
set output 'practica1-gnuplottex-fig9.pdf'
f(x)=1.19523e-009*x*x+-2.64497e-006*x+0.00183111
plot "./graph-data/bubble.dat" title 'Orden Empírico O', f(x) title 'adjustment'
