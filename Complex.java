/*
 * Complex number class
 */
public class Complex {
	/*
	 * real part and imaginary part
	 */
	private double r,i;
	public Complex(double r, double i){
		this.r = r;
		this.i = i;
	}
	/*
	 * multiplies two complex numbers and returns a new complex number of the product
	 */
	public Complex times(Complex other){
		return new Complex(r*other.getR()-i*other.getI(),r*other.getI()+i*other.getR());
	}
	/*
	 * adds two complex numbers and returns a new complex number of the sum
	 */
	public Complex plus(Complex other){
		return new Complex(r+other.getR(),i+other.getI());
	}
	public double getR(){
		return r;
	}
	public double getI(){
		return i;
	}
	/*
	 * returns the square of the magnitude of this complex number 
	 */
	public double magnitudeSquared(){
		return r*r+i*i;
	}
}
