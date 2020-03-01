# -*- coding: utf-8 -*-
"""
Created on Sat Feb 29 02:39:33 2020

@author: Lukas Häring García
"""
import numpy as np

import random

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


def violate_restriction(i, clusters, restrictions):
    
    print()
    
    return False
    

def COPKM(X, R, k):

    seed = 30
    
    random.seed(seed)
    np.random.seed(seed)
    
    RSI = random.sample(range(len(X)), len(X))
    mis = np.random.rand(k, dataset.shape[1])
    
    while True:
        
        has_change = False
    
        clusters = [[] for i in range(k)]
        
        print(mis)
        for i in RSI:
            xi = X[i]
            # Could be optimized using an Object {distance, index}
            nmmis = range(len(mis)) # Create Index Array
            dsmis = [distance(mi, xi) for mi in mis] # Create Distance Array
            # Find Array index of the minimun distance of a set of cluster
            idxmin = dsmis.index(min(dsmis))
            # Check if the element in that cluster satisfies the restrictions
            while len(dsmis) > 0 and violate_restriction(idxmin, clusters, R):
                dsmis.pop(idxmin)
                nmmis.pop(idxmin)
                idxmin = dsmis.index(min(dsmis))
            
            if len(dsmis) == 0:
                return []
            else:
                clusters[nmmis[idxmin]].append(xi)
            # Asignar xi a su cluster más cercano cj sin violar ninguna restricción
        
        for i in range(k):
            if len(clusters[i]) > 0:
                mis[i] = group_centroid(clusters[i])
        
        if not(has_change):
            return clusters

clusters = COPKM(dataset, constrictions, 3)
print(general_deviation(clusters))




