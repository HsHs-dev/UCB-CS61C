#include <stdio.h>
#include <stdlib.h>

#define mat(m, i, j, cols) (m[(i) * (cols) + (j)])

int dot(int a[], int b[], int length, int strideA, int strideB);
int* matmul(int a[], int rows_a, int cols_a, 
            int b[], int rows_b, int cols_b);

int main(void) {
  
  // int a[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};
  // int b[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};
  // printf("%d\n", dot(a,b, (sizeof(a) / sizeof(a[0]))));

  int rowsA = 5, colsA = 3;
  int rowsB = 3, colsB = 4;

  int* a = malloc(rowsA * colsA * sizeof(int));
  int* b = malloc(rowsB * colsB * sizeof(int));

  for (int i = 0; i < rowsA; i++) {
    for (int j = 0; j < colsA; j++) {
      mat(a, i, j, colsA) = i * 10 + j + 1;
    }
  }

  for (int i = 0; i < rowsB; i++) {
    for (int j = 0; j < colsB; j++) {
      mat(b, i, j, colsB) = 20 - (i * colsB + j);
    }
  }

  int* d = matmul(a, rowsA, colsA, b, rowsB, colsB);

  for (int i = 0; i < rowsA; i++) {
    for (int j = 0; j < colsB; j++) {
      printf("%5d", mat(d, i, j, colsB));
    }
    putchar('\n');
  }

  free(a);
  free(b);
  free(d);

  return 0;
}

int dot(int a[], int b[], int length, int strideA, int strideB) {
  int sum = 0;
  for (int i = 0; i < length; i++) {
    sum += a[i * strideA] * b[i * strideB];
  }

  return sum;
}

int* matmul(int a[], int rows_a, int cols_a, 
            int b[], int rows_b, int cols_b) {
  int* d = malloc(rows_a * cols_b * sizeof(int));
  for (int i = 0; i < rows_a; i++) {
    for (int j = 0; j < cols_b; j++) {
      d[(i * cols_b) + j] = dot((a + (i * cols_a)), (b + j), cols_a, 1, cols_b);
    }
  }

  return d;
}

