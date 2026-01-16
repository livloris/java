import java.awt.Color;
//import StdDraw;

public class Chaos {

   static final int MAXCOUNT = 100;
   static final double TOLERANCE = 0.001;
   static final double PENSIZE = 0.005;
   static final Complex[] ROOT = {new Complex(1), new Complex(-1), 
         new Complex(0, 1), new Complex(0, -1)};

   public static void main (final String[] args) {

      // Number of pixels from command line
      final int n = Integer.parseInt(args[0]);

      // Set canvas size and coordinate plane
      StdDraw.setCanvasSize(n, n);
      final double xmin = -1.0;
      final double xmax = 1.0;
      final double ymin = -1.0;
      final double ymax = 1.0;
      StdDraw.setXscale(xmin, xmax);
      StdDraw.setYscale(ymin, ymax);

      // Don't show canvas as it builds
      StdDraw.enableDoubleBuffering();     

      // Access every pixel on the canvas
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            // X coordinate
            final double x = xmin + (xmax - xmin) * i / n;
            // Y coordinate
            final double y = ymin + (ymax - ymin) * j / n;
            final Complex z = new Complex(x, y);
            // Calls function to find root and color of pixel
            StdDraw.setPenColor(findRoot(z));
            StdDraw.setPenRadius(PENSIZE);
            StdDraw.point(x, y); // Draw point
         }
      }
      // Show canvas all at once
      StdDraw.show();
   }

   // Apply Newton's Method
   static Color findRoot (final Complex z) {
      Complex zNext = z;
      int count = 0;
      // Check if complex number is close to one of the 4 roots
      while (true) {
         // 1+ 0i
         if (isClose(zNext, ROOT[0])) {
            return StdDraw.YELLOW;
         // -1 + 0i
         } else if (isClose(zNext, ROOT[1])) {
            return StdDraw.BLUE;
         // 0 +1i
         } else if (isClose(zNext, ROOT[2])) {
            return StdDraw.RED;
         // 0 -1i
         } else if (isClose(zNext, ROOT[3])) {
            return StdDraw.GREEN;
         // Once iterations reaches max count, paint black
         } else if (count > MAXCOUNT) {
            return StdDraw.BLACK;
         }
         final Complex f = f(zNext); // Function
         final Complex fprime = fprime(zNext); // Derivative of function
         // Newton's method
         zNext = Complex.minus(zNext, Complex.divides(f, fprime));
         count++; // +1 to iteration 
      }
   }

   // Check to see if number is within the tolerance (0.001) of a root
   static boolean isClose (final Complex num1, final Complex num2) {
      return Complex.abs(Complex.minus(num1, num2)) < TOLERANCE;
   }

   // Stores function in complex number form
   static Complex f (final Complex x) {
      return Complex.minus(Complex.times(Complex.times(x, x), Complex.times(x, x)), 
            new Complex(1));
      // return x * x * x * x - 1;
   }

   // Stores derivative of function in complex number form
   static Complex fprime (final Complex x) {
      return Complex.times(Complex.times(new Complex(4), x), Complex.times(x, x));
      // return 4 * x * x * x;
   }
}
