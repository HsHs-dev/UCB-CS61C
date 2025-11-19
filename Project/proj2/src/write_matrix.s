.globl write_matrix

.text
# ==============================================================================
# FUNCTION: Writes a matrix of integers into a binary file
# FILE FORMAT:
#   The first 8 bytes of the file will be two 4 byte ints representing the
#   numbers of rows and columns respectively. Every 4 bytes thereafter is an
#   element of the matrix in row-major order.
# Arguments:
#   a0 (char*) is the pointer to string representing the filename
#   a1 (int*)  is the pointer to the start of the matrix in memory
#   a2 (int)   is the number of rows in the matrix
#   a3 (int)   is the number of columns in the matrix
# Returns:
#   None
# Exceptions:
# - If you receive an fopen error or eof,
#   this function terminates the program with error code 93.
# - If you receive an fwrite error or eof,
#   this function terminates the program with error code 94.
# - If you receive an fclose error or eof,
#   this function terminates the program with error code 95.
# ==============================================================================
write_matrix:

    # save the arguments
    addi sp, sp, -20
    sw s0, 0(sp)
    sw s1, 4(sp)
    sw s2, 8(sp)
    sw s3, 12(sp)
    sw ra, 16(sp)

    mv s1, a1 # matrix in memory
    mv s2, a2 # rows
    mv s3, a3 # cols

    # open the file to be read
    mv a1, a0
    li a2, 1
    jal fopen

    # check if fopen call failed
    li t0, -1
    beq a0, t0, fopen_exit

    # save the file descriptor to s0
    mv s0, a0

    # write the headers to the file
    addi sp, sp, -8 # use the stack as a buffer to the two headers
    sw s2, 0(sp)
    sw s3, 4(sp)

    mv a1, s0
    mv a2, sp
    li a3, 2
    li a4, 4
    jal fwrite

    # Check if fwrite call failed
    li t0, 2
    bne a0, t0, fwrite_exit
    
    addi sp, sp, 8

    # compute the number of elements to be written
    mul t0, s2, s3 
    li t1, 4 # sizeof(int)

    mv a1, s0
    mv a2, s1
    mv a3, t0
    mv a4, t1
    jal fwrite

    # check if fwrite call failed
    mul t0, s2, s3
    bne a0, t0, fwrite_exit

    mv a1, s0
    jal fclose

    # check if fclose call failed
    li t0, -1
    beq a0, t0, fclose_exit

    lw s0, 0(sp)
    lw s1, 4(sp)
    lw s2, 8(sp)
    lw s3, 12(sp)
    lw ra, 16(sp)
    addi sp, sp, 20

    ret

fopen_exit:
    # exit with error code 93
    li a1, 93
    jal exit2

fwrite_exit:
    # exit with error code 94
    li a1, 94
    jal exit2

fclose_exit:
    # exit with error code 95
    li a1, 95
    jal exit2