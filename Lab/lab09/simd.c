#include "simd.h"
#include <emmintrin.h>
#include <stdio.h>
#include <time.h>
#include <x86intrin.h>

long long int sum(int vals[NUM_ELEMS]) {
  clock_t start = clock();

  long long int sum = 0;
  for (unsigned int w = 0; w < OUTER_ITERATIONS; w++) {
    for (unsigned int i = 0; i < NUM_ELEMS; i++) {
      if (vals[i] >= 128) {
        sum += vals[i];
      }
    }
  }
  clock_t end = clock();
  printf("Time taken: %Lf s\n", (long double)(end - start) / CLOCKS_PER_SEC);
  return sum;
}

long long int sum_unrolled(int vals[NUM_ELEMS]) {
  clock_t start = clock();
  long long int sum = 0;

  for (unsigned int w = 0; w < OUTER_ITERATIONS; w++) {
    for (unsigned int i = 0; i < NUM_ELEMS / 4 * 4; i += 4) {
      if (vals[i] >= 128)
        sum += vals[i];
      if (vals[i + 1] >= 128)
        sum += vals[i + 1];
      if (vals[i + 2] >= 128)
        sum += vals[i + 2];
      if (vals[i + 3] >= 128)
        sum += vals[i + 3];
    }

    // This is what we call the TAIL CASE
    // For when NUM_ELEMS isn't a multiple of 4
    // NONTRIVIAL FACT: NUM_ELEMS / 4 * 4 is the largest multiple of 4 less than
    // NUM_ELEMS
    for (unsigned int i = NUM_ELEMS / 4 * 4; i < NUM_ELEMS; i++) {
      if (vals[i] >= 128) {
        sum += vals[i];
      }
    }
  }
  clock_t end = clock();
  printf("Time taken: %Lf s\n", (long double)(end - start) / CLOCKS_PER_SEC);
  return sum;
}

// debugging function
void print_m128i_epi32(__m128i vec) {
  int vals[4];
  _mm_storeu_si128((__m128i *)vals, vec); // store vector lanes into array

  printf("[");
  for (int i = 0; i < 4; i++) {
    printf("%d", vals[i]);
    if (i != 3)
      printf(", ");
  }
  printf("]\n");
}

long long int sum_simd(int vals[NUM_ELEMS]) {
  clock_t start = clock();
  __m128i _127 = _mm_set1_epi32(
      127); // This is a vector with 127s in it... Why might you need this?
  long long int result = 0; // This is where you should put your final result!
  /* DO NOT DO NOT DO NOT DO NOT WRITE ANYTHING ABOVE THIS LINE. */

  for (unsigned int w = 0; w < OUTER_ITERATIONS; w++) {
    /* YOUR CODE GOES HERE */

    // we compute 4 elements per iteration
    for (unsigned int i = 0; i < NUM_ELEMS / 4 * 4; i += 4) {

      // load 4 32-bit values from vals to temp vector
      __m128i temp = _mm_loadu_si128((__m128i *)(vals + i));

      // cmp the values against 127, if > then 1, otherwise 0
      // then AND it with the mask to choose who survives to addition
      __m128i mask = _mm_cmpgt_epi32(temp, _127);
      temp = _mm_and_si128(temp, mask);

      // we store after each iteration to avoid overflow issues
      // this can be costive, but we tradeoff the small
      // performance hit to the correctness
      // this can be solved by throwing the accumulator vector and accumulate in
      // the result directly using _mm_hadd_epi32 which is horizontal add twice,
      // then extract one lane (it will contain the total sum) and add it to the
      // result

      // __m128i sum1 = _mm_hadd_epi32(temp, temp);
      // __m128i sum2 = _mm_hadd_epi32(sum1, sum1);

      // int lane_sum = _mm_cvtsi128_si32(sum2);
      // result += lane_sum;

      int lanes[4];
      _mm_storeu_si128((__m128i *)lanes, temp);
      for (int i = 0; i < 4; i++)
        result += lanes[i];
    }

    /* You'll need a tail case. */
    for (unsigned int i = NUM_ELEMS / 4 * 4; i < NUM_ELEMS; i++) {
      if (vals[i] >= 128)
        result += vals[i];
    }
  }

  clock_t end = clock();
  printf("Time taken: %Lf s\n", (long double)(end - start) / CLOCKS_PER_SEC);
  return result;
}

long long int sum_simd_unrolled(int vals[NUM_ELEMS]) {
  clock_t start = clock();
  __m128i _127 = _mm_set1_epi32(127);
  long long int result = 0;
  for (unsigned int w = 0; w < OUTER_ITERATIONS; w++) {
    /* COPY AND PASTE YOUR sum_simd() HERE */
    /* MODIFY IT BY UNROLLING IT */

    // we compute 16 elements per iteration
    for (unsigned int i = 0; i < NUM_ELEMS / 16 * 16; i += 16) {

      __m128i temp1 = _mm_loadu_si128((__m128i *)(vals + i));
      __m128i temp2 = _mm_loadu_si128((__m128i *)(vals + i + 4));
      __m128i temp3 = _mm_loadu_si128((__m128i *)(vals + i + 8));
      __m128i temp4 = _mm_loadu_si128((__m128i *)(vals + i + 12));

      __m128i mask1 = _mm_cmpgt_epi32(temp1, _127);
      __m128i mask2 = _mm_cmpgt_epi32(temp2, _127);
      __m128i mask3 = _mm_cmpgt_epi32(temp3, _127);
      __m128i mask4 = _mm_cmpgt_epi32(temp4, _127);

      temp1 = _mm_and_si128(temp1, mask1);
      temp2 = _mm_and_si128(temp2, mask2);
      temp3 = _mm_and_si128(temp3, mask3);
      temp4 = _mm_and_si128(temp4, mask4);

      __m128i temp = _mm_add_epi32(_mm_add_epi32(temp1, temp2),
                                   _mm_add_epi32(temp3, temp4));
      int lanes[4];
      _mm_storeu_si128((__m128i *)lanes, temp);
      for (int i = 0; i < 4; i++)
        result += lanes[i];
    }

    /* You'll need a tail case. */
    for (unsigned int i = NUM_ELEMS / 16 * 16; i < NUM_ELEMS; i++) {
      if (vals[i] >= 128)
        result += vals[i];
    }

    /* You'll need 1 or maybe 2 tail cases here. */
  }
  clock_t end = clock();
  printf("Time taken: %Lf s\n", (long double)(end - start) / CLOCKS_PER_SEC);
  return result;
}