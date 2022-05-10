/*
Course Name: CS4345 Operating Systems
Semester: Spring 2022
Student Names: Thuong Pham and Sean Vickers
Assignment 1
*/
#include <stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<sys/wait.h>
#include <sys/types.h>

int main(){
    
    pid_t p;
    int num;
    printf("Id at beginning: %d\n", getpid());
    printf("Enter a positive integer: ");
    scanf("%d", &num);
    if(num <= 0){
        printf("Invalid Number. Please run again with a Positive Integer.");
        return 1;
    }
    printf("Number entered is : %d\n", num);
    
    p = fork();

    if(p>0){
        printf("Parent Start with ID: %d\n", getpid());
        printf("Parent will now wait until child is done.\n");
        wait(NULL);
        printf("Parent End.\n\n");
    }
    else if(p==0){
        printf("Child Start with ID: %d\n", getpid());
        printf ("%d ", num);
        while(num > 1){
            if(num % 2 == 0) { //even number
                num = num/2;
            }
            else{ //odd number
                num = 3 * num + 1;
            }
            printf ("%d ", num); //Will print "1 "
        }
        printf("\nChild End.\n");
    }
    else{
       fprintf(stderr, "Fork failure. Code terminating.");
       return 1; 
    }

return 0;
}
