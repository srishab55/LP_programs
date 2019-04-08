/*Created by 
 * Name : Arushee_Garg
 * Project : Compilers_Building the Parser
 * Project description : Creates the LR parser LR(0) and LR(1) and parses given input strings.
 * Due Date : April26 2017
 * Course Number : CS 59000 -002
  */
//package compilers.parser;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

/* Creates the grammar for corresponding Parser.
 * Pass the int parameter(int parser) in the constructor for grammar to choose either for LR(0) or LR(1) parser
 * For eg : Creating grammar for LR(0) parser : Grammar grammar0 = new Grammar(0)
 */ 
public class Grammar {
    List<String> grammar;

    public Grammar(int parser){
        this.grammar = new ArrayList<String>();
        if(parser== 0){
            grammar.add("S->E$");
            grammar.add("E->E+T");
            grammar.add("E->T");
            grammar.add("T->i");
            grammar.add("T->(E)");
        }
        else if(parser == 1){
            grammar.add("S->E$");
            grammar.add("E->E+T");
            grammar.add("E->T");
            grammar.add("T->T*F");
            grammar.add("T->F");
            grammar.add("F->i");
            grammar.add("F->(E)");
        }
    }

    public HashMap<Character, Character[]> follow(){

        HashMap<Character, Character[]> follow = new HashMap<Character, Character[]>();
        follow.put('E',new Character[]{'+',')','$'});
        follow.put('T',new Character[]{'+','*',')','$'});
        follow.put('F',new Character[]{'+','*',')','$'});
        follow.put('S',new Character[]{'$'});
        return follow;
    }

    public String get(int i){
        return grammar.get(i);
    }
    public int get_size(){
        return grammar.size();
    }

    public void print(){
        for(int i = 0; i < grammar.size(); i++){
            System.out.println(grammar.get(i));
        }
    }

    public static void main(String[] args){

        int parser = 1;
        Grammar grammar  = new Grammar(parser);
        grammar.print();
    }
}
