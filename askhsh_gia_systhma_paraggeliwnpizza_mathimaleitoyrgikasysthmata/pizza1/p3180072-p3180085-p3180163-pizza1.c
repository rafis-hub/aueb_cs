#include <stdio.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>
#include <time.h>
#include "p3180072-p3180085-p3180163-pizza1.h"

	pthread_mutex_t lock;
	pthread_cond_t cond;
	int cook = 6;
	int oven = 5;
	double total_time = 0.0;
	double max_time = 0.0;

typedef struct{
	int id_order;
	int pizzas_per_order;

}OrderInfo;
	
void *order(void *x){
	
	OrderInfo *optr = (OrderInfo *)x; 
	OrderInfo orderItem = *optr; 
	struct timespec start, stop;	
	clock_gettime(CLOCK_REALTIME,&start);
	int id = orderItem.id_order;	
	int th;
	printf("This is order with id: %d! \n",id);
	th = pthread_mutex_lock(&lock);
	while (cook == 0){ 
		printf("Order with id: %d is prepared!Waiting for COOK!\n",id);
		th = pthread_cond_wait(&cond,&lock);
	}
	cook--;
	
	th = pthread_mutex_unlock(&lock);
	sleep(orderItem.pizzas_per_order*T_PREPARATION);
	th = pthread_mutex_lock(&lock);
	while (oven == 0){
		printf("Order with id: %d is prepared!Waiting for OVEN!\n",id);
		th = pthread_cond_wait(&cond,&lock);
	}
	oven--;
	th = pthread_mutex_unlock(&lock);
	sleep(T_BAKE);
	th = pthread_mutex_lock(&lock);
	oven++;
	cook++;	
	th = pthread_cond_signal(&cond);
	th = pthread_mutex_unlock(&lock);

	clock_gettime(CLOCK_REALTIME,&stop);
	
	double out = ((stop.tv_sec - start.tv_sec)
		+ (stop.tv_nsec - start.tv_nsec)/BILLION);

	total_time += out;	

	if (out > max_time){
		max_time = out;
	}	

	printf("Order with id: %d is ready! In %.1f \n",id, out);
	printf("\n-----------------------\n");
 	
	pthread_exit(0);
}

int main(int argc, char** argv){
	
	if (argc == 3){
		int orders = atoi(argv[1]);
		int seed = atoi(argv[2]);
		printf("Hello in pizza home!\n");
		int th; 
		int pizza; 
		OrderInfo ordersArray[orders];
		for (int i = 0; i < orders; i++){
			ordersArray[i].id_order = i;
			pizza = rand_r(&seed)%N_ORDERHIGH+N_ORDERLOW;	
			ordersArray[i].pizzas_per_order =  pizza;
		}
		pthread_t threads[orders];
		pthread_mutex_init(&lock, NULL);
		pthread_cond_init(&cond, NULL);	
		for (int i = 0; i < orders; i++){			
			th = pthread_create(&threads[i],NULL, order, &ordersArray[i]);
			sleep(rand_r(&seed)%T_ORDERHIGH+T_ORDERLOW);
		}
		for (int i = 0; i < orders; i++){
			th = pthread_join(threads[i],NULL);		
		} 
		printf("final time for all orders: %.1f\n", total_time);
		double avg = total_time/orders;
		printf("average time for order: %.1f\n", avg);
		printf("Time of the max time order: %.1f\n", max_time);
		pthread_mutex_destroy(&lock);
		pthread_cond_destroy(&cond);
		return 0;

	}
	else{
		print_error_args();
	}
	return 0;
}


