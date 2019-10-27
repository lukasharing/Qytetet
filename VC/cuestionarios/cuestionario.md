# Cuestionario 1
## Realizado por Lukas Häring García

1. **Diga en una sola frase cuál cree que es el objetivo principal de la Visión por Computador. Diga también cuáles la principal propiedad de las imágenesde cara a la creación de algoritmos que la procesen.**

    Creo que la **Visión por computador** es el estudio de técnicas que ayudan a un computador a analizar, interpretar y comprender imágenes digitales.
    <br>
    La principal propiedad de las imágenes es que se puede caracterizar una imagen según los píxeles de su alrededor.

2. **Expresar las diferencias y semejanzas entre las operaciones de correlación y convolución. Dar una interpretación de cada una de el las que en el contexto de uso en visión por computador.**

    La correlación (&sstarf;) es una técnica de procesamiento de imagen **local**, mientras que la convolución (&lowast;) es una es una técnica **local en un vecindario** (La región del alrededor de un pixel) **ARREGLAR**.
    
    La convolución es una correlación en la que el filtro está rotado 180 grados.

    El resultado de la correlación es equivalente a la convolución si el filtro utilizado es simétrico.

    Propiedades matemáticas:
    |                                  | Correlación     | Convolución |
    |----------------------------------|-----------------|-------------|
    | Asociativa                       | No generalmente | Siempre     |
    | Conmutativa                      | No generalmente | Siempre     |
    | Distributiva respecto de la suma |                 | Siempre     |
    | Escalar                          | Siempre         | Siempre     |
    | Elemento Neutro                  | Tiene           | Tiene       |
    
    Por último, otra similitud entre ellas y muy curiosa, es su asociatividad:

    A &sstarf; (B &lowast; C) = (A &sstarf; B) &lowast; C 


3. **¿Cuál es la diferencia “esencial” entre el filtro de convolución y el de mediana? Justificar la  respuesta.**

    La principal diferencia es su linealidad, la convolución es una aplicación lineal, mientras que el filtro de mediana no lo es. Por otro lado, el filtro de mediana no suaviza bordes, por lo que es normalmente utilizado para eliminar ruido sin emborronar mucho los bordes.

4. **Identifique el “mecanismo concreto” que usa un filtro de máscara para transformar una imagen.**

    Los filtro de máscara son una discretización de una integral (n-Dimensional), esto implica que se van a realizar operaciones con el vecindario, estos filtros son matrices (o tensores) con pesos reales y se va desplazando a través de toda la imagen digital. 

5. **¿De qué depende que una máscara de convolución pueda ser implementadapor convoluciones 1D? Justificar la respuesta.**

    Por el teoreoma de descomposición en valores singulares podemos dividir una máscara A, en convoluciones 1D. El teorema dice que la matriz A (máscara) es separable de la siguiente forma: 
    
    A = &sum;<sup>m</sup><sub>n=0</sub> a<sub>n</sub> C<sub>n</sub> F<sub>n</sub><sup>t</sup>, donde m es el número de valores propios de AA<sup>t</sup> no nulos, o lo que es lo mismo, el rango. C<sub>n</sub>, F<sub>n</sub> son matrices de dimensión 1 x k (siendo k el tamaño de A).

    Si I es la imagen, entonces:  I &lowast; A = I &lowast; &sum;<sup>m</sup><sub>n=0</sub> a<sub>n</sub> C<sub>n</sub> F<sub>n</sub><sup>t</sup> = I &lowast; (a<sub>0</sub>C<sub>0</sub>F<sub>0</sub><sup>t</sup>) + ... + I &lowast;(a<sub>m</sub>C<sub>m</sub>F<sub>m</sub><sup>t</sup>)

    Por lo que podemos concluir que todas las máscaras son separables, en máscaras 1D.

    Si queremos separar un filtro en **dos** filtros 1D, solo necesitamos un valor propio no nulo, o lo que es lo mismo, el rango sea **1**.
    

6. **Identificar las diferencias y consecuencias desde el punto de vista teórico y de la implementación entre:**
    1. **Primero alisar la imagen y después calcular las derivadas sobre la imagen alisada.**
    2. **Primero calcular las imágenes derivadas y después alisar dichas imágenes.** 
    
    **Justificar los argumentos.**

    Si se aplica la técnica de convolución y según lo comentador en el apartado 2.,no habría ninguna diferencia ya que la convolución es conmutativa y distributiva para la suma. La correlación al no ser conmutativa, si que hay diferencia entre aplicarlo en un orden u otro. 

    I = A &lowast; (S &lowast; (M<sub>x</sub> + M<sub>y</sub>)) = A &lowast; ((M<sub>x</sub> + M<sub>y</sub>) &lowast; S)

    Donde A es la imagen, S el filtro de suavizado. M<sub>x</sub> y M<sub>y</sub> los filtros de derivadas direccionales.

    A la hora de implementar esto eficientemente en convolución, bastaría con aplicar la propiedad de asociatividad sobre los filtros y podemos obtener un filtro pre-calculado. 

7. **Identifique las funciones de las que podemos extraer pesos correctos para implementar de forma eficiente la primera derivada de una imagen. Suponer alisamiento Gaussiano.**

    Según el enunciado, tenemos una máscara gaussiana G y una imagen I, a la que queremos calcular su derivada. Además, suponemos que estamos aplicando la convolución. Sabemos que estos filtros se pueden aplicar direccionalmente independientemente, por lo que la derivada en x, definido formalmente por: &part;<sub>x</sub>(I &lowast; G). Por el teorema de la derivada y de la propiedad de conmutatividad, (&part;<sub>x</sub>I) &lowast; G = I &lowast; (&part;<sub>x</sub>G), la parte de la derecha nos quiere decir que podemos convolucionar con la derivada direccional de la gaussiana, además como es separable, podemos realizar: I &lowast; (&part;<sub>x</sub>G + &part;<sub>y</sub>G), pudiéndose así pre-culacular estos filtros muestreando las derivadas direccionales de la gaussiana.

8. **Identifique las funciones de las que podemos extraer pesos correctos para implementar de forma eficiente la Laplacianade una imagen. Suponer alisamiento Gaussiano.**

    Vamos a calcular eficientemente la operación laplaciana &nabla;<sup>2</sup>, esta está definida sobre una Image de la siguiente forma: &nabla;<sup>2</sup>I =  &part;<sub>x</sub><sup>2</sup>I + &part;<sub>y</sub><sup>2</sup>I, como previamente queremos el filtro alisado de forma gaussiana G. Entonces &nabla;<sup>2</sup>(I &lowast; G) =  &part;<sub>x</sub><sup>2</sup>(I &lowast; G) + &part;<sub>y</sub><sup>2</sup>(I &lowast; G) =  I &lowast; &part;<sub>x</sub><sup>2</sup>G + I &lowast; &part;<sub>y</sub><sup>2</sup>G, esta última igualdad es consecuencia del teorema de la derivada sobre convolución.

    Como vemos, el resultado se obtiene de sumar las convoluciones de la imagen I con las respectivas segundas derivadas direccionales de la gaussiana. Esto quiere decir que, podemos pre-calcular la muestra de la segunda derivada de la gaussiana direccional y aplicar la convolución sobre la imagen.

9. **Suponga que le piden implementar de forma eficiente un algoritmo para el cálculo de la derivada de primer orden sobre una imagen usando alisamiento Gaussiano. Enumere y explique los pasos necesarios para llevarlo a cabo.**

    Este apartado es consecuencia directa del apartado 7., por lo que vamos a enunciar cada uno de los pasos para calcular eficientemente la derivada de una imagen con filtro gaussiano, supondremos que tenemos que la imagen no tiene aplicado el filtro gaussiano:
    1. Obtener la derivada del filtro gaussiano direccional de G<sub>u</sub>=1/(&#8730;(2&pi;)&sigma;)e<sup>-u^2/(2&sigma;^2)</sup>, G'<sub>u</sub> = -u/&sigma;G<sub>u</sub>
    2. Tomar un valor de 3&sigma; que corresponde a un 95% de la curva y muestrear el intervalo [-3&sigma;, +3&sigma;] de la derivada direccional sobre G'<sub>x</sub> y G'<sub>y</sub>.
    3. Aplicar la convolución sobre I primero sobre la dirección x.
    4. Para optimizar la propiedad de espacialización de la Caché, podemos transponer la imagen y aplicar la convolución sobre y.



10. **Identifique semejanzas y diferencias entre la pirámide gaussiana y el espacio de escalas de una imagen, ¿cuándo usar una u otra? Justificar los argumentos.**

    La principal semejanza entre la pirámide gaussiana y el espacio de escala es que ambas suavizan las imágenes con el filtro gaussiano.
    
    La mayor diferencia es la pirámide gaussiana escala la imagen a la mitad para cada escala.

    Para cada escala del espacio n, el sigma utilizado es &sigma;<sub>n</sub> = &sigma k<sup>n</sup>, siendo k el factor multiplicativo utilizado. Mientras que el sigma de la gaussiana sobre la otra gaussiana es &sqrt; 2 &sigma; entonces para cada escala está definido por &sigma; &#8730; 2 <sup>n</sup>. Vemos que debería coincidir si k=&#8730;(2)
11. **¿Bajo quécondicionespodemos garantizar una perfecta reconstrucción de una imagen a partir de su pirámide Laplaciana? Dar argumentos y discutir las opciones que considere necesario.**

    En primer lugar, si la recontruccion "perfecta" se considera sin pérdida de bits alguna, esto es imposible. Mientras que si perfecta, es considerada, que no se nota, esto es bastante plausible. Para demostrar que la recontrucción es total, bastaría con demostrar que cada uno de los filtros es reversible, lo cual el subsampling es imposible, ya que cuando reducimos a la mitad, si la imagen no es potencia de dos, vamos a peder una fila para las iteraciones de tamaño impar, mientras que si la imagen es potencia de dos, tendremos menos pérdida, pero cuando se interpola, estamos perdiendo pixeles de alrededor. Cabe destacar  

12. **¿Cuálesson las contribuciones más relevantes del algoritmo de Canny al cálculo de los contornos sobre una imagen?¿Existe alguna conexión entre las máscaras de Sobel y el algoritmo de Canny? Justificar la respuesta.**

13. **Identificar pros y contrasde k-medias como mecanismo paracrear un vocabulario visual a partir del cual poder caracterizar patrones.¿Quéganamos y que perdemos?Justificar los argumentos.**

14. **Identifique pros y contras del modelo de “Bolsa de Palabras”como mecanismo para caracterizar el contenido de unaimagen.¿Qué ganamos y que perdemos?Justificar los argumentos.**

15. **Suponga que dispone de unconjunto de imágenesde dos tiposde clases bien diferenciadas. Suponga que conoce como implementarde forma eficienteel cálculo de las derivadas hasta el orden N de la imagen. Describa como crear un algoritmo que permitadiferenciar,con garantías,imágenes de ambas clases. Justificar cada uno de los pasos que proponga.**
