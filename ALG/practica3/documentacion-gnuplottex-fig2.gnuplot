set terminal pdf color
set output 'documentacion-gnuplottex-fig2.pdf'
f(x)=3.61796e-008*x*x+-8.95664e-005*x+0.122892
plot "./datos/maximominimomatriz.dat" title 'Orden Empírico', f(x) title 'adjustment'
