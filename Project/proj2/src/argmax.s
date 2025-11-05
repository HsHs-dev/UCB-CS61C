.globl argmax

.text
# =================================================================
# FUNCTION: Given a int vector, return the index of the largest
#	element. If there are multiple, return the one
#	with the smallest index.
# Arguments:
# 	a0 (int*) is the pointer to the start of the vector
#	a1 (int)  is the # of elements in the vector
# Returns:
#	a0 (int)  is the first index of the largest element
# Exceptions:
# - If the length of the vector is less than 1,
#   this function terminates the program with error code 77.
# =================================================================
argmax:

    # if the length is less than 1, jump to error
    li t0, 1
    blt a1, t0, error 

    lw t4, 0(a0) # t4 has the first array element as the inital max
    add t5, x0, t0 # t5 holding the index of max
loop_start:
    beq t0, a1, loop_end
    slli t1, t0, 2
    add t2, a0, t1
    lw t3, 0(t2)
    ble t3, t4, loop_continue
    add t4, x0, t3
    add t5, x0, t0
loop_continue:
    addi t0, t0, 1
    j loop_start
loop_end:
    add a0, x0, t5
    ret

error:
    # exit the program with error code 77
    li a1, 77
    jal exit2