import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ll1_parser {

	public static void main(String[] args) {
		HashMap<String,ArrayList<String>> ParsingTable=new HashMap<>();
		
	}

	boolean parser(HashMap<String,ArrayList<String>> ParsingTable, String input, String start )
	{
		Stack<String> stack=new Stack<>();
		
		stack.push("$");
		stack.push(start+"");
		input+="$";
		int index=0;
		boolean flag=true;
		ArrayList<String> first_row=ParsingTable.get("terminals");
		while(flag)
		{
			
			String top=stack.peek();
			String cur=input.charAt(index)+"";
			if(top==cur)
			{
				String s=stack.pop();
				System.out.println(s+" : popped");
				index++;
			}
			else 
			{
				ArrayList<String> pro_var=ParsingTable.get(top);
				int Pro_index=first_row.indexOf(cur);
				
				String prod=pro_var.get(Pro_index);
				if(prod==null) {System.out.println("Wrong input");break;} //null entry
				stack.pop();
				if(prod=="~")continue; //Epsilon production , hence just pop the symbol from stack
				
				if(prod.length()==0) {System.out.println("Error in parsing"); break;} //null entry
				
				
				char c[]=prod.toCharArray();
				for(int j=c.length-1;j>0;j--)
					{
					stack.push(c[j]+"");}
				
				
			}
			if(stack.empty()) flag=false;
		}
		if(flag) return false;
		else return true;
	}
}
