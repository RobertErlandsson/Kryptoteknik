package project1;
import java.math.BigInteger;

//class to represent x^2 = y mod(N) and it's prime factoring;

public class A {
	private BigInteger x;
	private BigInteger y;
	private int[] factors;
	
	public A(BigInteger x, BigInteger y, int L){
		this.x = x;
		this.y = y;
		factors = new int[L];
	}
	
	public BigInteger getX(){
		return x;
	}
	
	public BigInteger getY(){
		return y;
	}
	
	public int[] get_factors(){
		return factors;
	}
}
