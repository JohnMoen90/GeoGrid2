# GeoGrid2
GeoGrid2 displays world tiles with an OpenGL renderer.

Currently this program does NOT have a way to select new long lat coordinates via a
gui or even from console commands, they must be changed manually in the Mesh class.

After starting the program random coords will be selected for you, restart the program to get a new random map.

If entering manual coordinates, keep in mind that only coords that would fall in the continental US will work.

Currently Color values only work at certain altitudes do to the strange scaling of the
.hgt files I have found the area in the rocky mountains around N45W115 to be the
best for the current scale. This is where the random coords are chosen from roughly.

The rounding scheme is selected randomly as well, rounding to the tenth and hundredth half the time each.
tenth is more staggered and defined, hundredth is more accurate.

Arrow keys - move map
z,x - change map scale (similar to zoom)
q,a - zoom in/out
esc - close window

