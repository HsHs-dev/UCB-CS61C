/************************************************************************
**
** NAME:        steganography.c
**
** DESCRIPTION: CS61C Fall 2020 Project 1
**
** AUTHOR:      Dan Garcia  -  University of California at Berkeley
**              Copyright (C) Dan Garcia, 2020. All rights reserved.
**				Justin Yokota - Starter Code
**				Hassan Siddig Mahmoud
**
** DATE:        2020-08-23
**
**************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include "imageloader.h"

//Determines what color the cell at the given row/col should be. This should not affect Image, and should allocate space for a new Color.
Color *evaluateOnePixel(Image *image, int row, int col)
{
	int blue_channel = image->image[row][col].B;
	int lsb_val = blue_channel & 1;

	int white = 255;
	int black = 0;

	Color* color = malloc(sizeof(Color));

	if (color == NULL) {
		printf("Memory allocation failed\n");
		exit(EXIT_FAILURE);
	}

	if (lsb_val) {
		color->R = white;
		color->B = white;
		color->G = white;
	} else {
		color->R = black;
		color->G = black;
		color->B = black;
	}

	return color;
}

//Given an image, creates a new image extracting the LSB of the B channel.
Image *steganography(Image *image)
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

	for (int i = 0; i < rows; i++) {
		for (int j = 0; j < cols; j++) {
			Color* color_ptr = evaluateOnePixel(image, i, j);
			new_image->image[i][j] = *color_ptr;
			free(color_ptr);
		}
	}

	return new_image;
}

/*
Loads a file of ppm P3 format from a file, and prints to stdout (e.g. with printf) a new image, 
where each pixel is black if the LSB of the B channel is 0, 
and white if the LSB of the B channel is 1.

argc stores the number of arguments.
argv stores a list of arguments. Here is the expected input:
argv[0] will store the name of the program (this happens automatically).
argv[1] should contain a filename, containing a file of ppm P3 format (not necessarily with .ppm file extension).
If the input is not correct, a malloc fails, or any other error occurs, you should exit with code -1.
Otherwise, you should return from main with code 0.
Make sure to free all memory before returning!
*/
int main(int argc, char **argv)
{

	Image* image = readData(argv[1]);
	Image* new_image = steganography(image);
	writeData(new_image);
	freeImage(image);
	freeImage(new_image);
	
	return 0;
}
