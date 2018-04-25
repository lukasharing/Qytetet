set terminal pdf color
set output 'practica1-gnuplottex-fig4.pdf'
f(x)=1.5138e-007*x+0.000529994
plot "./graph-data/tree_apo.dat" title 'Orden Empírico Θ', f(x) title 'adjustment'
