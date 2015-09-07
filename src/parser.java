
import java.util.Map;
import java.util.Vector;
import java.util.ArrayList;

/**
 * Created by IGOR on 16.05.2015.
 */
public class parser {
    String content;
    Vector<Vector<Integer>> stringOfKeys = new Vector<Vector<Integer>>();
    Vector<Vector<String>> stringOfTokens = new Vector<Vector<String>>();

    Map keywords;
    Map delimiters;
    Map digit;
    Map identifier;
    Map error;
    boolean start = true;
    ArrayList<Node> childrenStatementList = new ArrayList<Node>();

    private static Integer TS, i = 0, j = 0, i1 = 0, j1 = 0;

    parser(Vector<Vector<String>> stringOfTokens, String content, Map keywords, Map delimiters, Map digit, Map identifier, Map error) {
        this.content = content;
        this.delimiters = delimiters;
        this.digit = digit;
        this.identifier = identifier;
        this.keywords = keywords;
        this.error = error;
        this.stringOfTokens = stringOfTokens;
        for (int i = 0; i < stringOfTokens.size(); i++) {
            this.stringOfKeys.add(new Vector<Integer>());
            for (int j = 0; j < stringOfTokens.elementAt(i).size(); j++) {
                detectThisToken(identifier, stringOfTokens.elementAt(i).elementAt(j), i);
                detectThisToken(delimiters, stringOfTokens.elementAt(i).elementAt(j), i);
                detectThisToken(keywords, stringOfTokens.elementAt(i).elementAt(j), i);
                detectThisToken(digit, stringOfTokens.elementAt(i).elementAt(j), i);
                detectThisToken(error, stringOfTokens.elementAt(i).elementAt(j), i);
            }
        }
    }
    private void detectThisToken(Map table, String token, int i){
        if (table.containsKey(token)){
            this.stringOfKeys.elementAt(i).add((Integer) table.get(token));
        }
    }
    private void SCN(){
//        j1 = j;
//        i1 = i;
        if (start == false) j++;
        if (i < stringOfKeys.size()){
            if (j <= stringOfKeys.elementAt(i).size()){
                TS = stringOfKeys.elementAt(i).elementAt(j);
                start = false;
                if ((j == stringOfKeys.elementAt(i).size()-1)){
                    i++;
                    j = 0;
                    start = true;
                }
            }
        }
    }
    private void error(String causing){
        System.out.println("error at " + (i +1)  + ":" + (j+1));
        System.out.println(causing);
        System.exit(0);
    }
    private Node block(){
        ArrayList<Node> childrenList = new ArrayList<Node>();

        childrenList.add(declarations());
        if (TS != 401) error("BEGIN missed");
        SCN();
        childrenList.add(statements_list());
        if (TS != 402) error("END missed");
        else SCN();
        Node thisNode = new Node(childrenList, "<block>", null);
        return thisNode;
    }
    private Node declarations(){
        ArrayList<Node> childrenList = new ArrayList<Node>();

        childrenList.add(constant_declarations());
        Node thisNode = new Node(childrenList, "<declarations>", null);
        return thisNode;
    }
    private Node statements_list(){
        Node thisNode = new Node(null, "<statement-list>", "null");
        return thisNode;
    }
    private Node constant_identifier(){
        ArrayList<Node> childrenList = new ArrayList<Node>();

        childrenList.add(identifier());
        Node thisNode = new Node(childrenList, "<constant-identifier>", null);
        return thisNode;
    }
    private Node variable_identifier(){
        ArrayList<Node> childrenList = new ArrayList<Node>();

        childrenList.add(identifier());
        Node thisNode = new Node(childrenList, "<variable-identifier>", null);
        return thisNode;
    }
    private Node procedure_identifier(){
        ArrayList<Node> childrenList = new ArrayList<Node>();

        childrenList.add(identifier());
        Node thisNode = new Node(childrenList, "<procedure-identifier>", null);
        return thisNode;
    }
    private Node constant_declarations(){
        ArrayList<Node> childrenList = new ArrayList<Node>();

        Node thisNode = new Node(null, "<constants-declarations>", null);
        if (TS != 404) error("CONST is missed");
        else {
            SCN();
//            while (TS != 401){
            childrenList.add(constant_declarations_list());
            thisNode = new Node(childrenList, "<constants-declarations>", null);
//            }
        }
        return thisNode;
    }
    private Node constant_declarations_list(){
        ArrayList<Node> childrenList = new ArrayList<Node>();
        childrenList.add(constant_declaration());
        Node thisNode = new Node(childrenList, "<constants-declarations-list>", null);
        if (TS != 401) {
            childrenList.add(constant_declarations_list());
            thisNode = new Node(childrenList, "<constants-declarations-list>", null);
        }
        return thisNode;
    }
    private Node constant_declaration(){
        ArrayList<Node> childrenList = new ArrayList<Node>();

        childrenList.add(constant_identifier());
        if (TS != 2) error("= missed");
        else{
            SCN();
            childrenList.add(constant());
            if (TS != 3) error("; missed");
            else SCN();
        }
        Node thisNode = new Node(childrenList, "<constants-declaration>", null);
        return thisNode;
    }
    private Node identifier(){
        Node thisNode = new Node(null, "<identifier>", stringOfTokens.elementAt(i).elementAt(j));
        if (!identifier.containsValue(TS)) error("identifier missed");
        SCN();
        return thisNode;
    }
    private Node constant() {
        ArrayList<Node> childrenList = new ArrayList<Node>();
        childrenList.add(unsigned_integer());
        Node thisNode = new Node(childrenList, "<constant>", null);
        return thisNode;
    }
    private Node unsigned_integer(){
        Node thisNode = new Node(null, "<unsigned-integer>", stringOfTokens.elementAt(i).elementAt(j));
        if (!digit.containsValue(TS)) error("digit missed");
        SCN();
        return thisNode;
    }
    private Node parameters_list(){
        ArrayList<Node> childrenList = new ArrayList<Node>();

        Node thisNode = new Node(null, "<parameters-list>", null);
        if (TS == 3){
            return thisNode;
        }else{
            if (TS != 0) error("( missed");
            SCN();
//            if (TS != 1) {
            childrenList.add(declarations_list());
//            }
            thisNode = new Node (childrenList, "<parameters-list>", null);
            if (TS != 1) error(") missed");
            else SCN();
            return thisNode;
        }
    }
    private Node declarations_list(){
        ArrayList<Node> childrenList = new ArrayList<Node>();

        Node thisNode = new Node(null, "<declarations-list>", null);
        if (TS == 1){
            return thisNode;
        }else {
            childrenList.add(declaration());
            if (TS == 3) {
                SCN();
                childrenList.add(declarations_list());
            }
            thisNode = new Node(childrenList, "<declarations-list>", null);
            return thisNode;
        }
    }
    private Node declaration(){
        ArrayList<Node> childrenList = new ArrayList<Node>();

        childrenList.add(variable_identifier());
        if (TS != 4) error(": missed");
        else {
            SCN();
            childrenList.add(attribute());
        }
        Node thisNode = new Node(childrenList, "<declaration>", null);
        return thisNode;
    }
    private Node attribute(){
        Node thisNode = new Node(null, "<attribute>", stringOfTokens.elementAt(i).elementAt(j));
        if ((TS != 405) && (TS !=406)){
            error("type missed");
        }else{
            SCN();
        }
        return thisNode;
    }
    Node program(){
        ArrayList<Node> childrenList = new ArrayList<Node>();

        SCN();
        if (TS != 403){
            error("PROCEDURE missed");
        }else{
            SCN();
            childrenStatementList.add(procedure_identifier());
            childrenList.clear();
//            if (TS != 3){
            childrenStatementList.add(parameters_list());
            if (TS != 3) error("; missed");
            else SCN();
//            } else{
//            SCN();
            }
            childrenStatementList.add(block());
            if (TS != 3) error("; missed");
            Node thisNode = new Node(childrenStatementList, "<program>", null);
            return thisNode;

    }
}
