# -*- coding: utf-8 -*-
# Práctica 3 Realizada por Lukas Häring García
import cv2
import numpy as np

import random

import os
os.path.dirname(os.path.abspath(__file__))

# Sigma to Sobel Matriz length
def sigma2tam(sigma):
    return 2 * np.uint(sigma * 3) + 1;
    
# Returns the image with a size of a power of two (the reminder is a padding of 0)
def padding_image(img):
    
    height, width = img.shape
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
    new_image[:height,:width] = img
    
    return new_image

# Returns gaussian scales
def gaussian_scales(img, num_scales):
    last_scale = img
    last_scale.astype(np.float32)
    
    scales = [last_scale]
    for i in range(num_scales - 1):
        last_scale = cv2.pyrDown(last_scale)
        scales.append(last_scale)
    
    return scales

# Scale of the harmonic means
def harris_scales(gaussian_scales, blockSize = 5.0):
    harris_scales = []
    # Paper constant
    derivate_scale    = 1.0 # Derivate coefficient
    
    num_scales = len(gaussian_scales)
    for i in range(num_scales):
        # Eigen Values / Vectors of each pixel
        eigen_image = cv2.cornerEigenValsAndVecs(
            gaussian_scales[i],
            blockSize = blockSize,
            ksize = sigma2tam(derivate_scale),
            borderType = cv2.BORDER_CONSTANT
        )
        
        # Harmonic Means for each pixel
        harris_scales.append(
            eigen_image[:,:,0] * eigen_image[:,:,1] / (eigen_image[:,:,0] + eigen_image[:,:,1])
        )
    return harris_scales

# Non-Maximum Supression and High Pass Supression
def maximums_harris_scale(scales, gradient_x, gradient_y, blockSize = 5., high_pass = 10.):
    
    maximums = []
    # Non Maximum Supression and High pass For Each Scale
    for k in range(len(scales)):
        scale_maximums = []
        scale = scales[k]
        width, height = scale.shape
        
        scalef = np.power(2.0, k)
        # For each pixel check if it is maximum of the neighbourhood and pass the harmonic ratio threshold and return Keypoint
        # Because the mean can be infinity, we have to check that it is a number
        for j in range(1, height):
            for i in range(1, width):
                px = scale[j, i]
                if not(np.isnan(px)) and px > high_pass:
                    nmax = np.amax(scale[j - 1 : j + 2, i - 1 : i + 2])
                    if px >= nmax:
                        # The angle of the gradient is the arctan(dy / dx) in each scale 
                        angle = np.degrees(np.arctan2(gradient_y[k][j][i], gradient_x[k][j][i]))
                        scale_maximums.append(cv2.KeyPoint(i * scalef, j * scalef, (k + 1) * blockSize * 2.0, angle))
        # Add Maximums found on the scale
        maximums.append(scale_maximums)
    
    return maximums

# Supress Points near border
def supress_near_border(original, points, border):
    
    result = []
    (height, width) = original.shape
    # For each maximum
    for i in range(len(points)):
        ppoints = []
        fwidth = width
        fheight = height
        # Check if it is near to a border, if so, remove
        for point in points[i]:
            if (point.pt[0] >= border and point.pt[0] < (fwidth - border)) and (point.pt[1] >= border and point.pt[1] < (fheight - border)):
                ppoints.append(point)
        result.append(ppoints)
        
    return result
        

def ejercicio1_d(img, maximums, random_points = 5):
    # Unroll the matrix 
    maximums = [j for sub in maximums for j in sub]
    
    ## Get SubPixel
    # Corner Matrix (n, 1, 2) https://stackoverflow.com/questions/40461469/how-to-use-opencv-cornersubpix-in-python
    corners_preprocess = np.zeros((random_points, 1, 2), dtype = np.float32);
    
    # List of N Random Integers with no repetition
    random_unique_ints = random.sample(range(len(maximums)), random_points)
    
    # Set X, Y for the pre-processing corners in the same scale
    for i in range(random_points):
        random_i = random_unique_ints[i]
        corners_preprocess[i][0][0] = maximums[random_i].pt[0] # set X
        corners_preprocess[i][0][1] = maximums[random_i].pt[1] # set Y
    
    # https://docs.opencv.org/2.4/doc/tutorials/features2d/trackingmotion/corner_subpixeles/corner_subpixeles.html
    cv2.cornerSubPix(
        img,                # Image
        corners_preprocess, # out Vector variable
        (5, 5),             # Window Size for Edges
        (-1, -1),           # "zero zone" to avoid possible singularities of the autocorrelation matrix
        (cv2.TERM_CRITERIA_EPS | cv2.TERM_CRITERIA_COUNT, 100, 0.01) # Error of 0.001 in 50 iterations
    )
    
    subpixels = corners_preprocess.reshape(-1, 2)
    
    # Result Image
    r = 4 # Radius from the center
    z = 5 # Zoom
    result = np.zeros(((2 * r + 1) * z, (2 * r + 1) * z * random_points, 3), dtype = np.uint8)
    
    for i in range(random_points):
        subregion = np.zeros((r * z, r * z, 3), dtype = np.uint8)
        # First Point
        rand = random_unique_ints[i]
        # Pixels
        mx = int(maximums[rand].pt[0])
        my = int(maximums[rand].pt[1])
        # Subpixels
        smx = subpixels[i][0]
        smy = subpixels[i][1]
        
        # dx, dy
        dx = smx - mx
        dy = smy - my
        

        # Take rubregion
        subregion = img[my - r: my + r + 1, mx - r : mx + r + 1]
        # Zoom it
        subregion = cv2.resize(subregion, None, fx = z, fy = z, interpolation = cv2.INTER_NEAREST)
        subregion = cv2.cvtColor(subregion, cv2.COLOR_GRAY2BGR)
        
        # Draw Pixel Corner
        cv2.drawMarker(
            subregion,
            (r * z, r * z),
            (0, 0, 255),
            markerType = cv2.MARKER_STAR, 
            markerSize = z,
            thickness = 1,
            line_type = cv2.LINE_AA
        )
        
        # Draw Sub Cornel
        cv2.drawMarker(
            subregion,
            (int((r + dx) * z), int((r + dy) * z)),
            (0, 255, 0),
            markerType = cv2.MARKER_DIAMOND, 
            markerSize = z,
            thickness = 1,
            line_type = cv2.LINE_AA
        )
        
        result[:, i * subregion.shape[0] : (i + 1) * subregion.shape[0], :] = subregion
    
    return result

def ejercicio1_c(img, maximums):
    num_scales = len(maximums)
    # Print Maximums per scale
    for i in range(num_scales):
        print("Escala {}: Total de {} puntos Harris".format(i, len(maximums[i])))
    print("Total de puntos: {} puntos Harris".format(sum(len(m) for m in maximums)))
    
    # Sizes image
    height, width = img.shape
    
    # Gaussian Scale
    gaussian_scale = gaussian_scales(img, num_scales)
    
    # Draw one next to another
    result = np.zeros((height, width * num_scales, 3), dtype = np.uint8)
    #result = img[:height,:width]
    
    for i in range(len(maximums)):
        scalef = np.power(2, i)
        resized = cv2.resize(gaussian_scale[i], None, fx = scalef, fy = scalef, interpolation = cv2.INTER_NEAREST)
        
        # Scale Keypoints
        keypoints = cv2.drawKeypoints(
            resized,
            maximums[i],
            outImage = np.uint8([]),
            color = (0, 0, 255),
            flags = cv2.DRAW_MATCHES_FLAGS_DRAW_RICH_KEYPOINTS    
        )
        result[:, width * i: width* (i + 1)] = keypoints[:height, :width]
    
    
    return result
    
# Ejercicio 1
def ejercicio1(img, num_scales = 3, high_pass = 10.):
    # Resize Image to a power of two
    rimg = padding_image(img)
    
    # Gradient
    ksize = sigma2tam(4.5) # Sigma for the orientation
    grad_x = cv2.Sobel(img, cv2.CV_64F, 1, 0, ksize = ksize)
    grad_y = cv2.Sobel(img, cv2.CV_64F, 0, 1, ksize = ksize)
    
    # Gradient and Gaussian Scales
    scale_gradient_x = gaussian_scales(grad_x, num_scales) # Derivate x Space Scale
    scale_gradient_y = gaussian_scales(grad_y, num_scales) # Derivate y Space Scale
    gaussian_scale = gaussian_scales(rimg, num_scales) # Paper constant Gaussian coefficient
    
    # Harris Gaussian Scales
    scales = harris_scales(gaussian_scale, 5) # Use window 5x5 to find corners
    # Maximum Scale Supression
    maximums = maximums_harris_scale(scales, scale_gradient_x, scale_gradient_y, 5, high_pass)
    # Supress Maximum near borders
    maximums = supress_near_border(img, maximums, 10)
    
    return img, maximums
    
# Ejercicio 2  AKAZE
def create_akaze(img1, img2):
    
    akaze = cv2.AKAZE_create()
    kpts1, desc1 = akaze.detectAndCompute(img1, None)
    kpts2, desc2 = akaze.detectAndCompute(img2, None)
    return ((kpts1, desc1), (kpts2, desc2))

def draw_matches(img1, kpts1, img2, kpts2, matches, number_sample):
    # Random Sample
    matches = random.sample(matches, min(len(matches), number_sample))
    # Draw
    return cv2.drawMatches(
        img1, kpts1,
        img2, kpts2,
        matches,
        None,
        flags = cv2.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS
    )

def draw_matches_knn(img1, kpts1, img2, kpts2, matches, number_sample):
    # Random Sample
    matches = random.sample(matches, min(len(matches), number_sample))
    # Draw
    return cv2.drawMatchesKnn(
        img1, kpts1,
        img2, kpts2,
        matches,
        None,
        flags = cv2.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS
    )
    
def ejercicio2_brute_cross(img1, img2):
    
    (kpts1, desc1), (kpts2, desc2) = create_akaze(img1, img2)
    
    # Get Matches from descriptors
    # Create Matcher
    bfmatcher = cv2.BFMatcher(
        cv2.NORM_HAMMING, # Because are binary vectors
        crossCheck = True
    )
    
    # Generate matches for the descriptors
    matches = bfmatcher.match(desc1, desc2)
    
    return (img1, kpts1, img2, kpts2, matches)

# Lowe's https://www.cs.ubc.ca/~lowe/papers/ijcv04.pdf (7.1 Keypoint Matching)
def ejercicio2_lowe(img1, img2, ratio = 0.4):
    (kpts1, desc1), (kpts2, desc2) = create_akaze(img1, img2)
    
    # Get Matches from descriptors without crosscheck
    bfmatcher = cv2.BFMatcher(
        cv2.NORM_HAMMING, # Because are binary vectors
        crossCheck = False
    )
    # Get Two matches for each descriptor
    matches_nocross = bfmatcher.knnMatch(desc1, desc2, k = 2)
    
    # Find Bruteforce matches that are also in the other matches and pass the lowe's criteria
    lowes_matches = []
    
    # Find matches from crosscheck and no crosscheck
    for match_nocross in matches_nocross:
        lowes_ratio = match_nocross[0].distance / match_nocross[1].distance
        if lowes_ratio < ratio:
            lowes_matches.append(match_nocross[:-1])
    
    return (img1, kpts1, img2, kpts2, lowes_matches)

# Ejercicio 3
# https://en.wikipedia.org/wiki/Transformation_matrix#Other_kinds_of_transformations
def homgraphy_matrix(tx, ty, s = 1.):
    return np.array([
        [s, 0, tx],
        [0, s, ty],
        [0, 0, 1 ]        
    ], dtype = np.float64)

def get_homography_matrix(img1, img2, ratio = 0.8):
    (_, points1, _, points2, matches) = ejercicio2_lowe(img1, img2, ratio)
    
    k_from = np.array([points1[match[0].queryIdx].pt for match in matches])
    k_to   = np.array([points2[match[0].trainIdx].pt for match in matches])
    
    # Works with , cv2.RANSAC, 5.0
    return cv2.findHomography(k_from, k_to, cv2.RANSAC, 1.)[0]
    
def ejercicio3(img1, img2, ratio = 0.5):
    
    # Create Result Image
    width = 1500
    height = 700
    compose_image = np.zeros((height, width, 3), np.uint8)
    
    # Center Image    
    dw = (width - img1.shape[1])//2
    dh = (height - img1.shape[0])//2
    
    # Center Translation
    t_matrix = homgraphy_matrix(dw, dh)
    
    # Move first image
    compose_image = cv2.warpPerspective(
        img1,
        t_matrix,
        (width, height),
        dst = compose_image,
        borderMode = cv2.BORDER_TRANSPARENT
    )
    
    # Homography img2 over img1
    homography = get_homography_matrix(img2, img1, ratio)
    
    # Concat Transoformations (dot product of the transformations)
    transform = t_matrix @ homography
    
    # Warp the img 
    compose_image = cv2.warpPerspective(
        img2,
        transform,
        (width, height),
        dst = compose_image,
        borderMode = cv2.BORDER_TRANSPARENT
    )
    
    return compose_image

def homographies_queue(imgs, dx = 0., dy = 0., s = 1., ratio = 0.8):
    # Matches
    homographies = [
        homgraphy_matrix(s * dx, s * dy, s)
    ]
    
    for i in range(1, len(imgs)):
        homographies.append(
            homographies[i - 1] @ get_homography_matrix(imgs[i], imgs[i - 1], ratio)
        )
    
    return homographies

def ejercicio4(imgs, dx = 0., dy = 0., ratio = 0.8, scale = 0.4):
    if len(imgs) <= 1: return;
    
    width = 1500
    height = 700
    compose_image = np.zeros((height, width, 3), np.uint8)
    
    dw = (width - imgs[0].shape[1])//2
    dh = (height - imgs[0].shape[0])//2
    
    homographies = homographies_queue(imgs, dw - dx, dh - dy, scale, ratio)
    
    # Center Translation
    for i in range(len(imgs)):
        compose_image = cv2.warpPerspective(
            imgs[i],
            homographies[i],
            (width, height),
            dst = compose_image,
            borderMode = cv2.BORDER_TRANSPARENT
        )
    
    return compose_image
    
# Bonus
# https://cseweb.ucsd.edu/classes/wi07/cse252a/homography_estimation/homography_estimation.pdf
def find_homography(pnts1, pnts2):
    T = []
    # Homography Matrix Solver
    for i in range(len(pnts1)):
        x, y = pnts1[i][0], pnts1[i][1]
        xp, yp = pnts2[i][0], pnts2[i][1]

        T.append([-x, -y, -1, 0, 0, 0, xp * x, xp * y, xp])
        T.append([0, 0, 0, -x, -y, -1, yp * x, yp * y, yp])
    
    
    # Convert it into matrix
    T = np.matrix(T)
    
    # T = U * D * V^t
    _, _, V = np.linalg.svd(T)
    
    # Last Row has the smallest eigenvalue
    H = np.reshape(V[8], (3, 3))
    
    # Normalize the matrix dividing by the last cell
    H = H / H[2, 2]
        
    return H

# Geometric Function
def error_homography(pnt1, pnt2, H):
    # Apply Homography
    pnt1H2 = H @ pnt1
    # Normalize
    pnt1H2 = pnt1H2 / pnt1[2]

    # Error Distance
    error = pnt2 - pnt1H2
    
    # Return Norm of the error
    return np.linalg.norm(error)


def ransac(pnts1, pnts2, threshold = 0.1):
    # Pre-process data
    zeros = np.full((len(pnts1), 1), 1., dtype = np.float16)
    pnts1 = np.append(pnts1, zeros, axis = 1)
    pnts2 = np.append(pnts2, zeros, axis = 1)
    
    H_result = None # Final Homography
    max_good = [] # Array with the best points
    # 1000 iterations
    for i in range(1000):
        # Pick 4 Points
        rnd = random.sample(range(len(pnts1)), 4)
        
        random_pnts1 = [pnts1[i] for i in rnd]
        random_pnts2 = [pnts2[i] for i in rnd]
        
        H = find_homography(random_pnts1, random_pnts2)
        
        # Count Good Matches
        good = []
        for pt in range(len(pnts1)):
            # Apply homography to point1
            error = error_homography(pnts1[pt], pnts2[pt], H)
            if error < threshold:
                good.append(pnts1[pt])
    
        # If we have found a better match
        if len(good) >= len(max_good):
            max_good = good
            H_result = H
        
    return H_result
    

def ejercicioBonus(img1, img2, ratio = 0.8, threshold = 1.0):
    
    (_, points1, _, points2, matches) = ejercicio2_lowe(img1, img2, ratio)
    
    k_from = np.array([points1[match[0].queryIdx].pt for match in matches])
    k_to   = np.array([points2[match[0].trainIdx].pt for match in matches])
    
    # Bonus
    H = ransac(k_from, k_to, threshold)
    
    # Create Result Image
    width = 1500
    height = 700
    compose_image = np.zeros((height, width, 3), np.uint8)

    # Center Image    
    dw = (width - img1.shape[1])//2
    dh = (height - img1.shape[0])//2
    
    # Center Translation
    t_matrix = homgraphy_matrix(dw, dh)
    
    # Move first image
    compose_image = cv2.warpPerspective(
        img1,
        t_matrix,
        (width, height),
        dst = compose_image,
        borderMode = cv2.BORDER_TRANSPARENT
    )
    
    # Concat Transoformations (dot product of the transformations)
    transform = t_matrix @ np.linalg.inv(H)
    
    # Warp the img 
    compose_image = cv2.warpPerspective(
        img2,
        transform,
        (width, height),
        dst = compose_image,
        borderMode = cv2.BORDER_TRANSPARENT
    )
    
    return compose_image

###### Ejercicios

# Ejercicio 1
yosemite = cv2.imread("./imagenes/Yosemite3.jpg", cv2.IMREAD_GRAYSCALE)

img, maximums = ejercicio1(yosemite, 3, 0.33)

# C
cv2.imshow("Ejercicio 1", ejercicio1_c(img, maximums))
cv2.waitKey(0)
cv2.destroyAllWindows()

# D
cv2.imshow("Ejercicio 1", ejercicio1_d(img, maximums))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Ejercicio 2
img1 = cv2.imread("./imagenes/yosemite3.jpg", cv2.IMREAD_COLOR)
img2 = cv2.imread("./imagenes/yosemite4.jpg", cv2.IMREAD_COLOR)

# Ejercicio 2 Bruteforce
cv2.imshow("Ejercicio 2 - Bruteforce", draw_matches(*ejercicio2_brute_cross(img1, img2), 100))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Ejercicio 2 Bruteforce + Lowe
cv2.imshow("Ejercicio 2 - Lowe's criteria", draw_matches_knn(*ejercicio2_lowe(img1, img2, 0.60), 100))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Ejercicio 3 Mosaico 2 Imágenes
cv2.imshow("Ejercicio 3", ejercicio3(img1, img2))
cv2.waitKey(0)
cv2.destroyAllWindows()


# Ejercicio 4 Mosaico todas las imágenes
etsiit_mosaic = [
 cv2.imread("./imagenes/mosaico003.jpg", cv2.IMREAD_COLOR),
 cv2.imread("./imagenes/mosaico004.jpg", cv2.IMREAD_COLOR),
 cv2.imread("./imagenes/mosaico005.jpg", cv2.IMREAD_COLOR),
 cv2.imread("./imagenes/mosaico006.jpg", cv2.IMREAD_COLOR),
 cv2.imread("./imagenes/mosaico007.jpg", cv2.IMREAD_COLOR),
 cv2.imread("./imagenes/mosaico008.jpg", cv2.IMREAD_COLOR),
 cv2.imread("./imagenes/mosaico009.jpg", cv2.IMREAD_COLOR),
 cv2.imread("./imagenes/mosaico010.jpg", cv2.IMREAD_COLOR),
 cv2.imread("./imagenes/mosaico011.jpg", cv2.IMREAD_COLOR),
]

yosemite_mosaic_1 = [
 cv2.imread("./imagenes/yosemite1.jpg", cv2.IMREAD_COLOR),
 cv2.imread("./imagenes/yosemite2.jpg", cv2.IMREAD_COLOR),
 cv2.imread("./imagenes/yosemite3.jpg", cv2.IMREAD_COLOR),
 cv2.imread("./imagenes/yosemite4.jpg", cv2.IMREAD_COLOR),
]

# Ratio < 0.8
yosemite_mosaic_2 = [
 cv2.imread("./imagenes/yosemite5.jpg", cv2.IMREAD_COLOR),
 cv2.imread("./imagenes/yosemite6.jpg", cv2.IMREAD_COLOR),
 cv2.imread("./imagenes/yosemite7.jpg", cv2.IMREAD_COLOR),
]

cv2.imshow("Ejercicio 4", ejercicio4(etsiit_mosaic, 450, 150, 0.8, 1.4))
cv2.waitKey(0)
cv2.destroyAllWindows()

# Bonus
cv2.imshow("Bonus", ejercicioBonus(img1, img2, threshold = 1.0))
cv2.waitKey(0)
cv2.destroyAllWindows()