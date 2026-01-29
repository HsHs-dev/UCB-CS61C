#include "transpose.h"

/* The naive transpose function as a reference. */
void transpose_naive(int n, int blocksize, int *dst, int *src) {
  for (int x = 0; x < n; x++) {
    for (int y = 0; y < n; y++) {
      dst[y + x * n] = src[x + y * n];
    }
  }
}

/* Implement cache blocking below. You should NOT assume that n is a
 * multiple of the block size. */
void transpose_blocking(int n, int blocksize, int *dst, int *src) {

  // I use faster loads but non-cache friendly stores, this is a little
  // faster because loads misses stall the cpu, but store misses don't
  //   for (int x = 0; x < n; x++) {
  //     for (int y = 0; y < n; y++) {
  //       dst[x + y * n] = src[y + x * n];
  //     }
  //   }

  for (int x = 0; x < n; x += blocksize) {
    for (int y = 0; y < n; y += blocksize) {
      for (int chunkX = 0; chunkX < blocksize; chunkX++) {
        for (int chunkY = 0; chunkY < blocksize; chunkY++) {

          // coordinates inside the block
          int x_block_offset = x + chunkX;
          int y_block_offset = y + chunkY;

          // don't go out of bounds
          if (x_block_offset < n && y_block_offset < n) {
            dst[x_block_offset + y_block_offset * n] =
                src[y_block_offset + x_block_offset * n];
          }
        }
      }
    }
  }
}
