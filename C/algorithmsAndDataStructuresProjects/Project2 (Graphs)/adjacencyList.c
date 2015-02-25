/*********************************************************************/
/*             Algorithms and Data Structures- Assignment 2          */
/*             Name: Thanh Le         Student ID: 574897             */
/*********************************************************************/

/* Header included */
#include "adjacencyList.h"

/* initialize the graph */
void
initialize_graph( graph_t *g ) {
	int i; /* counter */
	
	/* allocate starting vertices and edges */
	g->nvertices = 0;
	g->nedges = 0;
	
	/* assign each node in the graph to NULL(initial state) */
	for (i=1; i<=MAXV; i++) 
		g->array[i] = NULL;
}


/* NOTE: INSERTS AT HEAD */
/* Adds an undirected edge to an undirected graph */
/* Adds an edge from src to dest. */
void
insert_edge( graph_t *g, int src, int dest, int cost, lookup_t *l ) {
	
	edgenode_t *p; /* temporary pointer */
	
	/* allocate node space */
	p = malloc( sizeof(edgenode_t) ); 
	
	p->weight = cost;            /* allocate travel cost */
	
	/*look up corresponding data and allocate*/
	p->data = l->lookup[dest].data;  

	p->next = g->array[src];     /* allocate the next pointer to the list */
	
	g->array[src] = p;           /* insert the node to the head of list */
	
	g->nedges = g->nedges + 1;   /* number of edges increased by 1 */
	
}

/* constructs the graph with the array of graph node data and cost files */
graph_t
*constructGraph( FILE *fpCost, lookup_t *l ) {
	/* allocate memory for the graph */
	graph_t *g = malloc(sizeof(graph_t));
	
	/* initialize the graph */
	initialize_graph( g );
	
	/* now the size of the lookup array corresponds to the number of 
	vertices. NOTE: No.Records = No.Graph vertices */
	g->nvertices = l->size;
	
	/* insert all the edges */
	read_graph( g, fpCost, l );
	
	return g;
}

/* Takes the graph, and insert the proper edges with the cost file and the
corresponding data of each node with the lookup array */
void
read_graph( graph_t *g, FILE *fpCost, lookup_t *l ) {
	char line[MAXLINELEN];
	
	int placeID_1;
	int placeID_2;
	int cost;
	
	while( fgets(line, sizeof(line), fpCost ) ){
		
		/* get field into buffers */
		if ( sscanf(line, "%d %d %d", &placeID_1, &placeID_2, &cost ) 
			!= 3 ) {
			printf("Number of fields are not correct in cost file"); 
			printf(".\nExitting\n");
			exit(EXIT_FAILURE);
		}
		insert_edge( g, placeID_1, placeID_2, cost, l);
		
	}
	

}

/* function uses the place file and stores the date of each graph node */
lookup_t
*getlookUp( FILE *fpPlace ) {
	int i = 0; /*counter*/
	
	/* variables to be stored in each scan */
	char line[MAXLINELEN];
	data_t data;
	
	int placeID;
	char placeName[MAXFIELDLEN];
	char continent[MAXFIELDLEN];
	char cultural;
	char outdoors;
	
	lookup_t *dataArray; /* array with all vertex information */
	
	/* temporary pointers for strings */
	char *placeNameTemp;
	char *continentTemp;
	
	/* allocate memory for lookup array */
	dataArray = malloc( sizeof(lookup_t) );
	
	/* scan  the first line. NOTE: the no. of records = no. of vertices */
	fgets(line, sizeof(line), fpPlace);
	if ( sscanf(line, "%d", &(dataArray->size)) != 1 ) {
		printf("Error reading number of records. File maybe empty.\n");
		printf("Exitting\n");
		exit(EXIT_FAILURE);
	}

	/* allocate memory for information of all vertices */
	dataArray->lookup = malloc(sizeof(edgenode_t) * dataArray->size +20 );

	while (fgets(line, sizeof(line), fpPlace)) {
		if ( sscanf(line, "%d %s %s %c %c",
			&placeID,
			placeName,
			continent,
			&cultural,
			&outdoors) != 5) {
		printf("Number of fields are not correct.\n");
		printf("Exitting\n");
		exit( EXIT_FAILURE );
			}
		
		i++; /* increment data */

		/* make space for string */
		placeNameTemp = malloc(strlen(placeName) + 1);
		continentTemp = malloc(strlen(continent) + 1);
		/* assign data */
		data.placeID = placeID;
		data.placeName = strcpy( placeNameTemp, placeName );
		data.continent = strcpy( continentTemp, continent );
		data.cultural = cultural;
		data.outdoors = outdoors;
		/* now place this data into array */
		dataArray->lookup[i].data = data;
	
	}
	
	
	/* error check the file, if the no. of records do not match exit */
	if ( dataArray->size != i ) {
		printf("Discrpenacies in the no. of records in places file.\n");
		printf("Exitting now.\n");
		exit( EXIT_FAILURE );
	}
	
	return dataArray;
}
		
/* function prints graph nodes */	
void
printGraph( graph_t *g ) {
	int i;              
	edgenode_t *p;
	
	for ( i=1; i<= g->nvertices; i++ ) {
		printf("%d: ", i );
		p = g->array[i];
		while (p != NULL) {
			printf(" %d", p->data.placeID);
			printf(" %s", p->data.placeName); 
			printf(" %d", p->weight);
			p = p->next;
		}
		printf("\n");
	}
}
	
	
