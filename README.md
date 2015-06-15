# Lights Out Solver (Extreme Edition)
A Java program that shows the optimal solution to the electronic puzzle game, Lights Out.  This program was motivated by numerous encounters of these annoying puzzles in different video games.  The program determines the optimal solution by solving a system of linear equations over finite fields using Gaussian elimination.  For more information on this topic, check out [this paper](http://web.archive.org/web/20100524223840/http://www.math.ksu.edu/~dmaldona/math551/lights_out.pdf) (some knowledge of linear algebra and [field](http://en.wikipedia.org/wiki/Field_(mathematics)) is required).  The program can also be repurposed to play Lights Out.

Below is the overview of the GUI:
![img1](/readme/img1.png)

Because the algorithm can easily be extended to handle harder variations of Lights Out, this program also comes with the options for larger board size and more light states (in case you REALLY like this game for some reason...)
![img2](/readme/img2.png)

## To run
Go into the project src directory and execute ```javac Main.java``` followed by ```java Main```.
