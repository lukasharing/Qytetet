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
def infeasibility(X, A, p_r, n_r):
    
    A_scale = np.diag(A + 1)
    A_scalei = np.diag([1/(a + 1) for a in A])
    A_scalei = np.where(np.isposinf(A_scalei), 0, A_scalei)
    
    # Mnn (M'nn Pr)^t
    P_D = np.matmul(
        A_scalei,
        np.transpose(np.matmul(A_scale, p_r))
    )
    
    # Mnn (M'nn Nr)^t
    N_D = np.matmul(
        A_scalei,
        np.transpose(np.matmul(A_scale, n_r))
    )
    
    # Sum c Pr / c != 1,0
    positive_violation = np.count_nonzero(np.where(P_D == 1, 0, P_D))
    # Sum c Nr / c = 1
    negative_violation = np.count_nonzero(np.where(N_D != 1, 0, N_D))
    
    return positive_violation + negative_violation

# Separa El vector de asignación
def split_to_clusters(X, A, k):
    clusters = [[] for i in range(k)]
    [clusters[v].append(X[i]) for i, v in enumerate(A)]
    return clusters

def f(X, A, p_r, n_r, k, flambda):
    return general_deviation(X, A, k) + flambda * infeasibility(X, A, p_r, n_r)

def plot2d(X, assigned, mis, negative_r, color = "red"):
    fig, ax = plt.subplots()
    
    colors = ['tab:blue', 'tab:orange', 'tab:green', 'tab:purple']
    
    for i in range(len(X)):
        for r in range(i, len(X)):
            if negative_r[i][r] == 1:
                ax.plot(
                    [X[i][0], X[r][0]],
                    [X[i][1], X[r][1]],
                    "r--",
                    linewidth = 1,
                    alpha = 0.1,
                    c = color
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

def generate_neighbourhood(parent, n, k):
    neighbourhood = []
    for i in range(n):
        pivot = parent[i]
        for j in range(0, pivot):
            parent[i] = j
            if has_one(parent, k):
                neighbourhood.append([i, j])
            parent[i] = pivot
            
        for j in range(pivot + 1, k):
            parent[i] = j
            if has_one(parent, k):
                neighbourhood.append([i, j])
            parent[i] = pivot
        
    return np.random.permutation(neighbourhood)

def COPKM(X, positive_r, negative_r, k, flambda = 0.0, seed = 10):
    
    random.seed(seed)
    np.random.seed(seed)
    
    size = len(X)
    # Return I could have used np.random.permutation...
    RSI = random.sample(range(size), size)

    mis = random_centroids(X, k)

    last_assigned = np.repeat(-1, size)
    while True:
        # Generate Assign Vector with -1
        assigned = np.repeat(-1, size)
        for i in RSI:
            
            xi = X[i]
            # Calculate distance row
            dsmis = [distance(mi, xi) for mi in mis]
            
            assigned[i] = best_cj = 0
            min_dinfeasability = infeasibility(X, assigned, positive_r, negative_r)
            min_distance = dsmis[0]
            
            for cj in range(1, k):
                assigned[i] = cj
                cinfeasability = infeasibility(X, assigned, positive_r, negative_r)
                
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
            return assigned
        
        last_assigned = assigned

def BL_PM(X, positive_r, negative_r, k, flambda = 0.0, seed = 10):
    
    random.seed(seed)
    np.random.seed(seed)
    
    size = len(X)
    
    # Initialization
    assigned_result = np.random.randint(k, size = size)
    # Sure that the elements have at least one cluster assign
    while not(has_one(assigned_result, k)):
        assigned_result = np.random.randint(k, size = size)
    
    min_f = f(X, assigned_result, positive_r, negative_r, k, flambda)
    
    total_evaluation_f = 0
    max_f_evaluation = 100000
    found_better = True
    while total_evaluation_f < max_f_evaluation and found_better:
        
        # Generate Neighbourhood
        neighbourhood = generate_neighbourhood(assigned_result, size, k)
        
        found_better = False
        for idx, cluster in neighbourhood:
            old_neighbour = assigned_result[idx]
            assigned_result[idx] = cluster
            current_f = f(X, assigned_result, positive_r, negative_r, k, flambda)
            
            if current_f < min_f:
                min_f = current_f
                found_better = True
                break;
            else: assigned_result[idx] = old_neighbour
            
            total_evaluation_f += 1
            if total_evaluation_f >= max_f_evaluation: break
        
    if not(found_better):
        print("Found solution in {} iterations!!".format(total_evaluation_f))
    elif total_evaluation_f >= max_f_evaluation:
        print("Exceeded number maximum of iterations!!")
    
    return assigned_result


def BL_MV(X, positive_r, negative_r, k, flambda = 0.0, seed = 10):
    
    random.seed(seed)
    np.random.seed(seed)
    
    size = len(X)
    
    # Initialization
    assigned_result = np.random.randint(k, size = size)
    
    # Sure that the elements have at least one cluster assign
    while not(has_one(assigned_result, k)):
        assigned_result = np.random.randint(k, size = size)
    
    min_f = f(X, assigned_result, positive_r, negative_r, k, flambda)
    
    total_evaluation_f = 0
    max_f_evaluation = 1000000
    found_better = True
    while total_evaluation_f < max_f_evaluation and found_better:
        
        # Generate Neighbourhood
        neighbourhood = generate_neighbourhood(assigned_result, size, k)
        
        found_better = False
        best_idx = -1
        best_cluster = 0
        
        for idx, cluster in neighbourhood:
            # Change Solution State
            old_neighbour = assigned_result[idx]
            assigned_result[idx] = cluster
            
            current_f = f(X, assigned_result, positive_r, negative_r, k, flambda)
            if current_f < min_f:
                found_better = True
                min_f = current_f
                best_idx = idx
                best_cluster = cluster
            
            # Return to last State
            assigned_result[idx] = old_neighbour
            
            total_evaluation_f += 1
            if total_evaluation_f >= max_f_evaluation: break
        
        if found_better:
            assigned_result[best_idx] = best_cluster
    
    if not(found_better):
        print("Found solution in {} iterations!!".format(total_evaluation_f))
    elif total_evaluation_f >= max_f_evaluation:
        print("Exceeded number maximum of iterations!!")
    
    return assigned_result


def algoritm(name, f, seeds, X, R, k):
    print("######### {} #########".format(name))
    mean_time = 0.
    mean_deviation = 0.
    mean_tasa_inf = 0.
    mean_agregado = 0.
    
    # Lambda
    # Calculate Lambda Factor (Constant in the whole process)
    flambda = lambda_factor(X, R)
    
    # Matrix
    positive_r = R - np.identity(R.shape[0])
    # c'ij= cij * (cij + 1)*1/2
    positive_r[:][:] = 0.5*positive_r[:][:]*(positive_r[:][:] + 1)
    negative_r = R - np.identity(R.shape[0])
    # c'ij= cij * (cij - 1)*1/2
    negative_r[:][:] = 0.5*negative_r[:][:]*(negative_r[:][:] - 1)
    
    for (i, seed) in enumerate(seeds):
        start_time = time.time()
        clusters = f(X, positive_r, negative_r, k, flambda, seed)
        # Time
        dt = time.time() - start_time
        mean_time += dt
        # General Deviation
        deviation = general_deviation(X, clusters, k)
        mean_deviation += deviation
        # Infeasability
        tasa_inf = infeasibility(X, clusters, positive_r, negative_r)
        mean_tasa_inf += tasa_inf
        # (Agregado) f Function: f = C + F * lambda
        tasa_agregado = tasa_inf * flambda + deviation
        mean_agregado += tasa_agregado
        
        print("\n######### Iteration {} in {} s #########".format(i + 1, round(dt, 2)))
        print("\t1. Deviation = {}".format(round(deviation, 2)))
        print("\t2. Infeasibility = {}".format(round(tasa_inf, 2)))
        print("\t3. Agregado = {}".format(round(tasa_agregado, 2)))
        
        if False:
            print("\n#### GRAPHS ####")
            mis = [group_centroid(c) for c in split_to_clusters(X, clusters, k)]
            #print("\t1. Not Link")
            plot2d(X, clusters, mis, negative_r, "red")
            #print("\t2. Must Link")
            #plot2d(X, clusters, mis, positive_r, "blue")
    
    print("\n\n######### MEAN #########")
    print("\t1. Mean time: {} ".format(round(mean_time / len(seeds), 2)))
    print("\t2. Mean deviation: {}".format(round(mean_deviation / len(seeds), 2)))
    print("\t3. Mean infeasability: {}".format(round(mean_tasa_inf / len(seeds), 2)))
    print("\t4. Mean agregado: {}".format(round(mean_agregado / len(seeds), 2)))

def main(name, n_constrictions, k):
    
    print("#########** {} **#########".format(name))
    # Load Dat File With Numpy
    path_dat = "./dataset/{}_set.dat".format(name)
    dataset = np.loadtxt(path_dat, dtype='f', delimiter=',')
    
    # Load Constrictions File With Numpy
    path_const = "./dataset/{}_set_const_{}.const".format(name, n_constrictions)
    constrictions = np.loadtxt(path_const, dtype='f', delimiter=',')
    
    seeds = [10, 11, 12, 13, 14]
    algoritm(
        "GREEDY",
        COPKM,
        seeds,
        dataset,
        constrictions,
        k
    )
    
    algoritm(
        "Local Search Best First",
        BL_PM,
        seeds,
        dataset,
        constrictions,
        k
    )
    
    algoritm(
        "Local Search Best Neighbour",
        BL_MV,
        seeds,
        dataset,
        constrictions,
        k
    )
    
    
print("#############################################")
main("rand", 10, 3)
print("#############################################")
main("iris", 10, 3)
print("#############################################")

main("ecoli", 10, 8)
print("#############################################")
main("newthyroid", 10, 3)
"""print("#############################################")
main("rand", 20, 3)
print("#############################################")
main("iris", 20, 3)
print("#############################################")
main("ecoli", 20, 8)
print("#############################################")
main("newthyroid", 20, 3)"""