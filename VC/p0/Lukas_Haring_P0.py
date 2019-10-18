# -*- coding: utf-8 -*-
# Práctica 0 Realizada por Lukas Häring García
import cv2
import numpy as np
import matplotlib.pyplot as plt


import os
os.path.dirname(os.path.abspath(__file__))

"""
    Ejercicio 1
    @filename : Existing File Path
    @flagColor: cv2.IMREAD_GRAYSCALE or cv2.IMREAD_COLOR
"""
def leeimagen(filename, flagColor):
    return cv2.imread(filename, flagColor)

"""
    Ejercicio 2
    @im : Image
"""
def pintaI(im):
    
    # Asumimos que "normalizar" una imagen 
    # Transformar el minimo en 0 y el máximo en 1
    # Pixel = (px - min) / (max - min)
    
    # Escalar Valores, Elevamos a 1./2.2 para la corrección gamma
    # http://iquilezles.org/www/articles/outdoorslighting/outdoorslighting.htm
    im = pow(im, 0.4545);
    
    # Distinguimos la normalización según el número de canales
    if len(im.shape) == 2: 
        [min_gs, max_gs] = [np.min(im), np.max(im)]
        im[:,:] = (im[:,:] - min_gs) / (max_gs - min_gs)
    else:
        [min_rd, max_rd] = [np.min(im[:,:,0]), np.max(im[:,:,0])]
        im[:,:, 0] = (im[:,:, 0] - min_rd) / (max_rd - min_rd)
        [min_gr, max_gr] = [np.min(im[:,:,1]), np.max(im[:,:,1])]
        im[:,:, 1] = (im[:,:, 1] - min_gr) / (max_gr - min_gr)
        [min_bl, max_bl] = [np.min(im[:,:,2]), np.max(im[:,:,2])]
        im[:,:, 2] = (im[:,:, 2] - min_bl) / (max_bl - min_bl)
    
    
    # Transformamos al rango 0 - 255 y casteamos a int
    im = np.uint8(im * 255)
    
    
    return im

"""
    Ejercicio 3
    @vim : List of images
"""
def pintaMI(vim):
    
    # Calculamos el tamaño de todas las imagenes puestas en horizontal (suma de anchuras)
    width = np.sum(img.shape[1] for img in vim)
    
    # Calculamos el tamaño máximo que alcanza la imágen más grande.
    height = max(img.shape[0] for img in vim)   
    
    # Creamos la imagen final con 3 Canales
    final = np.zeros((height, width, 3), dtype=np.uint8);
    
    last_x = 0 # Almacenamos el tamaño ocupado por la n-ésima imagen
    for img in vim:
        # Comprobamos el número de canales de la imagen
        if len(img.shape) == 2:
            img2 = cv2.cvtColor(img, cv2.COLOR_GRAY2BGR) # Transformamos a 3 Canales
            final[:img.shape[0],last_x:img.shape[1]+last_x,:] = img2
        else: 
            final[:img.shape[0],last_x:img.shape[1]+last_x,:] = img
        last_x += img.shape[1]
        
    return final


"""
    Ejercicio 4
    @vpx    : List of pixels with color grey scale (x, y, color grey)
    @srcimg : Path image 
"""
def modificaMP(img, vpx):

    if len(img.shape) == 2:    
        for px in vpx:
            img[px[1], px[0]] = px[2]
    else: 
        for px in vpx:
            img[px[1], px[0]] = (px[2], px[2], px[2])
        
    return img

"""
    Ejercicio 5
    @ imgs: Array 2D [Img, Title]
"""
def pintaMIV(imgs):
    
    fig, axes = plt.subplots(int(len(imgs)/3) + 1, 3); # Creamos matriz de plots 3 x 3n
    
    for i in range(0, len(imgs), 1):
        axe = axes[int(i / 3), int(i % 3)]
        axe.set_title(imgs[i][1]) # Añadimos títulos
        axe.xaxis.set_visible(False)
        axe.yaxis.set_visible(False)
        if len(imgs[i][0].shape) == 2:
            axe.imshow(imgs[i][0], cmap='gray') # Mapeamos a escala de grises
        else:
            img_src = cv2.cvtColor(imgs[i][0], cv2.COLOR_BGR2RGB) # Cambiamos canales
            axe.imshow(img_src)
    
    plt.show();

# Ejercicio 1
cv2.imshow('Ejericio 1', leeimagen("../images/logoOpenCV.jpg", cv2.IMREAD_GRAYSCALE))

# Ejercicio 2
cv2.imshow('Ejercicio 2', pintaI(leeimagen("../images/orapple.jpg", cv2.IMREAD_COLOR)))

# Ejercicio 3
cv2.imshow('Ejercicio 3', pintaMI([
    leeimagen("../images/dave.jpg"   , cv2.IMREAD_COLOR    ),
    leeimagen("../images/orapple.jpg", cv2.IMREAD_COLOR    ),
    leeimagen("../images/messi.jpg"  , cv2.IMREAD_COLOR    )
]))

# Ejercicio 4
cv2.imshow('Ejericio 5', modificaMP(
    leeimagen("../images/logoOpenCV.jpg", cv2.IMREAD_GRAYSCALE),
    [
     (101, 50, 255), (100, 100, 255)
    ]
))

# Ejercicio 5
#cv2.imshow('Ejercicio 3',
pintaMIV([
    [leeimagen("../images/dave.jpg"   , cv2.IMREAD_GRAYSCALE), "Dave"],
    [leeimagen("../images/orapple.jpg", cv2.IMREAD_COLOR), "Naranja Manzana"],
    [leeimagen("../images/messi.jpg"  , cv2.IMREAD_COLOR), "Messi"],
    [leeimagen("../images/messi.jpg"  , cv2.IMREAD_COLOR), "Messi"]
])

cv2.waitKey(0)
cv2.destroyAllWindows()