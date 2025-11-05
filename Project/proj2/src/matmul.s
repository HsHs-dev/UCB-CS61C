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
    addi sp, sp, -20
    sw s0, 0(sp)
    sw s1, 4(sp)
    sw s2, 8(sp)
    sw s3, 12(sp)
    sw s4, 16(sp)

    mv s0, a0 # pointer to m0
    mv s1, a3 # pointer to m1
    mv s2, a1 # m0 rows
    mv s3, a2 # m0 cols
    mv s4, a5 # m1 cols

    # allocate memroy for d
    # save before jumping to malloc
    addi sp, sp, -4
    sw ra, 0(sp)
    # calculating dimensions of d and allocating memroy for it
    # rows * cols * sizeof(element)
    mul a0, a1, a5
    li t0, 4
    mul a0, a0, t0
    jal ra, malloc
    mv a6, a0 # a6 points to the begining of matrix d allocated memory
    lw ra, 0(sp)
    addi sp, sp, 4

    li t0, 0 # outer loop counter
outer_loop_start:
    beq t0, a1, outer_loop_end
    li t1, 0 # inner loop counter
inner_loop_start:
    beq t1, a5, inner_loop_end

    mul t2, t0, s3
    add t2, t2, s0 
    mv a0, t2 # a0 = m0 + (i * m0-cols)

    add a1, s1, t1 # a1 = b + j

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
    add t2, s4, t1 # t2 = m1-cols + j
    mul t2, t2, t0 # t2 = i * m1-cols + j
    add t2, t2, a6 # t2 = d[i * m1-cols + j]
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
    addi sp, sp, 20
    ret

malloc:
    add a1, a0, x0
    addi a0, x0, 9
    ecall
    jr ra

m0_error:
  li a0, 72
  li a7, 10
  ecall
  j end

m1_error:
  li a0, 73
  li a7, 10
  ecall
  j end

comp_error:
  li a0, 74
  li a7, 10
  ecall

end: