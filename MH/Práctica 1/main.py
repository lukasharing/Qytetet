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

name = "rand"

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

def general_deviation(C):
    return np.sum([distance_intracluster(ci) for ci in C]) / len(C)

#def infeasibility(C):
#    np.sum([np.sum() for ])

def violate_restriction(i, assigned, ri):
    violations = 0
    for (j, restriction) in enumerate(ri):
        if assigned[j] >= 0 and i != j:
            if restriction == 1:
                violations += assigned[j] != assigned[i]
            elif restriction == -1:
                violations += assigned[j] == assigned[i]
    
    return violations

def lambda_factor(X, R):
    # Get Distance Matrix
    dist_mat = spatial.distance_matrix(X, X)
    # Get indices of the furthest points
    i, j = np.unravel_index(dist_mat.argmax(), dist_mat.shape)
    # Find Number of restrictions
    num_restrictions =  (np.sum(np.absolute(R)) - len(X)) / 2
    # Return D / R
    return distance(X[i], X[j]) / num_restrictions

def infeasability(X, A, R):
    result = 0.
    for i in range(len(X)):
        result += violate_restriction(i, A, R[i])
    
    return result

def f(X, C, A, R):
    return general_deviation(C) + lambda_factor(X, R) * infeasability(X, A, R)

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

def COPKM(X, R, k):

    seed = 10
    
    random.seed(seed)
    np.random.seed(seed)
    
    size = len(X)
    RSI = random.sample(range(size), size)
    
    mis = random_centroids(X, k)
    
    last_assigned = np.full((1, size), -1)
    while True:
        assigned = np.full((1, size), -1)
        clusters = [[] for i in range(k)]
        
        for i in RSI:
            xi = X[i]
            # Create Index Array
            nmmis = list(np.arange(len(mis)))
            
            # Create Distance Array
            dsmis = [distance(mi, xi) for mi in mis]
            
            # Find Array index of the minimun distance of a set of cluster
            idxmin = dsmis.index(min(dsmis))
            
            # Assign Element i to that cluster
            assigned[0][i] = nmmis[idxmin]
            
            # Check if the element in that cluster satisfies the restrictions
            while violate_restriction(i, assigned[0], R[i]) > 0:
                dsmis.pop(idxmin)
                nmmis.pop(idxmin)
                if len(dsmis) == 0: break
                idxmin = dsmis.index(min(dsmis))
                assigned[0][i] = nmmis[idxmin]
            
            # Check the violation of the change
            if len(dsmis) == 0:
                return []
            else:
                clusters[nmmis[idxmin]].append(xi)
        
        # Update Centroids
        for i in range(k):
            if len(clusters[i]) > 0:
                mis[i] = group_centroid(clusters[i])
        
        if (last_assigned[0] == assigned[0]).all():
            #plot2d(X, assigned[0], mis, R)
            return clusters
        
        last_assigned = assigned
    

def LOCAL(X, R, k):
    
    seed = 10
    
    random.seed(seed)
    np.random.seed(seed)
    
    size = len(X)
    
    assigned_result = np.random.randint(k, size = size)
    result = [[] for i in range(k)]
    [result[v].append(X[i]) for i, v in enumerate(assigned_result)]
    
    min_f = f(X, result, assigned_result, R)
    
    for i in range(10000):
        
        assigned = np.random.permutation(assigned_result)#np.random.randint(k, size = size)
        clusters = [[] for i in range(k)]
        [clusters[v].append(X[i]) for i, v in enumerate(assigned)]
    
        current_f = f(X, clusters, assigned, R)
        if current_f < min_f:
            assigned_result = assigned
            min_f = current_f
            result = clusters
    
    #mis = [group_centroid(c) for c in clusters]
    #plot2d(X, assigned_result, mis, R)
    
    return result

#clusters = COPKM(dataset, constrictions, 3)
start_time = time.time()
clusters = LOCAL(dataset, constrictions, 3)
print("--- %s ms ---" % ((time.time() - start_time) * 1000.))

if(len(clusters) == 0):
    print("No solution found")
else:
    print("Deviation = {}".format(general_deviation(clusters)))

