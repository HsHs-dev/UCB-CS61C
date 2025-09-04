#include <stdio.h>
#include "bit_ops.h"

// Return the nth bit of x.
// Assume 0 <= n <= 31
unsigned get_bit(unsigned x,
                 unsigned n) {
    // YOUR CODE HERE
    // Returning -1 is a placeholder (it makes
    // no sense, because get_bit only returns 
    // 0 or 1)

    // put the bit in the LSB position
    x = x >> n;

    // test against 1 with &
    x = x & 1;

    return x;
}

// Set the nth bit of the value of x to v.
// Assume 0 <= n <= 31, and v is 0 or 1
void set_bit(unsigned * x,
             unsigned n,
             unsigned v) {
    // YOUR CODE HERE

    // a mask used to clear the specified bit in order to set it
    // or ~(1 << n)
    unsigned clear_bit = -1 ^ (1 << n);
    *x = *x & clear_bit;

    // after the bit is cleared, we can now set it to v;
    *x = *x ^ (v << n);
}

// Flip the nth bit of the value of x.
// Assume 0 <= n <= 31
void flip_bit(unsigned * x,
              unsigned n) {
    // YOUR CODE HERE

    *x = *x ^ (1 << n);
}

