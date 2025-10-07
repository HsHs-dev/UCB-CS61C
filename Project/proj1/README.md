The provided script has a problem when the number of frames exceeds about 94 frames, it gives a segfault and doesn't produce the gif
use this command after using the prodided script to generate the gif:
```bash
convert -delay 20 -loop 0 \
  -scale 400% \
  studentOutputs/GliderGuns/frame_*.ppm \
  -colors 64 \
  -layers Optimize \
  GliderGuns.gif
```
