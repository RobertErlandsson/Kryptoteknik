package project3;

public class attacker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String ct = "0000111100111100001001010111001000000100111110101000011011100010101110010110011000011100111011000001101110011111010111111010101100100110111110111010100100001110011010110111010010101011001001000";
		// tring ct = "10110";
		int[] numct = stoint(ct);
		int nOfBits = numct.length;
		String s = "0";
		String[] tmp;
		int[] KeyStream;
		int dist = 0;
		// int[] poly = stoint("101011001010010011");
		int[] poly = stoint("110010010100110101");
		double val;
		int keylength = 17;
		for (int i = 0; i < Math.pow(2, keylength) - 1; i++) {
			tmp = s.split("");
			int[] num = new int[keylength];
			for (int j = tmp.length - 1; j > 0; j--) {
				num[keylength - (tmp.length - j)] = Integer.parseInt(tmp[j]);
			}
			KeyStream = genKeyStream(poly, num, nOfBits);
			dist = hemmingDist(numct, KeyStream);
			val = 1 - (double) (dist / (double) nOfBits);
			if (val > 0.7 || val < 0.3) {
				System.out.println("Hemming distance = " + val);
				System.out.println("We did it!!, value is: " + i);
				int result = i, m = keylength - 1, n = 0;
				while (true) {
					if (result == 1) {
						System.out.println("1");
						break;
					}
					if(result == 0){
						System.out.println("0");
						break;
					}
					n = (int) (result / Math.pow(2, m));
					if (n == 1) {
						result = (int) (result - Math.pow(2, m));
						System.out.print("1");
					} else
					{
						System.out.print("0");
					}
					m--;
				}
			}
			s = Integer.toBinaryString(Integer.valueOf(s, 2) + 1);
		}
	}

	private static int[] stoint(String s) {
		String[] bits = s.split("");
		int[] numct = new int[bits.length];
		for (int i = 0; i < bits.length - 1; i++) {
			numct[i] = Integer.parseInt(bits[i + 1]);
		}
		return numct;
	}

	private static int[] genKeyStream(int[] poly, int[] init_state,
			int lengthKeyStream) {
		// System.out.println(init_state[0] + " " + init_state[1] + " "
		// + init_state[2]);
		int length = init_state.length;
		int[] keyStream = new int[lengthKeyStream];
		int[] state = new int[length];
		for (int i = 0; i < length; i++) {
			state[i] = init_state[i];
		}
		int val = 0;
		for (int i = 0; i < lengthKeyStream; i++) {
			// System.out.println(state[0] + " " + state[1] + " " + state[2]);
			for (int k = 0; k < init_state.length; k++) {
				if (poly[k] == 1) {
					val += state[k];
					// System.out.println(k + " ," + state[k] + " ,"
					// + init_state[k]);
				}
			}
			keyStream[i] = state[0]; // (state[0] + state[1] + state[2] +
										// state[4] + state[6] + state[7] +
										// state[10] + state[11] + state[13]) %
										// 2
										// System.out.println(keyStream[i]);
			for (int j = 0; j < length - 1; j++) {
				state[j] = state[j + 1];
			}
			;
			state[length - 1] = val % 2;
			val = 0;
		}
		return keyStream;
	}

	private static int hemmingDist(int[] a, int[] b) {
		if (a.length != b.length) {
			System.out.println("Rethink code");
		}
		int sum = 0;
		for (int i = 0; i < a.length; i++) {
			// System.out.print(a[i] + " " + b[i]);
			if (a[i] != b[i]) {
				// System.out.print("increase sum");
				sum++;
			}
			// System.out.println();
		}
		return sum;
	}
}
