/* 
 E->x+T

 T->(E)
 T->x
 */
import java.util.*;

class RecursiveDescentParser {
	static int lookahead;
	static char[] input;
	
	public static void main(String args[]) {
		System.out.println("Enter the input string:");
		String s = new Scanner(System.in).nextLine();
		input = s.toCharArray();
		if(input.length < 2) {
			System.out.println("The input string is invalid.");
			System.exit(0);
		}
	 lookahead = 0;
		boolean isValid = E();
		if((isValid) & (lookahead == input.length)) {
			System.out.println("The input string is valid.");
		} else {
			System.out.println("The input string is invalid.");
		}
	}
	
	static boolean E() {

		int fall = lookahead;
		if(input[lookahead++] != 'x') {
			lookahead = fall;
			return false;
		}
		if(input[lookahead++] != '+') {
			lookahead = fall;
			return false;
		}
		if(T() == false) {
			lookahead = fall;
			return false;
		}
		return true;
	}
	
	static boolean T() {
		int fall = lookahead;
		if(input[lookahead] == 'x') {
			lookahead++;
			return true;
		}
		else {
			if(input[lookahead++] != '(') {
				lookahead = fall;
				return false;
			}
			if(E() == false) {
				lookahead= fall;
				return false;
			}
			if(input[lookahead++] != ')') {
				lookahead = fall;
				return false;
			}
			return true;
		}
	}
}