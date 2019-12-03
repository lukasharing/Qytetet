# Cuestionario 1
## Realizado por Lukas Häring García

1. **Identifique las semejanzas y diferencias entre los problemas de:.**

    a) **Clasificación  de  imágenes**

    b) **Detección  de  objetos**

    c) **Segmentación de imágenes**

    d) **Segmentación de instancias**

2. **¿Cuál es la técnica de búsqueda estándar para la detección de objetos  en  una  imagen?  Identifique  pros  y  contras  de  la  misma  e indique posibles soluciones para estos últimos.**

    La técnica de búsqueda estándar es la llamada, **Sliding Window**, pasamos una ventana deslizante por toda la imagén y para cada región, utilizamos un clasificador, repitiendo así por escalas.

    ### Pros
    * Es capaz de detecar todas las imágenes que el clasificador sea capaz de diferenciar.
    * No tiene en cuenta la pose, rotación, voltead, etc.
    * El clasificador puede cambiarse en cualquier momento para obtener mejores resultados.
    
    ### Contras
    * Existen "infinitas" ventanas de distinto tamaño y posición en la imagen.
    * El clasificador nos puede detectar muchas veces una misma imagen, por lo que tenemos que diferenciar de entre las regiones, cual nos deberíamos quedar.
    * Necesitamos un espacio de escalas, lo que hace que sea un proceso algo más lento.
    * El clasificador puede darnos falsos positivos que pueden ver incrementados según el salto de la ventana.

3. **Considere la aproximación que   extrae una   serie   de características en  cada píxel de  la  imagen  para  decidir  si  hay contorno o no. Diga si existe algún paralelismo entre la forma de actuar  de  esta  técnica  y  el  algoritmo  de  Canny.  En  caso  positivo identifique cuales son los elementos comunes y en que se diferencian los distintos.**



4. **Tanto  el  descriptor  de  SIFT  como  HOG  usan  el  mismo  tipo  de información de la imagen pero en contextos distintos. Diga en que se parecen y en que son distintos estos descriptores. Explique para que es útil cada uno de ellos.**

    Ambos son "extractores de características", además, ambos utilizan los gradientes de una imagen. 
    
    La principal diferencia es que Sift busca puntos de interés (keypoints) y descriptores según el espacio de escalas laplaciano-gaussiano, mientras que HoG, utiliza el espacio de escalas gaussiano y busca características a través de toda la imagen.

    HoG hace uso de "templates de gradientes" para reconocer imágenes, mientras que Sift es capaz de reconocer imágenes según la coincidencia con los puntos de interés.

    Sift es muy útil cuando la imagen a reconocer está transformada (rotación, escalado, semi-oculta), mientras que HoG no es capaz de reconocer este tipo de imágenes ya que utiliza un patrón.

    Sift es utilizado para comparar imágenes, creación de imágenes panorámicas, transformación de imágenes, seguir a objetos .

    Sift  Busca puntos de interés en una imagen, en ángulos distintos, condiciones de iluminación, scalado, etc (Utilizado para detectar videos) mientras que si en HoG, si el objeto rota, la ventana tambíen debería rotar, es decir deberíamos tener todas las posibles rotaciones

    Mientras que HoG es útil para buscar 
    Sift no le importa las sombras, ni el brillo, ni el tiempo, ni la oclusión

5. **Observando el funcionamiento global de una CNN, identifique que dos procesos fundamentales definen lo que se realiza en un pase hacia delante de una imagen por la red. Asocie las capas que conozca a cada uno de ellos.**



6. **Se ha visto que el aumento de la profundidad de una CNN es un factor  muy  relevante  para  la  extracción  de característicasen problemas complejos, sin embargo este enfoque añade nuevos problemas. Identifique cuales son y qué soluciones conoce para superarlos.**

    El aumento de profundidad hace que cada capa sea capaz de aprender cosas cada vez más generales, por ejemplo, la primera será capaz de detectar bordes, la siguiente, figuras geométricas, la tercera, conjunto de figuras, luego ojos y finalmente caras (es un ejemplo algo general, pero funciona para explicar el problema).
    
    Lo que pasa es que a la hora de aumentar la profundidad, estamos fijándonos cada vez en cosas más concretas, por ejemplo en caras específicas y cuando viene una nueva cara, somos tán específico que nuestro modelo no es capaz de detectarla. Por lo que introducir mayor profundidad, hace que nuestro modelo no sea capaz de generalizar y pueda dar lo que llamamos "*overfitting*".

    Otro problema y más claro es que al añadir mayor profundidad, tenemos más parámetros a entrenar, lo que añade más dificultad computacional al problema.

7. **Existe actualmente alternativas de  interés al aumento  de  la profundidad para el diseño de CNN. En caso afirmativo diga cuál/es y como son.**

8. **Considere una aproximación clásica al reconocimiento de escenas en donde extraemos de la imagen un vector de características y lo usamos  para  decidir  la  clase  de  cada  imagen.  Compare  este procedimiento  con  el  uso  de  una CNN  para  el  mismo  problema.  ¿Hay conexión entre ambas aproximaciones? En caso afirmativo indique en que parecen y en que son distintas.**

9.  **¿Cómo evoluciona el campo receptivo de las neuronas de una CNN con la profundidad de la capas? ¿Se solapan los campos receptivos de las distintas neuronas de una misma profundidad? ¿Es este hecho algo positivo o negativo de cara a un mejor funcionamiento?**

    El campo receptivo de las neuronas de una CNN aumenta cuanto mayor profundidad ya que al añadir más capas convoucionales, el tamaño del kernel incrementa, haciendo que estas tengan más información.

    Los campos receptivos de las distintas neuronas de una misma profundidad se solapan por ejemplo cuando hacemos un "*Max Pooling*", con una ventana de 3x3 y un stride de 1 (Salto de 1 en 1), esto quiere decir que, buscará el máximo dentro de la ventana de 3x3 y saltará hacia la siguiente región con un salto de una unidad, claramente la siguiente región se solapará con las 6 neuronas de antes, cuanto mayor sea el stride, menos solapamiento habrá. 

    Esto ayuda ya que a mayor profundidad se ha visto que a mayor profundidad, por ejemplo con *Max Pooling*, los detalles más significativos (los máximos), son los que mejor representan a una imagen, además de reducir la complejidad computacional.

    Aunque el tema del uso de *Max Pooling* sigue siendo un tema abierto de debate, pero empíricamente se ha probado que funcione bien. 

10. **¿Qué  operación  es  central  en  el  proceso  de  aprendizaje  y optmización de una CNN?**

    La operación central en el proceso de aprendizaje y de optimización de una CNN es la existencia de capas de "filtro" (Capas de activación) que hacen que nuestro modelo se convierta en un modelo no lineal. 

    Un modelo sin capas de activación se convierte en un modelo de regresión lineal, quiere decir que solo podemos separar dos conjuntos de datos utilizando una recta, lo que en problemas complejos, sería bastante inútil.

    Las capas de activación quitan esa linealidad y lo que llamamos "aprendizaje" consiste en calcular pesos (utilizando backpropagation) que son capaces de separar de forma más compleja conjunto de datos para así poder diferenciarlos.

11. **Compare los  modelos  de  detección  de  objetos  basados  en aproximaciones clásicas y los basados en CNN y diga que dos procesos comunes a ambos aproximaciones han sido muy mejorados en los modelos CNN. Indique cómo.**

    Ambos utilizan regiones de la imagen para detectar objetos.

    En el caso del método clásico "Desplazamiento de ventana", la ventana se mueve a través de toda la imagen, pero esto tiene un problema, como hemos comentado anteriormente, la imagen puede estar en distinta escala y no ser detectada, finalmente, es el clasificador utilizado quién decide si hay objeto o no.

    Al principio, la estructura del CNN utilizaba un detector de regiones, por lo que necesitaba un extractor de regiones como algoritmo complementario, luego estas regiones eran analizadas por la red y detectaban si hay o no un objeto.

    Más adelante, estos modelos de detección basados en CNN se convirtieron en una estructura End2End, quiere decir que, todos los "módulos", desde detección hasta el clasificador, están dentro de la red convolucional, por lo que es la propia red quien decide dónde está el objeto.

    El problema de este tipo de redes es que necesitamos un gran dataset, además con las regiones identificadas, pero ha sido demostrado funcionar muy bien además de tener todo embedido en una propia estructura que se puede entrenar al completo de una pasada.

12. **Es posible construir arquitecturas CNN que sean independientes de las dimensiones de la imagende entrada. En caso afirmativo diga cómo hacerlo y cómo interpretar la salida.**

    Supongamos que nuestra arquitectura tiene definida una entrada de un tamaño n x n, una de las posibles estrategias sería preprocesar todas las imágenes de entrada para que tengan dicho tamaño de entrada n x n, por lo que tenemos 3 casos:
    1. **La imagen tiene tamaño exacto n x n**. No tenemos que procesar la imagen.
    2. **La imagen es proporcional a n x n**. Bastaría con escalar la imagen (upsampling o downsampling).
    3. **La imagen tiene tamaño distinto a n x n y no es proporcional**. Podemos rellenar la imagen hasta hacerla proporcional a n x n, por ejemplo rellenándolo de 0's, finalmente, re-escalarla a tamaño n x n.

    Para interpretar estos resultados, bastaría aplicar el método inverso al procesado sobre los resultados obtenidos, si se entrara en la región del padding, se ignorararía el resultado obtenido.

13. **Suponga que entrenamos una arquitectura Lenet-5 para clasificar imágenes 128x128 de 5 clases distintas. Diga que cambios deberían de hacerse en la arquitectura del modelo para que se capaz de detectar las zonas de la imagen donde aparecen alguno de los objetos con los que fue entrenada.**

    Lenet-5 está entrenado sobre un dataset de dígitos escritos a mano, fue utilizado para clasificar dígitos en cheques bancarios en los Estados Unidos.
    
    Esto son dos problemas distintos, uno es detectar y el otro reconocer. La estrategía sería combinar dos modelos, el de detección, por ejemplo utilizando RCNN o YOLO (O su versiones optimizadas) y finalmente utilizar Lenet-5 para clasificar dichas regiones.

    Si optáramos por utilizar HoG, veríamos que se convertiría en un problema muy lento el utilizar una ventana deslizante.

14. **Argumente porqué la transformación de un tensor de dimensiones 128x32x32 en otro de dimensiones 256x16x16, usando una convolución 3x3  con  stride = 2,  tiene  sentido  que  pueda  ser  aproximada  por  una secuencia de tres convoluciones: convolución 1x1 + convolución 3x3 + convoluión 1x1. Diga también qué papel juegan cada una de las tres convoluciones.**

     <!-- https://medium.com/@zurister/depth-wise-convolution-and-depth-wise-separable-convolution-37346565d4ec 
     
     https://towardsdatascience.com/covolutional-neural-network-cb0883dd6529-->

15. **Identifique una propiedad técnicade los modelos CNN que permite pensar que podrían llegar   a aproximarcon precisión las características del modelo de visión humano, y que sin ella eso no sería posible. Explique bien su argumento.**
