#include <stdio.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>
#include <time.h>

#define BILLION 1000000000L
#define T_ORDERLOW 1 
#define T_ORDERHIGH 5
#define N_ORDERLOW 1
#define N_ORDERHIGH 5
#define T_PREPARATION 1
#define T_BAKE 10

void print_error_args(){
	printf("ERROR: Invalid arguments.\n");
}

