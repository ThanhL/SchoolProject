/*******************************************************/
/*         Engineering Computation - Assinment 2       */
/*         Thanh Le, Student ID: 574897                */
/******************************************************/


/************/
/*Libraries*/
/***********/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

/**************************/
/* Variable Declarations */
/*************************/
        /* Maximum inputs for antennaes */
#define MAX_ANTENNAE 26
        /* Maximum string length for antennaes*/
#define NAMESTRLEN 1
        /* Maximum co-ordinate inputs for stage 2*/
#define MAX_LOCATIONS 100
        /* The value of PI */
#define M_PI 3.14159265358979323846
        /* Absorption Coefficent */
#define CA 30.0
        /* Constants of Dispersion of the signal */
#define DISPER_CONST_1 1
#define DISPER_CONST_2 4
        /* Maximum plot inputs for stage 3 */
#define MAX_PLOTS 10
        /* Minimum P value for bar signals (stage 3) */
#define P_MIN 1.0e-07

/***********************************************/
/* Type Definitions and Structure Declarations */
/***********************************************/
/* String for antennae component */
typedef char namestr[NAMESTRLEN+1];

/*antennae component structure */
typedef struct {
	namestr antennae;
	double x_coord;
	double y_coord;
	double p_out;
	double deg;
	double eng_angle;
} ant_component_t;

/* all of the antennaes */
typedef struct {
	int n_antennae;
	ant_component_t antennaes[MAX_ANTENNAE];
} all_antennaes_t;

/* coordinates (x,y) structure */
typedef struct {
	double x;
	double y;
} location_t;

/* all the points for stage 2 structure */
typedef struct {
	int n_location;
	location_t points[MAX_LOCATIONS];
} all_points_t;

/* plot specifications for stage 3 */
typedef struct {
	location_t s_w_corner;
	location_t n_e_corner;
	double pixels;
} plot_comp_t;

/* all the plot specifications */
typedef struct {
	int n_plots;
	plot_comp_t plots[MAX_PLOTS];
} all_plots_t;

/*intervals and pixels struct*/
typedef struct {
	double x_base;
	double y_base;
	double x_in;
	double y_in;
	int x_pix;
	int y_pix;
} inter_pix_t;
	
/*************************/
/* Function Declarations */
/*************************/
        /* read all antennaes, points and plots */       
void read_all_ant(all_antennaes_t *all_antennaes);
void read_all_points(all_points_t *all_points);
void read_all_plot_spec(all_plots_t *all_plots);
        /* read the stage specifications (number of lines for each stage) */
int read_stage_spec(void);
        /* calculate the total power outage */
double calc_pout(all_antennaes_t *all_antennaes, int n_antennae);
        /* calculate the distance */
double distance(location_t p1, location_t p2);
        /* check if the "location" is within antennae range */
int check_bounds(location_t location, ant_component_t ant_component);
        /* calculate the dot product */
double dot_product(location_t p1, location_t p2);
        /* calculate the magnitude of a vector */
double magnitude(location_t p1);
        /* calculate the available power (Pa) */
double aval_power(double aperture, double power, double d, double absorp_co,
	double disper_const_1, double disper_const_2);
        /* do each stage */
void do_stage1(all_antennaes_t *all_antennaes); 
void do_stage_2(all_antennaes_t *all_antennaes, all_points_t *all_points,
	double absorp_co, double disper_const_1, double disper_const_2 );
void do_stage_3(all_plots_t *all_plots, all_antennaes_t *all_antennaes, 
	double absorp_co, double disper_const_1, double disper_const_2 );
        /* compute the intervals and pixels and assigns them to inter_pix */
void compute_i_and_p(plot_comp_t plot, inter_pix_t *inter_pix);
        /*calculates all the power and prints the corresponding signal */
void print_contour(location_t location, all_antennaes_t *all_antennaes,  
	double absorp_co, double disper_const_1, double disper_const_2);
        /* compares the power, prints the corresponding bar signal */
void check_print(double power);

/*********/
/* MAIN */
/********/
int
main(int argc, char *argv[]) {
	/* Declarations and Initializations */
	all_antennaes_t all_antennaes; 
	all_points_t all_points;	
	all_plots_t stage_3_spec;
	
	/* Stage 1 */
	read_all_ant(&all_antennaes);
	do_stage1(&all_antennaes);
        printf("\n");
        
	/*Stage 2 */
	read_all_points(&all_points);
	do_stage_2(&all_antennaes, &all_points, CA, 
		DISPER_CONST_1, DISPER_CONST_2);
	printf("\n");
	
	/*Stage 3*/
	read_all_plot_spec(&stage_3_spec);
        do_stage_3(&stage_3_spec, &all_antennaes, CA, 
        	DISPER_CONST_1, DISPER_CONST_2);

	return 0;
}

/*check the signal strength in bars and prints the  corresponding signal*/
void
check_print(double power) {
	
	if (power < P_MIN) {
		printf(".");
	} else if (P_MIN <= power && power < 3.2*P_MIN) {
		printf("1");
	} else if (3.2*P_MIN <= power && power < 10*P_MIN) {
		printf("2");
	} else if (10*P_MIN <= power && power < 3.2*10*P_MIN) {
		printf("3");
	} else if (3.2*10*P_MIN <= power && power < 100*P_MIN) {
		printf("4");
	} else {
		printf("5");
	}
}
/*Iterates through all the antennae locations, and calculates the power + prints 
the corresponding signal at the "location" */
void
print_contour(location_t location, all_antennaes_t *all_antennaes,  
	double absorp_co, double disper_const_1, double disper_const_2) {

	int i;
	double power, aperture, a_power = 0.0, d;
	double power_sig = 0.0;
	location_t antennae_comp;
	
	/*Iterates through all antennae components and checks power available*/
	for (i=0; i< (*all_antennaes).n_antennae; i++) {
	    /*check whether or not if the point is within antennae range */
	    if (check_bounds( location, (*all_antennaes).antennaes[i])) {
                aperture = (*all_antennaes).antennaes[i].eng_angle;
        	power = (*all_antennaes).antennaes[i].p_out;
        	antennae_comp.x = (*all_antennaes).antennaes[i].x_coord;
        	antennae_comp.y = (*all_antennaes).antennaes[i].y_coord;
        			
        	if (antennae_comp.x < location.x) {
        	       d =distance(antennae_comp, location);
                } else {
        	       d =distance(location, antennae_comp );
        	}
        	/*calculate the available power*/
        	a_power = aval_power(aperture, power, d, absorp_co, 
        		disper_const_1, disper_const_2);
                /*if the power  signal before is less than the calculated
                power, the stronger power takes its place*/
        	if (a_power > power_sig) {
        		/*power signal becomes the new available power*/
        		 power_sig = a_power;
        	}

            }
            
            	   
        }
        /* checks the power and prints the signal */
        check_print(power_sig);
}
	
/*do stage 3*/
void 
do_stage_3(all_plots_t *all_plots, all_antennaes_t *all_antennaes, 
	double absorp_co, double disper_const_1, double disper_const_2 ) {

	inter_pix_t inter_pix;
	int i, j, k;
	double x,y;
	location_t location;
	
	printf("Stage 3\n");
	printf("-------\n");

        /*iterates through all the plots required and plots them*/
	for (i=0; i<(*all_plots).n_plots; i++) {
		/*compute the intervals specified by each plot*/
		compute_i_and_p((*all_plots).plots[i], &inter_pix);

	        /*x and y values are initialized to the base values of x and y*/
		x = inter_pix.x_base;
		y = inter_pix.y_base;
		
		/*for every y, there will be an x */
		for (j=0; j<inter_pix.y_pix; j++) {
			/*since we're starting the print from the top, 
			the y-value will be y-max, and decrease to y-min*/
			y = -inter_pix.y_in*j + inter_pix.y_base;

			for(k=0; k<inter_pix.x_pix; k++) {
				x = inter_pix.x_in*k + inter_pix.x_base;
				/*assign the x,y coordinate to struct*/
				location.x = x; location.y = y;

                                /*prints the corresponding signal*/
				print_contour(location,all_antennaes,absorp_co, 
					disper_const_1, disper_const_2);
				
			}
			/*start on new line */
			printf("\n");
		}
	}
}
/*compute the intervals and pixels and assigns them to the strut inter_pix*/	
void
compute_i_and_p(plot_comp_t plot, inter_pix_t *inter_pix) {
	/* declarations */
	double xmax, xmin, ymax, ymin, x_pixel;
	double x_interval, y_interval;
	double y_pixel;
	
	/*assign corresponding max/min values as well as pixels(horizontal)*/
	xmin = plot.s_w_corner.x; ymin = plot.s_w_corner.y;
	xmax = plot.n_e_corner.x; ymax = plot.n_e_corner.y;
	x_pixel = plot.pixels; 
	
	/*compute x_intervals and y_intervals */
	x_interval = (xmax - xmin)/(x_pixel - 1);
	y_interval = 2*x_interval;
	
	/*find the amount of pixels required in y-dir */
	y_pixel = ((ymax - ymin)/y_interval) + 1;
	
	/* Rounds y-pixel accordingly */
	y_pixel = (int)(y_pixel+0.5);
	/*compute the new y-interval with the rounded y-pixel */
	y_interval = (ymax - ymin)/(y_pixel - 1);
	
	/*with the computed intervals and pixels, assignments are made to
	struct inter_pix */
	(*inter_pix).x_base = plot.s_w_corner.x; 
	(*inter_pix).y_base = plot.n_e_corner.y;
	(*inter_pix).x_in = x_interval; (*inter_pix).x_pix = x_pixel;
	(*inter_pix).y_in = y_interval; (*inter_pix).y_pix = y_pixel;
}
	
/*reads all the plots specifications and assigns it struct all_plots*/
void
read_all_plot_spec(all_plots_t *all_plots) {
	int n_plots = 0, i=0; 
	/*reads in how many inputs there will be */
	n_plots = read_stage_spec();
	
	/*iterates through the amount of inputs and assigns them accordingly*/
	for (i=0; i<n_plots; i++) {
	        scanf("%lf %lf %lf %lf %lf", 
	        	&(*all_plots).plots[i].s_w_corner.x, 
	        	&(*all_plots).plots[i].s_w_corner.y,
	        	&(*all_plots).plots[i].n_e_corner.x,
	        	&(*all_plots).plots[i].n_e_corner.y,
	        	&(*all_plots).plots[i].pixels);
	}
	/*amount of inputs correspond to the amount of plots*/
        (*all_plots).n_plots = n_plots;
        return;
}
		
	

/* do stage 2 */
void 
do_stage_2(all_antennaes_t *all_antennaes, all_points_t *all_points, 
	double absorp_co, double disper_const_1, double disper_const_2 ) {

        int i,j; 
        /* these variables will be used for neatness*/
        double aperture, power, d;
        location_t antennae_comp;
        
        printf("Stage 2\n");
        printf("-------\n");

        
        printf("              ");
        for (i=0; i < (*all_antennaes).n_antennae; i++) {
        	printf("%6s", (*all_antennaes).antennaes[i].antennae);
        }
        printf("\n");
        
        for (i=0; i < (*all_points).n_location; i++) {
        	/* print each point */
        	printf("at (%3.1f,%3.1f): ", (*all_points).points[i].x, 
        		(*all_points).points[i].y);
        	for (j=0; j< (*all_antennaes).n_antennae; j++) {
        		/*checks whether the point is within antennae view*/
        		if (check_bounds( (*all_points).points[i], 
        			(*all_antennaes).antennaes[j])) {
        		
        			/*assigns the corresponding components required
        			to be put into the function aval_power*/
        			aperture = 
        			(*all_antennaes).antennaes[j].eng_angle;
        			power = (*all_antennaes).antennaes[j].p_out;		
        			antennae_comp.x = 
        			(*all_antennaes).antennaes[j].x_coord;
        			antennae_comp.y = 
        			(*all_antennaes).antennaes[j].y_coord;
        			
        			/*compute the distance d, if statements
        			are used to avoid wrong computation of d
        			due to potential negative signs*/
        			
        			/*if antennae is before the coordinate: x1 */
        			if (antennae_comp.x < 
        				(*all_points).points[i].x) {
        				d =distance(antennae_comp, 
        					(*all_points).points[i]);
        			} else {
        				d =distance((*all_points).points[i], 
        					antennae_comp );
        			}
        			/*print the avalaible power at the point*/
        			printf("  %2.1e  ", aval_power(aperture, power, 
        				d, CA, DISPER_CONST_1, DISPER_CONST_2));
        		} else {
        			/*if its not in the antennae view */
        			printf("     ---    ");
        		}
        	}
        	printf("Watts\n");

        }     
}
/* calculate the distance using distance formula */
double
distance(location_t p1, location_t p2) {
	return sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y));
}

/* calculate the available power using the formula given */
double
aval_power(double aperture, double power, double d, double absorp_co, 
	double disp_const_1, double disp_const_2) {
        
        /*this is the bit with the dispersion constants, requires it to be
        raised to the power of what the dispersion constant is */
	double absorp_bit = pow((disp_const_1 + (absorp_co*d)), disp_const_2);
	/*compute the available power */
        return (360/aperture)*(power/absorp_bit);
}

/*calculate the dot product, required to determine whether the coordinate
is seen within the antennae's field of view */
double 
dot_product(location_t p1, location_t p2) {
	return ((p1.x)*(p2.x) + (p1.y)*(p2.y));
}

/*calculate the magnitude of a vector */
double 
magnitude(location_t p1) {
	return (sqrt((p1.x)*(p1.x) + (p1.y)*(p1.y)));
}

/*determine whether the point is within the antennae's field of view*/
int
check_bounds(location_t location, ant_component_t ant_component) {
	double half_aper_deg, deg_loc;
	double x, y;
	
	/*v1 correspongds to vector from the antennae to the point */
	/*v2 corresponds to the direction the antennae is facing */
	location_t v1, v2;
	
        /*half the aperture degrees*/
	half_aper_deg = (ant_component.eng_angle/2);
	/* x and y components of v1 */
	x = location.x - ant_component.x_coord;
        y = location.y - ant_component.y_coord;
	v1.x = x; v1.y = y;
	
	/* xand y components of v2 */
	v2.x = (sin(ant_component.deg*(M_PI/180)));
	v2.y = (cos(ant_component.deg*(M_PI/180)));
	
	/*location of the point with respect to the antennae in degrees. It
	is calculated by using the dot product formula */
	deg_loc = 
	(acos(dot_product(v1,v2)/(magnitude(v1)*magnitude(v2))))*(180/M_PI); 
	
	/*if the deg_loc is smaller than the aperture then it is within the
	antennaes field of view */
	if (deg_loc < half_aper_deg) {
		/*within field of view */
		return 1;
	}
	/*not in field of view */
	return 0;
}

/*read all the points inputted*/
void
read_all_points(all_points_t *all_points) {
	int n_points = 0, i=0; 
	/*reads in how many inputs there will be */
	n_points = read_stage_spec();
        /*iterates through the amount of inputs and assigns them accordingly*/
	for (i=0; i<n_points; i++) {
		scanf("%lf %lf", &(*all_points).points[i].x, 
			&(*all_points).points[i].y);
	}
	/*number of inputs correspond to the number of points */
        (*all_points).n_location = n_points;	
}
	


/* reads the specification of how many inputs there will be*/
int
read_stage_spec(void) {
	int ch, n_input;
	/*reads in the amount of inputs */
	scanf("%d", &n_input);
	/*chucks out the comments after*/
	while ((ch=getchar())!=EOF)	{
		/*stop to go to the new line */
		if (ch=='\n')	{
			break;
		}
	}
	return n_input;
}	

/*do stage 1*/
void
do_stage1(all_antennaes_t *all_antennaes) {
	printf("Stage 1\n");
	printf("-------\n");
	printf("number of antennae: %d\n", all_antennaes->n_antennae);
	printf("total power output: %2.1f Watts\n", calc_pout(all_antennaes, 
		all_antennaes->n_antennae));
	return;
}
	
/* calculate the total power output of the antennaes*/	
double
calc_pout(all_antennaes_t *all_antennaes, int n_antennae) {
	double p_out = 0.0;
	int i;
	/*Iterate through the struct and sums the power component*/
	for (i=0; i<n_antennae; i++) {
		p_out += (*all_antennaes).antennaes[i].p_out;
	}
	return p_out;
}
	
/*read all antennae components */
void
read_all_ant(all_antennaes_t *all_antennaes) {
	int n_ant = 0, i=0; 
	/*reads in how many inputs there will be */
	n_ant = read_stage_spec();
        /*iterates through the amount of inputs and assigns them accordingly*/
	for (i=0; i<n_ant; i++) {
	        scanf("%s %lf %lf %lf %lf %lf", 
	        	(*all_antennaes).antennaes[i].antennae,
	        	&(*all_antennaes).antennaes[i].x_coord,
	        	&(*all_antennaes).antennaes[i].y_coord,
	        	&(*all_antennaes).antennaes[i].p_out,
	        	&(*all_antennaes).antennaes[i].deg,
	        	&(*all_antennaes).antennaes[i].eng_angle);
	}
	/*the amount of antennaes correspond to the amount of input*/
        (*all_antennaes).n_antennae = n_ant;	
}
		
