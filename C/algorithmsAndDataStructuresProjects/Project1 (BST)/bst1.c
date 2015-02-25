/*********************************************************************/
/*             Algorithms and Data Structures- Assignment 1          */
/*             Name: Thanh Le         Student ID: 574897             */
/*********************************************************************/

/* including the header file for binary search */
#include "bst1.h"

/* Function to create the binary tree */
/* Takes the data and inserts it into the existing tree **l */
void
insert_tree(node_t **l, data_t x) {
	node_t *p; /* temporary pointer for one node in binary tree */

	if (*l == NULL) {
		/* temporary pointers  to store the data */
		char *temp;
		char *temp_initial;
		char *temp_phone;
		
		/* allocate the memory for node and data */
		p = (node_t *)malloc(sizeof(node_t)); /* create a new node */
		temp = (char *)malloc(strlen(x.surname)+ 1);
		temp_initial = malloc(sizeof(char) + 1);
		temp_phone = (char *)malloc(MAXPHONECHARS*sizeof(char));
		
		/* copy the data over to the node */
		p->data.surname = strcpy(temp, x.surname);
		p->data.initial = strcpy(temp_initial, x.initial);
		p->data.phoneNum = strcpy(temp_phone, x.phoneNum);
		p->left = p->right = NULL;
		
		*l = p; /* link it into the binary trees */
		return;
	}
        
	/* if the data is lower than the current node, pass it to the left */
	if (strcmp(x.surname, (*l)->data.surname) == -1) {
		insert_tree(&((*l)->left), x);
	} else { /* else go right */
		insert_tree(&((*l)->right),x);
	}
}

/* print the number of comparisons for each search */
/* comparisons are in a list */
void
printCmpStruct(cmp_t **l, int num) {
	int i;
	/* iterates through the list and prints each comparison made */
	for (i = 0; i < num; i++) {
		printf("%10s \t", l[i]->surname);
		printf("%2s \t", l[i]->initial);
		printf("%d \n", l[i]->comparison);
	}
}

/* Function to search the binary tree. Returns the number of comparisons */
int
search_tree(node_t *l, char *surname, char *initial, FILE *fout, int found, int cmp) {
	
	/* if reached a leaf node */
	if ( l == NULL ) {
		if (!found){ /* check if there's a result, if not NOTFOUND */
			fprintf(fout, "  NOTFOUND\n");
		} 
		return cmp;
	}
	
	/* is the search the same as the node? */
	if ( strcmp( l->data.surname, surname) == 0 ) {
		if ( strcmp( l->data.initial, initial) == 0 ) {
			/* print out the data if they are the same */
			if (found == 0) {
				fprintf(fout, "%10s\n", l->data.phoneNum);
			} else {
				fprintf(fout, "\t\t\t\t%10s\n", l->data.phoneNum);
			}
				
			found++; cmp++; /* updates no of comparisons/found */
			
			/* still have to search if there are duplicates, go
			to the right until reach a leaf node */
			return search_tree( l->right, surname, initial, fout, found, cmp );
		}
	}
	
	/* if its less than the current node, go left */
	if ( strcmp( surname, l->data.surname ) == -1 ) {
		cmp++;
		return search_tree( l->left, surname, initial, fout, found, cmp );
	} else { /* else go right */
		cmp++;
		return search_tree( l->right, surname, initial, fout, found, cmp );
	}
	/* comparisons (cmp) are updated each time */
}

/*Function inserts the number of comparisons made for each name into the list */
void
insertCmpList(cmp_t **l, char *surname, char *initial, int cmp, int index) { 
	/* temporary pointers to store the surname and initials */
	char *temp;
	char *temp_initial;
      
	temp = (char *)malloc(strlen(surname)+1);
	temp_initial = malloc(sizeof(char)+1);

	/* now assign the comparisons of each search to the list */
	l[index] = malloc(sizeof(cmp_t));
	l[index]->surname = strcpy(temp, surname);
	l[index]->initial = strcpy(temp_initial, initial);
	l[index]->comparison = cmp;
	
}
        	
