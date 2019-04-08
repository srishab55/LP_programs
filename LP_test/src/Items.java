/*Created by 
 * Name : Arushee_Garg
 * Project : Compilers_Building the Parser
 * Project description : Creates the LR parser LR(0) and LR(1) and parses given input strings.
 * Due Date : April26 2017
 * Course Number : CS 59000 -002
  */
//package compilers.parser;

import java.util.*;


/* Creates the Items for corresponding Parser.
 * Pass the int parameter(int parser) in the constructor for Items to choose either for LR(0) or LR(1) parser
 * For eg : Creating Items for LR(0) parser : Items item0 = new Items(0)
 * Prints all the 10  items in the ITems for LR(0) parser.
 * Items uses Closure , Goto and Follow methods.
 */ 
public class Items {

    HashMap<Integer, ArrayList<String>> items;
    Grammar grammar;

    public Items(int parser){
        items = new HashMap<>();
        grammar = new Grammar(parser);
        StringBuffer prod ;
        char dot;
        int state_item = 0;
        int count = 0;

        prod = new StringBuffer(grammar.get(count));
        prod = prod.insert(3, ".");// inserts dot after -> of the grammar
        closure(state_item, prod );

        if(parser == 0) {
            while (items.containsKey(state_item) == true && state_item < 9) {

                char next_input = inputSymbolsToStates.get(state_item + 1);
                Goto(state_item, next_input,parser);
                state_item++;
            }
        }
        else if(parser == 1){
            while (items.containsKey(state_item) == true && state_item < 12) {

                char next_input = inputSymbolsToStates1.get(state_item + 1);
                Goto(state_item, next_input, parser);
                state_item++;
            }
        }
    }

    public void closure(int state_item, StringBuffer s ){

        addToItems(state_item, s.toString());

        int index = s.indexOf(".");
        if(index != s.length()-1){
            char dot_next = s.charAt(index + 1);
            HashSet<Character> nonterminal = new HashSet<Character>();
            int next = 0;
            //if dot is one of the non-terminal
            while (dot_next == 'E' || dot_next == 'F' || dot_next == 'T' || dot_next == 'S') {
                // add all the strings from grammar which starts with dot
                for (int i = 0; i < grammar.get_size(); i++) {
                    if (dot_next == grammar.get(i).charAt(0)) {
                        String str = grammar.get(i);
                        str = new StringBuffer(str).insert(3, ".").toString();
                        addToItems(state_item, str);
                        //storing the first char in nonterminal array
                        nonterminal.add(str.charAt(4));
                    }
                }
                dot_next = (char) nonterminal.toArray()[next];
                next++;
            }
        }
    }


    public void Goto(int state_item, char input, int parser ) {

        int new_state_item = state_item + 1;
        int index;

        Integer[] input_from = inputstates.get(new_state_item);
        //if(parser == 0) input_from = inputstates.get(new_state_item);
        if(parser == 1) input_from = inputstates1.get(new_state_item);

        for(int state : input_from){
            ArrayList<String> prod = items.get(state);
            for (String prod_string : prod) {
                index = prod_string.indexOf(".");
                if (index < prod_string.length() - 1) {
                    char check = prod_string.charAt(index + 1);
                    if (check == input) {
                        StringBuffer s = new StringBuffer(prod_string).deleteCharAt(index);;
                        s = s.insert(index + 1, ".");
                        closure(new_state_item, s);
                    }
                }
            }
        }
    }

    public void addToItems(Integer mapKey, String s) {
        ArrayList<String> itemsList = items.get(mapKey);

        // if list does not exist create it
        if(itemsList == null) {
            itemsList = new ArrayList<String>();
            itemsList.add(s);
            items.put(mapKey, itemsList);
        } else {
            // add if item is not already in list
            if(!itemsList.contains(s)) itemsList.add(s);
        }
    }

    public void print(){
        for (Map.Entry<Integer, ArrayList<String>> entry : items.entrySet()) {
            System.out.printf( "%3s",entry.getKey());
            System.out.print(" | ");
            System.out.print( entry.getValue());//Returns the list of values
            System.out.println();
        }

    }

    private static Map<Integer,Character> inputSymbolsToStates = new HashMap<Integer, Character>()
    {{
        put(1,'E');put(2,'$');
        put(3,'+');put(4,'T');
        put(5,'i');put(6,'(');
        put(7,'E');put(8,')');
        put(9,'T');

    }};

    private static Map<Integer,Character> inputSymbolsToStates1 = new HashMap<Integer, Character>()
    {{
        put(1,'E');put(2,'$');
        put(3,'+');put(4,'F');
        put(5,'i');put(6,'(');
        put(7,'T');put(8,'*');
        put(9,'F');put(10,'E');
        put(11,'T');put(12,')');

    }};

    private static Map<Integer,Integer[]> inputstates = new HashMap<Integer, Integer[]>()
    {{
        put(1,new Integer[]{0});
        put(2,new Integer[]{1});
        put(3,new Integer[]{1});put(4,new Integer[]{3});
        put(5,new Integer[]{0,3});put(6,new Integer[]{0,3});
        put(7,new Integer[]{6});put(8,new Integer[]{7});
        put(9,new Integer[]{0,6});

    }};

    private static Map<Integer,Integer[]> inputstates1 = new HashMap<Integer, Integer[]>()
    {{
        put(1,new Integer[]{0});put(2,new Integer[]{1});
        put(3,new Integer[]{1});put(4,new Integer[]{0,3});
        put(5,new Integer[]{0,3});put(6,new Integer[]{0,3});
        put(7,new Integer[]{0,6});put(8,new Integer[]{7});
        put(9,new Integer[]{8}); put(10,new Integer[]{6});
        put(11,new Integer[]{3});put(12,new Integer[]{10});
    }};

    public boolean isInItem(String prod, int itemNo)
    {
        ArrayList<String> itemsList = items.get(itemNo);
        for(int i = 0; i < itemsList.size(); i++)
        {
            if(prod.equals(itemsList.get(i)))
                return true;
        }
        return false;
    }

    public int statefromItem( String prod){
        for(int k = 0; k < items.size(); k++){
            if(isInItem(prod,k)) return k;
        }
        return -1;
    }

    public int size()
    {
        return items.size();
    }

    public ArrayList<String> get(int state_item){
        return items.get(state_item);
    }

    public static void main(String[] args) {

        System.out.println("Items for LR(0)");
        Items items = new Items(0);
        items.print();
        System.out.println("\n Items for LR(1)");
        Items items1 = new Items(1);
        items1.print();
    }
}
