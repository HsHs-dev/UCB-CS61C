.globl read_matrix

.text
# ==============================================================================
# FUNCTION: Allocates memory and reads in a binary file as a matrix of integers
#
# FILE FORMAT:
#   The first 8 bytes are two 4 byte ints representing the # of rows and columns
#   in the matrix. Every 4 bytes afterwards is an element of the matrix in
#   row-major order.
# Arguments:
#   a0 (char*) is the pointer to string representing the filename
#   a1 (int*)  is a pointer to an integer, we will set it to the number of rows
#   a2 (int*)  is a pointer to an integer, we will set it to the number of columns
# Returns:
#   a0 (int*)  is the pointer to the matrix in memory
# Exceptions:
# - If malloc returns an error,
#   this function terminates the program with error code 88.
# - If you receive an fopen error or eof, 
#   this function terminates the program with error code 90.
# - If you receive an fread error or eof,
#   this function terminates the program with error code 91.
# - If you receive an fclose error or eof,
#   this function terminates the program with error code 92.
# ==============================================================================
read_matrix:

    # save the arguments to save registers
    addi sp, sp, -20
    sw s0, 0(sp)
    sw s1, 4(sp)
    sw s2, 8(sp)
    sw s3, 12(sp)
    sw ra, 16(sp)

    # s0 = rows pointer, s1 = cols pointer
    mv s0, a1
    mv s1, a2

    # open the file, int fopen(char *a1, int a2)
    mv a1, a0 
    li a2, 0
    jal fopen

    # save the file descriptor to s2
    mv s2, a0

    # check if the fopen call failed
    li t0, -1
    beq s2, t0, fopen_exit

    # read the dimensions of the matrix in order to allocate memory for it

    # allocate memory for the buffer in which the read dimensions will be stored
    # 8 bytes, 4 rows, 4 cols
    li a0, 8
    jal malloc

    # check the malloc call failed
    li t0, 0
    beq a0, t0, malloc_exit

    # save the buffer register
    addi sp, sp, -4
    sw a0, 0(sp)

    mv a2, a0 # save the buffer pointer to a2 preparing it to call fread
    mv a1, s2
    li t0, 8
    mv a3, t0
    jal fread

    # check if fread call failed
    li t0, 8
    bne a0, t0, fread_exit

    # restore the buffer register
    lw a0, 0(sp)
    addi sp, sp, 4

    lw t0, 0(a0) # t0 = rows
    lw t1, 4(a0) # t1 = cols

    # set the a0 and a1 pointers which will not be returned by this function
    sw t0, 0(s0)
    sw t1, 0(s1)

    # compute the number of matrix elements
    # sizof(int) * rows * cols
    li t2, 4 # sizeof(int)
    mul t2, t2, t1
    mul t2, t2, t0 # t2 = sizeof(int) * rows * cols

    addi sp, sp, -4
    sw t2, 0(sp)

    # free the buffer
    jal free

    lw t2, 0(sp)
    addi sp, sp, 4

    # save the t2 register
    addi sp, sp, -4
    sw t2, 0(sp)

    # allocate the memory for the matrix elements
    mv a0, t2
    jal malloc

    # restore the t2 register
    lw t2, 0(sp)
    addi sp, sp, 4

    # check the malloc call failed
    li t0, 0
    beq a0, t0, malloc_exit

    # save the buffer register and the size of the matrix
    addi sp, sp, -8
    sw a0, 0(sp)
    sw t2, 4(sp)

    mv a2, a0 # save the buffer pointer to a2 preparing it to call fread
    mv a1, s2
    mv a3, t2
    jal fread

    lw t2, 4(sp)

    # check if fread call failed
    bne a0, t2, fread_exit

    # restore
    lw a0, 0(sp)
    addi sp, sp, 8

    # save a0 to s3
    mv s3, a0

    # close the file
    mv a1, s2
    jal fclose

    # check if fclose failed
    li t0, -1
    beq a0, t0, fclose_exit

    # put the pointer to the matrix in a0
    mv a0, s3

    lw s0, 0(sp)
    lw s1, 4(sp)
    lw s2, 8(sp)
    lw s3, 12(sp)
    lw ra, 16(sp)
    addi sp, sp, 20

    ret

fopen_exit:
    # exit with error code 90
    li a1, 90
    jal exit2

malloc_exit:
    # exit with error code 88
    li a1, 88
    jal exit2

fread_exit:
    # exit with error code 91
    li a1, 91
    jal exit2

fclose_exit:
    # exit with error code 92
    li a1, 92
    jal exit2