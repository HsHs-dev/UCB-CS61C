# CS61CPU

Look ma, I made a CPU! Here's what I did:

- Started with Task 1 of implementing 12 ALU instructions, varying between Arithmetic ones and logical ones
- Constructed the Register File which governs writing to and reading from each one of the RISC-V 32 registers
- The `addi` instruction:
    * constructed the I-type immediate generator subcircuit
    * constructed the S-type immediate generator subcircuit
    * constructed the B-type immediate generator subcircuit
    * constructed the J-type immediate generator subcircuit
    * constructed the U-type immediate generator subcircuit
    * Added 2-stage pipeline to the `addi` instruction

- Part B:
    * edited the immediate generator to produce deffirent immediates depending on the immSel
