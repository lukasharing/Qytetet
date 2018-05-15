set terminal pdf color
set output 'documentacion-gnuplottex-fig3.pdf'
f(x)=5.26918e-009*x*x-3.93483e-008*x+8.30631e-005
plot "./datos/zapatospies.dat" title 'Orden Empírico', f(x) title 'adjustment'
