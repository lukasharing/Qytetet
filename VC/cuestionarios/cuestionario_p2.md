# Cuestionario 1
## Realizado por Lukas Häring García

1. **Identifique las semejanzas y diferencias entre los problemas de:.**

    a) **Clasificación de imágenes**

    b) **Detección de objetos**

    c) **Segmentación de imágenes**

    d) **Segmentación de instancias**

    Todos estos problemas tienen en común que clasifican, por lo que deben conocer previamente los elementos.

    La **clasificación de imágenes** nos ofrece etiquetas de lo que hay en la imagen sin importar dónde, que a diferencia de la **detección de objectos**, que indentifica las regiones ("*bounding box*") de las diferentes categorías en la imagen. La **segmentación de imágenes** y la **segmentacion de instancias** detectan también objetos, pero de forma precisa, es decir, detectan píxeles del objeto,  que a diferencia de la **detección de objetos**, que introducen pixeles que no pertenecen al objeto.

    La **segmentación de imágenes** da la categoría de cada pixel, por lo que dos objetos de la misma categoría juntos no serían diferenciables, mientras que la **segmentacion de instancias**, si que es capaz de diferenciarlos.   


2. **¿Cuál es la técnica de búsqueda estándar para la detección de objetos  en  una  imagen?  Identifique  pros  y  contras  de  la  misma  e indique posibles soluciones para estos últimos.**

    *** MAL *** Ver técnica más general y anterior a 2012

    La técnica de búsqueda estándar es la llamada, **Sliding Window**, pasamos una ventana deslizante por toda la imagén y utilziar un extractor de característias para cada región. Finalmente, utilizamos un clasificador sobre las características de este modelo, repitiendo así por escalas.

    ### Pros
    * Es capaz de detecar todas las imágenes que el clasificador sea capaz de diferenciar.
    * No tiene en cuenta la pose, rotación, voltead, etc.
    * El clasificador puede cambiarse en cualquier momento para obtener mejores resultados.
    
    ### Contras
    * Existen "infinitas" ventanas de distinto tamaño y posición en la imagen.
    * El clasificador nos puede detectar muchas veces una misma imagen, por lo que tenemos que diferenciar de entre las regiones, cual nos deberíamos quedar.
    * Necesitamos un espacio de escalas, lo que hace que sea un proceso algo más lento.
    * El clasificador puede darnos falsos positivos que pueden ver incrementados según el salto de la ventana.

3. **Considere la aproximación que   extrae una   serie   de características en  cada píxel de  la  imagen  para  decidir  si  hay contorno o no. Diga si existe algún paralelismo entre la forma de actuar  de  esta  técnica  y  el  algoritmo  de  Canny. En  caso positivo identifique cuales son los elementos comunes y en que se diferencian los distintos.**

    Lo fundamental en ambos es el gradiente. Para la aproximación que extrae características en cada pixel, utiliza el gradiente para extraer características, mientras que el algoritmo de Canny utiliza el gradiente para la supresión de no máximos y para el proceso de histéresis.
    
    Otro elemento común es que en ambos métodos utilizan una imagen suavizada con un kernel gaussiano.

    Una de las diferencias es 

4. **Tanto  el  descriptor  de  SIFT  como  HOG  usan  el  mismo  tipo  de información de la imagen pero en contextos distintos. Diga en que se parecen y en que son distintos estos descriptores. Explique para que es útil cada uno de ellos.**

    Ambos son "extractores de características", además, ambos utilizan los gradientes de una imagen. 
    
    La principal diferencia es que Sift busca puntos de interés (keypoints) y descriptores según el espacio de escalas laplaciano-gaussiano, mientras que HoG, utiliza el espacio de escalas gaussiano y busca características a través de toda la imagen.

    Los descriptores de SIFT son invariantes, quiere decir que no se ven afectadas por variaciones en la pose, brillo, escalado, rotación, etc. Mientras que en HoG si que se ven afectadas.

    Las características extraidas por el descriptor de Sift son utilizados utilizadas para comparar imágenes, creación de imágenes panorámicas, seguimiento de objetos, etc.
    Mientras que las características extraidas por el descriptor de HoG son utilizadas para ...

5. **Observando el funcionamiento global de una CNN, identifique que dos procesos fundamentales definen lo que se realiza en un pase hacia delante de una imagen por la red. Asocie las capas que conozca a cada uno de ellos.**

    Los dos procesos fundamentales de una CNN son la **extraccion de caracteristicas** que es realizada por las capas convolucionales, las de activación (normalmente "*RELU*")  y las de "*Pooling*". El otro proceso fundamental es la **clasificación**, que es realizada por las capas la de aplanamiento, totalmente conectadas y la capa de activación "*Softmax*".

6. **Se ha visto que el aumento de la profundidad de una CNN es un factor  muy  relevante  para  la  extracción  de característicasen problemas complejos, sin embargo este enfoque añade nuevos problemas. Identifique cuales son y qué soluciones conoce para superarlos.**

    Uno de los problemas tras el aumento de la profundidad es el **desvanecimiento del gradiente**, para resolverlo se pueden utilizar varias técnicas, por ejemplo, redes convolucionales residuales (saltos entre capas, *skip connection*).   

    El aumento de profundidad hace que cada capa combine características de las capas anteriores, haciendo que estas se especialicen cada vez más, hasta llegar a un punto que su especialización es tan alta sobre los datos de entrenamiento que hay "*overfitting*", es decir. Este problema se puede atacar de distintas maneras, por ejemplo con el **data augmentation**, así las neuronas no se especializarían, otra pero menos efectivo es utilizar capas dropout ya que se ha visto que 

    Otro problema y más claro es que al añadir mayor profundidad añade más dificultad computacional al entrenamiento del modelo, pero su solución es utilizar hardware más potente o optar por ampliar la anchura.

    
7. **Existe actualmente alternativas de  interés al aumento  de  la profundidad para el diseño de CNN. En caso afirmativo diga cuál/es y como son.**

    Existen diferentes alternativas al interés al aumento de la profundida, por ejemplo:

    * **Ampliar la anchura**, esto ayudaría al cálculo computacional, ya que permite un paralelismo.
    * **Skip connection**, ayuda a que neuronas utilicen información de capas anteriores y no acaben aprendiendo cosas tan específicas.
    * **Data augmentation o utilizar capas dropout**, esto ayuda a que las neuronas no se especialicen tanto con los datos de entrenamiento.   

8. **Considere una aproximación clásica al reconocimiento de escenas en donde extraemos de la imagen un vector de características y lo usamos  para  decidir  la  clase  de  cada  imagen.  Compare  este procedimiento  con  el  uso  de  una CNN  para  el  mismo  problema.  ¿Hay conexión entre ambas aproximaciones? En caso afirmativo indique en que parecen y en que son distintas.**

    Si hay conexión, la conexión principal entre ambas es utilizan filtros para extraer características.

    En la aproximación clásica, se especifican los filtros, mientras que utilizando una CNN, estos filtros son "aprendidos".

    La diferencia principal entre ambas es que la aproximaxión clásica hace uso de un clasificador que a diferencia de la CNN, no. 

9.  **¿Cómo evoluciona el campo receptivo de las neuronas de una CNN con la profundidad de las capas? ¿Se solapan los campos receptivos de las distintas neuronas de una misma profundidad? ¿Es este hecho algo positivo o negativo de cara a un mejor funcionamiento?**

    El campo receptivo de las neuronas aumenta a medida que incrementamos la profundidad ya que debe mantener información de la capa anterior.

    Sí, los campos receptivos pueden solaparse debido al tamaño del kernel de la capa de convolución.
    
    El hecho de que el campo receptivo de dos neuronas se solape es un hecho negativo ya que las neuronas comparten información y ambas extraigan características similares, lo cual no nos interesa ya que queremos variedad.

10. **¿Qué  operación  es  central  en  el  proceso  de  aprendizaje  y optmización de una CNN?**


    **La capa de activación se puede ver como una operación de filtro.**<br/>
    
    Un modelo sin capas de activación se convierte en un modelo de regresión lineal, quiere decir que solo podemos separar dos conjuntos de datos utilizando una recta, lo que en problemas complejos, sería bastante inútil.
    
    Las capas de activación quitan esa linealidad y lo que llamamos "aprendizaje", consiste en calcular pesos que son capaces de separar de forma más compleja conjunto de datos para así poder diferenciarlos.

    El **ajuste de los pesos**, también es una operacion y utilizado para reducir el error que es también fundamental para aprender y optimizar el modelo utilizando *Descenso Gradiente*.
    
11. **Compare los  modelos  de  detección  de  objetos  basados  en aproximaciones clásicas y los basados en CNN y diga que dos procesos comunes a ambos aproximaciones han sido muy mejorados en los modelos CNN. Indique cómo.**

    Ambos utilizan regiones de la imagen para detectar objetos.

    En el caso del método clásico "Desplazamiento de ventana", la ventana se mueve a través de toda la imagen, pero esto tiene un problema, como hemos comentado anteriormente, la imagen puede estar en distinta escala y no ser detectada, finalmente, es el clasificador utilizado quién decide si hay objeto o no.

    Al principio, la estructura del CNN utilizaba un detector de regiones, por lo que necesitaba un extractor de regiones como algoritmo complementario, luego estas regiones eran analizadas por la red y detectaban si hay o no un objeto.

    Más adelante, estos modelos de detección basados en CNN se convirtieron en una estructura End2End, quiere decir que, todos los "módulos", desde detección hasta el clasificador, están dentro de la red convolucional, por lo que es la propia red quien decide dónde está el objeto.

    El problema de este tipo de redes es que necesitamos un gran dataset, además con las regiones identificadas, pero ha sido demostrado funcionar muy bien además de tener todo embedido en una propia estructura que se puede entrenar al completo de una pasada.

    Además estas capas permiten optimizar el modelo utilizando 

12. **Es posible construir arquitecturas CNN que sean independientes de las dimensiones de la imagen de entrada. En caso afirmativo diga cómo hacerlo y cómo interpretar la salida.**

    Las capas convolucionales no tienen en cuenta el tamaño de entrada, pero sí lo tienen las capas totalmente conectadas. Para resolver este problema, se utiliza una capa entre las convolucionales y las totalmente conectadas conocida como "*Spatial pyramid pooling*", esta capa se encarga de transformar la entrada en una salida de tamaño fijo, capaz de ser utilizada por las capas totalmente connectadas.

    La salida de esta capa es interpretada por las capas totalmente conectadas para clasificar.

    Esta capa funciona dividiendo las características de la última capa convolucional y devuelve un número fijo de contenedores, de ahí que se pueda utilizar en la capa totalmente conectada. 

13. **Suponga que entrenamos una arquitectura Lenet-5 para clasificar imágenes 128x128 de 5 clases distintas. Diga que cambios deberían de hacerse en la arquitectura del modelo para que se capaz de detectar las zonas de la imagen donde aparecen alguno de los objetos con los que fue entrenada.** 
    


14. **Argumente porqué la transformación de un tensor de dimensiones 128x32x32 en otro de dimensiones 256x16x16, usando una convolución 3x3  con  stride = 2,  tiene  sentido  que  pueda  ser  aproximada  por  una secuencia de tres convoluciones: convolución 1x1 + convolución 3x3 + convolución 1x1. Diga también qué papel juegan cada una de las tres convoluciones.**

    La primera convolución 1x1, se encargará de reducir dimensionalidad (encodificar la profundidad), pero no alterará ni el alto ni el ancho, por lo que su stride debe ser 1.

    La segunda convolución 3x3 se encarga de redimensionar de 32 x 32 a 16 x 16, con el stride de 2, manteniendo la misma profundidad. se encargará de realizar los cálculos oportunos como la convolución 3x3, el stride es usado aquí para no perder características importantes, que si lo pusiéramos en el 1x1, perderíamos.

    Finalmente la ultima convolución 1x1 se encarga de incrementar la profundidad (decodificar) a la que entró, 256.

    Este modelo tiene sentido dimensionalemente (el output concuerdan con el modelo 3x3), pero la pregunta es algo ambigüa ya que no se especifica si cada convolución tiene asociada una capa de activación, por lo que encontramos dos casos:
    * **Si existen capas de activación**, las características de salida son distintas (por).
    * **Si no existen las capas de activación**, ambos modelos son equivalente ya que son modelos lineales.  

15. **Identifique una propiedad técnica de los modelos CNN que permite pensar que podrían llegar a aproximar con precisión las características del modelo de visión humano, y que sin ella eso no sería posible. Explique bien su argumento.**
    
    La propiedad técnica de los modelos CNN es la estructura jerárquica de las características según los niveles, quiere decir que las primeras capas se fijan es detalles más pequeños (bordes, esquinas, etc), mientras que vamos avanzando, se van combinando (otra propiedad importante) para formar en cada capa información más compleja, por ejemplo, caras particulares u objetos. Donde se ha visto que en el cortex visual, actua de la misma manera.