# -*- coding: utf-8 -*-
"""
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

# Búsqueda Local
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

def LocalSearc_Basic(X, assigned_result, f_max, positive_r, negative_r, k, flambda = 0.0):

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

def algorithm(name, seeds, X, R, alpha, beta, k):
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
    
        # Set Seed
        random.seed(seed)
        np.random.seed(seed)
        
        
        clusters = BS(X, positive_r, negative_r, k, flambda, alpha, beta)
                     
        
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
    
def generate(k, s, n):
    solutions = []
    for i in range(n):
        solution = np.random.randint(k, size = s)
        while not(has_one(solution, k)):
            solution = np.random.randint(k, size = s)
        
        solutions.append(solution)
    return solutions

############## GENETIC ALGORITHM #########################

def selection_tournament(genome, k):
    population = len(genome)
    r, v = np.random.randint(population, size = k)
    while(r == v): r, v = np.random.randint(population, size = k)
    return genome[r] if genome[r][1] < genome[v][1] else genome[v]

def uniform_cross(chromosome1, chromosome2, k):
    n = len(chromosome1)
    
    # Positions
    # 1. Generate list from 0 - (n - 1)
    # 2. Shuffle the list
    random_position = random.sample(range(n), n)
    
    child = np.zeros(n, dtype = np.int8)
    # Use half left for p1
    half_left = random_position[:int(n/2)]
    for i in half_left:
        child[i] = chromosome1[i]
    
    # Use half right for p2
    half_right = random_position[int(n/2):]
    for i in half_right:
        child[i] = chromosome2[i]
    
    # Repair (Or Multiple)
    while not(has_one(child, k)):
        for i in range(k):
            if len(np.where(child == i)) == 0:
                child[np.random.randint(n)] = i
    
    return (child, None)

def fixed_cross(chromosome1, chromosome2, k):
    n = len(chromosome1)
    
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
        child[i] = chromosome1[i]
    
    # Uniform
    # Shuffle Positions to imitate uniform selection
    random_position = random.sample(list(p12_indxs), len(p12_indxs))
    
    # Use half left for p1
    half_left = random_position[:int(n/2)]
    for i in half_left:
        child[i] = chromosome1[i]
    
    # Use half right for p2
    half_right = random_position[int(n/2):]
    for i in half_right:
        child[i] = chromosome2[i]
    
    # Repair (Or Multiple)
    while not(has_one(child, k)):
        for i in range(k):
            if len(np.where(child == i)) == 0:
                child[np.random.randint(n)] = i
    
    return (child, None)

def mutation_uniform(chromosome, k):
    n = len(chromosome)
    
    # New Solution
    while True:
        r = np.random.randint(n)
        old = chromosome[r]
        
        new = np.random.randint(k)
        while(old == new):
            new = np.random.randint(k)
        
        chromosome[r] = new
        # Check for validity
        if has_one(chromosome, k):
            break
        
        chromosome[r] = old
    
    return (chromosome, None)

########################################
def PSM(X1, X2, X3, n_clusters, alpha, beta):
    
    X0 = (X1 + X2) * 0.5;
    # Reflexion
    # ratio
    Xref = (1 + alpha) * X0 - alpha * X3
    for i in range(len(X1)):
        Xref[i] = max(min(Xref[i], n_clusters - 1), 0)
    
    # Contraction
    Xcont = (1 - beta) * X0 + beta * X3
    for i in range(len(X1)):
        Xcont[i] = max(min(Xcont[i], n_clusters - 1), 0)
    
    Xref = np.int_(np.round_(Xref))
    Xcont = np.int_(np.round_(Xcont))
    
    # Fixing Chromosomes (Add Explotation)
    for i in range(n_clusters):
        if len(np.where(Xref == i)[0]) == 0:
            Xref[np.random.randint(len(X1))] = i
        
        if len(np.where(Xcont == i)[0]) == 0:
            Xcont[np.random.randint(len(X1))] = i
    
    return X1, Xref, Xcont

def AlgoritmoEvolutivo(genome, cross_p, mutate_p, n_clusters):
    population_size = len(genome)
    chromosome_size = len(genome[0])
    
    # Selection
    new_genome = [None] * population_size
    for i in range(population_size):
        new_genome[i] = selection_tournament(genome, 2)
    
    # Crossover
    for i in range(0, population_size, 2):
        chr1 = new_genome[i + 0]
        chr2 = new_genome[i + 1]
        if np.random.uniform() < cross_p:
            new_genome[i + 0] = uniform_cross(chr1[0], chr2[0], n_clusters)
            new_genome[i + 1] = uniform_cross(chr1[0], chr2[0], n_clusters)
        else:
            new_genome[i + 0] = chr1
            new_genome[i + 1] = chr2
    
    # Mutation
    number_mutations = int(population_size * chromosome_size * mutate_p)
    mutations = np.random.randint(population_size, size = number_mutations)
    for mutation_idx in mutations:
        new_genome[mutation_idx] = mutation_uniform(new_genome[mutation_idx][0], n_clusters)

    
    return new_genome

def BS(X, positive_r, negative_r, n_clusters, flambda, alpha, beta):
    
    size = len(X)
    
    # Papper Common Constants
    cross_p = 0.70
    # Paper Constants Global Search
    n = 3
    Gsc = 20
    population_size = 10
    mutate_p = 0.05
    
    migration_count = 5 # Gmig
    
    #
    print("1. Global search")
    #
    superior_chromosomes, f_eval = GlobalSearch(X, positive_r, negative_r, n_clusters, flambda, cross_p, mutate_p, population_size, n, Gsc)
    
    # Paper Constants Local Search
    mutate_l = 0.05
    
    # Generate Local Population
    f_eval_populations = [0] * n
    local_populations = [None] * n
    index_local_populations = [None] * n
    #
    print("2. Cross Superior Chromosome with initial population")
    #
    for i in range(n):
        local_population = generate(n_clusters, size, population_size)
        
        for j in range(population_size):
            local_population[j] = fixed_cross(local_population[j], superior_chromosomes[i][0], n_clusters)
        
        # Evaluate
        local_population = list(map(lambda cr: (cr[0], f(X, cr[0], positive_r, negative_r, n_clusters, flambda) if cr[1] == None else cr[1]), local_population))
        index_local_population = np.argsort([gene[1] for gene in local_population])

        local_populations[i] = local_population
        index_local_populations[i] = index_local_population
        f_eval_populations[i] = f_eval_populations[i] + population_size
    
    max_f_eval = 100000
    
    migration = 0
    fval_max_partition = np.ceil((max_f_eval - np.sum(f_eval_populations)) / n)
    #
    print("3. Apply Local Search")
    #
    while np.sum(f_eval_populations) < (max_f_eval - f_eval):
        for i in range(n):
            # Population completely been inspected
            if f_eval_populations[i] < fval_max_partition:
                # Proporsal: Migration could controllated.
                migration_chromosome = None
                if migration >= migration_count:
                    migration = 0
                    index = np.random.randint(population_size)
                    migration_chromosome = local_populations[np.mod(i - 1, n)][index]
                    
                # Apply Search Algorithm
                new_population, index_new_population, f_eloval = LocalSearch(
                    X,
                    positive_r,
                    negative_r,
                    n_clusters,
                    flambda,
                    local_populations[i],
                    index_local_populations[i],
                    migration_chromosome,
                    cross_p,
                    mutate_l,
                    alpha,
                    beta
                )
                
                local_populations[i] = new_population
                index_local_populations[i] = index_new_population
                
                f_eval_populations[i] = f_eval_populations[i] + f_eloval
        
        migration += 1
    
    # Return best chromosome of local populations
    best_chromosome_in_each_local_population = [local_populations[i][index_local_populations[i][0]] for genome in range(n)]
    best_global_chromosome = min(best_chromosome_in_each_local_population, key=lambda t: t[1])
    return best_global_chromosome[0]
    

def GlobalSearch(X, positive_r, negative_r, n_clusters, flambda, cross_p, mutate_p, population_size, n, Gsc):
    
    size = len(X)
    
    superior_chromosomes = []
    for k in range(n):
        
        s = generate(n_clusters, size, population_size)
        genome = list(zip(s, [None] * population_size))
        
        best_chromosome = None
        max_fitness = 1e10
        
        msc = 0
        f_it = 0
        while True:
            
            genome = list(map(lambda cr: (cr[0], f(X, cr[0], positive_r, negative_r, n_clusters, flambda) if cr[1] == None else cr[1]), genome))
            
            # Increment Number of calls
            f_it += population_size
            
            current_best = min(genome, key=lambda t: t[1])
            
            # We are climbing to an optimal solution, keep searching
            if current_best[1] < max_fitness:
                msc = 0
                best_chromosome = current_best
                max_fitness = current_best[1]
            else:
                msc += 1
                if msc == Gsc: break
            
            # Replace
            genome = AlgoritmoEvolutivo(genome, cross_p, mutate_p, n_clusters)
        
        superior_chromosomes.append(best_chromosome)
    
    return superior_chromosomes, f_it

# Migration
def DefaultMigrationAlgorithm(genome, migration, index_sort):
    # Random Migration
    index = np.random.randint(len(genome))
    genome[index] = migration
    return genome
    
def OptimizedMigrationAlgorithm(genome, migration, index_sort):
    # Migration To the worst if it is better
    if migration[1] < genome[index_sort[-1]][1]:
        genome[index_sort[-1]] = migration
    return genome

# PSM Algorithm
def DefaultPSM(X, positive_r, negative_r, genome, index_sort, X1, X2, X3, n_clusters, flambda, alpha, beta):
    population_size = len(genome)
    
    # Pseudo Simplex Method (3 Best Chromosomes)
    xs = PSM(X1, X2, X3, n_clusters, alpha, beta)
    
    # Replace with PSM
    for x in xs:
        xfval = f(X, x, positive_r, negative_r, n_clusters, flambda)
        index = np.random.randint(population_size)
        genome[index] = (x, xfval)
    return genome, 3

def OptimizedPSM(X, positive_r, negative_r, genome, index_sort, X1, X2, X3, n_clusters, flambda, alpha, beta):    
    # Pseudo Simplex Method (3 Best Chromosomes)
    xs = PSM(X1, X2, X3, n_clusters, alpha, beta)
    
    # Replace with PSM        
    index_replacement = 0
    f_it = 0
    for x in xs:
        xfval = f(X, x, positive_r, negative_r, n_clusters, flambda)
        if xfval < genome[index_sort[index_replacement - 1]][1]:
            f_it = f_it + 1
            index_replacement = index_replacement - 1
            genome[index_sort[index_replacement]] = (x, xfval)
    
    return genome, f_it

# Local Search
def LocalSearch(X, positive_r, negative_r, n_clusters, flambda, local_population, index_sort, migration_chromosome, cross_p,  mutate_p, alpha, beta):
    
    population_size = len(local_population)
    
    # Mutation Parameters
    new_genome = AlgoritmoEvolutivo(local_population, cross_p, mutate_p, n_clusters)
    
    # Evaluate
    new_genome = list(map(lambda cr: (cr[0], f(X, cr[0], positive_r, negative_r, n_clusters, flambda) if cr[1] == None else cr[1]), new_genome))
    new_index_sort = np.argsort([gene[1] for gene in new_genome])
    
    # Set Number of Evaluations
    f_total_eval = population_size
    
    if migration_chromosome is not None:
        new_genome = OptimizedMigrationAlgorithm(new_genome, migration_chromosome, new_index_sort)
        new_index_sort = np.argsort([gene[1] for gene in new_genome]) # Re-Calculate
    
    # Pseudo Simplex Method (3 Best Chromosomes)
    new_genome, f_eval = OptimizedPSM(
        X,
        positive_r,
        negative_r,
        new_genome,
        new_index_sort,
        local_population[index_sort[0]][0],
        local_population[index_sort[1]][0],
        local_population[index_sort[2]][0],
        n_clusters,
        flambda,
        alpha,
        beta
    )
    
    f_total_eval = f_total_eval + f_eval
    
    # Re-Calculate Indexes for Migration and Genome
    new_index_sort = np.argsort([gene[1] for gene in new_genome])
    
    return new_genome, new_index_sort, f_total_eval
    


def main(name, n_constrictions, k, alpha, beta):
    
    print("#########** {} **#########".format(name))
    # Load Dat File With Numpy
    path_dat = "./dataset/{}_set.dat".format(name)
    dataset = np.loadtxt(path_dat, dtype='f', delimiter=',')
    
    # Load Constrictions File With Numpy
    path_const = "./dataset/{}_set_const_{}.const".format(name, n_constrictions)
    constrictions = np.loadtxt(path_const, dtype='f', delimiter=',')
    
    seeds = [10, 11, 12, 13, 14] 
    
    algorithm(
        "Algoritmo de Abejas",
        seeds,
        dataset,
        constrictions,
        alpha,
        beta,
        k
    )
   
print("#############################################")
main("rand", 10, 3, 1/3, 1/3)

print("#############################################")
main("iris", 10, 3, 1/3, 1/3)

print("#############################################")
main("ecoli", 10, 8, 1/3, 1/3)

print("#############################################")
main("newthyroid", 10, 3, 1/3, 1/3)

print("#############################################")
main("rand", 20, 3, 1/3, 1/3)
print("#############################################")
main("iris", 20, 3, 1/3, 1/3)

print("#############################################")
main("ecoli", 20, 8, 1/3, 1/3)

print("#############################################")
main("newthyroid", 20, 3, 1/3, 1/3)
