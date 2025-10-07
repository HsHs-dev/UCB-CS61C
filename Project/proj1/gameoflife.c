/************************************************************************
**
** NAME:        gameoflife.c
**
** DESCRIPTION: CS61C Fall 2020 Project 1
**
** AUTHOR:      Justin Yokota - Starter Code
**				Hassan Siddig Mahmoud
**
**
** DATE:        2020-08-23
**
**************************************************************************/

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include "imageloader.h"

// decode the rule into an 18 elements array (1 lives, 0 dies)
uint32_t* get_rules(int rule) {

	int num_of_rules = 18;
	uint32_t *result = malloc(num_of_rules * sizeof(uint32_t));

	if (result == NULL) {
		printf("Memory allocation failed\n");
		exit(EXIT_FAILURE);
	}

	int mask = 1;
	// fill the top first, because we are extracting right to left
	for (int i = 0; i < num_of_rules; i++) {
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
Color *evaluateOneCell(Image *image, int row, int col, uint32_t *rule_arr)
{
	// get the rules in an array for fast lookup
	// in a cleaner math:
	// int index = (state * 9) + neighbors; // state = 0 || 1
	// int next = (rule >> index) & 1;

	int rows = image->rows;
	int cols = image->cols;

	Color* new_color = malloc(sizeof(Color));
	new_color->R = 0;
	new_color->G = 0;
	new_color->B = 0;

	// evaluate each channel bits independently
	for (int channel = 0; channel < 3; channel++) {
		for (int bit = 0; bit < 8; bit++) {

			int alive = 0;

			int mask = 1 << bit;

			for (int r = -1; r <= 1; r++) {
				for (int c = -1; c <= 1; c++) {

					if (r == 0 && c == 0) continue;

					int new_row = floor_mod(row + r, rows);
					int new_col = floor_mod(col + c, cols);
					Color neigh = image->image[new_row][new_col];

					// pick the channel that we are working on
					uint8_t neigh_channel = (channel == 0) ? neigh.R :
															(channel == 1) ? neigh.G : neigh.B;
					
					// check only this bit
					if (neigh_channel & mask) {
						alive++;
					}
					
				}
			}

			// determine the state of that bit
			Color cur = image->image[row][col];
			uint8_t cur_channel = channel == 0 ? cur.R : channel == 1 ? cur.G : cur.B;
			int curr_state = (cur_channel & mask) ? 1 : 0;
			int new_state = rule_arr[(curr_state ? 9 : 0) + alive];

			// set this bit in the new color
			if (new_state) {
				if (channel == 0) {
					new_color->R |= mask;
				} else if (channel == 1) {
					new_color->G |= mask;
				} else {
					new_color->B |= mask;
				}
			}

		}
	}

	free(rule_arr);

	return new_color;
}

//The main body of Life; given an image and a rule, computes one iteration of the Game of Life.
//You should be able to copy most of this from steganography.c
Image *life(Image *image, uint32_t rule)
{
	Image* new_image = malloc(sizeof(Image));

	if (new_image == NULL) {
		printf("Memory allocation failed\n");
		exit(EXIT_FAILURE);
	}

	int rows = image->rows;
	int cols = image->cols;

	new_image->rows = rows;
	new_image->cols = cols;
	new_image->image = malloc(rows * sizeof(Color*));

	if (new_image->image == NULL) {
		printf("Memory allocation failed\n");
		free(new_image);
		exit(EXIT_FAILURE);
	}

	for (int i = 0; i < rows; i++) {
		new_image->image[i] = malloc(cols * sizeof(Color));
	}

	uint32_t *rule_arr = get_rules(rule);

	for (int i = 0; i < rows; i++) {
		for (int j = 0; j < cols; j++) {
			Color* color_ptr = evaluateOneCell(image, i, j, rule_arr);
			new_image->image[i][j] = *color_ptr;
			free(color_ptr);
		}
	}

	free(rule_arr);

	return new_image;
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

	Image* image = readData(argv[1]);
	Image* new_image = life(image, strtol(argv[2], NULL, 16));
	freeImage(image);
	writeData(new_image);
	freeImage(new_image);
	return 0;
}
