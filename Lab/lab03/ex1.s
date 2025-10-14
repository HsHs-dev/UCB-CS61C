.data # data segement, holds all initialized global and static variables
.word 2, 4, 6, 8
n: .word 9 # .word directive is used to reserve 4 bytes of memory and putting the given number there

.text # the code segment of memroy, where the actual executed code lives
main:
    add t0, x0, x0
    addi t1, x0, 1
    la t3, n
    lw t3, 0(t3)
fib:
    beq t3, x0, finish
    add t2, t1, t0
    mv t0, t1
    mv t1, t2
    addi t3, t3, -1
    j fib
finish:
    addi a0, x0, 1
    addi a1, t0, 0
    ecall # print integer ecall
    addi a0, x0, 10
    ecall # terminate ecall

# Run the program to completion. What number did the program output? What does this number represent?
# The outputed number is the ninth number of the fibonacci sequence

# At what address is n stored in memory? Hint: Look at the contents of the registers.
# 0x10000010

# Without actually editing the code (i.e. without going into the “Editor” tab), have the program calculate 
# the 13th fib number (0-indexed) by manually modifying the value of a register. You may find it helpful to first
# step through the code. If you prefer to look at decimal values, change the “Display Settings” option at the bottom.
# after loading the n to t3 register (lw t3, 0(t3)) change the value of the register from 9 to 13