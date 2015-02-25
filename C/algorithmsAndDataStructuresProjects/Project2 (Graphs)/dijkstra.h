/*********************************************************************/
/*             Algorithms and Data Structures- Assignment 2          */
/*             Name: Thanh Le         Student ID: 574897             */
/*********************************************************************/

/* Note: Dijkstra Algorithm implementation have been built upon by the lecture
material covered in Data Structures + Algorithm. COMP20003 */

/* Library files included */
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

/* typedefs */

/* heap node */
typedef struct {
	int vertex;
	int dist;
} heap_node_t;

/* the heap */
typedef struct {
	int size;  /* number of queue elements */
	int *pos; /* position of vertex in priority queue,use for decreaseKey */
	heap_node_t *queue; /* the priority queue */
} priorityQueue_t;


/* dijkstra algorithm */
void dijkstra( graph_t *g, data_t *userInput, lookup_t *l );
void initialize( graph_t *g, int nvertices, int src, int pred[], int dist[] );
void run( graph_t *g, int nvertices, int src, int pred[], int dist[] );
void update( int v, int u, int pred[], int dist[], priorityQueue_t *pq, 
	int weight );
void decreaseKey( priorityQueue_t *pq, int v, int dist );
void findUserPath( graph_t *g, data_t *userInput, int dist[], int pred[], 
	lookup_t *l );
void findPathPrint( lookup_t *l, int src, int dest, int pred[] );

/* dijkstra testers */
void printArr(int dist[], int pred[], int n);

