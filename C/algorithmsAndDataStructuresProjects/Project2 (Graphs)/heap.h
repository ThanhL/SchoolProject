/*********************************************************************/
/*             Algorithms and Data Structures- Assignment 2          */
/*             Name: Thanh Le         Student ID: 574897             */
/*********************************************************************/


/* Note: Priority Queue implementation have been built upon the logic of 
Steven S. Skiena, The Algorithm Design Manual */

/* Libraries included */
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

/* priority queues functions */
priorityQueue_t *makePQ( graph_t *g );
void insertPQ( priorityQueue_t *q, int vertex, int dist );
void downheap( priorityQueue_t *pq, int position );
void heapify( priorityQueue_t *pq, int position );
heap_node_t deletemin( priorityQueue_t *pq );
void pq_swap(  heap_node_t *a, heap_node_t *b );
int getChild( int n );
int getParent( int n );
int emptyPQ( priorityQueue_t *pq );

/* heap testers */
void printPriorityQueue( priorityQueue_t *pq );
