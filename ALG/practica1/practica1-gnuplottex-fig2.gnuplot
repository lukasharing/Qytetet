set terminal pdf color
set output 'practica1-gnuplottex-fig2.pdf'
f(x)=-1.51427e-009*x+2.73733e-005
plot "./graph-data/tree_abb.dat" title 'Orden Empírico Θ', f(x) title 'adjustment'
