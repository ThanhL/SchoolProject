# Basic Makfile for comp20003 AlgDS assignment 1, semester 2, 2014
#
# Directs compilation of  two dictionaries: dict1 and dict2
# Assumes separate files with main() functions for each
#
#
#
#
# This file organization can be changed, but the Makefile need to be
# changed correspondingly


dict1: dict1.o bst1.o 
	gcc -o dict1 dict1.o bst1.o 

dict2: dict2.o bst2.o
	gcc -o dict2 dict2.o bst2.o 

bst1.o: bst1.c bst1.h
	gcc -c -Wall bst1.c

bst2.o: bst2.c bst2.h
	gcc -c -Wall bst2.c

dict1.o: dict1.c dict.h
	gcc -c -Wall dict1.c

dict2.o: dict2.c dict.h
	gcc -c -Wall dict2.c

