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

def has_one(assigned_result, k):
    for i in range(k):
        if len(np.where(assigned_result == i)[0]) == 0:
            return False
    return True

################ 

def cluster_change(solution, k):
    result = solution.copy()
    n = len(solution)
    
    while True:
        # Generate Random Position index to mutate
        r = np.random.randint(n)
    
        result[r] = np.random.randint(k)
        # If the value is different and every cluster has one element.
        if solution[r] != result[r] or has_one(result, k): break;
        
        result[r] = solution[r]
        
    return result

def algorithm(name, f, search, max_f, seeds, X, R, k):
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
        
        clusters = f(X, search, max_f, positive_r, negative_r, k, flambda, seed)
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
        
        print("######### Iteration {} in {} s #########".format(i + 1, round(dt, 2)))
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
    
    if len(seeds) > 1:
        print("\n\n######### MEAN #########")
        print("\t1. Mean time: {} ".format(round(mean_time / len(seeds), 2)))
        print("\t2. Mean deviation: {}".format(round(mean_deviation / len(seeds), 2)))
        print("\t3. Mean infeasability: {}".format(round(mean_tasa_inf / len(seeds), 2)))
        print("\t4. Mean agregado: {}".format(round(mean_agregado / len(seeds), 2)))

    print("\n")

######### P1 ##################

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

def BL(X, assigned_result, f_max, positive_r, negative_r, k, flambda = 0.0):
    
    size = len(X)
    
    min_f = f(X, assigned_result, positive_r, negative_r, k, flambda)
    
    total_evaluation_f = 0
    found_better = True
    while total_evaluation_f < f_max and found_better:
        
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
            if total_evaluation_f >= f_max: break
    
    return assigned_result

#######################################
    
def generate(k, s, n):
    solutions = []
    for i in range(n):
        solution = np.random.randint(k, size = s)
        while not(has_one(solution, k)):
            solution = np.random.randint(k, size = s)
        
        solutions.append(solution)
    return solutions

#######################################

def ils_mutation(parent, k):
    
    solution = parent.copy()
    n = len(solution)
    
    r = np.random.randint(n)
    v = int(np.ceil(n * 0.1))
    
    # New Solution
    while True:
        mutations = np.random.randint(k, size = v)
        for i in range(v):
            solution[(r + i) % n] = mutations[i]
        
        # Check for the restrictions
        if has_one(solution, k) and not((solution == parent).all()): break
    
    return solution

#######################################

def cauchy_cooling(t, beta):
    return  t / (1 + beta * t)

def simple_cooling(t, beta): # beta = [0.9, 0.99]
    return beta * t

def ES(X, search, f_max, positive_r, negative_r, k, flambda = 0.0, seed = 10):
    
    random.seed(seed)
    np.random.seed(seed)
        
    size = len(X)
    
    f_it = 1
    
    # Generate initial Solution
    if search is None:
        s = generate(k, size, 1)[0]
    else:
        s = search
    
    s_f = f(X, s, positive_r, negative_r, k, flambda)
        
    best_s = s
    best_s_f = s_f
    
    
    # Initial Temperature Formulae
    # mu = Probability to accept the solution by one mu worst than the initial
    mu = phi = 0.3
    t0 = mu * s_f / -np.log(phi)
    
    # T0 -> initial Temperature
    # Tf -> final temperature ~ 0
    t = t0
    tf = 10e-3
    
    max_neighbours = 10 * size
    max_hits = 0.1 * max_neighbours
    
    # M -> Number of Coolings
    M = f_max / max_neighbours
    
    # Velocity decreasing probability of scaping from solution
    vk = 1
    while (t > tf) and (f_it < f_max):
        
        neighbours_generated = 0
        neighbours_hits = 0
        while ((f_it + neighbours_generated) < f_max) and (neighbours_generated < max_neighbours) and (neighbours_hits < max_hits):
            neighbours_generated += 1
            # Generate new Neighbour
            sp = cluster_change(s, k)
            sp_f = f(X, sp, positive_r, negative_r, k, flambda)
            # diff f value
            df = sp_f - s_f
            if (df < 0) or (np.random.uniform() < np.exp(-df / (vk * t))):
                neighbours_hits += 1
                # Replace last best result
                s = sp
                s_f = sp_f
                # Replace Best Solution
                if s_f < best_s_f:
                    best_s = s
                    best_s_f = s_f
        
        f_it += neighbours_generated
        
        # If the hits are 0, then just finish
        if neighbours_hits == 0: break;
        
        beta = (t0 - tf) / (M * t0 * tf)
        t = cauchy_cooling(t, beta)
    
    return best_s

def BMB(X, search, f_max, positive_r, negative_r, k, flambda = 0.0, seed = 10):
    
    random.seed(seed)
    np.random.seed(seed)
        
    size = len(X)
    
    # Generate initial Solution
    population = 10
    
    # 1 of 10
    s0 = generate(k, size, 1)[0]
    best = search(X, s0, f_max, positive_r, negative_r, k, flambda)
    best_f = f(X, best, positive_r, negative_r, k, flambda)
    
    # Rest
    for i in range(1, population):
        sn = generate(k, size, 1)[0]
        other = search(X, sn, f_max, positive_r, negative_r, k, flambda)
        other_f = f(X, other, positive_r, negative_r, k, flambda)
        
        if other_f < best_f:
            best_f = other_f
            best = other
    
    return best

def ILS(X, search, f_max, positive_r, negative_r, k, flambda = 0.0, seed = 10):
    
    random.seed(seed)
    np.random.seed(seed)
        
    size = len(X)
    
    # BL do generate an inital solution + optimizes it
    s0 = generate(k, size, 1)[0]
    s = search(X, s0, f_max, positive_r, negative_r, k, flambda)
    s_f = f(X, s, positive_r, negative_r, k, flambda)
    
    # 9 Iterations
    for i in range(0, 9):
        
        s_p = ils_mutation(s, k)
        s_pp = search(X, s_p, f_max, positive_r, negative_r, k, flambda)
        s_ppf = f(X, s_pp, positive_r, negative_r, k, flambda)
        
        # Best Criterion (Criterio del mejor)
        if s_ppf < s_f:
            s = s_pp
            s_f = s_ppf
    
    return s
    
    

def main(name, n_constrictions, k):
    
    print("#########** {} **#########".format(name))
    # Load Dat File With Numpy
    path_dat = "./dataset/{}_set.dat".format(name)
    dataset = np.loadtxt(path_dat, dtype='f', delimiter=',')
    
    # Load Constrictions File With Numpy
    path_const = "./dataset/{}_set_const_{}.const".format(name, n_constrictions)
    constrictions = np.loadtxt(path_const, dtype='f', delimiter=',')
    
    seeds = [10, 11, 12, 13, 14] 
    
    algorithm(
        "Enfriamento Simulado",
        ES,
        None,
        100000,
        seeds,
        dataset,
        constrictions,
        k
    )
    algorithm(
        "Búsqueda Multiarranque Básica",
        BMB,
        BL,
        10000,
        seeds,
        dataset,
        constrictions,
        k
    )
    algorithm(
        "Búsqueda Local Reiterada",
        ILS,
        BL,
        10000,
        seeds,
        dataset,
        constrictions,
        k
    )
    algorithm(
        "Búsqueda Local Reiterada Simulado",
        ILS,
        ES,
        10000,
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

print("#############################################")
main("rand", 20, 3)
print("#############################################")
main("iris", 20, 3)

print("#############################################")
main("ecoli", 20, 8)

print("#############################################")
main("newthyroid", 20, 3)

