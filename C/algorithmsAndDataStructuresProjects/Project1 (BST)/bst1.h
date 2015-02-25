/*********************************************************************/
/*             Algorithms and Data Structures- Assignment 1          */
/*             Name: Thanh Le         Student ID: 574897             */
/*********************************************************************/

/* libaries used */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* constants */
#define MAXCHARS 80
#define MAXPHONECHARS 9

/* data struct that stores the surname, initial and phone number */
typedef struct {
	char *surname;
	char *initial;
	char *phoneNum;
} data_t;

/* binary tree node, each tree contains a link list to store the data  */
typedef struct node {
	data_t data; /* data store here in the node */
	struct node *left; /* the left node */
	struct node *right; /* the right node */
} node_t;

/* struct that contains the surname, initials and comparisons made */
typedef struct {
	char *surname;
	char *initial;
	int comparison;
} cmp_t;

/* functions declarations */
void insert_tree(node_t **l, data_t x);
int search_tree(node_t *l, char *surname, char *initial, FILE *fout, 
	int found, int cmp);
void printCmpStruct(cmp_t **l, int num);
void insertCmpList(cmp_t **l, char *surname, char *initial, int cmp, int index);
