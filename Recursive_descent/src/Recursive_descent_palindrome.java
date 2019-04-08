import java.util.Scanner;

/*
 * P2 = { S -> aSa , 
 * 		  S -> bSb ,
 * 		 S -> aa , 
 * 		 S -> bb }. 
 */
public class Recursive_descent_palindrome {

	static String str="";
	static int ptr=0,l;
	static char ch,oth;
	static Recursive_descent_class rc;
	public static void main(String[] args) {
		
			Scanner sc=new Scanner(System.in);
			str=sc.nextLine();
			
			l=str.length();
			if(Production_rec_S()==-1) System.out.println("The String is not in proper format");
			else 
			{
				if(ptr==str.length()) System.out.println("The string is accepted ");
				else System.out.println("The String is not in proper format");
			}
			
	}
	public static int Production_rec_S()
	{
		if (ptr>=l) return -1;
		int flag=1;
		if(match('a'))
		{
			ptr++;
			flag=Production_rec_S();
			
			if(flag==-1) return -1;
			
			if(match('a')) {
				ptr++;return 1;
			}
		}
		else if (match('b'))
		{
			ptr++;
			flag=Production_rec_S();
			
			if(flag==-1) return -1;
			
			if(match('b')) {
				ptr++;return 1;
			}
		}
		return flag;
	}

	public static boolean match(char ch)
	{
		if (str.charAt(ptr)==ch)
			return true;
		else 
		return false;
	}

}
