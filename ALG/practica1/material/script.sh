#!/bin/bash
g++ -O2 ./tree.cpp -o ./tree.exe
rm tree_abb.dat
for i in `seq 0 10000 1000000`; do
  ./tree.exe $i >> tree_abb.dat
done;
