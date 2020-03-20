# -*- coding: utf-8 -*-
"""
Created on Sat Feb 29 02:39:33 2020

@author: Lukas Häring García
"""
import numpy as np
import matplotlib.pyplot as plt
from scipy import spatial

import random

import time

# Iris : 3 classes
# Rand : 3 classes
# Ecoli : 8 classes

name = "ecoli"
k = 8

# Load Dat File With Numpy
path_dat = "./dataset/"+ name +"_set.dat"
dataset = np.loadtxt(path_dat, dtype='f', delimiter=',')

# Load Constrictions File With Numpy
path_const = "./dataset/"+ name +"_set_const_10.const"
constrictions = np.loadtxt(path_const, dtype='f', delimiter=',')

# Centroid Function
def group_centroid(ci):
    return np.transpose([np.sum(xj) for xj in np.transpose(ci)]) / len(ci)

def distance(v1, v2):
    return np.linalg.norm(np.subtract(v1, v2))
    
def distance_intracluster(ci):
    if len(ci) == 0: return 0.0
    
    centroid = group_centroid(ci)
    return np.sum([distance(xj, centroid) for xj in ci]) / len(ci)

def general_deviation(X, A, k):
    C = split_to_clusters(X, A, k)
    return np.sum([distance_intracluster(ci) for ci in C]) / k

def lambda_factor(X, R):
    # Get Distance Matrix
    dist_mat = spatial.distance_matrix(X, X)
    # Get indices of the furthest points
    i, j = np.unravel_index(dist_mat.argmax(), dist_mat.shape)
    # Find Number of restrictions
    num_restrictions =  (np.sum(np.absolute(R)) - len(X)) / 2
    # Return D / R
    return distance(X[i], X[j]) / num_restrictions

# Este código está explicado en la memoria
def infeasability(X, A, p_r, n_r):
    
    A_scale = np.diag(A + 1)
    A_scalei = np.diag([1/(a + 1) for a in A])
    A_scalei = np.where(np.isposinf(A_scalei), 0, A_scalei)
    
    P_D = np.matmul(
        A_scalei,
        np.transpose(np.matmul(A_scale, p_r))
    )
    
    N_D = np.matmul(
        A_scalei,
        np.transpose(np.matmul(A_scale, n_r))
    )
    
    positive_violation = np.count_nonzero(np.where(P_D == 1, 0, P_D))
    negative_violation = np.count_nonzero(np.where(N_D != 1, 0, N_D))
    
    return positive_violation + negative_violation

def split_to_clusters(X, A, k):
    clusters = [[] for i in range(k)]
    [clusters[v].append(X[i]) for i, v in enumerate(A)]
    return clusters

def f(X, A, p_r, n_r, k, flambda):
    return general_deviation(X, A, k) + flambda * infeasability(X, A, p_r, n_r)

def plot2d(X, assigned, mis, restrictions):
    fig, ax = plt.subplots()
    
    colors = ['tab:blue', 'tab:orange', 'tab:green', 'tab:purple']
    
    for i in range(len(X)):
        for r in range(i, len(X)):
            if restrictions[i][r] == -1:
                ax.plot(
                    [X[i][0], X[r][0]],
                    [X[i][1], X[r][1]],
                    "r--",
                    linewidth = 1,
                    alpha = 0.1
                )
    
    for i in range(len(X)):
        ax.scatter(
            X[i][0], 
            X[i][1],
            c = colors[assigned[i]],
            s = 10.
        )
    
    centroids_x, centroids_y = map(list, zip(*mis))
    for i in range(len(mis)):
        ax.scatter(
            centroids_x[i], 
            centroids_y[i],
            c = colors[i],
            s = 50.,
            edgecolors = [0, 0, 0]
        )
    
    plt.show()

def random_centroids(X, k):
    # Initizalization mis
    min_val = np.amin(X, axis = 0)
    max_val = np.amax(X, axis = 0)
    
    # Scale Matrix (Diagonal Matrix)
    dif_val = np.diag(np.subtract(max_val, min_val))
    
    # M(n, m) random and transposed
    rand_mtx = np.random.rand(X.shape[1], k)
    
    # R * Dif + Min 
    return (
        np.transpose(
            np.matmul(
                dif_val,
                rand_mtx
            )
        ) #  R * Dif
        + min_val
    )

def has_one(assigned_result, k):
    for i in range(k):
        if len(np.where(assigned_result == k)) == 0:
            return False
    return True

def COPKM(X, R, k, seed = 11):
    
    random.seed(seed)
    np.random.seed(seed)
    
    size = len(X)
    RSI = random.sample(range(size), size)
    
    # Matrix
    positive_r = R - np.identity(R.shape[0])
    positive_r[:][:] = 0.5*positive_r[:][:]*(positive_r[:][:] + 1)
    negative_r = R - np.identity(R.shape[0])
    negative_r[:][:] = 0.5*negative_r[:][:]*(negative_r[:][:] - 1)
    
    mis = random_centroids(X, k)
    
    last_assigned = np.repeat(-1, size)
    while True:

        assigned = np.repeat(-1, size)
        for i in RSI:
            
            xi = X[i]
            dsmis = [distance(mi, xi) for mi in mis]
            
            assigned[i] = best_cj = 0
            min_dinfeasability = infeasability(X, assigned, positive_r, negative_r)
            min_distance = dsmis[0]
            
            for cj in range(1, k):
                assigned[i] = cj
                cinfeasability = infeasability(X, assigned, positive_r, negative_r)
                
                # Lowest Infeasability and proximal to distance
                if cinfeasability < min_dinfeasability and dsmis[cj] < min_distance:
                    min_dinfeasability = cinfeasability
                    min_distance = dsmis[cj]
                    best_cj = cj
            
            
            assigned[i] = best_cj
    
        # Update Centroids
        clusters = split_to_clusters(X, assigned, k)
        for i in range(k):
            if len(clusters[i]) > 0:
                mis[i] = group_centroid(clusters[i])
        
        if (last_assigned == assigned).all():
            #plot2d(X, assigned, mis, R)
            return assigned
        
        last_assigned = assigned
    

EPSILON = 0.0001
def LOCAL(X, R, k, seed = 10):
    
    # Calculate Lambda Factor (Constant in the whole process)
    flambda = lambda_factor(X, R)
    
    #random.seed(seed)
    #np.random.seed(seed)
    
    size = len(X)
    
    # Matrix
    positive_r = R - np.identity(R.shape[0])
    positive_r[:][:] = 0.5*positive_r[:][:]*(positive_r[:][:] + 1)
    negative_r = R - np.identity(R.shape[0])
    negative_r[:][:] = 0.5*negative_r[:][:]*(negative_r[:][:] - 1)
    
    # Initialization
    assigned_result = np.random.randint(k, size = size)
    min_f = f(X, assigned_result, positive_r, negative_r, k, flambda)
    
    for i in range(10000):
        
        # Check that all cluster have at least one element
        while True:
            mutate_idx = np.random.randint(size)
            last_cluster = assigned_result[mutate_idx]
            rand_cluster = np.random.permutation([i for i in range(k) if i != last_cluster])[0]
            
            assigned_result[mutate_idx] = rand_cluster
            if has_one(assigned_result, k): break
            else: assigned_result[mutate_idx] = last_cluster
        
        current_f = f(X, assigned_result, positive_r, negative_r, k, flambda)
        
        if current_f < min_f:
            min_f = current_f
        else:
            assigned_result[mutate_idx] = last_cluster
            # If the value is too small, then just break it
            """if abs(current_f - min_f) < EPSILON:
                print(current_f, min_f)
                print("Break in {} iterations".format(i))
                break"""
        
    #mis = [group_centroid(c) for c in split_to_clusters(X, assigned_result, k)]
    #plot2d(X, assigned_result, mis, R)
    
    return assigned_result

start_time = time.time()
clusters = COPKM(dataset, constrictions, k)
print("--- %s s ---" % round(time.time() - start_time, 2))
print("Deviation = {}".format(round(general_deviation(dataset, clusters, k), 2)))


start_time = time.time()
clusters = LOCAL(dataset, constrictions, k)
print("--- %s s ---" % round(time.time() - start_time, 2))
print("Deviation = {}".format(round(general_deviation(dataset, clusters, k), 2)))



