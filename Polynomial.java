package my_package;

public class Polynomial
{
	double[] nz_coef;
	int[] exp; 
	
	boolean expanded;
	
	public Polynomial()
	{
		nz_coef = new double[] {0};
		exp = new int[] {0};
		expanded = false;
	}
	
	public Polynomial(int size, boolean want_expanded)
	{
		nz_coef = new double[size];
		exp = new int[size];
		
		if (want_expanded)
		{
			for (int i = 0; i < size; i++)
			{
				
				exp[i] = i;
			}
		}
		//System.out.println("length: " + nz_coef.length);
		expanded = want_expanded;
	}
	
	public Polynomial(double[] new_coefficients, int[] new_exp)
	{
		nz_coef = new_coefficients;
		exp = new_exp;
		expanded = false;
	}
	
	public Polynomial(double[] new_coefficients, int[] new_exp, int length)
	{
		nz_coef = new double[length];
		exp = new int[length];
		
		for (int i = 0; i < length; i++)
		{
			nz_coef[i] = new_coefficients[i];
			exp[i] = new_exp[i];
		}
		//System.out.println("length: " + nz_coef.length);
		expanded = false;
	}
	
	public void print(String name)
	{
		int ln = this.size();
		System.out.println("name: " + name + "(");
		for (int i = 0; i < ln; i++)
		{
			System.out.println("coef: " + this.nz_coef[i] + " deg: " + this.exp[i]);
		}
		System.out.println(")");
	}
	private int max_degree(Polynomial poly, int size)
	{
		int max = 0;
		for (int i = 0; i < size; i++)
		{
			if (poly.exp[i] > max)
			{
				max = poly.exp[i];
			}
		}
		return max;
	}
	
	private void expand() 
	{
		int old_len = this.exp.length;
		// the size of the new polynomial arrays is going to be the max degree + 1
		int new_len = max_degree(this, old_len) + 1;
		
		
		System.out.println("old len: " + old_len);
		System.out.println("new len: " + new_len);
		
		if (this.expanded)
		{
			return;
		}
		
		//this.print("before expand");
		double coef[] = new double[new_len];
		int deg[] = new int[new_len];
		
		for (int i = 0; i < old_len; i++)
		{
			coef[this.exp[i]] = this.nz_coef[i];
		}
		for (int i = 0; i < new_len; i++)
		{
			deg[i] = i;
		}
		this.nz_coef = coef;
		this.exp = deg;
		//this.print("after expand");
	}
	
	public void shrink()
	{
		/*
		 * count the number of 0's
		 * make a new array with that length
		 */
		int ln = this.size();
		int deg[] = new int[ln];
		double coef[] = new double[ln];
		int count = 0;
	
		for (int i = 0; i < ln; i++)
		{
			if (this.nz_coef[i] != 0)
			{
				deg[count] = this.exp[i];
				coef[count] = this.nz_coef[i];
				count++;
			}
		}
		Polynomial poly = new Polynomial(coef, deg, count);
		this.nz_coef = poly.nz_coef;
		this.exp = poly.exp;
	}
	
	private Polynomial add_ghetto(Polynomial poly_b, Polynomial poly_s, boolean expanded)
	{
		// length of bigger array and smaller array
		int length_b = poly_b.size();
		int length_s = poly_s.size();
		
		int i = 0;
		double sum = 0;
		int count = 0;
		
		double [] coef = new double[length_b];
		int [] deg = new int[length_b];
		
		for (; i < length_s; i++)
		{
			sum = poly_b.nz_coef[i] + poly_s.nz_coef[i];
			if (expanded || sum != 0)
			{
				coef[count] = sum;
				deg[count] = i;
				count++;
			}
		}
		
		// from end of small array
		for (; i < poly_b.nz_coef.length; i++)
		{
			if (expanded || poly_b.nz_coef[i] != 0)
			{
				
				coef[count] = poly_b.nz_coef[i];
				deg[count] = i;
				count++;
			}
			//System.out.println("Sum: " + coef[i] + " degree: " + exp[i]);
			//System.out.println("add_ghetto: Sum: " + sum);
		}
		//this.print("add_ghetto 2: ");
		if (expanded)
		{
			Polynomial result = new Polynomial(coef, deg);
			result.print("result: ");
			return (result);
		}
		else
		{
			//System.out.println("Polynomial is not expanded");
			Polynomial result = new Polynomial(coef, deg, count);
			result.print("result: ");
			return (result);
		}
	}
	
	public Polynomial add(Polynomial other)
	{
		/* 1. turn both arrays into 1 array (expand)
		 * 2. produce a coefficient array for the polynomial
		 * 3. add all non-zero coefficients 
		 * 4. we end with an array that has trailing 0's
		 * 5. chop
		 * 6. done
		 * c1[1, 2, 3] e1[2, 0, 1]  c2[4, 5, 6, 7] e2[0, 1, 4, 2]
		 * 
		 * c1' = {};
		 * for (int i = 0; i < array.size; i++)
		 * {
		 * 	c1'[e1[i]] = c1[i];
		 * }
		 * c1' = [2, 3, 1]
		 * 
		 * c2' = {};
		 * for (int i = 0; i < 4; i++)
		 * {
		 * 	c2'[e2[i]] = c2[i];
		 * }
		 * c2' = [4, 5, 7, 0, 6]
		 * 
		 * adding the coefficients: [6, 8, 8, 0, 6]
		 * degrees: [0, 1, 2, 3, 4]
		 */
		
		this.print("before expand this: ");
		other.print("before expand other: ");
		
		this.expand();
		other.expand();
		
		this.print("this: ");
		other.print("other: ");
		
		int this_lth = this.size();
		int other_lth = other.size(); 
		
		if (this_lth >= other_lth)
		{
			return add_ghetto(this, other, this.expanded);
		}
		return add_ghetto(other, this, this.expanded);
	}
	

	public double evaluate(double x)
	{
		double sum = 0;
		
		for (int i = 0; i < this.nz_coef.length; i++)
		{
			sum += this.nz_coef[i] * Math.pow(x, this.exp[i]);
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
	
	private Polynomial multiply(double coef, int deg)
	{
		System.out.println("multiply coef: " + coef + "deg: " + deg);
		int len = this.size();
		Polynomial poly = new Polynomial(len, false);
		
		for (int i = 0; i < len; i++)
		{
			poly.nz_coef[i] = coef * this.nz_coef[i];
			poly.exp[i] = deg + this.exp[i];
		}
		poly.print("product: ");
		return poly;
	}
	
	public int degree()
	{
		return max_degree(this, this.size());
	}
	
	public int size()
	{
		return this.exp.length;
	}
	
	public Polynomial multiply(Polynomial poly)
	{
		// new size: max degree a1 + max deg a2 + 1
		int max1 = this.degree();
		int max2 = poly.degree();
		int new_size = max1 + max2 + 1;
		
		Polynomial result = new Polynomial(new_size, true);
		
		int other_ln = poly.size();
		
		for (int i = 0; i < other_ln; i++)
		{
			Polynomial product = multiply(poly.nz_coef[i], poly.exp[i]);
			//product.print("product: ");
			result = result.add(product);
			result.print("after adding: ");
		}
		
		result.shrink();
		return result;
	}
}