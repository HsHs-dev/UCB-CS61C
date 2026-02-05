if you get the latter error:

```bash
make server_basic                                                                                                 
gcc -std=gnu99 -c -fopenmp server.c -o server_basic.o
gcc -std=gnu99 -c -fopenmp libhttp/libhttp.c -o libhttp.o
gcc -std=gnu99 -c -fopenmp omp_apps.c -o omp_apps.o
gcc -std=gnu99 -c -fopenmp server_utils.c -o server_utils_serial.o
server_utils.c: In function ‘http_serve_directory’:
server_utils.c:92:38: warning: ‘%s’ directive writing up to 255 bytes into a region of size 242 [-Wformat-overflow=]
   92 |       sprintf(buf, "<li><a href=\".%s%s\">%s</a></li>\n", path, fname, fname);
      |                                      ^~
server_utils.c:92:7: note: ‘sprintf’ output 27 or more bytes (assuming 537) into a destination of size 256
   92 |       sprintf(buf, "<li><a href=\".%s%s\">%s</a></li>\n", path, fname, fname);
      |       ^~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
gcc -std=gnu99 -c -fopenmp libbmp/libbmp.c -o libbmp.o
gcc -fopenmp server_basic.o libhttp.o omp_apps.o server_utils_serial.o libbmp.o -o server_basic
/usr/bin/ld: server_utils_serial.o:(.bss+0x0): multiple definition of `server_fd'; server_basic.o:(.bss+0x0): first defined here
/usr/bin/ld: server_utils_serial.o:(.bss+0x4): multiple definition of `server_port'; server_basic.o:(.bss+0x4): first defined here
/usr/bin/ld: server_utils_serial.o:(.bss+0x8): multiple definition of `server_files_directory'; server_basic.o:(.bss+0x8): first defined here
collect2: error: ld returned 1 exit status
make: *** [Makefile:18: server_basic] Error 1
```

* go to the server_utils.h file
* add the `extern` keyword before *server_fd*, *server_port*, and *server_files_directory*. The `extern` keyword informs the compiler that the variables are defined elsewhere in the program (most likely in a different source file), so there’s no need to allocate space for them, which caused the linker problem earlier.
* choose either `server.c` or `server_utils.c` and declare the variables:

```c
int server_fd;
int server_port;
char *server_files_directory;
```

* build again:

```bash
make server_basic
```
