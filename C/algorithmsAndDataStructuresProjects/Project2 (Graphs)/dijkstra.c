/*********************************************************************/
/*             Algorithms and Data Structures- Assignment 2          */
/*             Name: Thanh Le         Student ID: 574897             */
/*********************************************************************/

/* Libraries included */
#include <limits.h>
#include <assert.h>

/* header file included */
#include "adjacencyList.h"
#include "dijkstra.h"
#include "heap.h"

/* Function definitions */

/*------------------------------------dijkstra--------------------------------*/
/* dijkstra function, calculates shortest distance to each node */
void
dijkstra( graph_t *g, data_t *userInput, lookup_t *l ) {
	int dist[ g->nvertices ], pred[ g->nvertices ];
	initialize( g, g->nvertices, userInput->placeID, pred, dist );
	run( g, g->nvertices, userInput->placeID, pred, dist );
	findUserPath( g, userInput, dist, pred, l );
}

/* initialize distance and predecessor array */
void
initialize(  graph_t *g,  int nvertices, int src, int pred[], int dist[] ) {
	int i;
	for ( i=1; i <= g->nvertices; i++ ) {
		dist[i] = INT_MAX;
		pred[i] = -1;
	}
	dist[ src ] = 0;
	
}
/* crux of dijkstra, implements dijkstra algorithm */
void
run( graph_t *g, int nvertices, int src, int pred[], int dist[] ) {
	priorityQueue_t *pq;
	pq = makePQ( g );
	
	int i = 1;
	for ( i=1; i <= g->nvertices; i++ ) {
		insertPQ( pq, i, dist[i] );
		pq->pos[i] = i;
	}
	
	while( !emptyPQ(pq) ) {
		heap_node_t u = deletemin( pq );
		
		edgenode_t *p = g->array[ u.vertex ];
		
		while( p != NULL ) {
			int  v = p->data.placeID;
			if ( dist[ u.vertex ] != INT_MAX && 
				dist[ u.vertex ] + p->weight < dist[v] )
				update( v, u.vertex, pred, dist, pq, p->weight);
			
			p = p->next;
		}
		
	}
}
/* updates the pred and dist array as well as the priority queue in dijkstra */	
void
update( int v, int u, int pred[], int dist[], priorityQueue_t *pq, int weight) {
	dist[v] = dist[u] + weight;
	pred[v] = u;
	
	decreaseKey( pq, v, dist[v] );
	
}	
/* decreases the key of priority queue. Uses the position array in priorityQueue_t 
to find index of vertices and decrease their weights */
void 
decreaseKey( priorityQueue_t *pq, int v, int dist ) {
	/* assign position of vertex */
	int i = pq->pos[v];
	/* assign distance value of the vertex at that index  */
	pq->queue[i].dist = dist;
	
	/* while the index is less than index */
	while ( i && pq->queue[i].dist < pq->queue[ getParent(i) ].dist ) {
		/* if pass the parent vertex, break out of it */
		if ( getParent(i) == -1 ) break;
		
		/* else, perform the swap when decreasing keys */
		
		/*update vertex index in heap */
		pq->pos[ pq->queue[i].vertex ] = getParent(i);    
		pq->pos[ pq->queue[ getParent(i) ].vertex ] = i;
		
		/* perform the heap node swap */
		pq_swap( &pq->queue[i], &pq->queue[ getParent(i) ] );
		
		/* now update the index to the parent */
		i = getParent(i);
	}
}

/* find the path and print the travel */
void
findPathPrint( lookup_t *l, int src, int dest, int pred[] ) {
	
	/* if the source is equal to the end */
	if ( src == dest ) {
		printf("%s", l->lookup[ src ].data.placeName);
	} else {
		/* push it to stack, and let recursion handle */
		findPathPrint( l, src, pred[ dest ], pred );
		/* print the vertex visited */
		/* use the lookup aray to find the place name  of vertex */
		printf(" %s", l->lookup[ dest ].data.placeName);
	}

}
/* find the location and path that mets the user requirements at lowest cost */
void
findUserPath( graph_t *g, data_t *userInput, int dist[], int pred[], 
	lookup_t *l ) {
	/* make a priority queue to find the lowest cost */
	priorityQueue_t *pq = makePQ(g);
	
	int i; /*counter*/
	
	/*iterate through each dest and see if it meets user requirements*/
	for (  i = 1; i <= g->nvertices; i++ ) {
		/* check continent requirements */
		if (strcmp(userInput->continent, l->lookup[i].data.continent)!=0 
			&& strcmp(userInput->continent, "any") != 0 )
			dist[i] = INT_MAX;
		
		/* check cultural requirements */
		if (userInput->cultural == 'Y') {
			if ( l->lookup[i].data.cultural == 'N' )
				dist[i] = INT_MAX;
		}
		
		/* check outdoor requirements */
		if (userInput->outdoors == 'Y') {
			if ( l->lookup[i].data.outdoors == 'N' )
				dist[i] = INT_MAX;
		}
		/* insert the now modified distance into priority queue */
		insertPQ( pq, i, dist[i] );
	}
	
	/* delete the minimum distance and rearrange the queue */
	heap_node_t finalDest = deletemin( pq );
	
	/* if the top of the queue is the source, re pop the queue */
	if ( finalDest.dist == 0 )
		finalDest = deletemin( pq );
	
	/* if they're all infinity, means no soln */
	if ( finalDest.dist == INT_MAX ) {
		printf("No valid destination. Sorry.\n");
	} else {
		/* print the final destination and cost using lookupArray */
		printf("%s $%d\n", l->lookup[ finalDest.vertex ].data.placeName, 
			finalDest.dist);
	}
	
	/* Stage 2 part */
	findPathPrint( l, userInput->placeID, finalDest.vertex, pred );
	printf("\n");
}
	

/* dijkstra testers */
void 
printArr(int dist[], int pred[], int n) {
    printf("Vertex   Distance from Source       Predescessor\n");
    int i;
    for ( i = 1; i <= n; i++)
        printf("%d \t %d \t\t\t\t %2d\n", i, dist[i], pred[i]);
    printf("\n");
}


