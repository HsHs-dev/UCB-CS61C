#include <stdio.h>

int sum(int* summands, int n);


int main(void) {

  int arr[] = {1, 2, 3, 4, 5};
  printf("%d\n", sum(arr, sizeof(arr)/sizeof(arr[0])));

  return 0;
}

int sum(int* summands, int n) {
  int sum = 0;
  printf("%zu\n", sizeof(summands)); // sizeof returns the size of the pointer not the array
  for (int i = 0; i < n; i++)
    sum += *(summands + i);
  return sum;
}
