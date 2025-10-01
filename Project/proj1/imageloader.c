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
	img->rows = height;
	img->cols = width;

	img->image = malloc(height * sizeof(Color*));
	for (int i = 0; i < height; i++) {
		img->image[i] = malloc(width * sizeof(Color));
	}

	// discard the scale
	fscanf(fp, "%*s");

	// set the Colors of the image
	int r, g, b;
	for (int i = 0; i < height; i++) {
		for (int j = 0; j < width; j++) {
			fscanf(fp, "%d %d %d", &r, &g, &b);
			img->image[i][j] = (struct Color) {r, g, b};
		}
	}

	fclose(fp);
	return img; 
}

//Given an image, prints to stdout (e.g. with printf) a .ppm P3 file with the image's data.
void writeData(Image *image)
{
	printf("P3\n%d %d\n%d\n", image->cols, image->rows, 255);

	for (int i = 0; i < image->rows; i++) {
    for (int j = 0; j < image->cols; j++) {
      printf("%3d ", image->image[i][j].R);
      printf("%3d ", image->image[i][j].G);
      printf("%3d", image->image[i][j].B);
			if (j != image->cols - 1) {
				printf("   ");
			}
    }

    putchar('\n');
  }
}

//Frees an image
void freeImage(Image *image)
{
	for (int i = 0; i < image->rows; i++) {
		free(image->image[i]);
	}
	
	free(image->image);
	free(image);
}