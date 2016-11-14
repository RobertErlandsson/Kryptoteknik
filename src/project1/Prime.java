package project1;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

public class Prime {
	static int[] primes;
	static A[] pairs;
	static short[][] M;
	
	public static void main(String[] args){
		ArrayList<BigInteger> numbers = readFile("DATA/input.txt"); //Reads from file. Numbers include those that should be tested plus our own number
		BigInteger N = numbers.get(1); //Gets one of the numbers 
		long start = System.currentTimeMillis();
		int L = 1000; //number of primes in the factorbase
		primes = new int[L]; // vector to store the first L primes
		pairs = new A[L+5];
		M = new char[L+5][L];
		
		//get the first L primes. implemented by someone
		get_primes();
		
		//find L+5 y^2 = x mod N and find their prime decomposition. implemented by Robert
		get_pairs();
		
		// find factors of N given all pairs implemented by linus
		find_factors(L);
		
		long end = System.currentTimeMillis();
		System.out.println("It took " + (end-start) + " ms");
	}

	public static void get_primes(){
		return;
	}
	
	public static void get_pairs(){
		return;
	}
	
	private static ArrayList<BigInteger> readFile(String s) {
		ArrayList<BigInteger> indata = new ArrayList<BigInteger>();
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(s));
			int limit = Integer.parseInt(br.readLine());
			for (int i = 0; i < limit; i++){
				indata.add(new BigInteger(br.readLine()));
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	/** Calculate the square root of a BigInteger in logarithmic time */
	public BigInteger squareRoot(BigInteger x) { 
	      BigInteger right = x, left = BigInteger.ZERO, mid; 
	      while(right.subtract(left).compareTo(BigInteger.ONE) > 0) { 
	            mid = (right.add(left)).shiftRight(1);
	            if(mid.multiply(mid).compareTo(x) > 0) 
	                  right = mid; 
	            else 
	                  left = mid; 
	      } 
	      return left; 
	}
	
	//constructs M, solves xM = 0 and finds decomposition.
	public static void find_factors(int L){
		//change so that you check if the row already exists in M!!
		int[] decomp;
		for(int i = 0; i < M.length; i++){
			decomp = pairs[i].get_factors();
			for(int j = 0; j < L; j++){
				if(decomp[j] > 0){
					M[i][j] = 1;
				}
			}
		}

		short[] sol = solve_system(L);
		return;
	}

	public static short[] solve_system(int L){
		short[] ans = new short[L];
		return ans;
	}
}

