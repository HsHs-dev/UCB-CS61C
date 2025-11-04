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

    # allocate memroy for d

    # save before jumping to malloc
    addi sp, sp, -12
    sw s0, 0(sp)
    sw ra, 4(sp)
    sw a0, 8(sp)
    # calculating dimensions of d and allocating memroy for it
    mul a0, a1, a5
    jal ra, malloc
    add s0, x0, a0 # s0 points to the begining of matrix d allocated memory
    lw ra, 4(sp)
    lw a0, 8(sp)
    addi sp, sp, 8

    # TODO: import dot.s, do the multiplication process

outer_loop_start:

inner_loop_start:

inner_loop_end:

outer_loop_end:


    # Epilogue
    
    
    ret

malloc:
    add a1, a0, x0
    addi a0, x0, 9
    ecall
    jr ra