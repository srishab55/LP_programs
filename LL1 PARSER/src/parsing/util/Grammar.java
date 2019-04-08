

package parsing.util;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Object;
/**
 * Grammar class for general context free grammar
 */
public class Grammar {
    NonTerminal111 start;
    List<Rule> rules;
    Map<NonTerminal,List<Rule>> ruleMap;
    Map<Symbol,List<Rule>> lcMap;
    List<Rule> epsilonList;
    Map<Rule,Integer> ruleNumMap;
    Map<Terminal,List<NonTerminal>> terminalMap;
    Map<Symbol,Integer> ntNumMap;
    NonTerminal[] numToNt;
    Map<NonTerminal,Set<Terminal>> firstSets;
    boolean firstSetsCalculated = false;

    /**
     * Constructor.
     * @param start the start symbol.
     * @param rules the grammar rules
     */
    public Grammar(NonTerminal start, List<Rule> rules) { 
        this.start = start;
        this.rules = new LinkedList<Rule>();
        this.rules.addAll(rules);

        ruleNumMap = new HashMap<Rule,Integer>();
        ruleMap = new HashMap<NonTerminal,List<Rule>>();
        ntNumMap = new HashMap<Symbol,Integer>();    
        terminalMap = new HashMap<Terminal,List<NonTerminal>>();
        int ntNum = 0;
        for (int i = 0; i < rules.size(); i++) {
            Rule r = rules.get(i);
            if (r.rhs().size() == 1 
                &&
                r.rhs().get(0) instanceof Terminal) {
                Terminal t = (Terminal) r.rhs().get(0);
                List<NonTerminal> l = terminalMap.get(t);
                List<NonTerminal> preTermList;
                if (l == null) {
                    preTermList = new LinkedList<NonTerminal>();
                    terminalMap.put(t, preTermList);
                } else {
                    preTermList = l;
                }
                preTermList.add(r.lhs());
            }

            ruleNumMap.put(r,new Integer(i));
            // record nonterminals found
            if (ntNumMap.get(r.lhs()) == null) {
                ntNumMap.put(r.lhs(), new Integer(ntNum++));
            }
            // Go through the rhs and record all the nonterminals
            // found. If the grammar does not contain unreachables
            // this is not necessary (but it doesn't hurt).
            for (int j = 0; j <  r.rhs().size(); j++) {


                if (r.rhs(j) instanceof NonTerminal 
                    && ntNumMap.get(r.rhs(j)) == null) {
                    ntNumMap.put(r.rhs(j), new Integer(ntNum++));
                }
            }
            List<Rule> foundRules = ruleMap.get(r.lhs());
            if (foundRules == null) {
                List<Rule> theRules = new LinkedList<Rule>();
                theRules.add(r);
                ruleMap.put(r.lhs(), theRules);
            } else {
                List<Rule> foundRulesList = foundRules;
                foundRulesList.add(r);
            }
        }
        Set nonTerminalsUsed = ntNumMap.keySet();
        numToNt = new NonTerminal[nonTerminalsUsed.size()];
        for (Iterator i = nonTerminalsUsed.iterator(); i.hasNext(); ) {
            NonTerminal nt = (NonTerminal) i.next();
            numToNt[ntNumMap.get(nt).intValue()] = nt;
        }
        lcMap = new HashMap<Symbol,List<Rule>>();
        epsilonList = new LinkedList<Rule>();
        for (int i = 0; i < rules.size(); i++) {
            Rule r = rules.get(i);
            Symbol lc = r.lc();
            if (lc == null) {
                epsilonList.add(r);
            }
            List<Rule> foundRules = lcMap.get(lc);
            if (foundRules == null) {
                List<Rule> theRules = new LinkedList<Rule>();
                theRules.add(r);
                lcMap.put(lc, theRules);
            } else {
                List<Rule> foundRulesList = foundRules;
                foundRulesList.add(r);
            }
        }
    }

    /**
     * Add a new super start symbol. If the start symbol
     * is S, the super start symbol would normally be
     * S'. But if S' already exists, then additional 
     * primes will be added until a new NonTerminal is
     * found. 
     * return a new grammar with S' --> S # added 
     *        (assuming S is the orignal start symbol).
     */
    public Grammar addSuperStart() {
        NonTerminal superStart = 
            NonTerminal.valueOf(start.toString()+"'");
        // Keep adding primes to the start until you get
        // something new.
        while (true) {
            if (numRhss(superStart) == 0) break;
            superStart = NonTerminal.valueOf(superStart.toString()+"'");
        }
        List<Rule> newRules = new LinkedList<Rule>();
        newRules.addAll(rules);
        List<Symbol> newRhs = new LinkedList<Symbol>();
        newRhs.add(start);
        newRhs.add(Terminal.hash);
        newRules.add(0, new Rule(superStart, newRhs));
        return new Grammar(superStart, newRules);
    }
  


    //-----------//
    // Getters   //
    //-----------//

    /**
     * Get the start symbol
     * @return the start symbol
     */
    public NonTerminal start() {
        return start;
    }

    /**
     * Get rules for a specified left hand side
     * @param lhs the left hand side
     * @return the list of rules
     */
    public List<Rule> lookUpRules(Symbol lhs) {
        List<Rule> result = ruleMap.get(lhs);
        if (result == null) {
            return new LinkedList<Rule>();
        } else {
            return result;
        }
    }

    /**
     * Get the number of right hand sides for 
     * a given left hand side.
     * @return the number of right hand sides.
     */
    public int numRhss(Symbol lhs) {
        return lookUpRules(lhs).size();
    }

    /**
     * Get a rule for a specified left hand side
     * @param lhs the left hand side
     * @param num the number of the rhs
     * @return the rule
     */
    public Rule lookUpRule(Symbol lhs, int num) {
        return ruleMap.get(lhs).get(num-1);
    }

    /**
     * Get the List of categories (left hand sides) 
     * for a terminal.
     * @param t the terminal
     */
    public List<NonTerminal> lookUpTerminal(Terminal t) {
        return terminalMap.get(t);
    }

    /**
     * Get rules for a spcified "left corner", i.e.,
     * a spcified first daughter.
     * @param lc the left corner
     * @return the list of BinaryRule's
     */
    public List<Rule> lookUpRulesLc(Symbol lc) {
        List<Rule> result = lcMap.get(lc);
        if (result == null) {
            return new LinkedList<Rule>();
        } else {
            return result;
        }
    }

    /**
     * Find all epsilon rules.
     * @return a list of all rules with empty right hand sides.
     */
    public List<Rule> lookUpRulesEpsilon() {
        return epsilonList;
    }


    /**
     * Get rules for a spcified right hand side
     * @param firstDtr the first daughter
     * @param secondDtr the second daughter
     * @return the list of BinaryRule's
     */
    public List<Rule> lookUpRulesRhs(List<Symbol> rhs) {
        if (rhs.size() == 0) {
            return epsilonList;
        }
        List<Rule> lcRules = lookUpRulesLc(rhs.get(0));
        List<Rule> result = new LinkedList<Rule>();
        for (int i = 0; i < lcRules.size(); i++) {
            Rule rule = lcRules.get(i);
            if (rule.rhs().equals(rhs)) {
                result.add(rule);
            }
        }
        return result;
    }
        
    /**
     * Method to get a unique int for each rule in the grammar.
     * The int value is simply the order of the rule in the list
     * of rules used in the constructor. Counting starts at 0.
     * @param r the rule
     * @return the int value of the rule
     */
    public int toInt(Rule r) {
        return ruleNumMap.get(r).intValue();
    }

    /**
     * Method to get a unique int for each NonTerminal used in
     * the grammar. Unreachable and nonproductive nonterminals
     * are also included.
     * @param nt the nonterminal to look up
     * @return the int value of the nonterminal
     */
    public int toInt(NonTerminal nt) {
        return ntNumMap.get(nt).intValue();
    }

    /**
     * Method to get back the NonTerminal associated with an
     * int. 
     * @see #toInt(parsing.util.NonTerminal)
     * @param i the integer associated with a NonTerminal
     * @return the NonTerminal associated with i
     */
    public NonTerminal toNonTerminal(int i) {
        return numToNt[i];
    }


    /** 
     * Method to get the number of nonterminals used in the 
     * grammar. 
     * @return the number of nonterminals
     */
    public int numNonTerminals() {
        return ntNumMap.size();
    }

    /**
     * Method to get a unique Integer for each rule in the
     * grammar. The int value is simply the 0-based order
     * of the rule in the list of rules used in the constructor.
     * @param r the rule
     * @return the Integer value of the rule
     */
    public Integer toInteger(Rule r) {
        return ruleNumMap.get(r);
    }

    /**
     * Method to get rule associated with an int.
     * @param i the number of the rule
     * @return the rule associated with i
     */
    public Rule lookUpRule(int i) {
        return rules.get(i);
    }

    /**
     * Method to get rule associated with an Integer.
     * @param i the number of the rule
     * @return the rule associated with i
     */
    public Rule lookUpRule(Integer i) {
        return rules.get(i.intValue());
    }
  

    /**
     * Method to get the total number of rules in the grammar
     * @return the number of rules
     */
    public int nRules() {
        return rules.size();
    }

    /**
     * Method to get all the rules
     * @return the list of rules
     */
    public List<Rule> rules() {
        return rules;
    }

    /**
     * Method to get all possible Terminals of the expansion of a NT
     * @param nt a nonterminal
     * @return Set of Terminals in the first set of nt
     */
    public Set getFirstSet(NonTerminal nt) {
        if (!firstSetsCalculated) {
            calculateFirstSets();
            firstSetsCalculated = true;
        }
        return firstSets.get(nt);
    }

    /*
     * Method called by the constructor to initialize the FIRST sets
     * Note that the method is private, and doesn't have a javadoc
     * comment.
     */
    private void calculateFirstSets(){
        firstSets = new HashMap<NonTerminal,Set<Terminal>>();
        for (int i = 0; i < numNonTerminals(); i++) {
            firstSets.put(toNonTerminal(i), new HashSet<Terminal>());
        }
        // Treat epsilon as a special kind of terminal
        // This is a bit of a hack. Maybe it would be better
        // for epsilon to be a separate subtype of Symbol.
        Terminal epsilon = Terminal.valueOf("epsilon");

        // Keep a separate list of nonEpsilon rules, so there
        // will be no errors attempting to access the left
        // corner.
        List<Rule> nonEpsilonRules = new LinkedList<Rule>();

        // Make first pass through all rules to find 
        // terminals in left corner position
        for (int i = 0; i < nRules(); i++) {
            Rule current = rules.get(i);
            Set<Terminal> found = 
                firstSets.get(current.lhs());
            if (current.rhs().isEmpty()) {
                found.add(epsilon);
            } else {
                nonEpsilonRules.add(current);
                Symbol firstRhs = current.rhs(0);
                if (firstRhs instanceof Terminal) {
                    found.add((Terminal) firstRhs);
                }
            }
        }

        // Now iterate, adding first sets from left
        // corner to the first sets of the left hand side
        // until no change is made.
        boolean changed = true;
        while (changed) {
            // Each iteration resets 'changed' to false. Then
            // 'changed' will be reset to 'true' whenever something
            // gets changed.
            changed = false;
            for (int i = 0; i < nonEpsilonRules.size(); i++) {
                Rule current = nonEpsilonRules.get(i);
                Symbol firstRhs = current.rhs(0);
                Set<Terminal> found = 
                    firstSets.get(current.lhs());
                if (firstRhs instanceof NonTerminal) {

                    // Note in the following line, the order is crucial. If you
                    // reverse the order:
                    // 
                    //    changed = changed || found.addAll(...);
                    //
                    // then if 'changed' is alread true, the second disjunct
                    // will not be evaluated. This is know as short-circuit
                    // evaluation. An alternative would be to use the '|'
                    // operator which, when applied to boolean arguments,
                    // indicates a non-short circuit 'or'. 
                    // 
                    // changed = found.addAll(...) | changed; // order arbitrary
                    // 
                    // Alternatively, one can use the |= operator:
                    // 
                    // changed |= found.addAll(...);
                    // 
                    // Perhaps it is simpler and less obscure to use a
                    // conditional:
                    // 
                    // if (found.addAll(...)); {
                    //   changed = true;
                    // }
                    // 
                    // Resist the temptation to add an else clause here:
                    // 
                    // if (found.addAll(...)); {
                    //   changed = true;
                    // } else {   // DON'T DO THIS
                    //   changed = false;
                    // }
                    // 
                    // Once you make a single change, then 'changed' should 
                    // stay true until the next iteration of the 'while' loop.
                    // A similar mistake is the following:
                    // 
                    // changed = found.addAll(...); // DON'T
                    // 
                    changed = 
                        found.addAll(firstSets.get(firstRhs))
                        ||
                        changed;
                }
                // To be added: iterate through the rhs of current, until
                // encountering a NonTerminal without epsilon in its
                // firstSets or a Terminal. For each NonTerminal NT on rhs
                // for which all NonTerminals to the left have epsilon
                // in their firstSets, add all of the firstSet of NT 
                // (except epsilon) to the firstSet of current.lhs(). 
                // If everthing on current.rhs() has epsilon in its
                // firstSet, then add epsilon to the firstSet of
                // current.lhs().
            }
        }
    }
  
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("start: " + start.toString() + "\n");
        for (int i = 0; i < rules.size(); i++) {
            b.append(rules.get(i).toString() + "\n");
        }
        return b.toString();
    }

    /**
     * Exception class for readGrammar method.
     */
    public static class GrammarFormatException extends Exception {
        /**
         * Constructor
         * @param msg the exception message
         */
        public GrammarFormatException(String msg) {
            super(msg);
        }
    }

    /**
     * Read a grammar from an input file. The format is a sequence
     * of rules where each rule is of the form:<pre>
     * A --> B1 B2 ... BN
     * </pre>
     * The set of nonterminals is taken from the set of strings
     * on the left hand side of a rule. So in a rule such as<pre>
     * Det --> the
     * </pre>
     * if the string "the" never occurs on the lhs of any rule,
     * it must be a terminal.</P>
     * <P>The start symbol is assumed to be the lhs of the first
     * rule in the grammar file.
     * @param fileName the text file containing the grammar
     * @return the Grammar
     * @throws IOException if there is trouble reading the file
     * @throws Grammar.GrammarFormatException if the rules in 
     *     the file are malformed
     */

    static Grammar readGrammar(String fileName) throws 
        IOException, GrammarFormatException {
        FileReader reader = new FileReader(fileName);

        BufferedReader in = new BufferedReader(reader);
        Set<NonTerminal> nonTerminals = new HashSet<NonTerminal>();
        List<String> lines = new ArrayList<String>();
        List<Rule> rules = new LinkedList<Rule>();
        String inputLine;

        // Make first pass through the file to collect all
        // left hand sides, so that terminals can be 
        // distinguished from nonterminals.
        while (true) {
            inputLine = in.readLine();
            if (inputLine == null) {
                break;
            }
            StringTokenizer tok = new StringTokenizer(inputLine);
            if (!tok.hasMoreTokens()) continue;
            lines.add(inputLine);
            NonTerminal lhs = NonTerminal.valueOf(tok.nextToken());
            nonTerminals.add(lhs);
        }

        NonTerminal lhs;
        for (int i = 0; i < lines.size(); i++) {
            inputLine = (String) lines.get(i);
            StringTokenizer tok = new StringTokenizer(inputLine);
            lhs = NonTerminal.valueOf(tok.nextToken());
            if (!tok.hasMoreTokens()) {
                throw new GrammarFormatException("Missing '-->' and rhs in rule: " 
                                                 + (i+1));        
            } else {
                String nextTok = tok.nextToken();
                if  (!nextTok.equals("-->") && !nextTok.equals("->")) {
                    throw new GrammarFormatException("Missing '-->' in rule: " + (i+1));
                }
            }
            List<Symbol> rhs = new LinkedList<Symbol>();
            while (tok.hasMoreTokens()) {
                String next = tok.nextToken();
                NonTerminal nt = NonTerminal.valueOf(next);
                if (nonTerminals.contains(nt)) {
                    rhs.add(nt); 
                } else {
                    Terminal t = Terminal.valueOf(next);
                    rhs.add(t);
                }
            }
            Rule rule = new Rule(lhs, rhs);
            rules.add(rule);
        }    
        reader.close();
        NonTerminal start = rules.get(0).lhs();
        Grammar g = new Grammar(start, rules);

        return g;
    }




    // A small test using grammar from Grune & Jacobs
    public static void main(String[]  args)
        throws IOException, GrammarFormatException {

    
        List<Symbol> rhs1 = new LinkedList<Symbol>();
        rhs1.add(NonTerminal.Expr);
        rhs1.add(Terminal.plus);
        rhs1.add(NonTerminal.Term);

        Rule r1 = new Rule(NonTerminal.Expr, rhs1);

        List<Symbol> rhs2 = new LinkedList<Symbol>();
        rhs2.add(NonTerminal.Term);
        Rule r2 = new Rule(NonTerminal.Expr, rhs2);

        List<Rule> allRules = new LinkedList<Rule>();
        allRules.add(r1);
        allRules.add(r2);

        List<Symbol> rhs3 = new LinkedList<Symbol>();
        rhs3.add(Terminal.valueOf("4"));
        Rule r3 = new Rule(NonTerminal.Term, rhs3);
        allRules.add(r3);


        Grammar g = new Grammar(NonTerminal.Expr, allRules);

        System.out.println(g.getFirstSet(NonTerminal.Expr));


        System.out.println(g);
    
        System.out.println(g.lookUpRulesLc(NonTerminal.Term));
    
    
        // Define grammar used in chapter 12
        System.out.println(g.lookUpRulesRhs(rhs1));
        System.out.println();
        allRules.clear();
        List<Symbol> rhs = new LinkedList<Symbol>();
        rhs.add(NonTerminal.valueOf("L"));
        rhs.add(NonTerminal.valueOf("S"));
        rhs.add(NonTerminal.valueOf("R"));
        Rule r = new Rule(NonTerminal.S, rhs);
        allRules.add(r);

        // epsilon rule out
        //rhs = new LinkedList();
        //r = new Rule(NonTerminal.S, rhs);
        //allRules.add(r);

        rhs = new LinkedList<Symbol>();
        rhs.add(Terminal.valueOf("("));
        r = new Rule(NonTerminal.valueOf("L"), rhs);
        allRules.add(r);

        //rhs = new LinkedList();
        //r = new Rule(NonTerminal.valueOf("L"), rhs);
        //allRules.add(r);

        rhs = new LinkedList<Symbol>();
        rhs.add(Terminal.valueOf(")"));
        r = new Rule(NonTerminal.valueOf("R"), rhs);
        allRules.add(r);

        g = new Grammar(NonTerminal.S, allRules);
        System.out.println(g);

        System.out.println(g.toInt(r));
        System.out.println(g.nRules());

        System.out.println("===");
        System.out.println(g.addSuperStart());

        System.out.println(g.toNonTerminal(2));

        System.out.println(g.getFirstSet(NonTerminal.S));
    
        System.out.println("+++++++++++++++++");
        /*
          Grammar gx = readGrammar("/afs/sfs/lehre/dg/parsing/grammars/grammar.txt");
          System.out.println(gx);


          System.out.println("SIMPX: " + gx.getFirstSet(NonTerminal.valueOf("SIMPX")));
          System.out.println("C: " + gx.getFirstSet(NonTerminal.valueOf("C")));
          System.out.println("VF: " + gx.getFirstSet(NonTerminal.valueOf("VF")));
          System.out.println("LK: " + gx.getFirstSet(NonTerminal.valueOf("LK")));
          System.out.println("MF: " + gx.getFirstSet(NonTerminal.valueOf("MF")));
          System.out.println("VC: " + gx.getFirstSet(NonTerminal.valueOf("VC")));
          System.out.println("ADJX: " + gx.getFirstSet(NonTerminal.valueOf("ADJX")));
          System.out.println("ADVX: " + gx.getFirstSet(NonTerminal.valueOf("ADVX")));
          System.out.println("NX: " + gx.getFirstSet(NonTerminal.valueOf("NX")));
          System.out.println("PX: " + gx.getFirstSet(NonTerminal.valueOf("PX")));
        */
    }
}