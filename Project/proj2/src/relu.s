.globl relu

.text
# ==============================================================================
# FUNCTION: Performs an inplace element-wise ReLU on an array of ints
# Arguments:
# 	a0 (int*) is the pointer to the array
#	a1 (int)  is the # of elements in the array
# Returns:
#	None
# Exceptions:
# - If the length of the vector is less than 1,
#   this function terminates the program with error code 78.
# ==============================================================================
relu:

    # if the length is less than 1, jump to error
    li t0, 1
    blt a1, t0, error 

    li t0, 0 
loop_start:
    beq t0, a1, loop_end
    slli t1, t0, 2
    add t2, a0, t1
    lw t3, 0(t2)
    bgt t3, x0, loop_continue # if t0 <= t1 then target
    sw x0, 0(t2)
loop_continue:
    addi t0, t0, 1
    j loop_start

loop_end:
	ret

error:
    # exit the program with error code 78
    li a1, 78
    jal exit2