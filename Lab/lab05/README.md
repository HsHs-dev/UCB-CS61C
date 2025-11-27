# Testing

In order to test your files, run:

```bash
./test.sh
```

If you get `/usr/bin/env: ‘python’: No such file or directory`, then change the first line of testing/test.py from

```bash
#!/usr/bin/env python
```

to 

```
#!/usr/bin/env python3
```

If it says you don’t have permission, run:

```bash
chmod +x test.sh
```
