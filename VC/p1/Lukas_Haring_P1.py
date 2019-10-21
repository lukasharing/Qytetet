# -*- coding: utf-8 -*-
# Práctica 0 Realizada por Lukas Häring García
import cv2
import numpy as np

import os
os.path.dirname(os.path.abspath(__file__))

"""
    UTILS
"""
# Sigma to Size
def sigma2tam(sigma):
    return 2 * np.uint(sigma * 3) + 1;

# Read image path
def leeimagen(filename, flagColor):
    return cv2.imread(filename, flagColor)

# Normalize Kernels into the interval [0, 1]
def normalizeKernels(kernels):
    results = []
    for i in range(len(kernels)):
        result = kernels[i].T
        result /= np.sum(abs(kernels[i]))
        results.append(np.transpose(result))
    
    return (results[0], results[1])

# Normalize a matrix into the interval [0, 255] integer
def normalize(img, outer = False):
    
    minm = np.amin(img)
    maxm = np.amax(img)
    
    if outer: # Normalize all values
        if len(img.shape) == 2:
            for i in range(0, img.shape[0], 1): # Grey scale Image
                for j in range(0, img.shape[1], 1):
                    img[i, j] = (img[i, j] - minm) / (maxm - minm) * 255
        else:
            for i in range(0, img.shape[0], 1): # Multiple channel Image
                for j in range(0, img.shape[1], 1):
                    img[i, j, :] = (img[i, j, :] - minm) / (maxm - minm) * 255
    else: # Normalize exterior Values
        if len(img.shape) == 2: # Grey scale Image
            for i in range(0, img.shape[0], 1):
                for j in range(0, img.shape[1], 1):
                    if img[i, j] < 0 or img[i, j] >= 255: # Map only negative values
                        img[i, j] = (img[i, j] - minm) / (maxm - minm) * 255
        else:
            for n in range(0, img.shape[2]): # Multiple channel Image
                for i in range(0, img.shape[0], 1):
                    for j in range(0, img.shape[1], 1):
                        if img[i, j, n] < 0 or img[i, j, n] >= 255: # Map only negative values
                            img[i, j, n] = (img[i, j, n] - minm) / (maxm - minm) * 255
    
    return img.astype(np.uint8)

# Returns Gaussian Blurr and Resized Image
def GSharp(img, hw, hh, sigma, border = cv2.BORDER_CONSTANT):
    smoothed = cv2.GaussianBlur(img, (0, 0), sigma, sigma, border)
    return cv2.resize(smoothed, (hw, hh), interpolation = cv2.INTER_LINEAR)


"""
    Ejercicio 1
    @filename : Existing File Path
    @flagColor: cv2.IMREAD_GRAYSCALE or cv2.IMREAD_COLOR
"""
def Convolution_1D(img, fil, border = cv2.BORDER_CONSTANT):
    # old
    old_height, old_width = img.shape
    
    # Matrix transpose and get the first row (vector)
    fil = fil.T[0]
    size_mask = len(fil)
    
    # Add Padding
    padding = np.uint8((size_mask + 1) / 2)
    img = cv2.copyMakeBorder(img, padding - 1, padding - 1, padding - 1, padding - 1, border)
    
    height, width = img.shape
    # mask of 000000...000000m1m2m3...mn0000000...00000000
    num_zeros = width - padding
    size = 2 * num_zeros + size_mask
    
    mask = np.zeros((1, size), dtype=float)
    
    mask[0, num_zeros : num_zeros + size_mask] = fil
    
    result = np.zeros((height, width), dtype=float)
    
    # Convolute each row
    for j in range(0, height):
        for i in range(0, width):
            offset = num_zeros + np.uint(size_mask * .5) - i
            # Apply Mask x1 * v1 + x2 * v2 + ... (The same as (R * M^t)[0])
            result[j, i] = np.dot(mask[0, offset : offset + width], img[j])     
    
    # Remove padding
    result = result[padding - 1: padding - 1 + old_height, padding - 1: padding  - 1 + old_width]
    
    return result

def conv_1D_1D(img, fil, border = cv2.BORDER_CONSTANT):
    # Convolution 1
    res = Convolution_1D(img, fil[0], border)
    
    res = np.transpose(res)
    res = Convolution_1D(res, fil[1], border)
    res = np.transpose(res)    
    
    return res

def laplacian_gauss(img, sigma, abs_res = True, border = cv2.BORDER_CONSTANT):
    
    tam = sigma2tam(sigma)
    
    # Apply Gaussian Blur
    img = cv2.GaussianBlur(img, None, sigma, sigma, border)
    
    # Kernels derivation
    d2x = cv2.getDerivKernels(2, 0, tam)
    d2y = cv2.getDerivKernels(0, 2, tam)
    
    # d^2x and d^2y
    conv_x = conv_1D_1D(img, d2x, border)
    conv_y = conv_1D_1D(img, d2y, border)
    
    # d^2x + d^2y
    laplacian = conv_y + conv_x
    
    """
    sigma^2(d^2x + d^2y)
    
    If we have very low values and not too high values, we can shift all the colors to the positive side,
    so we get that the zeros remain zeros and the negative values get closer to 1 (because they are higher than the positive).
    and the positive go to zero because by normalizing these are lower to the abs(negative)
    """
    
    return (sigma * sigma) * (abs(laplacian) if abs_res else laplacian)
    
# GAUSS PIRAMID
def gauss_pyramid(img, max_level, sigma, border = cv2.BORDER_CONSTANT):
    
    # Create Image 1.5 * Size
    img_res = np.zeros((img.shape[0] + 100, np.uint(1.5 * img.shape[1] + .5)), dtype=np.uint8)
    
    #Put First Image (Not scaled in position)
    img_res[100:100 + img.shape[0],:img.shape[1]] = img
    wdt_scl = np.uint(img.shape[1] / img.shape[0] * 90)
    
    # Put Image Preview
    img_res[:90,:wdt_scl] = cv2.resize(img, (wdt_scl, 90), interpolation = cv2.INTER_NEAREST)
    img_res = gauss_pyramid_helper(img_res, img, img.shape[1] - 1, 100, 1, max_level, sigma)
    
    return img_res    

def gauss_pyramid_helper(dest, sourc, x_displacement, y_displacement, level, max_level, sigma):    
    # Apply Gaussian Blur and Resize it to the half
    filtering = GSharp(sourc, np.uint(sourc.shape[1] * 0.5), np.uint(sourc.shape[0] * 0.5), sigma);
    
    # Put Image Preview
    wdt_scl = np.uint(sourc.shape[1] * 90 / sourc.shape[0])
    dsx = wdt_scl * level
    dest[:90, dsx:dsx + wdt_scl] = cv2.resize(filtering, (wdt_scl, 90), interpolation = cv2.INTER_NEAREST)
    
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
    img = img.astype(float)
    
    # Apply Reduction
    filtering = GSharp(img, np.uint(img.shape[1] * 0.5), np.uint(img.shape[0] * 0.5), sigma)
    
    # Transformamos en una matrix de floats
    result = np.zeros((img.shape[0], img.shape[1]), dtype=float)
    result[:,:] = normalize(img - GSharp(filtering, img.shape[1], img.shape[0], sigma), True)
    
    #Put First Image (Not scaled in position)
    img_res[100:100 + result.shape[0],:result.shape[1]] = result
    wdt_scl = np.uint(result.shape[1] / result.shape[0] * 90)
    
    # Put Image Preview
    img_res[:90,:wdt_scl] = cv2.resize(result, (wdt_scl, 90), interpolation = cv2.INTER_NEAREST)
    img_res = laplacian_pyramid_helper(img_res, filtering, img.shape[1] - 1, 100, 1, max_level, sigma)
    
    return img_res

def laplacian_pyramid_helper(dest, sourc, x_displacement, y_displacement, level, max_level, sigma):
    
    # Apply Reduction
    filtering = GSharp(sourc, np.uint(sourc.shape[1] * 0.5), np.uint(sourc.shape[0] * 0.5), sigma)
    
    # Transformamos en una matrix de floats
    result = np.zeros((sourc.shape[0], sourc.shape[1]), dtype=float)
    result[:,:] = normalize(sourc - GSharp(filtering, sourc.shape[1], sourc.shape[0], sigma), True)
    
    # Put Image Preview
    wdt_scl = np.uint(sourc.shape[1] / sourc.shape[0] * 90)
    dsx = wdt_scl * level
    dest[:90, dsx:dsx + wdt_scl] = cv2.resize(result, (wdt_scl, 90), interpolation = cv2.INTER_NEAREST)
    
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

    #NOTA: Podemos escalar Convolucionando sigma * sigma => la escala será sqrt(sigma * sigma + sigma * sigma) = sigma * sqrt(2)
    
    # Go for each level (LOD)
    height, width = img.shape
    
    levels = []
    maximals = []
    for i in range(0, num_steps):
        
        # Laplacian that will be converted to maximal supressed
        laplacian = normalize(laplacian_gauss(img, sigma * scale ** (i + 1)))
        
        # Make a copy with padding
        supression = np.pad(laplacian, (1, 1), "constant")
        
        # Non-maximum Supression
        maximal = []
        for j in range(1, height + 1):
            for k in range(1, width + 1):
                # Get Maximum of neighbourhood
                maximum = np.amax(supression[j - 1 : j + 2, k - 1 : k + 2])
                
                # Check if the current pixel is maximal, then add it to the list
                # Create Maximal supressed Image
                # Shift values because of the padding
                # High Pass Filter by a threshold
                if supression[j, k] == maximum and supression[j, k] > threshold:
                    laplacian[j - 1, k - 1] = maximum
                    maximal.append([j - 1, k - 1, maximum])
                else:
                    laplacian[j - 1, k - 1] = 0
        
        # Add maximal list
        maximals.append(maximal)
        # Add maximal supression image
        levels.append(laplacian)

    if num_steps > 1:

        bottom = maximals[0]
        # Bottom Layer Draw Circles
        radius = np.uint(sigma * np.sqrt(2))
        for j in range(len(bottom)):
            if bottom[j][2] > levels[1][bottom[j][0]][bottom[j][1]]:
                cv2.circle(result, (bottom[j][1], bottom[j][0]), radius, (0, 0, 255))
        
        # Top Layer Draw Circle
        top = maximals[num_steps - 1]
        radius = np.uint((sigma * scale ** num_steps) * np.sqrt(2))
        for j in range(len(top)):
            if top[j][2] > levels[num_steps - 2][top[j][0]][top[j][1]]:
                cv2.circle(result, (top[j][1], top[j][0]), radius, (0, 0, 255))
            
        # Middle layers Draw Circle
        for i in range(1, num_steps - 1):
            radius = np.uint((sigma * scale ** i) * np.sqrt(2))  
            for j in range(0, len(maximals[i])):
                bottomlayer = levels[i - 1][maximals[i][j][0]][maximals[i][j][1]]
                toplayer = levels[i + 1][maximals[i][j][0]][maximals[i][j][1]]
                if maximals[i][j][2] > bottomlayer and maximals[i][j][2] > toplayer:
                    cv2.circle(result, (maximals[i][j][1], maximals[i][j][0]), radius, (0, 0, 255))

    return result

# Returns Low of Img1 And High Of Image 2
def low_high(img1, img2, asigma, bsigma = 2., border = cv2.BORDER_CONSTANT):
    
    low_img1 = cv2.GaussianBlur(img1, (0, 0), asigma, asigma, border);
    low_img2 = cv2.GaussianBlur(img2, (0, 0), bsigma, bsigma, border);
    
    return (low_img1, img2 - low_img2)

def low_high_hybrid(img1, img2, asigma, bsigma = 2., border = cv2.BORDER_CONSTANT):
    
    img1 = img1.astype(float)
    img2 = img2.astype(float)
    
    low, high = low_high(img1, img2, asigma, bsigma, border)
    hybrid = low + high
    
    width = img1.shape[1]
    
    low = normalize(low, True)
    high = normalize(high, True)
    hybrid = normalize(hybrid, True)
    
    if len(img1.shape) == 2:
        result = np.zeros((img1.shape[0], 3 * width), dtype=np.uint8)
        result[:, width * 0 : width * 1] = low
        result[:, width * 1 : width * 2] = high
        result[:, width * 2 : width * 3] = hybrid
        return result
    else:
        result = np.zeros((img1.shape[0], 3 * width, 3), dtype=np.uint8)
        for i in range(3):
            result[:, width * 0 : width * 1, i] = low[:, :, i]
            result[:, width * 1 : width * 2, i] = high[:, :, i]
            result[:, width * 2 : width * 3, i] = hybrid[:, :, i]
        return result
    

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
    
    img = img.astype(float)
    
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
    return -scalar_V * conv_1D_1D(img, [col_U, row_W])


# Ejercicio 1 Gaussian Blur and Get Deriv Kernels
img = leeimagen("../images/messi.jpg", cv2.IMREAD_GRAYSCALE)

sigma = 1
tam = sigma2tam(sigma)

# Ejercicio 1.A - Gaussiana

kernel = cv2.getGaussianKernel(tam, sigma)
kernel = [kernel, kernel]
"""
gaussian_2d_cv2 = cv2.GaussianBlur(img, None, sigma, sigma, cv2.BORDER_REPLICATE)
gaussian_2_1d = normalize(conv_1D_1D(img, kernel, cv2.BORDER_REPLICATE))

#np.hstack((gaussian_2d_cv2, ))
cv2.imshow("Gaussian Smoothing", np.hstack((gaussian_2d_cv2, gaussian_2_1d)))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Ejercicio 1.B - Laplaciana d^2(img)/d^2x + d^2(img)/d^2y
laplacian = normalize(laplacian_gauss(img, sigma, False, cv2.BORDER_WRAP), True)
laplacian_abs = normalize(laplacian_gauss(img, sigma, True, cv2.BORDER_WRAP), True)
cv2.imshow("Laplacian", np.hstack((laplacian, laplacian_abs)))
cv2.waitKey(0)
cv2.destroyAllWindows()

"""
# Ejercicio 2.A - Gauss Piramid
cv2.imshow("Gauss Piramid", gauss_pyramid(img, 4, sigma, cv2.BORDER_CONSTANT))
cv2.waitKey(0)
cv2.destroyAllWindows()

"""
# Ejercicio 2.B - Laplacian Piramid
cv2.imshow("Laplacian Piramid", laplacian_pyramid(img, 4, sigma))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Ejercicio 2.C
cv2.imshow("Blob detection", blob_detection(img, 3, sigma, 1.2, 20.))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Ejercicio 3.1
img1 = leeimagen("../images/einstein.bmp", cv2.IMREAD_GRAYSCALE)
img2 = leeimagen("../images/marilyn.bmp", cv2.IMREAD_GRAYSCALE)
cv2.imshow("Hybrid Image", low_high_hybrid(img1, img2, 2.))
cv2.waitKey(0)
cv2.destroyAllWindows()

img1 = leeimagen("../images/dog.bmp", cv2.IMREAD_GRAYSCALE)
img2 = leeimagen("../images/cat.bmp", cv2.IMREAD_GRAYSCALE)
cv2.imshow("Hybrid Image", low_high_hybrid(img1, img2, 3.))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Bonus 3. 2.
img1 = leeimagen("../images/plane.bmp", cv2.IMREAD_COLOR)
img2 = leeimagen("../images/bird.bmp", cv2.IMREAD_COLOR)
cv2.imshow("Hybrid Image", low_high_hybrid(img1, img2, 2.5))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Ejercicio 3. 3
img1 = leeimagen("../images/submarine.bmp", cv2.IMREAD_GRAYSCALE)
img2 = leeimagen("../images/fish.bmp", cv2.IMREAD_GRAYSCALE)
low, high = low_high(img1, img2, 2.5)
hybrid = normalize(low + high)
cv2.imshow("Laplacian Piramid", gauss_pyramid(hybrid, 4, sigma))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Bonus 1
fl = np.transpose([[1, 2, 1]])
fk = np.transpose([[-1, 1, -1]])
mtx_fil = normalize(Conv2D_2_1_D(img, np.matmul(fk, np.transpose(fl))))
vec_fil = normalize(conv_1D_1D(img, [fl, fk]))

cv2.imshow("Bonus 1", np.hstack((mtx_fil, vec_fil)))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Bonus 3
img1 = leeimagen("../images/fish-1.jpg", cv2.IMREAD_COLOR)
img2 = leeimagen("../images/fish-2.jpg", cv2.IMREAD_COLOR)
cv2.imshow("Hybrid Image", low_high_hybrid(img1, img2, 1.5, 3.5))
cv2.waitKey(0)
cv2.destroyAllWindows()
"""