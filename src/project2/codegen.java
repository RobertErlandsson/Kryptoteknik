package project2;

public class codegen {
	public static void main (String[] args){
		int[] first = gen2();
		//when adding together the two sequences 5x+y works, hint from patrik
		int[] corr = new int[4];
		int x4 = 2, x3 =  0, x2 = 2, x1 = 1 ,x0 = 1;
		corr[0] = x4;
		corr[1] = x3;
		corr[2] = x2;
		corr[3] = x1;
		int[] second = gen5(x4, x3, x2, x1, x0);
		int totsize = 10^4 + 3;
		int[] GF = new int[totsize];
		int j = 0,k = 0;
		for(i = 3; i < totsize; i++){
			GF[i] = 5 * first[j++] + second[k++];
			if(j==16) j = 0;
			if(k==625) k = 0; //maybe 624
		}
	}
	
	private static int[] gen2(){
		// create a Bruijin sequence of length 16
		//the polynomial used is x^4 + x^3 + 1, this is a generator
		int[] seq = new int[16];
		int[] num = new int[4];
		
		//initiate the sequence to the coefficients in the polynomial 
		num[0] = 1;
		num[1] = 1;
		
		//create the sequence, ^ is XOR instead of mod 2
		for(int i = 0; i < 15; i++){
//			System.out.println(num[0] + " " + num[1] + " " + num[2] + " " + num [3]);
			seq[i] = (num[0] ^ num[3]) * 1;
			num[0] = num[1];
			num[1] = num[2];
			num[2] = num[3];
			num[3] = seq[i];
		}
		
		//manually inserting a zero
		int j = 0;
		for(j = 2; j < 16; j++){
			if(seq[j] == 0 && seq[j-1] == 0 && seq[j-2] == 0){
				j++;
				break;
			}
		}
		System.out.println(j);
		for(int k = 15; k > j; k--){
			seq[k] = seq[k-1];
		}
		seq[j] = 0;
		return seq;
	}
	
	private static int[] gen5(int x4, int x3, int x2, int x1, int x0){
		// create a Bruijin sequence of length 5^4
		//the polynomial used is 2x^4 + 0x^3 + 2x^2 + 1x + 1, this is a generator
		int numbers = (int) Math.pow(5,4);
		int[] seq = new int[2*numbers];
		int[] num = new int[4];
		
		//initiate the sequence to the coefficients in the polynomial 
		num[0] = x4;
		num[1] = x3;
		num[2] = x2;
		num[3] = x1;
		
		//create the sequence of mod 5
		for(int i = 0; i < 2*numbers - 1; i++){
			seq[i] = ((x4 * num[0] + x3 * num[1] + x2 * num[2] + x1 * num[3]) * x0) % 5;
//			System.out.println(num[0] + " " + num[1] + " " + num[2] + " " + num [3] + " result = " + seq[i]);
			num[0] = num[1];
			num[1] = num[2];
			num[2] = num[3];
			num[3] = seq[i];
		}
		
		/*
		//manually insert a zero
		int j = 0;
		for(j = 2; j < numbers - 1; j++){
			if(seq[j] == 0 && seq[j-1] == 0 && seq[j-2] == 0){
				j++;
				System.out.println("Inserting 0 at: " + j);
				break;
			}
		}
		System.out.println("numbers = " + numbers +" j = " + j);
		for(int k = numbers - 1; k > j; k--){
			seq[k] = seq[k-1];
		}
		seq[j] = 0;
		*/
		return seq;
	}
}
