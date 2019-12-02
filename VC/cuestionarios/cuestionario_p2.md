# Cuestionario 1
## Realizado por Lukas Häring García

1. **Identifique las semejanzas y diferencias entre los problemas de:.**

    a) **Clasificación  de  imágenes**

    b) **Detección  de  objetos**

    c) **Segmentación de imágenes**

    d) **Segmentación de instancias**

2. **¿Cuál es la técnica de búsqueda estándar para la detección de objetos  en  una  imagen?  Identifique  pros  y  contras  de  la  misma  e indique posiblessoluciones para estos últimos.**

3. **Considere la aproximación que   extrae una   serie   de características en  cada píxel de  la  imagen  para  decidir  si  hay contorno o no. Diga si existe algún paralelismo entre la forma de actuar  de  esta  técnica  y  el  algoritmo  de  Canny.  En  caso  positivo identifique cuales son los elementos comunes y en que se diferencian los distintos.**

4. **Tanto  el  descriptor  de  SIFT  como  HOG  usan  el  mismo  tipo  de información de la imagen pero en contextos distintos. Diga en que se parecen y en que son distintos estos descriptores. Explique para que es útil cada uno de ellos.**

5. **Observando el funcionamiento global de una CNN, identifique que dos procesos fundamentales definen lo que se realiza en un pase hacia delante de una imagen por la red. Asocie las capasque conozcaa cada uno de ellos.**

6. **Se ha vistoque el aumento de la profundidad de una CNN es un factor  muy  relevante  para  la  extracción  de característicasen problemas complejos, sin embargo este enfoque añadenuevos problemas. Identifique cuales son y qué soluciones conoce para superarlos.**

7. **Existe actualmente alternativas de  interés al aumento  de  la profundidad para el diseño de CNN. En caso afirmativo diga cuál/es y como son.**

8. **Considere una aproximación clásica al reconocimiento de escenas en donde extraemos de la imagen un vector de características y lo usamos  para  decidir  la  clase  de  cada  imagen.  Compare  este procedimiento  con  el  uso  de  una CNN  para  el  mismo  problema.  ¿Hay conexión entre ambas aproximaciones? En caso afirmativo indique en que parecen y en que son distintas.**

9. **¿Cómo evoluciona el campo receptivo de las neuronas de una CNN con la profundidad de la capas? ¿Se solapan los campos receptivos de las distintas neuronas de una misma profundidad? ¿Es este hecho algo positivo o negativo de cara a un mejor funcionamiento?**

10. **¿Qué  operación  es  central  en  el  proceso  de  aprendizaje  y optmización de una CNN?**

11. **Compare los  modelos  de  detección  de  objetos  basados  en aproximaciones clásicas y los basados en CNN y diga que dos procesos comunes a ambos aproximaciones han sido muy mejorados en los modelosCNN. Indique cómo.**

12. **Es posible construir arquitecturas CNN que sean independientes de las dimensiones de la imagende entrada. En caso afirmativo digacómo hacerloy cómo interpretar la salida.**

13. **Suponga que entrenamos una arquitectura Lenet-5 para clasificar imágenes 128x128 de 5 clases distintas. Diga que cambios deberían de hacerse en la arquitectura del modelo para que se capaz dedetectar las zonas de la imagen donde aparecen alguno de los objetos con los que fue entrenada.**

14. **Argumente porqué la transformaciónde un tensorde dimensiones 128x32x32 en otro de dimensiones 256x16x16, usando una convolución 3x3  con  stride = 2,  tiene  sentido  que  pueda  ser  aproximada  por  una secuencia de tres convoluciones: convolución 1x1 + convolución 3x3 + convoluión 1x1. Diga también qué papel juegan cada una de las tres convoluciones.**

15. **Identifique una propiedad técnicade los modelos CNN que permite pensar que podrían llegar   a aproximarcon precisión las características del modelo de visión humano, y que sin ella eso no sería posible. Explique bien su argumento.**
