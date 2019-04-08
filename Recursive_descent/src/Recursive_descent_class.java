import java.util.Scanner;

/*
 * P1 = { S -> aSb , 
 * 			S -> ab }. 
 
 */
public class Recursive_descent_class {
	static String str="";
	static int ptr=0;
	public static void main(String args[])
	{
		Scanner sc =new Scanner(System.in);
		str=sc.nextLine();
		if(Production_S()==-1) System.out.println("The String is not in proper format");
		else 
		{
			if(ptr==str.length()) System.out.println("The string is accepted ");
			else System.out.println("The String is not in proper format");
		}
	}
	public static int Production_S()
	{
		int flag=1;
		if(match('a'))
		{
			ptr++;
							
			flag=Production_S();
				
			 if (flag!=1) return -1;
			 if(match('b')) {ptr++; return 1;}
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
