.globl matmul

.text
# =======================================================
# FUNCTION: Matrix Multiplication of 2 integer matrices
# 	d = matmul(m0, m1)
# Arguments:
# 	a0 (int*)  is the pointer to the start of m0 
#	a1 (int)   is the # of rows (height) of m0
#	a2 (int)   is the # of columns (width) of m0
#	a3 (int*)  is the pointer to the start of m1
# 	a4 (int)   is the # of rows (height) of m1
#	a5 (int)   is the # of columns (width) of m1
#	a6 (int*)  is the pointer to the the start of d
# Returns:
#	None (void), sets d = matmul(m0, m1)
# Exceptions:
#   Make sure to check in top to bottom order!
#   - If the dimensions of m0 do not make sense,
#     this function terminates the program with exit code 72.
#   - If the dimensions of m1 do not make sense,
#     this function terminates the program with exit code 73.
#   - If the dimensions of m0 and m1 don't match,
#     this function terminates the program with exit code 74.
# =======================================================

# =======================================================
# Refer to pseudo.c to get a high-level understanding of
# how the function works
# =======================================================

matmul:

    li t0, 1

    # check the dimensions of m0
    blt a1, t0, m0_error
    blt a2, t0, m0_error

    # check the dimensions of m0
    blt a4, t0, m1_error
    blt a5, t0, m1_error

    # if cols a != rows b, jump to comp_error
    bne a2, a4, comp_error

    # save the arguments to local registers
    addi sp, sp, -24
    sw s0, 0(sp)
    sw s1, 4(sp)
    sw s2, 8(sp)
    sw s3, 12(sp)
    sw s4, 16(sp)
    sw s5, 20(sp)

    mv s0, a0 # pointer to m0
    mv s1, a3 # pointer to m1
    mv s2, a1 # m0 rows
    mv s3, a2 # m0 cols
    mv s4, a5 # m1 cols
    mv s5, a6 # a6 (pointer to result)

    li t0, 0 # outer loop counter
outer_loop_start:
    beq t0, s2, outer_loop_end
    li t1, 0 # inner loop counter
inner_loop_start:
    beq t1, s4, inner_loop_end

    mul t2, t0, s3
    slli t2, t2, 2
    add t2, t2, s0 
    mv a0, t2 # a0 = m0 + (i * m0-cols)

    slli t2, t1, 2
    add t2, t2, s1
    mv a1, t2 # a1 = m1 + j

    mv a2, s3 # a2 = length

    li a3, 1 # stride-m0 = 1

    mv a4, s4 # stride-m1 = m1-cols

    # call the dot function
    addi sp, sp, -12
    sw ra, 0(sp)
    sw t0, 4(sp)
    sw t1, 8(sp)
    jal ra, dot
    lw ra, 0(sp)
    lw t0, 4(sp)
    lw t1, 8(sp)
    addi sp, sp, 12

    # set d's element to the returned value
    mul t2, t0, s4 # t2 = (i * m1-cols)
    add t2, t2, t1 # t2 = (i * m1-cols) + j
    slli t2, t2, 2 # multiply by 4 to get the actual bytes number
    add t2, t2, s5 # t2 = d[i * m1-cols + j]
    sw a0, 0(t2)

    addi t1, t1, 1
    j inner_loop_start
inner_loop_end:
    addi t0, t0, 1
    j outer_loop_start

outer_loop_end:
    # Prologue
    lw s0, 0(sp)
    lw s1, 4(sp)
    lw s2, 8(sp)
    lw s3, 12(sp)
    lw s4, 16(sp)
    lw s5, 20(sp)
    addi sp, sp, 24
    ret

m0_error:
  # exit with error code 72 using the test harness exit2 wrapper
  li a1, 72
  jal exit2

m1_error:
  # exit with error code 73 using the test harness exit2 wrapper
  li a1, 73
  jal exit2

comp_error:
  # exit with error code 74 using the test harness exit2 wrapper
  li a1, 74
  jal exit2