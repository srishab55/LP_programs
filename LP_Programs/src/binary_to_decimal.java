import java.util.Scanner;

public class binary_to_decimal {

	
	public static void main(String[] args) {
		
		String input;
		Scanner sc=new Scanner(System.in);
		System.out.println("enter input");
		input=sc.next();
		
		if(input.contains("."))
			;
		else 
		{
			input+=".0";
		}
		eval(input);
	}

	private static void eval(String input) {
		String left = "";
		int i=0;
		while(!(input.charAt(i)+"").equals("."))
			{left+=input.charAt(i++)+"";}
		
		i++;
		String right="";
		while(i<input.length())
		{
			right+=input.charAt(i++)+"";
		}
		
		parse leftparse=new parse(left);
		parse rightparse=new parse(right);
		int cnt=leftparse.cnt+rightparse.cnt;
		double val=leftparse.val+rightparse.val*Math.pow(2,-1*rightparse.cnt);
		System.out.println("count: "+cnt);
		System.out.println("value : "+ val);
	}

}
class parse
{
	int cnt;
	double val=0.0;
	public parse(String input) {
		
		
		if(input.length()==1)
		{
			//initialize with val,cnt
			initialize i=new initialize(input);
			cnt=i.cnt;
			val=i.val;
		}
		else
		{
			String last=input.charAt(input.length()-1)+"";
			String left=input.substring(0, input.length()-1);
			parse p=new parse(last);
			parse p1=new parse(left);
			cnt=p.cnt+p1.cnt;
			val=p.val+2*p1.val;
		}
	}
}
class initialize
{
	int cnt=0;
	double val=0.0;
	public initialize(String input) {
	
		cnt=1;
		val=Double.parseDouble(input);
	}
}
