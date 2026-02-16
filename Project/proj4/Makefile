CC = gcc
CFLAGS = -g -Wall -std=c99 -fopenmp -mavx -mfma -pthread
LDFLAGS = -fopenmp
CUNIT = -L/home/ff/cs61c/cunit/install/lib -I/home/ff/cs61c/cunit/install/include -lcunit
PYTHON_INC = $(shell python3-config --includes)
PYTHON_LDFLAGS = $(shell python3-config --ldflags)

install:
	if [ ! -f files.txt ]; then touch files.txt; fi
	rm -rf build
	xargs rm -rf < files.txt
	python3 setup.py install --record files.txt

install_dumb:
	if [ ! -f files2.txt ]; then touch files2.txt; fi
	rm -rf build
	xargs rm -rf < files2.txt
	python3 setup_dumbpy.py install --record files2.txt

uninstall:
	if [ ! -f files.txt ]; then touch files.txt; fi
	rm -rf build
	xargs rm -rf < files.txt

clean:
	rm -f *.o
	rm -f test
	rm -rf build
	rm -rf __pycache__

test:
	rm -f test
	$(CC) $(CFLAGS) mat_test.c matrix.c -o test \
	$(LDFLAGS) $(CUNIT) $(PYTHON_INC) $(PYTHON_LDFLAGS)
	./test

.PHONY: test