# -*- coding: utf-8 -*-

#########################################################################
############ CARGAR LAS LIBRERÍAS NECESARIAS ############################
#########################################################################
import  numpy  as np
import  matplotlib.pyplot as plt

import  keras
import  keras.utils as  np_utils

from keras.models import Sequential
from keras.layers.convolutional import Conv2D, MaxPooling2D

from keras.layers import Activation, Dense, Flatten

from keras.preprocessing.image import ImageDataGenerator  

# Importar  el  optimizador a usar
from  keras.optimizers  import  SGD

# Importar  el  conjunto  de datos
from  keras.datasets  import  cifar100

#########################################################################
######## FUNCIÓN PARA CARGAR Y MODIFICAR EL CONJUNTO DE DATOS ###########
#########################################################################

def  cargarImagenes ():
  # Cargamos  Cifar100. Cada  imagen  tiene  tamaño
  # (32, 32, 3). Nos  vamos a quedar  con las 
  # imágenes  de 25 de las  clases.
  
  (x_train , y_train), (x_test , y_test) = cifar100.load_data(label_mode='fine')
  x_train = x_train.astype('float32')
  x_test = x_test.astype('float32')
  
  x_train  /= 255
  x_test  /= 255
  
  train_idx = np.isin(y_train , np.arange(25))
  train_idx = np.reshape(train_idx, -1)
  
  x_train = x_train[train_idx]
  y_train = y_train[train_idx]
  
  test_idx = np.isin(y_test , np.arange(25))
  test_idx = np.reshape(test_idx, -1)
  
  x_test = x_test[test_idx]
  y_test = y_test[test_idx]
  
  # Transformamos  los  vectores  de  clases  en  matrices.
  # Cada  componente  se  convierte  en un  vector  de ceros
  # con un uno en la  componente  correspondiente a la
  # clase a la que  pertenece  la  imagen. Este  paso es
  # necesario  para la  clasificaci ́on multiclase  en keras.
  y_train = np_utils.to_categorical(y_train, 25)
  y_test = np_utils.to_categorical(y_test, 25)
  
  return  x_train, y_train, x_test, y_test

#########################################################################
######## FUNCIÓN PARA OBTENER EL ACCURACY DEL CONJUNTO DE TEST ##########
#########################################################################

# Esta  función devuelve  el  accuracy  de un modelo, definido
# como el  porcentaje  de  etiquetas  bien  predichas
# frente  al total  de  etiquetas. Como  paŕametros  es
# necesario pasarle el vector de etiquetas verdaderas
# y el  vector  de  etiquetas  predichas, en el  formato  de
# keras (matrices  donde  cada  etiqueta  ocupa  una fila,
# con un 1 en la  posici ́on de la clase a la que  pertenece y
# 0 en las dem ́as).
def calcularAccuracy(labels , preds):
  labels = np.argmax(labels , axis = 1)
  preds = np.argmax(preds , axis = 1)
  accuracy = sum(labels  == preds ) / len(labels)
  return  accuracy

#########################################################################
## FUNCIÓN PARA PINTAR LA PÉRDIDA Y EL ACCURACY EN TRAIN Y VALIDACIÓN ###
#########################################################################

# Esta  funci ́on pinta  dos gráficas, una con la  evolucíon
# de la funci ́on de p erdida  en el  conjunto  de train y
# en el de validación, y otra  con la  evolución del
# accuracy en el  conjunto  de train y el de  validación.
# Es  necesario  pasarle  como  par ́ametro  el  historial  del
# entrenamiento del modelo (lo que devuelven las
# funciones  fit() y fit_generator()).
def  mostrarEvolucion(hist):
  loss = hist.history['loss']
  val_loss = hist.history['val_loss']
  plt.plot(loss)
  plt.plot(val_loss)
  plt.legend(['Training  loss', 'Validation  loss'])
  plt.show()
  acc = hist.history['acc']
  val_acc = hist.history['val_acc']
  plt.plot(acc)
  plt.plot(val_acc)
  plt.legend(['Training  accuracy','Validation  accuracy'])
  plt.show()

#########################################################################
################## DEFINICIÓN DEL MODELO BASENET ########################
#########################################################################

BaseNet = Sequential([
  # 5 Kernel = 4 + 1 ( central ) = 2 (left/right, top/bottom)
  # 32 - 2 (left/bottom) - 2 (right/top) = 28
  # Input Image 32 x 32 x 3
  Conv2D(6, kernel_size = (5, 5), padding = 'valid', input_shape = (32, 32, 3)),
  
  Activation('relu'),
  
  # 28 / 2 = 14
  MaxPooling2D(pool_size = (2, 2), strides = (2, 2)),
  
  # 14 - 2 (left/bottom) - 2 (right/top) = 10
  Conv2D(16, kernel_size = (5, 5), padding = 'valid'),
  
  Activation('relu'),
  
  # 10 / 2 = 5
  MaxPooling2D(pool_size = (2, 2), strides = (2, 2)),
  
  # Aplanar antes de pasarlo por las capas dense
  Flatten(),

  # Input 400, output 50
  Dense(units = 50, input_shape = (400,)),
  
  Activation('relu'),
  
  Dense(units = 25, input_shape = (50,))
])

#BaseNet.summary()

"""

ADAM : https://arxiv.org/pdf/1712.07628.pdf

"""

BaseNet.compile(
  loss = 'categorical_crossentropy', 
  #optimizer = SGD(lr = 0.01, decay = 1e-6, momentum = 0.9, nesterov = True),
  optimizer = 'adam', 
  metrics = ['accuracy']
)

split = 0.1
datagen = ImageDataGenerator(
  validation_split = split,
  #featurewise_center = True,
  #featurewise_std_normalization = True,
  #rotation_range = 20,
  #width_shift_range = 0.2,
  #height_shift_range = 0.2,
  #horizontal_flip = True
)


x_train, y_train, x_test, y_test = cargarImagenes()

# Entrena el modelo para un número fijo de épocas (Iteraciones en un dataset)
datagen.fit(x_train)

# Aplica configuración normalizada a un batch de entrada
datagen.standardize(x_train)

# https://stackoverflow.com/questions/45943675/meaning-of-validation-steps-in-keras-sequential-fit-generator-parameter-list
batch_size = 32
verbose = 2 # 0 = No Infor, 1 = Full Infom, 2 = Half Info
history = BaseNet.fit_generator(
  generator = datagen.flow(
    x_train,
    y_train,
    batch_size = batch_size,
    subset = "training"
  ),
  validation_data = datagen.flow(
    x_test,
    y_test,
    batch_size = batch_size,
    subset = "validation"
  ),
  epochs = 32,
  steps_per_epoch = len(x_train) * (1. - split) / batch_size,
  validation_steps = len(x_train) * split / batch_size,
  verbose = verbose,
)

# Evaluamos
evaluation = BaseNet.evaluate(
  x_test,
  y_test
)

print("Loss: ", evaluation[0])
print("Accuracy: ", evaluation[1])

mostrarEvolucion(history)

# accuracy = calcularAccuracy(y_train, predictions)

# print(accuracy)

#########################################################################
######### DEFINICIÓN DEL OPTIMIZADOR Y COMPILACIÓN DEL MODELO ###########
#########################################################################

# A completar

# "Quitar constantes"
# Poner todas en la misma escala (Media 0, Desviación típica 1)
# Intentar que las variables sean lo menos coorreladas. Intentar que la co-varianza sea una esfera: pre-whitening

# Una vez tenemos el modelo base, y antes de entrenar, vamos a guardar los
# pesos aleatorios con los que empieza la red, para poder reestablecerlos
# después y comparar resultados entre no usar mejoras y sí usarlas.
weights = BaseNet.get_weights()

#########################################################################
###################### ENTRENAMIENTO DEL MODELO #########################
#########################################################################

# A completar

#########################################################################
################ PREDICCIÓN SOBRE EL CONJUNTO DE TEST ###################
#########################################################################

# A completar

#########################################################################
########################## MEJORA DEL MODELO ############################
#########################################################################

# A completar. Tanto la normalización de los datos como el data
# augmentation debe hacerse con la clase ImageDataGenerator.
# Se recomienda ir entrenando con cada paso para comprobar
# en qué grado mejora cada uno de ellos.
