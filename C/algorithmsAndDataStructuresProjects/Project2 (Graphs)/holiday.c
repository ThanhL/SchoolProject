/*********************************************************************/
/*             Algorithms and Data Structures- Assignment 2          */
/*             Name: Thanh Le         Student ID: 574897             */
/*********************************************************************/

/* Libraries used for main */
#include <stdio.h>
#include <stdlib.h>

/* Header files included */
#include "adjacencyList.h"
#include "dijkstra.h"

data_t *userQuery(void); 
void getUserPlaceID( lookup_t *l, data_t *user );

int
main(int argc, char **argv) {
	
	/* file variables */
	FILE *fpPlace;
	FILE *fpCost;
	
	/* check stdin and see if the files open cleanly */
	if (argc != 3) {
		printf("Usage: requires Place File and Cost File \n");
		exit( EXIT_FAILURE );
	}
	
	if ( !(fpPlace = fopen(argv[1], "r")) || !(fpCost = fopen(argv[2], "r")) ) {
		printf("Error opening file(s)\n");
		exit( EXIT_FAILURE );
	}
	
	/* construct the data for all graph nodes using places file */
	lookup_t *lookUpArray = getlookUp( fpPlace );
	/* now use the lookup array and use the cost file to construct graph */
	graph_t *graph = constructGraph( fpCost, lookUpArray );
	 
	/*get user input and determine the placeID of the users start location*/
	data_t *userInput = userQuery();
	getUserPlaceID( lookUpArray, userInput );
	
	
	
	
	/* dijkstra part */
	dijkstra( graph, userInput, lookUpArray );
	
	
	return 0;
}

void
getUserPlaceID( lookup_t *l, data_t *user ) {
	int i; /*counter*/
	for ( i =1; i <= l->size; i++ ) {
		/* Found the starting location don't exit */
		if ( strcmp( user->placeName, l->lookup[i].data.placeName ) == 0 ) {
			/* now assign the placeID with the starting location */
			user->placeID = l->lookup[i].data.placeID;
			return;
		}
	}
	/* if we can't find the starting location, exit. */
	printf("Can't find the place you are looking for. Now exiting.\n");
	exit( EXIT_FAILURE );
}


/* Scanning characters still has issues: (RETURN) */
data_t
*userQuery(void) {
	/* data structure to store userInput */
	data_t *userInput;
	
	/* allocate memory for user input */
	userInput = malloc(sizeof(data_t));
	
	/* allocate memory for string inputs */
	userInput->placeName = malloc(sizeof(char)*MAXFIELDLEN);
	userInput->continent = NULL;
	
	char *name = malloc(sizeof(char)*MAXFIELDLEN);
	
	/* Query user and allocate data */
	printf("Enter your starting point: \n");
	scanf("%s", userInput->placeName);
	
	printf("Enter the continent you prefer [Australia,Asia,NorthAmerica,SouthAmerica,Europe,Africa,Antarctica]\n");
	fgets( name, MAXLINELEN, stdin);
	if ( scanf("%[^\n]%*c", name) == 0 ) {
		userInput->continent = "any";
	} else {
		userInput->continent = name;
	}
	
	printf("Cultural interest required? [Y/N]\n");
	char cultural;
	if (scanf(" %c", &cultural) != 1) {
		userInput->cultural = 'N';
	} else {
		userInput->cultural = cultural;
	}

	printf("Outdoors activities required? [Y/N]\n");
        char outdoors;
	if (scanf(" %c", &outdoors) != 1) {
		userInput->outdoors = 'N';
	} else {
		userInput->outdoors = outdoors;
	}


	return userInput;         /* return data of user */
}
