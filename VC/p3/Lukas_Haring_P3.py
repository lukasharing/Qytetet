# -*- coding: utf-8 -*-
# Práctica 0 Realizada por Lukas Häring García
import cv2
import numpy as np

import os
os.path.dirname(os.path.abspath(__file__))

# Sigma to Sobel Matriz length
def sigma2tam(sigma):
    return 2 * np.uint(sigma * 3) + 1;

# Returns the image with a size of a power of two (the reminder is a padding of 0)
def padding_image(img):
    
    width, height = img.shape
    # Find nearest power of two to fit the image
    pow2_x = np.ceil(np.log2(width))
    pow2_y = np.ceil(np.log2(height))
    # Make it square
    max_pow2 = np.maximum(pow2_x, pow2_y)
    
    # Size of the image
    image_size = int(np.power(2, max_pow2))
    
    # Calculate offset
    new_image = np.zeros((image_size, image_size), dtype = img.dtype)
    
    # Put Image inside of the new
    new_image[:width,:height] = img
    
    return new_image

# Returns the gradient (x derivate and y derivate of the image)
def gradient(img, sigma):
    ksize = sigma2tam(sigma)
    
    # Calculate x and y derivate
    grad_x = cv2.Sobel(img, cv2.CV_64F, 1, 0, ksize = ksize)
    grad_y = cv2.Sobel(img, cv2.CV_64F, 0, 1, ksize = ksize)
    
    return list(zip(grad_x, grad_y))

# Returns next gaussian scale
def gaussian_scale(img, sigma):
    gs_image = cv2.GaussianBlur(img, None, sigma, sigma)
    gs_image = cv2.resize(gs_image, None, fx = 0.5, fy = 0.5, interpolation = cv2.INTER_NEAREST)
    return gs_image

# Gaussian Scale of the harmonic means
def harris_scales(img, num_scales, block_size):
    harris_scales = []
    # Paper constants
    integration_scale = 1.5 # Gaussian coefficient
    derivate_scale    = 1.0 # Derivate coefficient
    
    last_scale = img
    for i in range(num_scales):
        scale_block = (i + 1) * block_size
        
        # Eigen Values / Vectors of each pixel
        eigen_image = cv2.cornerEigenValsAndVecs(
            last_scale,
            blockSize = scale_block,
            ksize = sigma2tam(derivate_scale),
            borderType = cv2.BORDER_CONSTANT
        )
        
        # Harmonic Means
        harris_scales.append(
            eigen_image[:,:,0] * eigen_image[:,:,1] / (eigen_image[:,:,0] + eigen_image[:,:,1])
        )
        
        last_scale = gaussian_scale(last_scale, integration_scale)
        
    return harris_scales

def nonmaximun_scale_supression(scales):
    if len(scales) <= 1: return scales
    
    # Add Padding (1, 1) to all scales to suppress    
    # Non Maximum Supression For Each Scale
    for k in range(len(scales)):
        scale = scales[k]
        width, height = scales[k].shape
        for j in range(1, height):
            for i in range(1, width):
                px = scale[j, i]
                if px > 0:
                    maximum = np.amax(scale[j - 1 : j + 2, i - 1 : i + 2])
                    if px < maximum:
                        scale[j, i] = 0
    
    return scales

# Ejercicio 1 Gaussian Blur and Get Deriv Kernels
def ejercicio1(img):
    # Sizes image
    width, height = img.shape
    
    # Resize Image to a power of two
    rimg = padding_image(img)
    
    # Calculate Gradient
    igrad = gradient(rimg, 4.5)
    
    # Harris Gaussian Scales
    scales = harris_scales(rimg, 2, 4)
    
    # Maximum Scale Supression
    maximum = nonmaximun_scale_supression(scales)
    
    print(maximum[0])
    # Unpadding
    result = rimg[ :width, :height]
    
    return result

img = cv2.imread("./imagenes/yosemite7.jpg", cv2.IMREAD_GRAYSCALE)

cv2.imshow("Hybrid Image", ejercicio1(img))
cv2.waitKey(0)
cv2.destroyAllWindows()


"""
# Calcular ángulo, alisar fuerte 4.5,
# Calcular el gradiente 
# Calcular el ángulo arctan(y / x)


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
"""