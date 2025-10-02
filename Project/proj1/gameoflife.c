/************************************************************************
**
** NAME:        gameoflife.c
**
** DESCRIPTION: CS61C Fall 2020 Project 1
**
** AUTHOR:      Justin Yokota - Starter Code
**				YOUR NAME HERE
**
**
** DATE:        2020-08-23
**
**************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include "imageloader.h"

// decode the rule into an 18 elements array (1 lives, 0 dies)
int* get_rules(int rule) {

	int num_of_rules = 18;
	int *result = malloc(num_of_rules * sizeof(int));

	if (result == NULL) {
		printf("Memory allocation failed\n");
		exit(EXIT_FAILURE);
	}

	int mask = 1;
	// fill the top first, because we are extracting right to left
	for (int i = num_of_rules - 1; i >= 0; i--) {
		result[i] = rule & mask;
		rule = rule >> 1;
	}

	return result;
}

// returns the floor mod of two numbers
int floor_mod(int a, int b) {
	return ((a % b) + b) % b;
}

//Determines what color the cell at the given row/col should be. This function allocates space for a new Color.
//Note that you will need to read the eight neighbors of the cell in question. The grid "wraps", so we treat the top row as adjacent to the bottom row
//and the left column as adjacent to the right column.
Color *evaluateOneCell(Image *image, int row, int col, uint32_t rule)
{
	// get the rules in an array for fast lookup
	// in a cleaner math:
	// int index = (state * 9) + neighbors; // state = 0 || 1
	// int next = (rule >> index) & 1;
	int *rule_arr = get_rules(rule);



	// free(rule_arr);


}

//The main body of Life; given an image and a rule, computes one iteration of the Game of Life.
//You should be able to copy most of this from steganography.c
Image *life(Image *image, uint32_t rule)
{
	//YOUR CODE HERE
}

/*
Loads a .ppm from a file, computes the next iteration of the game of life, then prints to stdout the new image.

argc stores the number of arguments.
argv stores a list of arguments. Here is the expected input:
argv[0] will store the name of the program (this happens automatically).
argv[1] should contain a filename, containing a .ppm.
argv[2] should contain a hexadecimal number (such as 0x1808). Note that this will be a string.
You may find the function strtol useful for this conversion.
If the input is not correct, a malloc fails, or any other error occurs, you should exit with code -1.
Otherwise, you should return from main with code 0.
Make sure to free all memory before returning!

You may find it useful to copy the code from steganography.c, to start.
*/
int main(int argc, char **argv)
{
	//YOUR CODE HERE
}
