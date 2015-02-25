/*********************************************************************/
/*             Algorithms and Data Structures- Assignment 2          */
/*             Name: Thanh Le         Student ID: 574897             */
/*********************************************************************/

/* Header files included */
#include "adjacencyList.h"
#include "dijkstra.h"
#include "heap.h"

/* ---------------------------priority Queues-------------------------------- */

/* insert into a priority queue */
void
insertPQ( priorityQueue_t *q, int vertex, int dist ) {
	heap_node_t *p = malloc(sizeof(heap_node_t));  /*temporary heap node*/
	
	/* allocation of heap node */
	p->vertex = vertex;
	p->dist = dist;
	
	/* insertion into queue */
	q->size = (q->size) + 1;   /* size increases by one */
	q->queue[ q->size ] = *p;  /* store the node at the end of heap */
	heapify( q, q->size );     /* now restore heap condition at the end */
}

/* initializes a priority queue */
priorityQueue_t 
*makePQ( graph_t *g ) {
	priorityQueue_t *pq = malloc(sizeof(priorityQueue_t));
	pq->size = 0;
	pq->pos = malloc(sizeof(int) * g->nvertices) ;
	pq->queue = malloc(sizeof(heap_node_t) * g->nvertices + 2);
	return pq;
}

/* get the parent node */
int
getParent( int n ) {
	/* if at the top of the heap return a sentinel */
	if ( n == 1 ) {
		return -1;
	} else {
		/* using floor division to get the parent */
		return( (int) n/2 ); 
	}
}

/* restores heap condition after insertion, otherwise known as up heap */
void
heapify( priorityQueue_t *pq, int position ) {
	/* if we reached the top, stop */
	if ( getParent(position) == -1 ) return;
	
	/* check if the parent distance is higher than the distance at pos */
	if ( pq->queue[getParent(position)].dist > pq->queue[position].dist ) {
		/* positions in queue have swapped */
		pq->pos[ pq->queue[getParent(position)].vertex ] = position; 
		pq->pos[ pq->queue[position].vertex ] = getParent(position);
		
		/* swap the nodes in the queue */
		pq_swap(&pq->queue[position], &pq->queue[getParent(position)]);
		
		/* and heapif and the position */
		heapify( pq, getParent(position) );
	}
}

/* downheap, restore heap propert after deletion */
void
downheap( priorityQueue_t *pq, int position ) {
	
	/* let the minimum index be at position */
	int min_index = position;
	/* get the child from the position */
	int c = getChild( position );
	/* counter */
	int i;
	 
	
	for ( i=0; i <= 1; i++ ) {
		/* while we are still within pq*/
		if ( (c+i) <= pq->size ) {
			/* if the distance at the min index is higher */
			if ( pq->queue[min_index].dist > pq->queue[c+i].dist ){
				 /* update the minimum index */
				min_index = c + i;
			}
		}
	}
	
	/* now if the minimum index is not at the position entered, swap ok */
	if ( min_index != position ) {
		/* position of vertex in priority queue changes */ 
		pq->pos[ pq->queue[min_index].vertex ] = position;
		pq->pos[ pq->queue[position].vertex ] = min_index;
		
		/* now swap the nodes */
		pq_swap( &pq->queue[position], &pq->queue[min_index] );
		
		/* now downheap at the min_index, until heap order is ok */
		downheap( pq, min_index );
	}
}			

/* utility function to swap the nodes in the queue */
void
pq_swap( heap_node_t *a, heap_node_t *b ) {
	heap_node_t temp = *a;
	*a = *b;
	*b = temp;
}

/* get the child */
int
getChild( int n ) {
	return (2*n);
}

/* is the priority queue empty ? */
int
emptyPQ( priorityQueue_t *pq ) {
	/* check the number of nodes, if zero false */
	return pq->size == 0;
}

/* extracts the top of the heap and restores heap order */
heap_node_t
deletemin( priorityQueue_t *pq ) {
	
	/* if the heap is empty return NULL */
	if ( emptyPQ(pq) ) {
		printf(" Empty priority queue, proceed with caution! \n");
	}
	
	
	/* store the root node, so it can be returned */
	heap_node_t root = pq->queue[1];
	
	/* position of vertex has changed */
	pq->pos[ root.vertex ] = pq->size;
	pq->pos[ pq->queue[ pq->size ].vertex ] = 1;
	
	/* store the end node the front */
	pq->queue[1] = pq->queue[ pq->size ];
	pq->size = (pq->size) - 1; /* size is reduced */
	downheap( pq, 1 );        /* downheap the root of the priority queue*/
	
	return root; /* return the minimum */
}
	

/* heap testers */

void
printPriorityQueue( priorityQueue_t *pq ) {
	
	printf("printing priority queue\n");
	int i;

	for ( i=1; i <= pq->size; i++ ) {
		printf("%d \t %d \n", pq->queue[i].vertex, pq->queue[i].dist);
	}
	printf("\n");
}
