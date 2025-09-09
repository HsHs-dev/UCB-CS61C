#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include "lfsr.h"
#include "bit_ops.c"

void setBit(uint16_t*, int, uint16_t);

void lfsr_calculate(uint16_t *reg) {

  // extract the bits
  uint16_t zero = get_bit(*reg, 0);
  uint16_t two = get_bit(*reg, 2);
  uint16_t three = get_bit(*reg,3);
  uint16_t five = get_bit(*reg, 5);

  // shift the register 1-bit to the right
  *reg >>= 1;

  // feed the XOR of the bits to the MSB
  uint16_t xor = (zero ^ two) ^ three ^ five;

  setBit(reg, 15, xor);
    
}


void setBit(uint16_t* reg, int bit, uint16_t val) {

  unsigned clear_bit = -1 ^ (1 << bit);
  *reg &= clear_bit;

  // after the bit is cleared, we can now set it to v;
  *reg ^= (val << bit);

}
