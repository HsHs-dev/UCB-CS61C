# RISC-V assembly for a simple Artificial Neural Network (ANN)

**Project Specification: <https://www.learncs.site/docs/curriculum-resource/cs61c/projects/proj2>**

This repo houses the material for UCB CS61C course Project 2: Building a classify function for a pre-trained *MNIST data set* weights. The project is split into two parts:

* Part A: Mathematical Functions: These include a **dot product**, **matrix multiplication**, an **element-wise rectifier function (ReLU)**, and an **argmax** function for vectors.

* Part B: File Operations and Main: This includes functions to read matrices from and write matrices to binary files. Then you will combine all individual functions to run a pre-trained MNIST digit classifier.

## Project Layout

![Repository Layout - this nice layout is made by the command line tool lsd](image.png)

## Try it out

Just for fun, you can also draw your own handwritten digits and pass them to the neural net. First, open up any basic drawing program like Microsoft Paint. Next, resize the image to 28x28 pixels, draw your digit, and save it as a .bmp file in the directory inputs/mnist/student_inputs/.

Then use the provided `bmp_to_bin.py` python script to convert your bmp to bin file. Make sure that the outputted ASCII art is kinda similar to your drawing.

after saving your .bmp file to the specified directory, do

```bash
python3 bmp_to_bin.py YOUR_BMP
```

This will create the binary file which will be passed to the neural net.

> [!WARNING]
> Make sure that your bmp image is 28 x 28 and that the *bit depth* is 24 (google: microsoft paint bmp bit depth 24), otherwise the script will write garbage.

## Usage

clone the repo

```bash
git clone https://github.com/HsHs-dev/RISCV-ANN.git
```

cd into it

```bash
cd RISCV-ANN
```

then run the venus jar on your input as follows:

```bash
java -jar venus.jar src/main.s -ms -1 -it inputs/mnist/bin/m0.bin inputs/mnist/bin/m1.bin inputs/mnist/student_inputs/<YOUR_INPUT>.bin outputs/test_mnist_main/student_input_mnist_output.bin 2>/dev/null
```

The classified number will appear


## Self Opinion

This project was a great add to my knowledge regrading assembly (especially RISC-V) and to my view of computer architecture. When you write alot of assembly you really understand how the memory is laid down, how pointers and pointer arithmetic works, and how all the sophisticated functions that *C* give us for granted works. If there is one thing that I will take home from this project, is that I will never pass the wrong number of bytes to `malloc` when writing C again.
