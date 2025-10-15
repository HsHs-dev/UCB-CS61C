.globl factorial

.data
n: .word 8

.text
main:
    la t0, n
    lw a0, 0(t0)
    jal ra, factorial

    addi a1, a0, 0
    addi a0, x0, 1
    ecall # Print Result

    addi a1, x0, '\n'
    addi a0, x0, 11
    ecall # Print newline

    addi a0, x0, 10
    ecall # Exit

factorial:
    # begin of epilouge
    addi sp, sp, -8
    sw ra, 0(sp)
    sw s0, 4(sp)
    # end of epilouge
    addi t0, x0, 2
    blt a0, t0, exit # if a0 is less than 2, jump to exit
    add s0, x0, a0 # save this call argument
    addi a0, a0, -1 # decrement a0 for the next call
    jal factorial
    # begin of prolouge
    lw ra, 0(sp)
    lw s0, 4(sp)
    addi sp, sp, 8 # restore the stack
    # end of prolouge
    mul a0, s0, a0 # multiply the reutrned value by this call argument and put in a0 for the previous call
    jr ra

exit:
    lw ra, 0(sp)
    lw s0, 4(sp)
    addi sp, sp, 8 # restore the stack
    jr ra

# this factorial function is roughly equivalent to this C code:
# int factorial(int n) {
#     if (n == 0 || n == 1) {
#         return 1;
#     } 
#     return n * factorial(n - 1);
# }