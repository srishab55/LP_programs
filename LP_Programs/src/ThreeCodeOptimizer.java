import java.util.Scanner;

//Task 12: Generate an optimized three address code for a given expression.
//Enter the maximum number of  expressions : 5

//Enter the input : 
//+ 4 2 t1
//+ a t1 t2
//- b a t3
//+ a 6 t4
//* t3 t4 t5
//Optimized code is : 
//+ a 6 t2
//- b a t3
//* t3 t2 t5

public class ThreeCodeOptimizer {
public static void main(String args[])
{
	actions a=new actions();
	a.input();
	a.constant();
	a.expression();
	a.output();
}

}
class expr
{
char[] op=new char[2];
char[] op1=new char[5];
char[] op2=new char[5];
char[] res=new char[5];
int flag;
}
class actions{
	int n;
	expr arr[]=new expr[5];
	public actions() {
	
		for(int i=0;i<5;i++)
			arr[i]=new expr();
	}
public void input()
{
int i;
Scanner sc=new Scanner(System.in);
System.out.println("\n\nEnter the maximum number of  expressions : ");
n=sc.nextInt();
//sc.nextLine();
System.out.println("\nEnter the input : \n");
for(i=0;i<n;i++)
{
 String input=sc.next();
 arr[i].op=input.toCharArray();
 input=sc.next();
 arr[i].op1=input.toCharArray();
 input=sc.next();
 arr[i].op2=input.toCharArray();
 input=sc.next();
 arr[i].res=input.toCharArray();
 arr[i].flag=0;
 
}
sc.close();
}

public void constant()
{
int i;
int op1,op2,res = 0;
char op;
char res1[]=new char[5];
for(i=0;i<n;i++)
{
 if(Character.isDigit(arr[i].op1[0]) && Character.isDigit(arr[i].op2[0])) //if both digits, store them in variables
 {
	String Op1=new String(arr[i].op1);
	//System.out.println(Op1);
   op1=Integer.parseInt(Op1);
   op2=Integer.parseInt(new String(arr[i].op2));
   op=arr[i].op[0];
   switch(op)
   {
     case '+':
       res=op1+op2;
       break;

     case '-':
       res=op1-op2;
       break;

     case '*':
       res=op1*op2;
       break;

     case '/':
       res=op1/op2;
       break;
   }
   res1=(res+"").toCharArray();
   arr[i].flag=1; //eliminate expr and replace any operand below that uses result of this expr
   change(i,i,new String(res1));
 }
}
}


void expression()
{
int i,j;
for(i=0;i<n;i++)
{
 for(j=i+1;j<n;j++)
 {
   String iOp=new String(arr[i].op),jOp=new String(arr[j].op);
   if(iOp.equals(jOp)) //if operators are same
   {
	   
     if(iOp.equals("+")||iOp.equals("*")) //order doesn't matter if operators are + or *
     {
    	 String iOp1=new String(arr[i].op1),jOp1=new String(arr[j].op1);
    	 String iOp2=new String(arr[i].op2),jOp2=new String(arr[j].op2);
       if((iOp1.equals(jOp1)&&iOp2.equals(jOp2)) || (iOp1.equals(jOp2)&&iOp2.equals(jOp1)))  
       {
         arr[j].flag=1; //does't print
         change(i,j,null); //change any operand below that uses result of this expression
       }
     }
     
     else                   
     {
    	 String iOp1=new String(arr[i].op1),jOp1=new String(arr[j].op1);
    	 String iOp2=new String(arr[i].op2),jOp2=new String(arr[j].op2);
       if((iOp1.equals(jOp1))&&(iOp2.equals(jOp2)))
	          {
         arr[j].flag=1;
         change(i,j,null);
       }
     }
   }
 }
}
}

public void output()
{
int i=0;
System.out.println(("\nOptimized code is : "));
for(i=0;i<n;i++)
{
 if(arr[i].flag==0)
 {
   System.out.println(new String(arr[i].op)+" "+new String(arr[i].op1)+" "+new String(arr[i].op2)+" "+ new String(arr[i].res));
 }
}
return ;
}

	void change(int p,int q,String res)
	{
		int i;
		for(i=q+1;i<n;i++)
{

	String pRes=new String(arr[p].res);
	String qRes=new String(arr[q].res),iOp1=new String(arr[i].op1);
	String iOp2=new String(arr[i].op2);
 if((qRes.equals(iOp1)))
   if(res == null)  {               
	   arr[i].op1=pRes.toCharArray();}//for csub

   else {                    
	   arr[i].op1=res.toCharArray();}//for ceval
                  
 else if((qRes.equals(iOp2)))
   if(res == null)                         //for csub
     {	arr[i].op2=pRes.toCharArray();}

   else                                    //for ceval
     {	arr[i].op2=res.toCharArray();}

}
}

}