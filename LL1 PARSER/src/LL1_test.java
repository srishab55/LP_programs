import java.util.ArrayList;
import java.util.Stack;

/*
* G->E$
* E->TK
* K->+TK|null
* T->FH
* H->*FH|null
* F->(E)|a
* */
public class LL1_test {
	int index=-1;
	String input;
	String table[][]={{"E$",null,null,"E$",null,null},
            {"TK",null,null,"TK",null,""},
            {null,"+TK",null,null,"",""},
            {"FH",null,null,"FH",null,null},
            {null,"","*FH",null,"",""},
            {"a",null,null,"(E)",null,null}};
	ArrayList<String> nonTers;
	ArrayList<String> terms;
	Stack<String> stack;
	public LL1_test() {
	
		terms=new ArrayList<>();
		nonTers=new ArrayList<>();
		stack=new Stack<>();
		 nonTers.add("G");nonTers.add("E");nonTers.add("K");nonTers.add("T");nonTers.add("H");nonTers.add("F");
	        terms.add("a");terms.add("+");terms.add("*");terms.add("(");terms.add(")");terms.add("$");
	}
	public static void main(String args[])
	{
		LL1_test ll1=new LL1_test();
		ll1.Algorithm("a*a$");
	}
	
	void  pushRule(String rule)
	{
		for(int i=rule.length()-1;i>=0;i--)
		{
			stack.push(rule.charAt(i)+"");
		}
		
	}
	void Algorithm(String input)
	{	this.input=input;
		stack.push("$G");
		String res=read();
		do {
			String top=stack.pop();
			
			if(nonTers.contains(top))
			{
				String rule=table[nonTers.indexOf(top)][terms.indexOf(res)];
				pushRule(rule);
				
			}
			else
			{
				if(!terms.contains(top))
				{
					System.out.println("Error");
					break;
				}
				else {
					if(top.equals(res))
				
				{
					System.out.println("Next");
					res=read();
				}
				else {
						System.out.println("error");
						break;
				}
			}
			}
		if(res.equals("$"))	break;
		}while(true);
		
		if(index!=input.length()-1) System.out.println("error");
		else System.out.println("correct");
	}
	String read()
	{
		index++;
		return input.charAt(index)+"";
	}
	
	

}
