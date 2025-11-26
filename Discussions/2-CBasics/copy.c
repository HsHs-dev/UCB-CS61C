#include <stdio.h>

void copy(char* src, char* dst);

int main(void) {

  char name[] = "Hassan";
  char me[sizeof(name)/sizeof(name[0])];

  copy(name, me);

  printf("%s\n", me);

  return 0;
}

void copy(char* src, char* dst) {
  while (*dst++ = *src++);
}
