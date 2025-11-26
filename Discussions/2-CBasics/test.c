#include <stdio.h>

int bar(int *arr, size_t n);

int main(void) {

  int arr[] = {1, 2, 3, 4, 5};
  size_t n = sizeof(arr) / sizeof(arr[0]);
  printf("%d\n", bar(arr, n));
  return 0;
}

int bar(int *arr, size_t n) {
  int sum = 0, i;
  for (i = n; i > 0; i--) {
    sum += !arr[i - 1];
  }
  return ~sum + 1;
}
