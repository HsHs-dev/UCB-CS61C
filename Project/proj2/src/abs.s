.globl abs

.text
# =================================================================
# FUNCTION: Given an int return its absolute value.
# Arguments:
# 	a0 (int) is input integer
# Returns:
#	a0 (int) the absolute value of the input
# =================================================================
abs:
    bge a0, x0, done # if a0 is greater than or equal to 0, jump to done
    sub a0, x0, a0   # else, a0 is negative, a0 = 0 - a0

done:
    ret
