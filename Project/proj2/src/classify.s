.globl classify

.text
classify:
    # =====================================
    # COMMAND LINE ARGUMENTS
    # =====================================
    # Args:
    #   a0 (int)    argc
    #   a1 (char**) argv
    #   a2 (int)    print_classification, if this is zero, 
    #               you should print the classification. Otherwise,
    #               this function should not print ANYTHING.
    # Returns:
    #   a0 (int)    Classification
    # Exceptions:
    # - If there are an incorrect number of command line args,
    #   this function terminates the program with exit code 89.
    # - If malloc fails, this function terminats the program with exit code 88.
    #
    # Usage:
    #   main.s <M0_PATH> <M1_PATH> <INPUT_PATH> <OUTPUT_PATH>

    # check the command line args
    li t0, 5
    bne a0, t0, cmd_exit

    # save the arguments
    addi sp, sp, -32
    sw s0, 0(sp)
    sw s1, 4(sp)
    sw s2, 8(sp)
    sw s3, 12(sp)
    sw s4, 16(sp) # hidden layer pointer
    sw s5, 20(sp) # number of elements in hidden layer
    sw s6, 24(sp)
    sw ra, 28(sp)

    lw s0, 4(a1) # M0_PATH
    lw s1, 8(a1) # M1_PATH
    lw s2, 12(a1) # INPUT_PATH
    lw s3, 16(a1) # OUTPUT_PATH
    mv s6, a2

	# =====================================
    # LOAD MATRICES
    # =====================================

    # Load pretrained m0
    li a0, 8
    jal malloc # FREE
    beq a0, x0, malloc_exit
    addi sp, sp, -8
    sw a0, 0(sp)
    add a1, x0, a0
    addi a2, a0, 4
    mv a0, s0
    jal read_matrix
    sw a0, 4(sp)

    # Load pretrained m1
    li a0, 8
    jal malloc # FREE
    beq a0, x0, malloc_exit
    addi sp, sp, -8
    sw a0, 0(sp)
    add a1, x0, a0
    addi a2, a0, 4
    mv a0, s1
    jal read_matrix
    sw a0, 4(sp)

    # Load input matrix
    li a0, 8
    jal malloc # FREE
    beq a0, x0, malloc_exit
    addi sp, sp, -8
    sw a0, 0(sp)
    add a1, x0, a0
    addi a2, a0, 4
    mv a0, s2
    jal read_matrix
    sw a0, 4(sp)

    # sp layout now
    # 0-4: input matrix
    # 4-8: input dimensions
    # 8-12: m1 matrix
    # 12-16: m1 dimensions
    # 16-20: m0 matrix
    # 20-24: m0 dimensions

    # =====================================
    # RUN LAYERS
    # =====================================
    # 1. LINEAR LAYER:    m0 * input
    # 2. NONLINEAR LAYER: ReLU(m0 * input)
    # 3. LINEAR LAYER:    m1 * ReLU(m0 * input)

    # 1. Hidden layer, matmul(m0, input)

    # allocate memory for the resulting matrix
    lw t0, 20(sp)
    lw t1, 0(t0)
    lw a1, 0(t1) # m0 rows
    lw t0, 4(sp)
    lw t1, 0(t0)
    lw a5, 4(t1) # input cols

    mul a0, a1, a5
    mv s5, a0 # number of elements
    slli a0, a0, 2
    jal malloc # FREE
    beq a0, x0, malloc_exit

    # prepare the call for matmul
    mv s4, a0
    mv a6, a0 # resulting matrix pointer
    lw a0, 16(sp) # m0 matrix
    lw t0, 20(sp)
    lw t1, 0(t0)
    lw a1, 0(t1) # m0 rows
    lw a2, 4(t1) # m0 cols

    lw a3, 0(sp) # input matrix
    lw t0, 4(sp)
    lw t1, 0(t0)
    lw a4, 0(t1) # input rows
    lw a5, 4(t1) # input cols
    jal matmul

    mv a0, s4
    mv a1, s5
    jal relu

    # second matmul(m1, hidden_layer)
    lw t0, 12(sp)
    lw t1, 0(t0)
    lw a1, 0(t1) # m1 rows
    lw t0, 4(sp)
    lw t1, 0(t0)
    lw a5, 4(t1) # input cols

    mul a0, a1, a5
    slli a0, a0, 2
    jal malloc
    beq a0, x0, malloc_exit

    mv s5, a0
    mv a6, s5
    lw a0, 8(sp)
    lw t0, 12(sp)
    lw t1, 0(t0)
    lw a1, 0(t1) # m1 rows
    lw a2, 4(t1) # m1 cols

    mv a3, s4
    lw t0, 20(sp)
    lw t1, 0(t0)
    lw a4, 0(t1) # m0 rows
    lw t0, 4(sp)
    lw t1, 0(t0)
    lw a5, 4(t1) # input cols

    jal matmul

    # =====================================
    # WRITE OUTPUT
    # =====================================
    # Write output matrix

    mv a0, s3
    mv a1, s5
    lw t0, 12(sp)
    lw t1, 0(t0)
    lw a2, 0(t1) # m1 rows
    lw t0, 4(sp)
    lw t1, 0(t0)
    lw a3, 4(t1) # input cols
    jal write_matrix

    # =====================================
    # CALCULATE CLASSIFICATION/LABEL
    # =====================================
    # Call argmax
    mv a0, s5
    lw t0, 12(sp)
    lw t1, 0(t0)
    lw a1, 0(t1) # m1 rows
    lw t0, 4(sp)
    lw t1, 0(t0)
    lw a5, 4(t1) # input cols
    mul a1, a1, a5
    jal argmax

    # Print classification
    beq s6, x0, print
    j end
print:
    mv a1, a0
    jal print_int
    # Print newline afterwards for clarity
    li a1, '\n'
    jal print_char

end:

    # free all allocated memory

    mv a0, s5
    jal free

    mv a0, s4
    jal free

    # free input matrix
    lw a0, 0(sp)
    jal free

    # free input dims inner blocks
    lw t0, 4(sp)        # dims pointer
    lw t1, 0(t0)        # rows pointer
    mv a0, t1
    jal free
    lw t1, 4(t0)        # cols pointer
    mv a0, t1
    jal free

    # free input dims block itself
    mv a0, t0
    jal free


    # -------- Free m1 matrix + dims --------
    # free m1 matrix
    lw a0, 8(sp)
    jal free

    # free m1 dims inner blocks
    lw t0, 12(sp)
    lw t1, 0(t0)
    mv a0, t1
    jal free
    lw t1, 4(t0)
    mv a0, t1
    jal free

    # free dims block
    mv a0, t0
    jal free


    # -------- Free m0 matrix + dims --------
    # free m0 matrix
    lw a0, 16(sp)
    jal free

    # free m0 dims inner blocks
    lw t0, 20(sp)
    lw t1, 0(t0)
    mv a0, t1
    jal free
    lw t1, 4(t0)
    mv a0, t1
    jal free

    # free dims block
    mv a0, t0
    jal free

    # restore the stack
    addi sp, sp, 24

    # prolougue
    lw s0, 0(sp)
    lw s1, 4(sp)
    lw s2, 8(sp)
    lw s3, 12(sp)
    lw s4, 16(sp)
    lw s5, 20(sp)
    lw s6, 24(sp)
    lw ra, 28(sp)
    addi sp, sp, 32

    ret

cmd_exit:
    # exit with error code 89
    li a1, 89
    jal exit2

malloc_exit:
    # exit with error code 88
    li a1, 88
    jal exit2