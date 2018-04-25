set terminal pdf color
set output 'practica1-gnuplottex-fig5.pdf'
f(x)=7.88641e-011*x*x+9.44417e-009*x+2.31858e-006
plot "./graph-data/selection.dat" title 'Orden Empírico Θ', f(x) title 'adjustment'
