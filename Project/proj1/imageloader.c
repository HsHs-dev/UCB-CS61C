/************************************************************************
**
** NAME:        imageloader.c
**
** DESCRIPTION: CS61C Fall 2020 Project 1
**
** AUTHOR:      Dan Garcia  -  University of California at Berkeley
**              Copyright (C) Dan Garcia, 2020. All rights reserved.
**              Justin Yokota - Starter Code
**				YOUR NAME HERE
**
**
** DATE:        2020-08-15
**
**************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include <string.h>
#include "imageloader.h"

//Opens a .ppm P3 image file, and constructs an Image object. 
//You may find the function fscanf useful.
//Make sure that you close the file with fclose before returning.
Image *readData(char *filename) 
{

	FILE* fp = fopen(filename, "r");

	if (fp == NULL) {
		printf("Failed to open %s file", filename);
		exit(EXIT_FAILURE);
	}

	// discard the P3 header
	fscanf(fp, "%*s");

	// read the width and height
	int width, height;
	fscanf(fp, "%d %d", &width, &height);

	// initialize the image
	Image* img = malloc(sizeof (Image));
	img->rows = width;
	img->cols = height;

	img->image = malloc(width * sizeof(Color*));
	for (int i = 0; i < height; i++) {
		img->image[i] = malloc(height * sizeof(Color));
	}




	// fclose(fp);
	// return ... 

}

//Given an image, prints to stdout (e.g. with printf) a .ppm P3 file with the image's data.
void writeData(Image *image)
{
	//YOUR CODE HERE
}

//Frees an image
void freeImage(Image *image)
{
	//YOUR CODE HERE
}