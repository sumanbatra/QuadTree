/**
 * @author ramkeerthyathinarayanan
 *
 */

public class MathematicalOperations {
	public static int log(int x, int b) {
		return (int) (Math.log(x) / Math.log(b));
	}
	
	public static double logDouble(int x, int b) {
		return (Math.log(x) / Math.log(b));
	}
	
	public static boolean isInteger(double x) {
		if (x == (int)x)
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean checkLogIsInteger(int x, int b) {
		double logValue = (Math.log(x) / Math.log(b));
		return isInteger(logValue);
	}
}
