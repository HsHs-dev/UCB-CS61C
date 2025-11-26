# CALL, RISC-V Procedures

## Pre-Check

<!-- 1.1: false, the compiler replace pseudo instructions with actual ones -->
1.1: true, the pseudo instructions are replaced by the assembler, not the compiler

1.2: false, the main job of the assembler is to translate assembly code into machine code
<!-- That’s the job of the compiler. The assembler is primarily responsible for
replacing pseudoinstructions and resolving offsets. -->

1.3: false, the linker will edit some of the code, like labels, replacing them with actual offsets
<!--  The linker needs to relocate all absolute address references. -->

<!-- 1.4: true, the linker will determine the actual offsets to jump to -->
1.4: false, the jumps which relies on a register (e.a jalr) are determined at runtime

## CALL

2.1: the Stored Program concept is the idea that the instructions and data are stored together in memory, this allows us to make more dynamic programs

2.2: 2 passes, first to save the positions of labels, second to use labels to generate code

2.3:

* Header: describe the size and position of the other pieces of the obj. file
* Text: the actual machine code
* Data: binary representaion of the static data in the source file
* Relocation Table:  Identifies lines of code that need to be “handled” by the Linker (jumps to external labels (e.g. lib files), references to static data)
* Symbol Table: list of this file's labels and static data that can be referenced across files
* Debugging Info: additional info for debuggers (standard ELF format)

2.4:

* Relative addressing: Assembling
* Absolute addressing: Linking

## Assembling RISC-V

3.1:

* 5 sum: la t0, array
* 6 li t1, 4
* 7 mv t2, x0
* 14 j loop
* 15 end: mv a0, t2

3.2:

* first pass: loop
* second pass: end

3.3:

* sum
* end
* loop

3.4: array, print_int

<!-- 4.1: from -4096 to +4094 -->

4.2: -2^18 to 2^18 - 1