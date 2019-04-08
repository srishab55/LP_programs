/*Created by 

 * Name : Arushee_Garg
 * Project : Compilers_Building the Parser
 * Project description : Creates the LR parser LR(0) and LR(1) and parses given input strings.
 * Due Date : April26 2017
 * Course Number : CS 59000 -002 lr(1) clr 
  */
//package compilers.parser;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;



/* Creates the LR parsing Table and parses the input string corresponding to Parser.
 * Is the most significant java file for the project
 * Pass the int parameter(int parser) in the constructor for Build_LR to choose either for LR(0) or LR(1) parser
 * For eg : Creating Parser Table for LR(0) parser : Build_LR parser0 = new Build_LR(0);
 * parsing the input string : parser0.parse_string(input_string);
 */ 
public class Build_LR{
    //creates the LR parsing table

    int parser;
    Grammar grammar;
    StateTable statetable;
    Items items;
    HashMap<Integer, String[]> parsr_table;
    static final char[] terminals = new char[]{'i','(',')','+','$','*'};
    static final char[] nonterminals = new char[]{'S','E','T','F'};


    public Build_LR(int parser){
        this.grammar = new Grammar(parser);
        this.statetable = new StateTable(parser);
        this.items = new Items(parser);
        this.parser = parser;


        this.parsr_table = new HashMap<Integer, String[]>();

        if(parser == 0) parsr_table.put(-1, new String[]{"i","(",")","+","$","S","E","T"});
        else if(parser == 1) parsr_table.put(-1, new String[]{"i","(",")","+","*","$","S","E","T","F"});
        char next_char = 'c';

        for(int state_item = 0; state_item < items.size();state_item++){
            ArrayList<String> prod_list = items.get(state_item);
            for(String prod : prod_list){

                int index = prod.indexOf('.');
                char start_char = prod.charAt(0);

                //Case1 Shift
                if(index < (prod.length() - 1) && prod.charAt(index+1)!=next_char){
                    next_char = prod.charAt(index+1);
                    StringBuffer new_prod = new StringBuffer(prod).deleteCharAt(index);
                    new_prod.insert(index+1,'.');
                    int shift = -1;
                    if(isterminal(next_char)){shift = items.statefromItem(new_prod.toString());}
                    else if(isnonterminal(next_char)){shift = items.statefromItem(new_prod.toString());}
                    add_into_parsr_table(state_item,"s"+shift, String.valueOf(next_char));
                }
                // Case2 Reduce
                else if(index == prod.length() -1 && start_char != 'S'){
                    StringBuffer new_prod = new StringBuffer(prod).deleteCharAt(index);
                    //int reduce = -1;
                    //char first = 'a';
                    for (int i = 0; i < grammar.get_size(); i++) {
                        if (grammar.get(i).equals(new_prod.toString())) {
                            int reduce = i;
                            char first = grammar.get(i).charAt(0);
                            add_into_parsr_table(state_item,"r"+ reduce,"a"+first);
                        }
                    }
                }
                //Case3 Accept
                else if(index == prod.length() -1 && start_char == 'S'){
                    add_into_parsr_table(state_item,"Accept","a"+start_char);
                }
            }
        }
    }

    public void add_into_parsr_table(int state_item, String new_state, String next_char){

        String[] list = parsr_table.get(state_item);

        // if list does not exist create it
        if(list == null) {
            list = new String[8];
            if(parser==1) list = new String[10];
            java.util.Arrays.fill(list,"-");
        }

        if(parser == 0){
            String[] symbols = new String[]{"i","(",")","+","$","S","E","T"};
            for(int i = 0; i < 5; i++)
                if(next_char.equals(symbols[i]))  list[i]=new_state;
            for(int i = 5; i < symbols.length; i++)
                if(next_char.equals(symbols[i]))  list[i] = new_state.substring(1);

            //for all the reduce cases
            if(next_char.startsWith("a")){
                for(int i = 0; i < 5; i++) list[i]=new_state;
            }
        }
        else if(parser == 1){
            String[] symbols = new String[]{"i","(",")","+","*","$","S","E","T","F"};
            for(int i = 0; i < 6; i++)
                if(next_char.equals(symbols[i]))  list[i]=new_state;
            for(int i = 6; i < symbols.length; i++)
                if(next_char.equals(symbols[i]))  list[i] = new_state.substring(1);

            //for reduce cases and accept
            if(next_char.startsWith("a")){
                char first = next_char.charAt(1);
                Character[] follow = grammar.follow().get(first);
                for(int i = 0; i < follow.length; i++){
                    for(int j = 2; j < 6; j++) {
                        if (symbols[j].equals(String.valueOf(follow[i]))) list[j] = new_state;
                    }
                }
            }
        }
        parsr_table.put(state_item,list);
    }

    public static boolean isterminal(char next_char){
        for(int i = 0; i < terminals.length ; i++){
            if(next_char == terminals[i]) return true;
        }
        return false;
    }
    public static boolean isnonterminal(char next_char){
        for(int i = 0; i < nonterminals.length ; i++){
            if(next_char == nonterminals[i]) return true;
        }
        return false;
    }

    public void print(){
        for (Map.Entry<Integer, String[]> entry : parsr_table.entrySet()) {
            System.out.printf( "%3s",entry.getKey());
            System.out.print(" | ");
            for(int i = 0; i < entry.getValue().length; i++){
                System.out.printf("%8s",entry.getValue()[i]);//Returns the list of values
            }
            System.out.println();
        }
        System.out.println();
    }

    public int symboltoint(char token){
        int output = -1;
        char[] symbols = new char[]{'i','(',')','+','$','S','E','T'};
        if(parser==1) symbols = new char[]{'i','(',')','+','*','$','S','E','T','F'};

        for(int i = 0; i < symbols.length; i++) {
            if (token == symbols[i]) output = i;
        }
        return output;
    }

    public boolean parse_string(String input_string) {
        System.out.println("Stack \t \t \t \t \t Input \t \t \t \t Action");

        String stackString = "$0", stackStringRev = null;
        Stack<Integer> stack = new Stack<Integer>();
        Stack<Integer> stackRev = new Stack<Integer>();
        Stack<Character> expression = new Stack<Character>();
        stack.push(0);
        char token = input_string.charAt(0);     //token reads from the input_string
        int i = 0, tokenizer;
        int top_of_stack; // top_of_stack peeks from the top of the stack
        char top_of_expression; // top_of_expression peeks from top of expression
        String action, input_buffer = input_string, gram;

        while(true) {
            System.out.printf("%12s\t \t \t %10s \t \t \t", stackString,input_buffer);
            tokenizer = symboltoint(token);
            top_of_stack = stack.peek();
            action = parsr_table.get(top_of_stack)[tokenizer];
            System.out.println(action);

            //If action is shift
            if (action.startsWith("s")) {
                stack.push(Integer.parseInt(action.substring(1)));
                stackString = stackString + action.substring(1);
                input_buffer = input_buffer.substring(1);
                expression.push(token);
                if(!action.equals("s2"))
                    token = input_buffer.charAt(0);
            }
            //If action is reduce
            else if (action.startsWith("r")) {

                //reduce via grammar and pop number of elements on the right of -> grammar
                //store reduced grammar in expression
                //goto of [top_of_stack, expression.pop()]
                //push the goto into stack

                gram = grammar.get(Integer.parseInt(action.substring(1)));

                int number_of_elements = gram.length()-3;
                for(int j = 0; j < number_of_elements ; j++ ) {
                    stack.pop();      //pop number of characters reduced
                    stackString = stackString.substring(0, stackString.length() - 1);
                    expression.pop();
                }
                expression.push(gram.charAt(0));
                top_of_expression = expression.peek();
                top_of_stack= stack.peek();
                tokenizer = symboltoint(top_of_expression);
                action = parsr_table.get(top_of_stack)[tokenizer];

                stackString = stackString + action.substring(0);
                stack.push(Integer.parseInt(action.substring(0)));

            }
            // If action is accept
            else if (action.equals("Accept")) {
                return true;
            // If action is Error
            } else {
                System.out.println("Error");
                return false;
            }
        }
    }


    public static void main(String[] args){

        System.out.println("For LR(0) Parser");
        Grammar grammar  = new Grammar(0);
        System.out.println("LR(0) Grammar");
        grammar.print();

        Build_LR parser = new Build_LR(0);
        System.out.println("The LR(0) Parser Table : ");

        parser.print();

        String input_string = "i+i$";
        parser.parse_string(input_string);

        System.out.println("--------");
        System.out.println("For LR(1) Parser");
        Grammar grammar1  = new Grammar(1);
        System.out.println("LR(1) Grammar");
        grammar1.print();
        Build_LR parser1 = new Build_LR(1);
        System.out.println("The LR(1) Parser Table : ");

        parser1 .print();

        String input_string1 = "i+i*(i+i)*i$";
        String input_string2 = "i+i*i$";
        parser1.parse_string(input_string2);
    }
}
