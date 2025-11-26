#include <stddef.h>
#include <stdio.h>

void cs61c(char* src, size_t length);

int main(void) {

  char fact[] = "hassan is awesome!";
  size_t length = sizeof(fact) / sizeof(fact[0]);
  cs61c(fact, length);
  printf("%s\n", fact);

  return 0;
}

void cs61c(char* src, size_t length) {
  char *srcptr, *replaceptr;
  char replacement[16] = "61C is awesome!";
  srcptr = src;
  replaceptr = replacement;
  if (length >= 16) {
    for (int i = 0; i < 16; i++)
      *srcptr++ = *replaceptr++;
  }
}
