/*********************************************************************/
/*             Algorithms and Data Structures- Assignment 1          */
/*             Name: Thanh Le         Student ID: 574897             */
/*********************************************************************/

/* header files */
#include "dict1.h"
#include "bst1.h"

int
main(int argc, char **argv)
{
	int    i=0;
	char   line[MAXLINELEN];
	FILE  *fp;
	char   arg1[MAXFIELDLEN],arg2[MAXFIELDLEN],arg3[MAXFIELDLEN];
	data_t data; /* place to put the data */
	node_t **tree; /* pointer to pointer to the binary tree */
	
	/* initialize the tree */
	tree = (node_t **)malloc(sizeof(tree));
	*tree = NULL;

	if(argc != 3)
	{   
		printf("Usage: readphonebook f, 
			where f is name of phonebook file\n");
		printf("Also, may have missed output file name \n");
		exit(ARGERROR);
	}                 

	/* open file for reading*/
	if( (fp=fopen(argv[1],"r")) ==NULL)
	{
		printf("Error opening file \n");
		exit(FILEERROR);
	}

	/* read from file  */
	i=0;
	while(fgets(line, sizeof(line), fp))
	{
		i++;
		/* check the line actually got read correctly */
	if(DEBUG) printf("READ LINE %d:  %s", i,line);

	/* get fields into buffers*/
	sscanf(line,"%s %s %s", arg1, arg2, arg3);

	/* check got  the 3rd field */
	if(DEBUG) printf("\t\tARG 3: %s\n",arg3);

	/* to actually use this value, have to copy it to another place,
         or else it will get overwritten each time around the loop */
	 
         /* assign what was read into a data struct */
         data.surname = arg1;
	 data.initial = arg2;
	 data.phoneNum = arg3;

	 /* binary tree insertion of data read */
	 insert_tree(tree, data); 
	
	}
	
	if(DEBUG) printf("read %d lines\n", i);
  
	/* time to implement search keys */
	
	FILE *fout; /* file to be written to */
	fout = fopen(argv[2], "w"); /* 2nd argument is the file name */
	
	/* allocates memory for comparison struct */
	/* stores comparisons into a list */
	int current_size = 10; /* let the initial list have size of 10*/
	cmp_t **compare = malloc(current_size * sizeof(cmp_t));
  
	int j = 0;
	while((fgets(line,MAXLINELEN,stdin))!=NULL)
	{
		i++;
		int found = 0, cmp = 0; /* new found/comparisons each search */
		
		sscanf(line,"%s %s", arg1, arg2);
		
		/* writes the surname and initial to the file */
		fprintf(fout,"%20s \t %2s \t", arg1, arg2);
		
		/* comparisons for each are assigned to cmp */
		cmp = search_tree(*tree, arg1, arg2, fout, found, cmp);
		
		/* if the size of the list is equal to current size */
		if ( j == current_size ) 
		{
			/* allocate more memory to the list */
			current_size *= 2;
			compare = realloc(compare, current_size*sizeof(cmp_t));
		}
		
		/* temporary pointers to store the surname and initials */
		insertCmpList( compare, arg1, arg2, cmp, j ); 
		j++;
      
	}

        /* output the comparisons made by each search at the end */
	printCmpStruct(compare, j);
	
	return(OK);
}
