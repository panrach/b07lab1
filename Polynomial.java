public class Polynomial
{
	double[] coefficients;
	
	public Polynomial()
	{
		coefficients = new double[] {0};
	}
	
	public Polynomial(double[] new_coefficients)
	{
		coefficients = new_coefficients;
	} 
	
	private Polynomial add_ghetto(Polynomial poly_b, Polynomial poly_s)
	{
		double [] zeroes = new double[poly_b.coefficients.length];
		Polynomial ret_poly = new Polynomial(zeroes);
		
		int length_s = poly_s.coefficients.length;
		
		int i = 0;
		
		for (; i < length_s; i++)
		{
			ret_poly.coefficients[i] = poly_b.coefficients[i] + poly_s.coefficients[i];
		}
		
		for (; i < poly_b.coefficients.length; i++)
		{
			ret_poly.coefficients[i] = poly_b.coefficients[i];
		}
		
		return ret_poly;
	}
	
	public Polynomial add(Polynomial other_poly)
	{
		/* 1. get the longer polynomial
		 * 2. add the coefficients
		 * 3. the remaining coefficients become the coefficients of the new array
		 * 4. done
		 */
		
		int og_lth = coefficients.length;
		int other_lth = other_poly.coefficients.length; 
		
		if (og_lth >= other_lth)
		{
			return add_ghetto(this, other_poly);
		}
		return add_ghetto(other_poly, this);
	}
	
	public double evaluate(double x)
	{
		double sum = 0;
		for (int i = 0; i < this.coefficients.length; i++)
		{
			sum += this.coefficients[i] * Math.pow(x, i);
		}
		return sum;
	}
	
	public boolean hasRoot(double x)
	{
		if (this.evaluate(x) == 0)
		{
			return true;
		}
		return false;
	}
}
