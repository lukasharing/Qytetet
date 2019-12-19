# -*- coding: utf-8 -*-
# Práctica 0 Realizada por Lukas Häring García
import cv2
import numpy as np

import random

import os
os.path.dirname(os.path.abspath(__file__))

# Sigma to Sobel Matriz length
def sigma2tam(sigma):
    return 2 * np.uint(sigma * 3) + 1;

# Draw Keypoints because cv2 is ***
def drawKeypoints(img, keypoints, scalef, color = (0, 0, 255)):
    
    result = cv2.cvtColor(img, cv2.COLOR_GRAY2RGB)
    
    for keypoint in keypoints:
        position = (int(round(keypoint.pt[0] * scalef)), int(round(keypoint.pt[1] * scalef)))
        radius = int(round(keypoint.size / 2.))
        
        cv2.circle(result, position, radius, color, 1, cv2.LINE_AA)
        direction = (position[0] + int(np.cos(keypoint.angle) * radius), position[1] + int(np.sin(keypoint.angle) * radius)) 
        cv2.line(result, position, direction, color, 1, cv2.LINE_AA)
        
    return result
    
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

# Returns next gaussian scale
def gaussian_scale(img, sigma):
    gs_image = cv2.GaussianBlur(img, None, sigma, sigma)
    gs_image = cv2.resize(gs_image, None, fx = 0.5, fy = 0.5, interpolation = cv2.INTER_NEAREST)
    return gs_image

def gaussian_scales(img, sigma, num_scales):
    last_scale = img
    scales = [img]
    
    for i in range(num_scales - 1):
        last_scale = gaussian_scale(last_scale, sigma)
        scales.append(last_scale)
    
    return scales

# Gaussian Scale of the harmonic means
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
        
        # Harmonic Means
        harris_scales.append(
            eigen_image[:,:,0] * eigen_image[:,:,1] / (eigen_image[:,:,0] + eigen_image[:,:,1])
        )
    return harris_scales

# Non-Maximum Supression and High Pass Supression
def maximums_harris_scale(scales, gradient_x, gradient_y, high_pass = 10.):
    # Non Maximum Supression and High pass For Each Scale
    maximums = []
    for k in range(len(scales)):
        scale_maximums = []
        scale = scales[k]
        width, height = scale.shape

        for j in range(1, height):
            for i in range(1, width):
                px = scale[j, i]
                if not(np.isnan(px)) and px > high_pass:
                    nmax = np.amax(scale[j - 1 : j + 2, i - 1 : i + 2])
                    if px >= nmax:
                        angle = np.arctan2(gradient_y[k][j][i], gradient_x[k][j][i])
                        scale_maximums.append(cv2.KeyPoint(i, j, (k + 1) * 16., angle, 0., k))
        # Add Maximums found on the scale
        maximums.append(scale_maximums)
    
    return maximums

def ejercicio1_d(img, maximums, random_points = 3):
    ## Get SubPixel
    # Corner Matrix (n, 1, 2) https://stackoverflow.com/questions/40461469/how-to-use-opencv-cornersubpix-in-python
    corners_preprocess = np.zeros((len(maximums), 1, 2), dtype = np.float32);
    
    # Set X, Y for the pre-processing
    for i in range(len(maximums)):
        corners_preprocess[i][0][0] = maximums[i].pt[0] # set X
        corners_preprocess[i][0][1] = maximums[i].pt[1] # set Y
    
    # https://docs.opencv.org/2.4/doc/tutorials/features2d/trackingmotion/corner_subpixeles/corner_subpixeles.html
    cv2.cornerSubPix(
        img,                # Image
        corners_preprocess, # out Vector variable
        (5, 5),             # Window Size for Edges
        (-1, -1),           # "zero zone" to avoid possible singularities of the autocorrelation matrix
        (cv2.TERM_CRITERIA_EPS | cv2.TERM_CRITERIA_COUNT, 50, 0.001) # Error of 0.001 in 50 iterations
    )
    
    subpixels = corners_preprocess.reshape(-1, 2)
    
    # List of N Random Integers with no repetition
    random_unique_ints = random.sample(range(len(maximums)), random_points)
    random_subpixel = [subpixels[i] for i in random_unique_ints]
    random_pixels   = [list(maximums[i].pt) for i in random_unique_ints]
    
    # Scale the Image
    z = 
    resized = cv2.resize(img, None, fx = z, fy = z, interpolation = cv2.INTER_NEAREST)
    
    # Draw points
    # Draw Fixed Pixels
    for subpixel in random_subpixel:
        cv2.drawMarker(
            resized,
            (int(subpixel[0] * z), int(subpixel[1] * z)),
            (0, 0, 255),
            markerType = cv2.MARKER_STAR, 
            markerSize = 2,
            thickness = 1,
            line_type = cv2.LINE_AA
        )
    
    # Draw Original Pixels
    for pixel in random_pixels:
        cv2.drawMarker(
            resized,
            (int(pixel[0] * z), int(pixel[1] * z)),
            (255, 0, 0),
            markerType = cv2.MARKER_DIAMOND, 
            markerSize = 2,
            thickness = 1,
            line_type=cv2.LINE_AA
        )
    
    # Crop Each Region
    r = int(7 * z)
    rh = r // 2
    # Put padding to avoid memory
    resized = np.pad(resized, (r, r), mode = 'constant')
    
    result = np.zeros((r, r * random_points), dtype = np.uint8)
    for i in range(random_points):
        x = int(random_pixels[i][0] * z)
        y = int(random_pixels[i][1] * z)
        result[:, r * i : r * (i + 1)] = i * 10#resized[y - rh : y + rh + 1, x - rh : x + rh + 1]
        
        
    return result

# Ejercicio 1
def ejercicio1(img, random_points, num_scales = 3, high_pass = 10.):
    # Sizes image
    height, width = img.shape
    # Resize Image to a power of two
    rimg = padding_image(img)
    
    # Gradient
    ksize = sigma2tam(4.5) # Sigma for the orientation
    grad_x = cv2.Sobel(img, cv2.CV_64F, 1, 0, ksize = ksize)
    grad_y = cv2.Sobel(img, cv2.CV_64F, 0, 1, ksize = ksize)
    
    # Gradient and Gaussian Scales
    scale_gradient_x = gaussian_scales(grad_x, 0.01, num_scales) # sigma = 0.1, smol smoothing
    scale_gradient_y = gaussian_scales(grad_y, 0.01, num_scales) #sigma = 0.1, smol smoothing
    gaussian_scale = gaussian_scales(rimg, 1.5, num_scales) # Paper constant Gaussian coefficient
    
    # Harris Gaussian Scales
    scales = harris_scales(gaussian_scale, 5) # Use window 5x5 to find corners
    # Maximum Scale Supression
    maximums = maximums_harris_scale(scales, scale_gradient_x, scale_gradient_y, high_pass)
    
    # Print Maximums per scale
    for i in range(num_scales):
        print("Escala {}: Total de {} puntos Harris".format(i, len(maximums[i])))
    
    # Unroll the matrix 
    maximums = [j for sub in maximums for j in sub]
    
    result = ejercicio1_d(img, maximums)
    
    """
    
    # Draw one next to another
    result = np.zeros((height, width * num_scales), dtype = np.uint8)
    result = img[:height,:width]
    
    for i in range(num_scales):
        maximums_scale = maximums[i]
        scalef = np.power(2, i)
        scale_resize = cv2.resize(gaussian_scale[i], None, fx = scalef, fy = scalef, interpolation = cv2.INTER_NEAREST)
        keypoints = drawKeypoints(
            scale_resize,
            maximums_scale,
            scalef
        )
        result[:height, width * i: width * (i + 1)] = keypoints[:height, :width]
    """
    
    return result

# Ejercicio 2  AKAZE
def create_akaze(img1, img2):
    
    akaze = cv2.AKAZE_create()
    kpts1, desc1 = akaze.detectAndCompute(img1, None)
    kpts2, desc2 = akaze.detectAndCompute(img2, None)
    return ((kpts1, desc1), (kpts2, desc2))

def match_akaze(desc1, desc2, crossCheck = True, k = 1):
    # Create Matcher
    bfmatcher = cv2.BFMatcher(
        crossCheck = crossCheck
    )
    
    # Return Match (Best nth correspondencies for each point)
    return bfmatcher.knnMatch(desc1, desc2, k = k)
    

def draw_matches(img1, kpts1, img2, kpts2, matches, number_sample):
    # Random Sample
    matches = random.sample(matches, number_sample)
    
    # Draw
    return cv2.drawMatchesKnn(
        img1, kpts1, 
        img2, kpts2, 
        matches,
        outImg = np.array([]),
        flags = cv2.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS
    )
    

def ejercicio2_brute_cross(img1, img2):
    
    (kpts1, desc1), (kpts2, desc2) = create_akaze(img1, img2)
    
    # Get Matches from descriptors
    matches = match_akaze(desc1, desc2, k = 1)
    
    return (img1, kpts1, img2, kpts2, matches)

# Lowe's https://www.cs.ubc.ca/~lowe/papers/ijcv04.pdf (7.1 Keypoint Matching)
def ejercicio2_lowe(img1, img2, ratio = 0.8):
    
    (kpts1, desc1), (kpts2, desc2) = create_akaze(img1, img2)
    
    # Get Matches from descriptors
    matches = match_akaze(desc1, desc2, crossCheck = False, k = 2)
    
    # Ratio between each 
    ratio_matches = []
    for first, second in matches:
        if first.distance / second.distance < ratio:
          ratio_matches.append(first)
    
    return (img1, kpts1, img2, kpts2, ratio_matches)

# Ejercicio 3
# https://en.wikipedia.org/wiki/Transformation_matrix#Other_kinds_of_transformations
def homgraphy_matrix(tx, ty, s = 1.):
    return np.array([
        [s, 0, tx],
        [0, s, ty],
        [0, 0, 1 ]        
    ], dtype = np.float64)

def get_homography_matrix(img1, img2, ratio = 0.8):
    
    (img1, kpts1, img2, kpts2, matches) = ejercicio2_lowe(img1, img2, ratio)
    
    points1 = np.array([ kpts1[match.queryIdx].pt for match in matches])
    points2 = np.array([ kpts2[match.trainIdx].pt for match in matches])
    
    # Works with , cv2.RANSAC, 5.0
    return cv2.findHomography(points1, points2, cv2.RANSAC, 1.)[0]
    
def ejercicio3(img1, img2, ratio = 0.8):
    # Matches
    homography = get_homography_matrix(img2, img1, ratio)
    
    # Create Result Image
    width = 1500
    height = 700
    compose_image = np.zeros((height, width, 3), np.uint8)

    # Center Image    
    dw = (width - img1.shape[1])//2
    dh = (height - img1.shape[0])//2
    compose_image[dh : dh + img1.shape[0], dw : dw + img1.shape[1]] = img1
    
    # Center Translation
    t_matrix = homgraphy_matrix(dw, dh)
    
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
        homgraphy_matrix(dx, dy, s)
    ]
    
    for i in range(1, len(imgs)):
        homographies.append(
            homographies[i - 1] @ get_homography_matrix(imgs[i], imgs[i - 1], ratio)
        )
    
    return homographies

def ejercicio4(imgs, s = 0.8):
    if len(imgs) <= 1: return;
    # Sort by image size, Biggest image always in the middle
    imgs.sort(key = lambda x: x.shape[0] * x.shape[1])
    
    width = 1000
    height = 700
    compose_image = np.zeros((height, width, 3), np.uint8)
    
    dw = (width - imgs[0].shape[1])//2
    dh = (height - imgs[0].shape[0])//2
    
    homographies = homographies_queue(imgs, dw, dh, s)
    
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

def find_homography_bonus(pnts1, pnts2):
    
    # Get 4 Points
    pnts14 = random.sample(pnts1, 4)
    
    print(pnts14)
    
    # Using Ransac

def ejercicioBonus(img1, img2):
    
    (img1, kpts1, img2, kpts2, matches) = ejercicio2_lowe(img1, img2, 0.8)
    
    points1 = np.array([ kpts1[match.queryIdx].pt for match in matches])
    points2 = np.array([ kpts2[match.trainIdx].pt for match in matches])
    
    find_homography_bonus(points1, points2)
    

#./imagenes/yosemite7.jpg
img2 = cv2.imread("./imagenes/yosemite2.jpg", cv2.IMREAD_GRAYSCALE)

tablero = cv2.imread("./imagenes/Tablero1.jpg", cv2.IMREAD_GRAYSCALE)

cv2.imshow("Ejercicio 1", ejercicio1(tablero, 3, 4, 0.1))
cv2.waitKey(0)
cv2.destroyAllWindows()

cv2.imshow("Ejercicio 2", draw_matches(*ejercicio2_lowe(tablero, img2), 100))
#cv2.waitKey(0)
#cv2.destroyAllWindows()

#cv2.imshow("Ejercicio 3", ejercicio3(img1, img2))
#cv2.waitKey(0)
#cv2.destroyAllWindows()

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

yosemite_mosaic_2 = [
 cv2.imread("./imagenes/yosemite5.jpg", cv2.IMREAD_COLOR),
 cv2.imread("./imagenes/yosemite6.jpg", cv2.IMREAD_COLOR),
 cv2.imread("./imagenes/yosemite7.jpg", cv2.IMREAD_COLOR),
]



#cv2.imshow("Ejercicio 4", ejercicio4(etsiit_mosaic))
#cv2.waitKey(0)
#cv2.destroyAllWindows()


#ejercicioBonus(img1, img2)