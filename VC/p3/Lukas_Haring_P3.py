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
def drawKeypoints(img, keypoints, color = (0, 0, 255)):
    
    result = cv2.cvtColor(img, cv2.COLOR_GRAY2RGB)
    
    for keypoint in keypoints:
        position = (int(round(keypoint.pt[0])), int(round(keypoint.pt[1])))
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

# Returns the gradient (x derivate and y derivate of the image)
def gradient(img, sigma):
    ksize = sigma2tam(sigma)
    
    # Calculate x and y derivate
    grad_x = cv2.Sobel(img, cv2.CV_64F, 1, 0, ksize = ksize)
    grad_y = cv2.Sobel(img, cv2.CV_64F, 0, 1, ksize = ksize)
    
    # Pair each gradient x and gradient y
    return np.dstack((grad_x, grad_y))

# Returns next gaussian scale
def gaussian_scale(img, sigma):
    gs_image = cv2.GaussianBlur(img, None, sigma, sigma)
    gs_image = cv2.resize(gs_image, None, fx = 0.5, fy = 0.5, interpolation = cv2.INTER_NEAREST)
    return gs_image

def gaussian_scales(img, num_scales):
    
    last_scale = img
    scales = [img]
    
    # Paper constant
    integration_scale = 1.5 # Gaussian coefficient
    
    for i in range(num_scales - 1):
        last_scale = gaussian_scale(last_scale, integration_scale)
        scales.append(last_scale)
    
    return scales

# Gaussian Scale of the harmonic means
def harris_scales(gaussian_scales):
    harris_scales = []
    # Paper constant
    derivate_scale    = 1.0 # Derivate coefficient
    
    num_scales = len(gaussian_scales)
    for i in range(num_scales):
        scale_block = int(np.power(2, i))
        
        # Eigen Values / Vectors of each pixel
        eigen_image = cv2.cornerEigenValsAndVecs(
            gaussian_scales[i],
            blockSize = scale_block,
            ksize = sigma2tam(derivate_scale),
            borderType = cv2.BORDER_CONSTANT
        )
        
        # Harmonic Means
        harris_scales.append(
            eigen_image[:,:,0] * eigen_image[:,:,1] / (eigen_image[:,:,0] + eigen_image[:,:,1])
        )
    return harris_scales

# Non-Maximum Supression and High Pass Supression
def maximums_harris_scale(grad, scales, high_pass = 10.):
    # Non Maximum Supression For Each Scale
    maximums = []
    for k in range(len(scales)):
        scale_maximums = []
        scale = scales[k]
        width, height = scale.shape        
        # Factor Scales
        scalef = np.power(2, k)
        for j in range(1, height):
            for i in range(1, width):
                px = scale[j, i]
                if not(np.isnan(px)) and px >= high_pass:
                    nmax = np.amax(scale[j - 1 : j + 2, i - 1 : i + 2])
                    if px >= nmax:
                        sx = i * scalef
                        sy = j * scalef
                        angle = np.arctan2(grad[sy][sx][1], grad[sy][sx][0])
                        scale_maximums.append(cv2.KeyPoint(sx, sy, scalef * 16., angle))
        maximums.append(scale_maximums)
    
    return maximums


# Ejercicio 1
def ejercicio1(img, num_scales = 3, high_pass = 10.):
    # Sizes image
    height, width = img.shape
    # Resize Image to a power of two
    rimg = padding_image(img)

    # Gradient and Gaussian Scales    
    igrad = gradient(img, 4.5)
    gaussians = gaussian_scales(rimg, num_scales)
    
    # Harris Gaussian Scales
    scales = harris_scales(gaussians)

    # Maximum Scale Supression
    maximums = maximums_harris_scale(igrad, scales, high_pass)
    
    # Draw one next to another
    result = np.zeros((height, width * num_scales, 3), dtype = rimg.dtype)
    
    for i in range(len(gaussians)):
        scalef = np.power(2, i)
        scale_resize = cv2.resize(gaussians[i], None, fx = scalef, fy = scalef, interpolation = cv2.INTER_NEAREST)
        keypoints = drawKeypoints(
            scale_resize,
            maximums[i]
        )
        result[:height, width * i: width * (i + 1)] = keypoints[:height, :width]
    
    return result

# Ejercicio 2  AKAZE
def create_akaze(img1, img2):
    akaze = cv2.AKAZE_create()
    kpts1, desc1 = akaze.detectAndCompute(img1, None)
    kpts2, desc2 = akaze.detectAndCompute(img2, None)
    return ((kpts1, desc1), (kpts2, desc2))

def match_akaze(desc1, desc2, k = 1):
    
    # Create Matcher
    bfmatcher = cv2.BFMatcher(
        crossCheck = True
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
        flags = cv2.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS
    )
    

def ejercicio2_brute_cross(img1, img2):
    
    (kpts1, desc1), (kpts2, desc2) = create_akaze(img1, img2)
    
    # Get Matches from descriptors
    matches = match_akaze(desc1, desc2, 1)
    
    return (img1, kpts1, img2, kpts2, matches)

# Lowe's https://www.cs.ubc.ca/~lowe/papers/ijcv04.pdf (7.1 Keypoint Matching)
def ejercicio2_lowe(img1, img2, ratio = 0.8):
    
    (kpts1, desc1), (kpts2, desc2) = create_akaze(img1, img2)
    
    # Get Matches from descriptors
    matches = match_akaze(desc1, desc2, 2)
    
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
    

#./imagenes/yosemite7.jpg
img1 = cv2.imread("./imagenes/yosemite5.jpg", cv2.IMREAD_COLOR)
img2 = cv2.imread("./imagenes/yosemite1.jpg", cv2.IMREAD_COLOR)

#cv2.imshow("Ejercicio 1", ejercicio1(img1, 3, 0.1))
#cv2.waitKey(0)
#cv2.destroyAllWindows()

cv2.imshow("Ejercicio 2", draw_matches(*ejercicio2_brute_cross(img1, img2), 100))
cv2.waitKey(0)
cv2.destroyAllWindows()

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
