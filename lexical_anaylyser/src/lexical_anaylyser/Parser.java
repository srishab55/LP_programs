package lexical_anaylyser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {

	@SuppressWarnings("null")
	public static void main(String[] args) throws IOException {
		String temp="",comment = "";
		ArrayList<String> arl =new ArrayList<>();
		FileReader sc =new FileReader("/home/rishab/eclipse-workspace/lexical_anaylyser/src/lexical_anaylyser/Parser.java");
		int t;
		char ch;
		while ((t=sc.read())!=-1)
		{
			ch=(char) t;
			
			 if (ch=='/')
			{
				char nt;
				comment+=ch+"";	
				//System.out.println(comment);
				if(comment.equals("//")) 
				{	
				
				comment="";
					while((nt=(char)sc.read())!='\n')
					{
						
					}
				}	
				continue;
			}
			if (ch!='-'&&ch!='*'&&ch!='+'&&ch!='^'&&ch!='|'&&ch!='%'&&ch!='&'&&ch!=']'&&ch!='['&&ch!='{'&&ch!='}'&&ch!='('&&ch!=')'&&ch!='#'&&ch!=' '&&ch!=';'&&ch!='.'&&ch!='\n'&&ch!='='&&ch!='"'&&ch!='<'&&ch!='>'&&ch!='!'&&ch!=',')
			{
				temp+=ch+"";
				
			}
			else
			{
				if(!temp.trim().equals(""))
				arl.add(temp.trim()+"");
				if(ch!=' '&&ch!='\n')
				arl.add(ch+"");
				temp="";
			}
			
		}
		if(t==-1)
		{
			arl.add(temp+"");
		}
		for ( String st : arl)
		{
			System.out.println(st);
		}
		System.out.println(arl);

	}

}
