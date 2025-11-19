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

	li t0, 5
	bne a0, t0, cmd_exit

	addi sp, sp, -48
	sw s0, 0(sp) # argv
	sw s1, 4(sp) # a2
	sw s2, 8(sp) # m0 matrix pointer
	sw s3, 12(sp) # m0 rows
	sw s4, 16(sp) # m0 cols
	sw s5, 20(sp) # m1 matrix pointer
	sw s6, 24(sp) # m1 rows
	sw s7, 28(sp) # m1 cols
	sw s8, 32(sp) # input matrix pointer
	sw s9, 36(sp) # input rows
	sw s10, 40(sp) # input cols
	sw ra, 44(sp) # ra

	mv s0, a1
	mv s1, a2

		# =====================================
    # LOAD MATRICES
    # =====================================

    # Load pretrained m0
		lw a0, 4(s0) # m0 path
		addi sp, sp, -8
		mv a1, sp
		addi a2, sp, 4
		jal read_matrix
		lw s3, 0(sp)
		lw s4, 4(sp)
		addi sp, sp, 8
		mv s2, a0

    # Load pretrained m1
		lw a0, 8(s0)
		addi sp, sp, -8
		mv a1, sp
		addi a2, sp, 4
		jal read_matrix
		lw s6, 0(sp)
		lw s7, 4(sp)
		addi sp, sp, 8
		mv s5, a0

    # Load input matrix
		lw a0, 12(s0)
		addi sp, sp, -8
		mv a1, sp
		addi a2, sp, 4
		jal read_matrix
		lw s9, 0(sp)
		lw s10, 4(sp)
		addi sp, sp, 8
		mv s8, a0

    # =====================================
    # RUN LAYERS
    # =====================================
    # 1. LINEAR LAYER:    m0 * input
    # 2. NONLINEAR LAYER: ReLU(m0 * input)
    # 3. LINEAR LAYER:    m1 * ReLU(m0 * input)

		# allocate memory for the hidden_layer matrix = matmul(m0, input)
		mv t0, s3 # m0 rows
		mv t1, s10 # input cols
		mul a0, t0, t1
		slli a0, a0, 2
		jal malloc
		beq a0, x0, malloc_exit

		addi sp, sp, -4
		sw a0, 0(sp) # save the hidden_layer matrix pointer

		# prepare the call for matmul
		mv a6, a0
		mv a0, s2
		mv a1, s3
		mv a2, s4
		mv a3, s8
		mv a4, s9
		mv a5, s10
		jal matmul

		lw a0, 0(sp)
		addi sp, sp, 4

		# call ReLU on hidden_layer
		addi sp, sp, -4
		sw a0, 0(sp)
		mv t0, s3 # m0 rows
		mv t1, s10 # input cols
		mul a1, t0, t1
		jal relu

		lw t0, 0(sp) # restore the result of relu (hidden_layer pointer) in t0
		addi sp, sp, 4

		# prepare the call for matmul

		addi sp, sp, -4
		sw t0, 0(sp)

		# allocate memory for the scores matrix
		mv t0, s6 # m1 rows
		mv t1, s10 # input cols
		mul a0, t0, t1
		slli a0, a0, 2
		jal malloc
		beq a0, x0, malloc_exit

		lw t0, 0(sp)
		addi sp, sp, 4

		addi sp, sp, -8
		sw t0, 0(sp) # the hidden layer matrix pointer
		sw a0, 4(sp) # the scores matrix pointer

		mv a6, a0
		mv a0, s5
		mv a1, s6
		mv a2, s7
		mv a3, t0
		mv a4, s3 # m0 rows
		mv a5, s10 # input cols
		jal matmul

		lw a0, 0(sp)
		lw t0, 4(sp)
		addi sp, sp, 8

		addi sp, sp, -4
		sw t0, 0(sp)

		# free the hidden layer (a0)
		jal free

		lw t0, 0(sp)
		addi sp, sp, 4

    # =====================================
    # WRITE OUTPUT
    # =====================================
    # Write output matrix

		addi sp, sp, -4
		sw t0, 0(sp)

		lw a0, 16(s0)
		mv a1, t0
		mv a2, s6
		mv a3, s10
		jal write_matrix

		lw t0, 0(sp)
		addi sp, sp, 4

    # =====================================
    # CALCULATE CLASSIFICATION/LABEL
    # =====================================
    # Call argmax
		addi sp, sp, -4
		sw t0, 0(sp)
		mv a0, t0
		mv t0, s6
		mv t1, s10
		mul a1, t0, t1
		jal argmax

		lw t0, 0(sp)
		addi sp, sp, 4

    # Print classification
		beq s1, x0, print
		j end
    
print:
		addi sp, sp, -4
		sw t0, 0(sp) # the scores matrix pointer

		mv a1, a0
		jal print_int
    # Print newline afterwards for clarity
		li a1, '\n'
		jal print_char

		lw t0, 0(sp)
		addi sp, sp, 4
end:
		# FREE ALL MATRICES AND THEIR DIMENSIONS (the dimensions are allocated 
		# because of how I implemented read_matrix), don't implement it like me, 
		# use stack over malloc

		mv a0, t0
		jal free # free scores matrix

	  mv a0, s2
    jal free

    mv a0, s5
    jal free

    mv a0, s6
    jal free

    mv a0, s7
    jal free

    mv a0, s8
    jal free

    lw s0, 0(sp)
    lw s1, 4(sp)
    lw s2, 8(sp)
    lw s3, 12(sp)
    lw s4, 16(sp)
    lw s5, 20(sp)
    lw s6, 24(sp)
    lw s7, 28(sp)
    lw s8, 32(sp)
    lw s9, 36(sp)
    lw s10, 40(sp)
    lw ra, 44(sp)
    addi sp, sp, 48

    ret

cmd_exit:
	# exit with error code 89
	li a1, 89
	jal exit2

malloc_exit:
	# exit with error code 88
	li a1, 88