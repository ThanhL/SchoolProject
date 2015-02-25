/*********************************************************************/
/*             Algorithms and Data Structures- Assignment 2          */
/*             Name: Thanh Le         Student ID: 574897             */
/*********************************************************************/

/* Note: Adjacency List implementation have been built upon the logic of 
Steven S. Skiena, The Algorithm Design Manual */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAXFIELDLEN 80 /* max string length */
#define MAXLINELEN 256 /* Max length for line */

#define MAXV 1000         /*maximum number of vertices*/

/* data structure to store the information of each node */
/* Note: this is the data from the place files */
typedef struct {
	int placeID;
	char *placeName;
	char *continent;
	char cultural;
	char outdoors;
} data_t;
	

/* Structure prototypes */

/* graph node s */
typedef struct edgenode {
	data_t data;              /* data of the node */
	int weight;               /* the weight from a vertex to this node */
	struct edgenode *next;    /* the next node linked to it */
} edgenode_t;

/* graph struct */
typedef struct {
	edgenode_t *array[ MAXV + 1 ];   /* adjacency info */
	int nvertices;             /* number of vertices in graph */
	int nedges;                /* number of edges in graph */
} graph_t;

/* struct for looking up data items */
typedef struct {
	edgenode_t *lookup;   /* arrray for lookup */
	int size;             /* size of the lookup array */
} lookup_t;

/* function declarations */
graph_t *constructGraph( FILE *fpCost, lookup_t *l );
void initialize_graph( graph_t *g );
void insert_edge( graph_t *g, int src, int dest, int cost, lookup_t *l );
void read_graph( graph_t *g, FILE *fpCost, lookup_t *l );
lookup_t *getlookUp( FILE *fpPlace );

void printGraph( graph_t *g );
