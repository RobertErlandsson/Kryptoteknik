package project1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;

public class Prime {
	static long[] primes;
	static A[] pairs;
	static short[][] M;
	static int L;
	static int goalRs;
	static ArrayList<BigInteger> rs;
	static ArrayList<BigInteger> r2s;
	static ArrayList<boolean[]> solutions;

	public static void main(String[] args) {
		// ArrayList<BigInteger> numbers = readFile("DATA/input.txt"); // Reads
		// from file Numbers include those that should be tested plus our own
		// number

		BigInteger N = new BigInteger("16637"); // numbers.get(1); // Gets
													// one
													// of the numbers
		long start = System.currentTimeMillis();
		L = 12;// number of primes in the factorbase
		goalRs = L + 5; // Number of different r:s we need
		primes = new long[L]; // vector to store the first L primes
		pairs = new A[goalRs];
		M = new short[goalRs][L];
		rs = new ArrayList<BigInteger>();
		r2s = new ArrayList<BigInteger>();
		solutions = new ArrayList<boolean[]>();

		// get the first L primes. implemented by someone
		primes = generate_primes(L);

		System.out.println("after get_primes");

		// find L+5 y^2 = x mod N and find their prime decomposition.
		// implemented by Robert
		GenRs(N);
		System.out.println("after GenRs");

		// find factors of N given all pairs implemented by linus
		getSolutions(N);
		System.out.println("after getSolution");
		//find_factors(N);
		
		long end = System.currentTimeMillis();
		System.out.println("It took " + (end - start) + " ms");
	}

	/*
	 * public static void get_primes() { Scanner scan = null; // BufferedReader
	 * br = null; try { // br = new BufferedReader(new //
	 * FileReader("C:\\Github\\Kryptoteknik\\src\\project1\\prim_2_24.txt"));
	 * FileReader fr = new FileReader(
	 * "h:\\d6\\z\\tpi13lro\\krypto\\Kryptoteknik\\src\\project1\\prim_2_24.txt"
	 * ); scan = new Scanner(fr); } catch (FileNotFoundException e) {
	 * e.printStackTrace(); } for (int i = 0; i < L; i++) { primes[i] =
	 * scan.nextInt(); } scan.close(); }
	 */

	public static BigInteger GenRs(BigInteger N) {
		long k = 1;
		int nOfRows = 0;
		// System.out.println("rs.size() = " + rs.size() + "goalRs = " +
		// goalRs);
		while (rs.size() < goalRs && k < 1024) {
			long j = 1;
			// System.out.println("j = " + j);
			while (j < 1024 && rs.size() < goalRs) {
				// System.out.println("rs.size() = " + rs.size() + "goalRs = " +
				// goalRs);
				// System.out.println();
				BigInteger r = squareRoot(N.multiply(BigInteger.valueOf(k))).add(BigInteger.valueOf(j));
				// System.out.println("xxxxxxx");
				BigInteger r2 = r.multiply(r).mod(N);
				nOfRows += test_BI(r, r2, nOfRows);
				// System.out.println("xxxxxxxxxxxx");
				// System.out.println("j = " + j + "k = " + k);
				j++;
			}
			k++;
		}
		return null;
	}
	
	public static void getSolutions(BigInteger N) {
		PrintStream ps = null;
		try {
			ps = new PrintStream(new File("in"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		ps.print(r2s.size());		
		ps.print(" ");					
		ps.println(L);					
	
		for (int i = 0; i < L; i++) {
			for (int j = 0; j < goalRs; j++) {
				ps.print(M[j][i]%2);
				ps.print(" ");
			}
			ps.println();
		}

		ps.close();

		try {
			new File("out").createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		@SuppressWarnings("unused")
		Process proc;
		try {
			proc = Runtime.getRuntime().exec("./GaussBin.exe in out");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scanner scan = null;

		try {
			scan = new Scanner(new FileReader(new File("out")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		A Solutions = new A(BigInteger.ONE,BigInteger.ONE,L);
		int nSolutions= scan.nextInt();
		for (int i = 0; i < nSolutions; i++) {
			for (int j = 0; j < goalRs; j++) {
				if(scan.nextInt() == 1){
					multiply(Solutions,pairs[j]);
				}
			}
			test_solution(Solutions,N);
		}
		scan.close();
	}

	
	public static long[] generate_primes(int L) {
		long[] primes = new long[L];
		int n = 0, i, size = L, j, k;
		for (i = 2; n < size; i++) {
			k = (int) Math.sqrt((double) i) + 1;
			for (j = 0; j < n && primes[j] <= k; ++j) {
				if ((i % primes[j]) == 0) {
					j = 0;
					break;
				}
			}
			if (j != 0 || n == 0) {
				primes[n++] = i;
			}
		}
		return primes;
	}
	
	private static int test_BI(BigInteger x, BigInteger y, int nOfRows) {
		A number = new A(x, y, L);
		int[] expo = new int[L];
		boolean change = true;
		int counter = 0;
		while (change && y.compareTo(BigInteger.valueOf(1)) != 0) {
			change = false;
			for (int i = 0; i < L; i++) {
				// System.out.println(x.remainder(BigInteger.valueOf(primes[i])));
				if (y.remainder(BigInteger.valueOf(primes[i])).compareTo(BigInteger.valueOf(0)) == 0) {
					expo[i]++;
					y = y.divide(BigInteger.valueOf(primes[i]));
					// System.out.println(k + " " + i);
					change = true;
					break;
				}
			}
			// System.out.println(counter++);
		}
		number.set_factors(expo);
		short[] binary = new short[L];

		for (int i = 0; i < L; i++) {
			binary[i] = (short) (expo[i] % 2);
		}

		if (y.compareTo(BigInteger.valueOf(1)) == 0 && !containss(expo)) {
			for (int i = 0; i < L; i++) {
				M[nOfRows][i] = (short) expo[i];
			}
			pairs[nOfRows] = number;
			System.out.println("Hittat " + nOfRows + "tal");
			rs.add(x);
			r2s.add(x);
			return 1;
		}
		return 0;
	}

	private static boolean containss(int[] binary) {
		boolean row = true;
		// System.out.println("in containss");
		for (int i = 0; i < goalRs; i++) {
			row = true;
			for (int j = 0; j < L; j++) {
				if (binary[j] != M[i][j]) {
					row = false;
				}
			}
			if (row) {
				return true;
			}
		}
		return false;
	}

	private static void print_matrix() {
		for (int i = 0; i < goalRs; i++) {
			for (int j = 0; j < L; j++) {
				System.out.print(" " + M[i][j]);
			}
			System.out.println("");
		}
	}

	private static void print_pairs(BigInteger N) {
		for (int i = 0; i < goalRs; i++) {
			System.out.print("x = " + pairs[i].getX().mod(N) + " y = " + pairs[i].getY());
			int[] temp = pairs[i].get_factors();
			for (int j = 0; j < L; j++) {
				if (M[i][0] != 2) {
					System.out.print(" " + temp[j]);
				}
			}
			System.out.println("");
		}
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
	public static void find_factors(BigInteger N) {
		// change so that you check if the row already exists in M!!
		/*
		 * int[] decomp; short[] temp = new short[L]; int currRow = 0; for (int
		 * i = 0; i < goalRs; i++) { decomp = pairs[i].get_factors(); for (int j
		 * = 0; j < L; j++) { temp[j] = (short) (decomp[j] % 2); } //
		 * System.out.println(temp[1]);
		 * 
		 * if (notExist(temp)) { // System.out.println("xxx"); for (int k = 0; k
		 * < L; k++) { M[i][k] = temp[k]; } currRow++; } System.out.println(
		 * "fixing rox " + i + "in M"); }
		 * 
		 * // print_pairs(); // print_matrix();
		 * 
		 * // To know which rows in M that is "unused" for (int i = currRow; i <
		 * L; i++) { M[i][0] = 2; }
		 */

		// eliminate row by row that is uneven in M
		int workRow = 0, j;
		boolean uneven = false;
		for (int i = 0; i < L; i++) {
			System.out.println("processing column " + i);
			for (j = 0; j < L; j++) {
				if (M[j][0] != 2 && M[j][i] == 1) {
					workRow = j;
					uneven = true;
					break;
				}
			}
			if (uneven) {
				for (int k = j; k < L; k++) {
					if (M[k][0] != 2 && M[k][i] == 1) {
						multiply(pairs[k], pairs[workRow]);
					}
				}
				M[workRow][0] = 2;
			}
			uneven = false;
		}

		print_pairs(N);

		// test the different numbers now obtained on the form x^2 = y^2 mod N.
		for (int i = 0; i < L; i++) {
			if (M[i][0] != 2) {
				test_solution(pairs[i], N);
			}
		}
		return;
	}

	private static A multiply(A target, A workRow) {
		int[] temp = target.get_factors();
		int[] temp2 = workRow.get_factors();
		for (int i = 0; i < L; i++) {
			temp[i] += temp2[i];
		}
		target.setX(target.getX().multiply(workRow.getX()));
		target.setY(target.getY().multiply(workRow.getY()));
		target.set_factors(temp);
		return target;
	}

	private static boolean notExist(short[] temp) {
		boolean notExists;
		for (int i = 0; i < L; i++) {
			notExists = false;
			for (int j = 0; j < L; j++) {
				if (temp[j] != M[i][j]) {
					notExists = true;
				}
			}
			if (!notExists) {
				return false;
			}
		}
		return true;
	}

	private static void test_solution(A solution, BigInteger N) {
		BigInteger x = solution.getX().mod(N);
		BigInteger y = solution.getY();
		int[] sol = solution.get_factors();
		for (int i = 0; i < L; i++) {
			sol[i] /= 2;
		}
		BigInteger aa = new BigInteger("1");
		for (int i = 0; i < L; i++) {
			aa = aa.multiply(BigInteger.valueOf(primes[i]).pow(sol[i])).mod(N);
		}
		BigInteger GCD = N.gcd(x.subtract(aa));
		System.out.println(GCD);
		if (GCD.compareTo(BigInteger.valueOf(1)) != 0 && GCD.compareTo(N) != 0) {
			System.out.println("SUCESS!!");
			System.out.println(GCD);
			BigInteger factor = N.divide(GCD);
			System.out.println(factor);
		}
	}
}
