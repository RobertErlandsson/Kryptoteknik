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

	public static void main(String[] args) {
		ArrayList<BigInteger> numbers = readFile("DATA/input.txt"); // Reads
																	// from
																	// file.
																	// Numbers
																	// include
																	// those
																	// that
																	// should be
																	// tested
																	// plus our
																	// own
																	// number
		BigInteger N = numbers.get(1); // Gets one of the numbers
		long start = System.currentTimeMillis();
		int L = 1000; // number of primes in the factorbase
		primes = new int[L]; // vector to store the first L primes
		pairs = new A[L + 5];
		M = new short[L + 5][L];

		// get the first L primes. implemented by someone
		get_primes();

		// find L+5 y^2 = x mod N and find their prime decomposition.
		// implemented by Robert
		get_pairs();

		// find factors of N given all pairs implemented by linus
		find_factors(L, N);

		long end = System.currentTimeMillis();
		System.out.println("It took " + (end - start) + " ms");
	}

	public static void get_primes() {
		return;
	}

	public static void get_pairs() {
		return;
	}

	private static ArrayList<BigInteger> readFile(String s) {
		ArrayList<BigInteger> indata = new ArrayList<BigInteger>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(s));
			int limit = Integer.parseInt(br.readLine());
			for (int i = 0; i < limit; i++) {
				indata.add(new BigInteger(br.readLine()));
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return indata;

	}

	/** Calculate the square root of a BigInteger in logarithmic time */
	public static BigInteger squareRoot(BigInteger x) {
		BigInteger right = x, left = BigInteger.ZERO, mid;
		while (right.subtract(left).compareTo(BigInteger.ONE) > 0) {
			mid = (right.add(left)).shiftRight(1);
			if (mid.multiply(mid).compareTo(x) > 0)
				right = mid;
			else
				left = mid;
		}
		return left;
	}

	// constructs M, solves xM = 0 and finds decomposition.
	public static void find_factors(int L, BigInteger N){
		//change so that you check if the row already exists in M!!
		int[] decomp;
		short[] temp = new short[L];
		int currRow = 0;
		for(int i = 0; i < M.length; i++){
			decomp = pairs[i].get_factors();
			for(int j = 0; j < L; j++){
				if(decomp[j] %2 != 0){
					M[i][j] = 1;
				}
				if(notExist(temp, M, L)){
					M[currRow] = temp;
					currRow++;
				}
			}
		}
		
		//To know which rows in M that is "unused" 
		for(int i = currRow; i < L; i++){
			M[i][0] = 2;
		}

		// eliminate row by row that is uneven in M
		int index = 0, workRow = 0, j;
		for(int i = 0; i < L ; i++){
			for(j = 0; j < L; j++){
				if(M[j][0] != 2 && M[j][index] == 1){
					workRow = j;
					break;
				}
			}
			for(int k = j; k < L; k++){
				if(M[k][0] != 2 && M[j][index] == 1){
					multiply(pairs[k], pairs[workRow], L);
				}
			}
			M[workRow][0] = 2;
		}
		
		// test the different numbers now obtained on the form x^2 = y^2 mod N.
		for(int i = 0; i < L; i++){
			if(M[i][0] != 2){
				test_solution(pairs[i], N);
			}
		}
		return;
	}

	private static A multiply(A target, A workRow, int L){
		int[] temp = target.get_factors();
		int[] temp2 = workRow.get_factors();
		for(int i = 0; i < L; i++){ 
			temp[i] += temp2[i];
		}
	target.setX(target.getX().multiply(workRow.getX()));
	target.setY(target.getY().multiply(workRow.getY()));
	target.set_factors(temp);
	return target;
	}
	
	private static boolean notExist(short[] temp, short[][] M, int L) {
		boolean notExists = true;
		for (int i = 0; i < L; i++) {
			for (int j = 0; j < L; j++) {
				if (temp[j] != M[i][j]) {
					notExists = false;
				}
			}
			if (!notExists) {
				return false;
			}
		}
		return true;
	}
	
	private static void test_solution(A solution, BigInteger N){
		BigInteger x = squareRoot(solution.getX());
		BigInteger y = squareRoot(solution.getY());
		BigInteger GCD = N.gcd(x.subtract(y));
		if(!GCD.equals(1) && !GCD.equals(N)){
			System.out.println("SUCESS!!");
			System.out.println(GCD);
		}
	}
}
