#include <stdio.h>

void increment(char* string);

int main(void) {

  char name[] = "Hassan";
  increment(name);
  printf("%s\n", name);

  return 0;
}

void increment(char* string) {
  for (int i = 0; string[i] != 0; i++) {
    (*(string + i))++;
  }
}
