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

def generate_genome(k, s, n):
    genome = []
    for i in range(n):
        gen = np.random.randint(k, size = s)
        while not(has_one(gen, k)):
            gen = np.random.randint(k, size = s)
        
        genome.append(gen)
    return genome


def uniform_cross(chromosome1, chromosome2, k):
    n = len(chromosome1[0])
    
    # Positions
    # 1. Generate list from 0 - (n - 1)
    # 2. Shuffle the list
    random_position = random.sample(range(n), n)
    
    child = np.zeros(n, dtype = np.int8)
    # Use half left for p1
    half_left = random_position[:int(n/2)]
    for i in half_left:
        child[i] = chromosome1[0][i]
    
    # Use half right for p2
    half_right = random_position[int(n/2):]
    for i in half_right:
        child[i] = chromosome2[0][i]
    
    # Repair (Or Multiple)
    while not(has_one(child, k)):
        for i in range(k):
            if len(np.where(child == i)) == 0:
                child[np.random.randint(n)] = i
    
    return (child, None)


def fixed__cross(chromosome1, chromosome2, k):
    n = len(chromosome1[0])
    
    r, v = np.random.randint(n, size=2)
    
    # Two pointers
    i = r
    j = (r + v) % n
    
    # Two cases i >= j or i < j
    indxs = np.arange(n)
    p1_indxs = indxs[min(i,j):max(j,i)]
    p12_indxs = np.concatenate((indxs[0:min(i,j)], indxs[max(j,i):n]))
    if i > j:
        p12_indxs, p1_indxs = p1_indxs, p12_indxs
    
    child = np.zeros(n, dtype = np.int8)
    # Work on p1 from k to k+v
    for i in p1_indxs:
        child[i] = chromosome1[0][i]
    
    # Uniform
    # Shuffle Positions to imitate uniform selection
    random_position = random.sample(list(p12_indxs), len(p12_indxs))
    
    # Use half left for p1
    half_left = random_position[:int(n/2)]
    for i in half_left:
        child[i] = chromosome1[0][i]
    
    # Use half right for p2
    half_right = random_position[int(n/2):]
    for i in half_right:
        child[i] = chromosome2[0][i]
    
    # Repair (Or Multiple)
    while not(has_one(child, k)):
        for i in range(k):
            if len(np.where(child == i)) == 0:
                child[np.random.randint(n)] = i
    
    return (child, None)

def mutate_uniform(chromosome, k):
    n = len(chromosome)
    
    # Generate Random Position index to mutate
    r = np.random.randint(n)
    
    initial_gen = chromosome[r]
    
    chromosome[r] = np.random.randint(k)
    # Check that the clusters have at least one element and different than before
    while initial_gen == chromosome[r] or not(has_one(chromosome, k)):
        chromosome[r] = np.random.randint(k)
    
    return chromosome


def selection_tournament(genome, k):
    s = len(genome)
    # Generate k random values (2 -> Binary)
    sub_i = random.sample(range(s), k)
    # Get SubList
    sub_isorted = [genome[i] for i in sub_i]
    # Sort SubList
    sob_sorted = sorted(sub_isorted, key=lambda t: t[1])
    # Get best chromosome (Minimum f)
    return sob_sorted[0]

# EXTRA! But not added
def selection_roulette(genome, V, k):
    # Generate k random values (2 -> Binary)
    sub_i = np.random.sample(range(len(genome)), k)
    # Take Evaluations
    sub_V = np.take(V, sub_i)
    # Find Sorted Index List of evaluation
    sub_isorted = sub_V.argsort()
    # Sort both by evaluation
    sub_X = np.take(genome, sub_i)[sub_isorted]
    sub_V = sub_V[sub_isorted]
    
    # (max - f1) + ... + (max - fk) = (max * k - (f1 + ... + fk)) = sum(df)
    total_dv = (sub_V[-1] * k - np.sum(sub_V))
    # (max - fk) / sum(df) in [0, 1]
    i_interval = [(sub_V[-1] - v) / total_dv for v in sub_V]
    # Accumulate
    # [f1, f1+f2, ..., f1+...+fn] where f1+...+fn = 1.0, f1+...+fv < 1, v!=n
    i_sum = [np.sum(i_interval[:i]) for i in range(k)]
    
    # Random Selection (RS)
    prob = np.random.random()
    # Return Best Chromosome (Minimum f)
    return sub_X[np.where(i_sum <= prob)[0]]


def algoritm(name, f, population_size, selection_f, selection_k, cross_f, cross_p, mutate_f, mutate_p, elitism, seeds, X, R, k, ge_bls = 0.0, prob_bls = 0.0, to_best = False):
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
        
        clusters = f(X, positive_r, negative_r, k, population_size, selection_f, selection_k, cross_f, cross_p, mutate_f, mutate_p, elitism, flambda, seed, ge_bls, prob_bls, to_best)
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

#######################################

def AGG(X, positive_r, negative_r, k, population_size, selection_f, selection_k, cross_f, cross_p, mutate_f, mutate_p, elitism, flambda = 0.0, seed = 10, ge_bls = 0.0, prob_bls = 0.0, to_best = False):
    
    random.seed(seed)
    np.random.seed(seed)
        
    size = len(X)
    
    # initial
    initial_genome = generate_genome(k, size, population_size)
    genes_evaluation = [f(X, assigned, positive_r, negative_r, k, flambda) for assigned in initial_genome]
    sorted_genes = np.argsort(genes_evaluation)
    
    # Pair Gene, Value
    genome = list(zip(initial_genome, genes_evaluation))
    
    # Maximum Number of executions of F
    f_max = 100000
    
    # Mutation Hope Variables
    have_to_mutate = population_size * size * mutate_p
    
    # Count First Nth mutations
    f_executions = population_size
    
    while f_executions < f_max:
        # Selection
        selected_genome = [None] * population_size
        for j in range(population_size):
            selected_genome[j] = selection_f(genome, selection_k)
        
        # Cross
        new_genome = [None] * population_size
        for j in range(int(population_size / 2)):
            
            chr_i = random.sample(range(population_size), 2)
            chr1, chr2 = [selected_genome[i] for i in chr_i]
            
            p = np.random.uniform()
            if p < cross_p:
                chr1, chr2 = cross_f(chr1, chr2, k), cross_f(chr1, chr2, k) 
            
            new_genome[2 * j + 0] = chr1[0]
            new_genome[2 * j + 1] = chr2[0]
        
        # Mutate elements by mathematic hope
        if have_to_mutate >= 1:
            number_mutations = int(have_to_mutate)
            have_to_mutate -= number_mutations
            mutations = np.random.randint(population_size, size = number_mutations)
            for mutation_idx in mutations:
                new_genome[mutation_idx] = mutate_f(new_genome[mutation_idx], k)
        
        # Calculate new Genes Evaluations
        new_genes_evaluation = [f(X, assigned, positive_r, negative_r, k, flambda) for assigned in new_genome]
                
        # Elitism Replacement
        if elitism > 0:
            new_sorted = np.argsort(new_genes_evaluation)
            for i in range(1, elitism + 1):
                new_genome[new_sorted[-i]] = genome[sorted_genes[i - 1]][0]
                new_genes_evaluation[new_sorted[-i]] = genome[sorted_genes[i - 1]][1]
             # Re-Sort Array
            sorted_genes = np.argsort(new_genes_evaluation)
        
        # Genome Replacement
        genome = list(zip(new_genome, new_genes_evaluation))
        
        have_to_mutate += population_size * size * mutate_p
        f_executions += population_size
    
    # Return Best Chromosome
    return sorted(genome, key=lambda t: t[1])[0][0]

#######################################

def AGE(X, positive_r, negative_r, k, population_size, selection_f, selection_k, cross_f, cross_p, mutate_f, mutate_p, elitism, flambda = 0.0, seed = 10, ge_bls = 0.0, prob_bls = 0.0, to_best = False):
    
    random.seed(seed)
    np.random.seed(seed)
        
    size = len(X)
    
    # initial
    initial_genome = generate_genome(k, size, population_size)
    genes_evaluation = [f(X, assigned, positive_r, negative_r, k, flambda) for assigned in initial_genome]
    sorted_genes = np.argsort(genes_evaluation)
    
    # Pair Gene, Value
    genome = list(zip(initial_genome, genes_evaluation))
    
    # Maximum Number of executions of F
    f_max = 100000
    
    # Mutation Hope Variables
    have_to_mutate = 2 * size * mutate_p
    
    # Count First Nth mutations
    f_executions = population_size
    
    while f_executions < f_max:
        # Selection
        chromosome1, chromosome2 = selection_f(genome, selection_k), selection_f(genome, selection_k)
        
        # Cross (Get Two Childs)
        new_chromosome1, new_chromosome2 = cross_f(chromosome1, chromosome2, k)[0], cross_f(chromosome1, chromosome2, k)[0] 
        
        # Mutate 1 element by mathematic hope each f_evaluations iterations
        if have_to_mutate >= 1:
            number_mutations = int(have_to_mutate)
            have_to_mutate -= number_mutations
            for i in range(number_mutations):
                if np.random.randint(2) == 0:
                    new_chromosome1 = mutate_f(new_chromosome1, k)
                else:
                    new_chromosome2 = mutate_f(new_chromosome2, k)
        
        # Calculate new Genes Evaluations
        ev_chromosome1 = f(X, new_chromosome1, positive_r, negative_r, k, flambda)
        ev_chromosome2 = f(X, new_chromosome2, positive_r, negative_r, k, flambda)
                
        # Replace If they are better the worst before
        # Swap Elements if one is smaller to the other
        if ev_chromosome2 < ev_chromosome1:
            ev_chromosome1, ev_chromosome2 = ev_chromosome2, ev_chromosome1
            new_chromosome1, new_chromosome2 = new_chromosome2, new_chromosome1
        
        # Because of order, we can just check 2 conditions and 3 changes.
        if ev_chromosome1 < genome[sorted_genes[-1]][1]:
            genes_evaluation[sorted_genes[-1]] = ev_chromosome1
            genome[sorted_genes[-1]] = (new_chromosome1, ev_chromosome1)
            # Check for the next element (Because of the order, we can just check one)
            if ev_chromosome2 < genome[sorted_genes[-2]][1]:
                genes_evaluation[sorted_genes[-2]] = ev_chromosome2
                genome[sorted_genes[-2]] = (new_chromosome2, ev_chromosome2)
        elif ev_chromosome1 < genome[sorted_genes[-2]][1]:
            genes_evaluation[sorted_genes[-2]] = ev_chromosome1
            genome[sorted_genes[-2]] = (new_chromosome1, ev_chromosome1)
        
        # Re-Sort Elements
        sorted_genes = np.argsort(genes_evaluation)
        
        have_to_mutate += 2 * size * mutate_p
        f_executions += 2
    
    # Return Best Chromosome
    return sorted(genome, key=lambda t: t[1])[0][0]    

#######################################

def BLS(X, positive_r, negative_r, k, assigned_result, f_curr_executions, flambda = 0.0, per_error = 0):
    
    n = len(X)
    RSI = random.sample(range(n), n)
    
    # Error = % * N
    error = n * per_error
    
    # Initialization
    min_f = f(X, assigned_result, positive_r, negative_r, k, flambda)
    
    fails = 0
    found_better = True
    i = 0
    f_executions = f_curr_executions
    while (found_better or fails < error) and i < n and f_executions < 100000:
        
        found_better = False
        
        best_m = -1
        best_f = 1e10

        # Found Better Possibilities        
        old_m = assigned_result[RSI[i]]
        for m in range(k):
            assigned_result[RSI[i]] = m
            # Distinct and Validity
            if old_m != m and has_one(assigned_result, k):
               nbest_f = f(X, assigned_result, positive_r, negative_r, k, flambda)
               f_executions += 1
               if nbest_f < best_f:
                   best_m = m
                   best_f = nbest_f
            assigned_result[RSI[i]] = old_m
            
            # Check if out of limit
            if f_executions >= 100000: break;
        
        # Replace if better
        if best_f < min_f:
            min_f = best_f
            assigned_result[RSI[i]] = best_m
        else:
            fails += 1
        
        i += 1
    
    return (assigned_result, min_f, f_executions)

def AM(X, positive_r, negative_r, k, population_size, selection_f, selection_k, cross_f, cross_p, mutate_f, mutate_p, elitism, flambda = 0.0, seed = 10, ge_bls = 0, prob_bls = 0.0, to_best = False):
    
    random.seed(seed)
    np.random.seed(seed)
        
    size = len(X)
    
    # initial
    initial_genome = generate_genome(k, size, population_size)
    genes_evaluation = [f(X, assigned, positive_r, negative_r, k, flambda) for assigned in initial_genome]
    sorted_genes = np.argsort(genes_evaluation)
    
    # Pair Gene, Value
    genome = list(zip(initial_genome, genes_evaluation))
    
    # Maximum Number of executions of F
    f_max = 100000
    
    # Mutation Hope Variables
    have_to_mutate = 2 * size * mutate_p
    
    # Count First Nth mutations
    f_executions = population_size
    
    # Number of generations
    bls_its = 0
    while f_executions < f_max:
        if bls_its >= ge_bls: # BLS
            bls_its = 0
            
            population_representation = int(prob_bls * population_size)
            to_explore_idx = sorted_genes[:population_representation] if to_best else random.sample(list(sorted_genes), population_representation)
            
            # Search
            for i in to_explore_idx:
                new_gene, new_f_value, new_f_executions = BLS(X, positive_r, negative_r, k, genome[i][0], f_executions, flambda, 0.1)
                genes_evaluation[i] = new_f_value
                genome[i] = (new_gene, new_f_value)
                f_executions = new_f_executions
                
            
        else: # AGE
            # Icrement BLS Iteration
            bls_its += 1
            # Selection
            chromosome1, chromosome2 = selection_f(genome, selection_k), selection_f(genome, selection_k)
            
            # Cross (Get Two Childs)
            new_chromosome1, new_chromosome2 = cross_f(chromosome1, chromosome2, k)[0], cross_f(chromosome1, chromosome2, k)[0] 
            
            
            # Mutate 1 element by mathematic hope each f_evaluations iterations
            if have_to_mutate >= 1:
                number_mutations = int(have_to_mutate)
                have_to_mutate -= number_mutations
                for i in range(number_mutations):
                    if np.random.randint(2) == 0:
                        new_chromosome1 = mutate_f(new_chromosome1, k)
                    else:
                        new_chromosome2 = mutate_f(new_chromosome2, k)
            
            # Calculate new Genes Evaluations
            ev_chromosome1 = f(X, new_chromosome1, positive_r, negative_r, k, flambda)
            ev_chromosome2 = f(X, new_chromosome2, positive_r, negative_r, k, flambda)
                    
            # Replace If they are better the worst before
            # Swap Elements if one is smaller to the other
            if ev_chromosome2 < ev_chromosome1:
                ev_chromosome1, ev_chromosome2 = ev_chromosome2, ev_chromosome1
                new_chromosome1, new_chromosome2 = new_chromosome2, new_chromosome1
            
            # Because of order, we can just check 2 conditions and 3 changes.
            if ev_chromosome1 < genome[sorted_genes[-1]][1]:
                genes_evaluation[sorted_genes[-1]] = ev_chromosome1
                genome[sorted_genes[-1]] = (new_chromosome1, ev_chromosome1)
                # Check for the next element (Because of the order, we can just check one)
                if ev_chromosome2 < genome[sorted_genes[-2]][1]:
                    genes_evaluation[sorted_genes[-2]] = ev_chromosome2
                    genome[sorted_genes[-2]] = (new_chromosome2, ev_chromosome2)
            elif ev_chromosome1 < genome[sorted_genes[-2]][1]:
                genes_evaluation[sorted_genes[-2]] = ev_chromosome1
                genome[sorted_genes[-2]] = (new_chromosome1, ev_chromosome1)
            
            have_to_mutate += 2 * size * mutate_p
        
        # Re-Sort Elements
        sorted_genes = np.argsort(genes_evaluation)
        f_executions += 2
    
    # Return Best Chromosome
    return sorted(genome, key=lambda t: t[1])[0][0]

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
        "AGG-Uniform",
        AGG,
        50, # Population size
        selection_tournament, 
        2, # Number os selection
        uniform_cross,
        0.7, # Cross Probability
        mutate_uniform,
        0.001, # Mutation Probability
        1, # elitism
        seeds,
        dataset,
        constrictions,
        k
    )
    
    algoritm(
        "AGG-Position",
        AGG,
        50, # Population size
        selection_tournament, 
        2, # Number os selection
        fixed__cross,
        0.7, # Cross Probability
        mutate_uniform,
        0.001, # Mutation Probability
        1, # elitism
        seeds,
        dataset,
        constrictions,
        k
    )
    algoritm(
        "AGE-Uniform",
        AGE,
        50, # Population size
        selection_tournament, 
        2, # Number os selection
        uniform_cross,
        1.0, # Cross Probability
        mutate_uniform,
        0.001, # Mutation Probability
        1, # elitism
        seeds,
        dataset,
        constrictions,
        k
    )
    
    algoritm(
        "AGE-Position",
        AGE,
        50, # Population size
        selection_tournament, 
        2, # Number os selection
        fixed__cross,
        1.0, # Cross Probability
        mutate_uniform,
        0.001, # Mutation Probability
        1, # elitism
        seeds,
        dataset,
        constrictions,
        k
    )
    # Best was AGE
    """
    algoritm(
        "AM-10,1.0",
        AM,
        10, # Population size
        selection_tournament, 
        2, # Number os selection
        fixed__cross,
        1.0, # Cross Probability
        mutate_uniform,
        0.001, # Mutation Probability
        1, # elitism
        seeds,
        dataset,
        constrictions,
        k,
        ge_bls = 10, 
        prob_bls = 1.0
    )
    
    algoritm(
        "AM-10,0.1",
        AM,
        10, # Population size
        selection_tournament, 
        2, # Number os selection
        fixed__cross,
        1.0, # Cross Probability
        mutate_uniform,
        0.001, # Mutation Probability
        1, # elitism
        seeds,
        dataset,
        constrictions,
        k,
        ge_bls = 10, 
        prob_bls = 0.1
    )
    
    algoritm(
        "AM-10,0.1mej",
        AM,
        10, # Population size
        selection_tournament, 
        2, # Number os selection
        fixed__cross,
        1.0, # Cross Probability
        mutate_uniform,
        0.001, # Mutation Probability
        1, # elitism
        seeds,
        dataset,
        constrictions,
        k,
        ge_bls = 10, 
        prob_bls = 0.1,
        to_best = True
    )
    """


"""
print("#############################################")
main("rand", 10, 3)
print("#############################################")
"""
main("iris", 10, 3)

"""
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
"""