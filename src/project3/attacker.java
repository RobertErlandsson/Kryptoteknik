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
					if (result == 0) {
						System.out.println("0");
						break;
					}
					n = (int) (result / Math.pow(2, m));
					if (n == 1) {
						result = (int) (result - Math.pow(2, m));
						System.out.print("1");
					} else {
						System.out.print("0");
					}
					m--;
				}
			}
			s = Integer.toBinaryString(Integer.valueOf(s, 2) + 1);
		}
		testsolution(ct);
	}

	private static void testsolution(String correct) {
		int[] K1 = stoint("0000101000111");
		int[] K2 = stoint("010011110011110");
		int[] K3 = stoint("00111101101100100");
		int[] P1 = stoint("1011001101011");
		int[] P2 = stoint("101011001101010");
		int[] P3 = stoint("11001001010011010");
		System.out.println(P2.length);
		for(int i = 0; i < P2.length-2; i++){
			System.out.println(P2[i]);
		}
		int lengthKeyStream = 193, v1 = 0, v2 = 0, v3 = 0;
		int[] Stream = new int[lengthKeyStream + 2];
		for (int i = 0; i < lengthKeyStream; i++) {
			// calculate what goes into keystream
			System.out.println("i = " + i + " " + K1[0] + " " + K2[0] + " " + K3[0]);
			if (K1[0] == 1 && K2[0] == 1 || K1[0] == 1 && K3[0] == 1
					|| K2[0] == 1 && K3[0] == 1) {
				Stream[i] = 1;
			} else {
				Stream[i] = 0;
			}

			// update K1
			for (int k = 0; k < P1.length; k++) {
				if (P1[k] == 1) {
					v1 += K1[k];
				}
			}

			for (int j = 0; j < P1.length - 2; j++) {
				K1[j] = K1[j + 1];
			}
			K1[P1.length - 2] = v1 % 2;

			// update K2
			for (int k = 0; k < P2.length; k++) {
				if (P2[k] == 1) {
					v2 += K2[k];
//					System.out.println("Added " + K2[k] + " from index " + k);
				}
			}
			System.out.println();
			for (int j = 0; j < P2.length - 2; j++) {
				K2[j] = K2[j + 1];
			}
			K2[P2.length - 2] = v2 % 2;

			// update K3
			for (int k = 0; k < P3.length; k++) {
				if (P3[k] == 1) {
					v3 += K3[k];
				}
			}
			for (int j = 0; j < P3.length - 2; j++) {
				K3[j] = K3[j + 1];
			}
			K3[P3.length - 2] = v3 % 2;
	//		System.out.println("i = " + i + " v1 = " + v1 + " v2 = " + v2 + " v3 = " + v3);
			v1 = 0;
			v2 = 0;
			v3 = 0;
		}
		int[] corr = stoint(correct);
		for(int i = 0; i < Stream.length; i++){
			System.out.println("i = "+ i + " " + Stream[i] + " " + corr[i]);
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
