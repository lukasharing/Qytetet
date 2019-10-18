# -*- coding: utf-8 -*-
# Práctica 0 Realizada por Lukas Häring García
import cv2
import numpy as np

import os
os.path.dirname(os.path.abspath(__file__))

def sigma2tam(sigma):
    return 2 * np.uint(sigma * 3) + 1;

"""
    Ejercicio 1
    @filename : Existing File Path
    @flagColor: cv2.IMREAD_GRAYSCALE or cv2.IMREAD_COLOR
"""
def leeimagen(filename, flagColor):
    return cv2.imread(filename, flagColor)

def Convolution_1D(img, fil, padding = "constant"):
    # Matrix transpose and get the first row (vector)
    fil = fil.T[0]
    size_mask = len(fil)
    
    height, width = img.shape
    # mask of 000000...000000m1m2m3...mn0000000...00000000
    num_zeros = width - np.uint8((size_mask + 1) / 2)
    size = 2 * num_zeros + size_mask
    
    mask = np.zeros((1, size), dtype=float)
    
    mask[0, num_zeros : num_zeros + size_mask] = fil
    
    result = np.zeros((height, width), dtype=float)
    for j in range(0, height):
        for i in range(0, width):
            offset = width - 1 - i
            result[j, i] = np.dot(mask[0, offset : offset + width], img[j])     
    
    return result
    

def normalize(img):
    
    wd = img.shape[1]
    hg = img.shape[0]
    minm = np.amin(img)
    maxm = np.amax(img)

    img.astype(float)

    for i in range(0, hg, 1):
        for j in range(0, wd, 1):
            img[i,j] = (img[i,j] - minm) / (maxm - minm);
 
    return np.uint8(img * 255);

def conv_1D_1D(img, fil, padding = "constant"):
    
    # Convolution 1
    res = Convolution_1D(img, fil[0], padding)
    
    res = np.transpose(res)
    res = Convolution_1D(res, fil[1], padding)
    res = np.transpose(res)    
    
    return res

def convolution_img(img, fil):
    return normalize(conv_1D_1D(img, fil))

def laplacian_gauss(img, sigma):
    
    tam = sigma2tam(sigma)
    
    # Apply Gaussian Blur
    img = cv2.GaussianBlur(img, (0, 0), sigma, sigma, cv2.BORDER_CONSTANT)
    
    # Kernels derivation
    d2x = cv2.getDerivKernels(2, 0, tam)
    d2y = cv2.getDerivKernels(0, 2, tam)
    
    # d^2x
    conv_x = conv_1D_1D(img, d2x)
    # d^2y
    conv_y = conv_1D_1D(img, d2y)
    
    # d^2x + d^2y
    laplacian = conv_y + conv_x
    
    """
    sigma^2(d^2x + d^2y)
    
    If we have very low values and not too high values, we can shift all the colors to the positive side,
    so we get that the zeros remain zeros and the negative values get closer to 1 (because they are higher than the positive).
    and the positive go to zero because by normalizing these are lower to the abs(negative)
    """
    return normalize((sigma * sigma) * abs(laplacian))
    
# GAUSS PIRAMID
def gauss_pyramid(img, max_level, sigma):
    # Create Image 1.5 * Size
    img_res = np.zeros((img.shape[0] + 100, np.uint(1.5 * img.shape[1] + .5)), dtype=np.uint8)
    #Put First Image (Not scaled in position)
    img_res[100:100 + img.shape[0],:img.shape[1]] = img
    wdt_scl = np.uint(img.shape[1] / img.shape[0] * 90)
    # Put Image Preview
    img_res[:90,:wdt_scl] = cv2.resize(img, (wdt_scl, 90), interpolation = cv2.INTER_LINEAR)
    img_res = gauss_pyramid_helper(img_res, img, img.shape[1] - 1, 100, 1, max_level, sigma)
    return img_res

def GSharp(img, hw, hh, sigma):
    smoothed = cv2.GaussianBlur(img, (0, 0), sigma, sigma, cv2.BORDER_CONSTANT)
    return cv2.resize(smoothed, (hw, hh), interpolation = cv2.INTER_LINEAR)
    

def gauss_pyramid_helper(dest, sourc, x_displacement, y_displacement, level, max_level, sigma):    
    # Apply Gaussian Blur and Resize it to the half
    filtering = GSharp(sourc, np.uint(sourc.shape[1] * 0.5), np.uint(sourc.shape[0] * 0.5), sigma);
    
    # Put Image Preview
    wdt_scl = np.uint(sourc.shape[1] * 90 / sourc.shape[0])
    dsx = wdt_scl * level
    dest[:90, dsx:dsx + wdt_scl] = cv2.resize(filtering, (wdt_scl, 90), interpolation = cv2.INTER_LINEAR)
    
    # Put Image Resized and displace the y coord to the next image
    dest[y_displacement:y_displacement + filtering.shape[0], x_displacement:x_displacement + filtering.shape[1]] = filtering
    y_displacement += np.uint(sourc.shape[0] * 0.5)
    
    # If level == Level => Return Image
    if level == max_level:
        return dest
    else:
        return gauss_pyramid_helper(dest, filtering, x_displacement, y_displacement - 1, level + 1, max_level, sigma)

# LAPLACE PIRAMID
def laplacian_pyramid(img, max_level, sigma):
    # Create Image 1.5 * Size
    img_res = np.zeros((img.shape[0] + 100, np.uint(1.5 * img.shape[1])), dtype=np.uint8)
    
    # Apply Reduction
    filtering = GSharp(img, np.uint(img.shape[1] * 0.5), np.uint(img.shape[0] * 0.5), sigma)
    
    # Transformamos en una matrix de floats
    result = np.zeros((img.shape[0], img.shape[1]), dtype=float)
    result[:,:] = img # Realizamos una copia
    result = normalize(result - GSharp(filtering, img.shape[1], img.shape[0], sigma))
    
    #Put First Image (Not scaled in position)
    img_res[100:100 + result.shape[0],:result.shape[1]] = result
    wdt_scl = np.uint(result.shape[1] / result.shape[0] * 90)
    # Put Image Preview
    img_res[:90,:wdt_scl] = cv2.resize(result, (wdt_scl, 90), interpolation = cv2.INTER_LINEAR)
    img_res = laplacian_pyramid_helper(img_res, filtering, img.shape[1] - 1, 100, 1, max_level, sigma)
    return img_res

def laplacian_pyramid_helper(dest, sourc, x_displacement, y_displacement, level, max_level, sigma):
    
    # Apply Reduction
    filtering = GSharp(sourc, np.uint(sourc.shape[1] * 0.5), np.uint(sourc.shape[0] * 0.5), sigma)
    
    # Transformamos en una matrix de floats
    result = np.zeros((sourc.shape[0], sourc.shape[1]), dtype=float)
    result[:,:] = sourc # Realizamos una copia
    result = normalize(result - GSharp(filtering, sourc.shape[1], sourc.shape[0], sigma))
    
    # Put Image Preview
    wdt_scl = np.uint(sourc.shape[1] / sourc.shape[0] * 90)
    dsx = wdt_scl * level
    dest[:90, dsx:dsx + wdt_scl] = cv2.resize(result, (wdt_scl, 90), interpolation = cv2.INTER_CUBIC)
    
    # Put Image Resized and displace the y coord to the next image
    dest[y_displacement:y_displacement + result.shape[0], x_displacement:x_displacement + result.shape[1]] = result
    y_displacement += result.shape[0]
    
    # If level == Level => Return Image
    if level == max_level:
        return dest
    else:
        return laplacian_pyramid_helper(dest, filtering, x_displacement, y_displacement - 1, level + 1, max_level, sigma)

def blob_detection(img, num_steps, sigma, scale, threshold):
    
    assert scale >= 1.20 and scale <= 1.40, "El coeficiente debe estar entre [1.20, 1.40]"
    
    # Because we need an image to render the circles with colours we just create a 3-channel image
    result = np.zeros((img.shape[0], img.shape[1], 3), dtype = np.uint8)
    result[:,:,0] = img
    result[:,:,1] = img
    result[:,:,2] = img

    # Podemos escalar Convolucionando sigma * sigma => la escala será sqrt(sigma * sigma + sigma * sigma) = sigma * sqrt(2)

    # Go for each level (LOD)    
    for i in range(0, num_steps):
        level = laplacian_gauss(img, sigma)
        pixels = []
        
        # Make a copy
        img = np.pad(img, (1, 1), "constant")
        
        # Non-maximum Supression
        for j in range(1, img.shape[0] - 1):
            for k in range(1, img.shape[1] - 1):
                
                # Get Maximum of neighbourhood
                maximum = np.amax(level[j - 1 : j + 2, k - 1 : k + 2])
                
                # Check if the current pixel is maximal, then add it to the list
                if img[j, k] == maximum:
                    pixels.append([j - 1, k - 1, maximum]) # Shift values because of the padding        
        
        # This is fast to check by calculating the second order derivate of the gaussian and comparing it to zero
        # sigma = radius / sqrt(2) => radius = sigma * sqrt(2)
        radius = np.uint(sigma * np.sqrt(2))
        
        # iterate over pixels to highpass filter
        for m in range(0, len(pixels)):
            if pixels[m][2] > threshold: # Check if the maximum is higher than a given threshold
                cv2.circle(result, (pixels[m][1], pixels[m][0]), radius, (0, 0, 255)) # Draw circle
        
        sigma *= scale

    return result


def hybrid_image(img1, img2, sigma):
    
    width = img1.shape[1]
    height = img1.shape[0]
    
    img1_low = cv2.GaussianBlur(img1, (0, 0), sigma, sigma, cv2.BORDER_CONSTANT);
    img2_low = cv2.GaussianBlur(img2, (0, 0), sigma, sigma, cv2.BORDER_CONSTANT);
    
    if len(img1.shape) == 2:  # 1 Channel
        combination = np.zeros((height, width), dtype=float);
        combination[:,:] = img2_low + (img1 - img1_low)
    else: # 3 Channels
        combination = np.zeros((height, width, 3), dtype=float);
        combination[:,:,:] = img2_low + (img1 - img1_low)
    
    return normalize(combination)

"""  BONUS 1
Descomposición 2D a 2 de 1D

Suppose A can be decomposite into 3 matrix
A = U V W^t
U^-1 = U^t (U is Orthogonal)
W^-1 = W^t (W is Orthogonal)

For U
AA^t = (U V W^t)(U V W^t)^t = (U V W^t) (W^t^t V^t U^t) = U V (W^t W) V^t U^t
AA^t = U (V V^t) U^t
U is the eigenvectors matrix of AA^t

For W
A^tA = (U V W^t)^(U V W^t) = (W^t^t V^t U^t) (U V W^t) = W V (U^t U) V^t W^t
A^tA = W (V V^t) W^t
W is the eigenvectors matrix of AA^t


VV^t = Diagonal of Eigenvalues of AA^t or A^tA (Eigenvalues of A = Eigenvalues of A^t)
So V = Digagonal of sqrt(Eigenvalues) of AA^t=A^tA , or eigenvalues of A

Si A es simétrica (A = A^t)
Entonces

A^2 = W (V V^t) W^t
A^2 = U (V V^t) U^t

Because Rank 1, then it has to have just one eigen value

    [u1 u4 u7]   [v1 0 0]   [w1 w2 w3]
A = [u2 u5 u8] x [0  0 0] x [w4 w5 w6]
    [u3 u6 u9]   [0  0 0]   [w7 w8 w9]
    
    [u1v1 0 0]   [w1 w2 w3]
A = [u2v1 0 0] x [w4 w5 w6]
    [u3v1 0 0]   [w7 w8 w9]

Note the result is equal to 1 column of U escaled by v1

    [u1v1w1 u1v1w2 u1v1w3]
A = [u2v1w1 u2v1w2 u2v1w3]
    [u3v1w1 u3v1w2 u3v1w3]

Note the result is equal to v1 * col(U, 0) * row(w, 0)^t

"""

def Conv2D_2_1_D(img, A):
    assert np.linalg.matrix_rank(A) == 1, "El rango debe ser 1"
    
    # Get A^t*A and A*A^t
    AAT = np.matmul(A, np.transpose(A))
    ATA = np.matmul(np.transpose(A), A)
    
    # Get eigenvectors matrix
    U = np.linalg.eig(AAT)[1]
    W = np.transpose(np.linalg.eig(ATA)[1])
    
    # Get only used row / column
    col_U = np.transpose([np.transpose(U)[0]])
    row_W = np.transpose([W[0]])
    
    # Get first eigenvalue
    scalar_V = np.sqrt(np.linalg.eig(AAT)[0][0])    
    
    # No sé por qué me dan los valores invertidos, pero creo que puede ser por la raiz del escalar
    # Por eso multiplico -1 por el escalar
    return normalize(-scalar_V * conv_1D_1D(img, [col_U, row_W]))


# Ejercicio 1 Gaussian Blur and Get Deriv Kernels
img = leeimagen("../images/cat.bmp", cv2.IMREAD_GRAYSCALE)

sigma = 5
tam = sigma2tam(sigma);


# Ejercicio 1.A - Gaussiana

kernel = cv2.getGaussianKernel(tam, sigma)
kernel = [kernel, kernel]

gaussian_cv2 = cv2.GaussianBlur(img, (0, 0), sigma, sigma, cv2.BORDER_CONSTANT) 
gaussian_mine = convolution_img(img, kernel)

cv2.imshow("Gaussian Smoothing", np.hstack((gaussian_cv2, gaussian_mine)))
cv2.waitKey(0)
cv2.destroyAllWindows()
"""
# Ejercicio 1.B - Laplaciana d^2(img)/d^2x + d^2(img)/d^2y
cv2.imshow("Laplacian", laplacian_gauss(img, sigma))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Ejercicio 2.A - Gauss Piramid
cv2.imshow("Gauss Piramid", gauss_pyramid(img, 4, 1.))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Ejercicio 2.B - Laplacian Piramid
cv2.imshow("Laplacian Piramid", laplacian_pyramid(img, 4, 0.4))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Ejercicio 2.C
cv2.imshow("Blob detection", blob_detection(img, 5, sigma, 1.2, 10.))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Ejercicio 3.1
img1 = leeimagen("../images/einstein.bmp", cv2.IMREAD_GRAYSCALE)
img2 = leeimagen("../images/marilyn.bmp", cv2.IMREAD_GRAYSCALE)
cv2.imshow("Hybrid Image", np.hstack((img1, img2, hybrid_image(img1, img2, 5))))
cv2.waitKey(0)
cv2.destroyAllWindows()

img1 = leeimagen("../images/dog.bmp", cv2.IMREAD_GRAYSCALE)
img2 = leeimagen("../images/cat.bmp", cv2.IMREAD_GRAYSCALE)
hybrid_dog_cat = hybrid_image(img1, img2, 5)
cv2.imshow("Hybrid Image", np.hstack((img1, img2, hybrid_dog_cat)))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Bonus 2 and one of the 3. 2.
img1 = leeimagen("../images/plane.bmp", cv2.IMREAD_COLOR)
img2 = leeimagen("../images/bird.bmp", cv2.IMREAD_COLOR)
hybrid_plane_bird = hybrid_image(img1, img2, 5)
cv2.imshow("Hybrid Image", np.hstack((img1, img2, hybrid_plane_bird)))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Ejercicio 3. 3
cv2.imshow("Laplacian Piramid", gauss_pyramid(hybrid_dog_cat, 4, 0.4))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Bonus 1
fl = np.transpose([[1, 2, 1]])
fk = np.transpose([[-1, 1, -1]])
mtx_fil = Conv2D_2_1_D(img, np.matmul(fk, np.transpose(fl)))
vec_fil = convolution_img(img, [fl, fk])


cv2.imshow("Bonus 1", np.hstack((mtx_fil, vec_fil)))
cv2.waitKey(0)
cv2.destroyAllWindows()
"""