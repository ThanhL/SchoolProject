/*************************************************************/
/*               Assignment 1 - Allocation of Components     */
/*               Thanh Le - Due 28th April 2013              */
/*************************************************************/

/******************/
/* Libraries used */
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
/******************/

/******************/
/* Variables      */
/******************/
/* Maximum components input */
#define MAXCOMPONENTS 1000
/* Coil dimensions */
#define COIL_WIDTH 3.5
#define COIL_LENGTH 24

/*******************/
/*    Functions    */
/*******************/
/* Asssign x and y inputs into an array */
int assign_double_array(double xarray[], double yarray[], int maxvals);
/* Assign components into an array */
void assign_components(int component_array[], int ncomponents);
/* Computes the allocation of x components, and allocates it to a new array */
void
compute_allo_comp(double xarray[], double x_allo[], double max_length, int n);
/* Print Internal Wastage and End Wastage */
void print_wastage(double in_waste, double end_waste, double width, 
	double length, double area, int coil);
/* Prints the stage outputs */
void print_stage(double x[], double y[], double x_allo[], int components[], 
	int n);
/* Sort the components from largest to lowest in each coil */
void 
sort_coils(double xarray[], double new_x[], double yarray[], double new_y[],
	int components[], int new_comp[], double bat_length, int n) ;
/* Sort the array from largest to lowest */
void sort_double_array(double x[], double y[], int components[], int n);
/* Swap positions of doubles and ints */
void double_swap(double *p1, double *p2);
void int_swap(int *p1, int *p2);
/* Check whether the array is valid, required for sorting coils */
int check_valid(double A[], int n);
/* Calculate internal and end wastages */
double calc_in_waste(double length, double width, double area);
double calc_end_waste(double length, double maxlength, double width);

int
main(int argc, char *argv[]) {
	/* Declaration + Initializations */
	
	/* Stage 1+2 Declarations */
	double xarray[MAXCOMPONENTS], yarray[MAXCOMPONENTS];
	/* Allocation of the components (length wise 'x') array */
	double x_allo[MAXCOMPONENTS];
	/* Number of componets, Components array respectively */
	int ncomponents, components[MAXCOMPONENTS];
	
        /* Stage 3 Declarations */
        
        /* New sorted x, y, and component arrays for stage 3 */
        double x[MAXCOMPONENTS]; double y[MAXCOMPONENTS]; int c[MAXCOMPONENTS];
	
	/* Stage 1 + 2 */

	ncomponents = assign_double_array(xarray, yarray, MAXCOMPONENTS);
	assign_components(components, ncomponents);
	printf("Stage 1+2\n");
	printf("---------\n");
	compute_allo_comp(xarray, x_allo, COIL_LENGTH, ncomponents);
	print_stage(xarray, yarray, x_allo, components, ncomponents);
	printf("\n");	
	
	/* Stage 3 */		
	printf("Stage 3\n");
	printf("-------\n");
	sort_coils(xarray, x, yarray, y, components, c, COIL_LENGTH, 
		ncomponents);	
	compute_allo_comp(x, x_allo, COIL_LENGTH, ncomponents); 
	print_stage(x, y, x_allo, c, ncomponents);
	
	return 0;
}
/* takes x,y, x allocated, and component arrays to print the specified format*/
void
print_stage(double x[], double y[], double x_allo[], int components[], int n) {
	double length = 0.0, area_used = 0.0, total_waste = 0.0;
	double in_waste, end_waste;
	int i, coil = 1;
	for (i=0; i<n; i++){
		/* adds the length and checks whether its within coil length */
		length += x[i];
		if (length > COIL_LENGTH) {
			/* minus the previous values since its out of coil */
			length -= x[i];
			
			/*calculate  the internal and end wastage components */
			in_waste = calc_in_waste(
				length, COIL_WIDTH, area_used);
			end_waste = calc_end_waste(length, COIL_LENGTH, 
				COIL_WIDTH);
			/*inwastage, endwastage and the area can't be negative*/
			assert(in_waste >= 0.0 && end_waste >= 0.0);
			assert(area_used >= 0.0);
			/* print the wastage of the previous coil */
			print_wastage(in_waste, end_waste, COIL_WIDTH, 
				COIL_LENGTH, area_used, coil);
			
			/*calculate total wastage */
			total_waste += in_waste; total_waste += end_waste;
			
			/* reinitialize variables for new coil */
			length = x[i]; area_used = x[i]*y[i];
			/*new coil and reset the wastages to zero for new coil*/ 
			coil++; in_waste = 0.0; end_waste = 0.0;
			
			/* print the current component on new coil */
			printf("component %d, (%4.1f,%4.1f)", components[i], 
				x[i], y[i]);
		        printf(" starting (%4.1f,%4.1f)", x_allo[i], 0.0);
		        printf(" on coil %d\n", coil); 
		} else if (length == COIL_LENGTH) {
			/* print the current component first */
			printf("component %d, (%4.1f,%4.1f)", components[i],
				x[i], y[i]);
		        printf(" starting (%4.1f,%4.1f)", x_allo[i], 0.0);
		        printf(" on coil %d\n", coil); 
		        /* add the current area as well */ 
		        area_used += x[i]*y[i];
		        
		        /* calculate the wastages and print them like before */
			in_waste = calc_in_waste(
				length, COIL_WIDTH, area_used);
			end_waste = calc_end_waste(length, COIL_LENGTH,
				COIL_WIDTH);
			assert(in_waste >= 0.0 && end_waste >= 0.0);
			assert(area_used >= 0.0);			
			print_wastage(in_waste, end_waste, COIL_WIDTH, 
				COIL_LENGTH, area_used, coil);
			
			total_waste += in_waste; total_waste += end_waste;
			
			/* reintialize for the new coil: New area and length */
			area_used = 0.0; length = 0.0;
			coil++; in_waste = 0.0; end_waste = 0.0;				        
		} else {
                        area_used += x[i]*y[i];
		        printf("component %d, (%4.1f,%4.1f)", components[i], 
		        	x[i], y[i]);
	                printf(" starting (%4.1f,%4.1f)", x_allo[i], 0.0);
	                printf(" on coil %d\n", coil); 
	        }
	}
	/* calculate the last internal and end wastages */
	in_waste = calc_in_waste(length, COIL_WIDTH, area_used);
	end_waste = calc_end_waste(length, COIL_LENGTH, COIL_WIDTH);
		
	if (length != 0) {
		/* if the length does not equal zero, print out the wastages */
	                print_wastage(in_waste, end_waste, COIL_WIDTH, 
				COIL_LENGTH, area_used, coil);
			total_waste += in_waste;
	}
	/* finally print the total wastage component */
	printf("overall, total wastage %4.1f m^2\n", total_waste); 
}

/*wastage functions start */
double 
calc_in_waste(double length, double width, double area) {
	double m_sheet = 0.0, in_waste = 0.0;
	/* finding the area with the full width of the sheet (m_sheet)*/
	m_sheet = length*width;
	/* subtract m_sheet by the area_used to find the internal waste */ 
	in_waste = (m_sheet-area);
	if (in_waste <= 0) {
	          return 0;
	} 
	return in_waste;
}

double
calc_end_waste(double length, double maxlength, double width) {
	double length_rem = 0.0, end_waste = 0.0;
	/* find the length remaining to calculate the end wastage */
	length_rem = maxlength - length;
	end_waste = length_rem*width;
	return end_waste;
}

/*print the wastage at the end of each coil */
void
print_wastage(double in_waste, double end_waste, double width, double length, 
	double area, int coil) {
	printf("coil %2d", coil);
        printf(" internal wastage %4.1f m^2, ", in_waste);
        printf("end wastage %4.1f m^2\n", end_waste);
        
}        
/*wastage functions end */

/* allocation of components into arrays */
void
assign_components(int component_array[], int ncomponents) {
	int i, component = 1;
	for (i=0; i<ncomponents; i++) {
		component_array[i] = component;
		component++;
	}
}
/* Computes the allocation of x components, and allocates it to a new array */
void
compute_allo_comp(double xarray[], double x_allo[], double max_length, int n) {
	int i;
	double length;
	
	/*set the initial values for allocation of x, and the length*/
	x_allo[0] = 0.0; length = xarray[0];
	for (i=1; i<n; i++) {
		/*adds the length and checks whether or not it is within coil*/
		length += xarray[i];
		if (length <= max_length) {
			/*allocates the component accordingly if within coil*/
			x_allo[i] = length - xarray[i];
		} else {
			/* if out of coil, initial length is set to the
			current value of x. x_allo starts on new coil */
			length = xarray[i];
			x_allo[i] = 0.0;
		}
	}
}

/* Assign x and y arrays */			
int
assign_double_array(double xarray[], double yarray[], int maxvals) {
	int components = 0, overflow = 0;
	double xinput, yinput;
	/* check if its a valid input first */
	if (scanf("%lf %lf", &xinput, &yinput) != 2) {
		exit(EXIT_FAILURE);
	}
	/* if passes the first check, assign the first component */
	xarray[components] = xinput;
	yarray[components] = yinput;
	components++;
	
        /* assign remaining components inputted */
	while (scanf("%lf %lf", &xinput, &yinput) == 2) {
		/* can't have negative components */
		assert(xinput >= 0.0 && yinput >= 0.0);
		if (components == maxvals) {
			/* if its greater than max components, overflow keeps
			adding and no assignment is made */
			overflow++;
		} else {
			/* assign x and y components accordingly */
			xarray[components] = xinput;
			yarray[components] = yinput;
			/* number of components is added after each assignment*/
			components++;
		}
	}
	
	return components;
}
/* swap ints */	
void
double_swap(double *p1, double *p2) {
	double tmp;
	tmp = *p1;
	*p1 = *p2;
	*p2 = tmp;
}
/* swaps doubles */
void
int_swap(int *p1, int *p2) {
	int tmp;
	tmp = *p1;
	*p1 = *p2;
	*p2 = tmp;
}

void
sort_coils(double xarray[], double new_x[], double yarray[], double new_y[], 
	int components[], int new_comp[], double bat_length, int n) {
	int i, count;
	double length = 0.0;
	/*sort the array to largest to shortest first */
	sort_double_array(xarray, yarray, components, n);
	count = 0;
	/* checks if there any more unallocated components */
	while (!check_valid(xarray,n)) {
		for (i=0; i<n; i++) {
			/* if the length is within the first coil */
			if ((length+xarray[i] <= bat_length) && 
				(xarray[i] != -1)) {
				length += xarray[i]; /*add the length */
				/*place the  x sorted component into new array*/
				new_x[count] = xarray[i];
				/* place the corresponding components
				and y-values into new array */
				new_y[count] = yarray[i];
				new_comp[count] = components[i];
				/* move to next position, and mark A[i] as used
				(-1) */
				count++; xarray[i] = -1;	
			}
		}
		/*once done iterating through every element, start new coil */
		length = 0;
	}
}

void
sort_double_array(double x[], double y[], int components[], int n) {
	int i, j;
	/*takes three arrays: x, y and component arrays and sorts them */
	for (i=1; i<n; i++) {
		for (j=i-1; j>=0 && x[j+1]>x[j]; j--) {
			/* sorts the x array from largest to smallest */
			double_swap(&x[j], &x[j+1]);
			/* swaps the corresponding y values and components into
			position of the sorted x's */
			double_swap(&y[j], &y[j+1]);
			int_swap(&components[j], &components[j+1]);
		}
	}
}
/* checks if there any more unallocated components */
int
check_valid(double A[], int n) {
	int i = 0;
	for (i=0; i<n; i++) {
		/* if umallocated (not equal to -1) return 0 */
		if (A[i] != -1) {
			return 0;
		}
	}
	return 1;
}

/* !! programming is fun !! */
