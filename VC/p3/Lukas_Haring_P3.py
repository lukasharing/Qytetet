# -*- coding: utf-8 -*-
# Práctica 0 Realizada por Lukas Häring García
import cv2
import numpy as np

import os
os.path.dirname(os.path.abspath(__file__))

"""
    UTILS
"""
# Read image path
def leeimagen(filename, flagColor):
    return cv2.imread(filename, flagColor)

# Sigma to Sobel Matriz length
def sigma2tam(sigma):
    return 2 * np.uint(sigma * 3) + 1;

def gaussianScaleSpace(img, n):
    # Get all scales
    scales = [img]
    # n + 1 scales (with the real image included)
    for i in range(n - 1):
        scales.append(cv2.pyrUp(cv2.pyrDown(scales[i])))
    
    return scales

# Ejercicio 1 Gaussian Blur and Get Deriv Kernels
img = leeimagen("../images/plane.bmp", cv2.IMREAD_GRAYSCALE)

# Ejercicio 1. Detectar puntos de Harris
num_scales = 2
scales = gaussianScaleSpace(img, num_scales)
scales = list(map(lambda scale: np.pad(scale, (1, 1), "constant"), scales))
height, width = img.shape

####  For each scale detect HARRIS POINTS
fHMs = []
for i in range(num_scales):
    
    # [λ1, λ2, (x1, y1 eigen vector λ1), (x2, y2 eigen vector λ2)]
    blockSize = 3 * (i + 1) # 3 x 3 Neighbourhood
    points = cv2.cornerEigenValsAndVecs(
        scales[i],
        blockSize = blockSize,
        ksize = sigma2tam(1.5), # Paper σ_i=1.5
        borderType = cv2.BORDER_CONSTANT
    )
    
    # f = (λ1 * λ2) / (λ1 + λ2)
    fHMs.append((points[:,:,0] * points[:,:,1]) / (points[:,:,0] + points[:,:,1]))

#### MAXIMUM SUPPRESSION
threshold = 10.
allmaximums = []
for k in range(len(fHMs)):
    maximums = []
    for j in range(1, height + 1):
        for i in range(1, width + 1):
            maximum = np.amax(fHMs[k][j - 1 : j + 2, i - 1 : i + 2])
            if fHMs[k][j, i] >= maximum and fHMs[k][j, i] > threshold:
                maximums.append([i, j, maximum, 1. / (k + 1.)])
    
    allmaximums.append(maximums)

if len(scales) > 1:
    interestpoints = []
    
    # Bottom Layer
    #interestbottom = []
    for maximum in allmaximums[0]:
        cx = maximum[0]
        cy = maximum[1]
        # Get maximum from top layer only
        max_top = np.amax(fHMs[1][cy - 1 : cy + 2, cx - 1 : cx + 2])
        if maximum[2] > max_top:
            interestpoints.append(cv2.KeyPoint(cx, cy, maximum[3], 0))
    
    #interestpoints.append(interestbottom)
    
    # Middle Layers
    for k in range(1, num_scales - 1):
        #interestmiddle = []
        for maximum in allmaximums[k]:
            cx = maximum[0]
            cy = maximum[1]
        
            max_b = np.amax(fHMs[i - 1][cy - 1 : cy + 2, cx - 1 : cx + 2])
            max_t = np.amax(fHMs[i + 1][cy - 1 : cy + 2, cx - 1 : cx + 2])
            
            if (maximum[2] > max_b and maximum[2] > max_t):
                interestpoints.append(cv2.KeyPoint(cx, cy, maximum[3], 0))
        
        #interestpoints.append(interestmiddle)
    
    
    # Top Layer
    #interesttop = []
    for maximum in allmaximums[num_scales - 1]:
        cx = maximum[0]
        cy = maximum[1]
        # Get maximum from top layer only
        max_bottom = np.amax(fHMs[num_scales - 2][cy - 1 : cy + 2, cx - 1 : cx + 2])
        if maximum[2] > max_bottom:
            interestpoints.append(cv2.KeyPoint(cx, cy, maximum[3], 0))
    
    #interestpoints.append(interesttop)
    
    
    print(interestpoints)
    
    # np.arctan(gy, gx)
    
output = cv2.drawKeypoints(
    img,
    interestpoints,
    outImage = np.array([]),
    color = (0, 0, 255)
)


cv2.imshow("Hybrid Image", output)
cv2.waitKey(0)
cv2.destroyAllWindows()


# print(values)