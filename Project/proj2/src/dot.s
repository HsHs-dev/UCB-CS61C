.globl dot

.text
# =======================================================
# FUNCTION: Dot product of 2 int vectors
# Arguments:
#   a0 (int*) is the pointer to the start of v0
#   a1 (int*) is the pointer to the start of v1
#   a2 (int)  is the length of the vectors
#   a3 (int)  is the stride of v0
#   a4 (int)  is the stride of v1
# Returns:
#   a0 (int)  is the dot product of v0 and v1
# Exceptions:
# - If the length of the vector is less than 1,
#   this function terminates the program with error code 75.
# - If the stride of either vector is less than 1,
#   this function terminates the program with error code 76.
# =======================================================
dot:

  # if the length of the vector length is < 1, jump to 
  # error_length
  li t0, 1
  blt a2, t0, error_length

  # if the stride of either vector is < 1, jump to
  # error_stride
  blt a3, t0, error_stride
  blt a4, t0, error_stride

  li t0, 0 # loop counter
  li t6, 0 # sum = 0
loop_start:
  beq t0, a2, loop_end

  slli t1, a3, 2
  mul t1, t1, t0
  add t1, t1, a0 # t1 has the pointer to a0[t0]

  slli t2, a4, 2
  mul t2, t2, t0
  add t2, t2, a1 # t2 has the pointer to a1[t0]

  lw t3, 0(t1) # t3 = a0[t0]
  lw t4, 0(t2) # t4 = a1[t0]

  mul t5, t3, t4
  add t6, t6, t5

  addi t0, t0, 1
  j loop_start
loop_end:
    add a0, x0, t6
    ret

error_length:
  li a1, 75
  jal exit2

error_stride:
  li a1, 76
  jal exit2