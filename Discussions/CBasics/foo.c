#include <stdio.h>
#include <stdlib.h>

char* foo();

int main(void) {

  printf("%s\n", foo());


  return 0;
}

char* foo() {

  char* buffer[64];
  gets(buffer);

  printf("buffer read: %s\n", buffer);

  char* important_stuff = (char*) malloc(11 * sizeof(char));

  int i;
  for (i = 0; i < 10; i++) important_stuff[i] = buffer[i];
  important_stuff[i] = '\0';
  return important_stuff;

}
