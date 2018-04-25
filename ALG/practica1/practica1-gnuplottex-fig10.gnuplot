set terminal pdf color
set output 'practica1-gnuplottex-fig10.pdf'
f(x)=4.58241e-010*x+1.6424e-007
plot "./graph-data/bubble_omega.dat" title 'Orden Empírico Ω', f(x) title 'adjustment'
