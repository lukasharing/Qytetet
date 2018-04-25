set terminal pdf color
set output 'practica1-gnuplottex-fig4.pdf'
f(x)=-4.57096e-014*x*x+3.33947e-008*x+0.000106985+0.0001
g(x)=3.20187e-008*x+0.000113866-0.0001
plot "./datos/mayorfreq.dat" title 'Orden Empírico Θ', f(x) title 'Upper bound', g(x) title 'Lower bound'
