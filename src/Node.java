
import java.util.ArrayList;

/**
 * Created by IGOR on 18.05.2015.
 */

class printFix{
    static StringBuffer tab = new StringBuffer("\t");
}
public class Node {
    ArrayList<Node> children = new ArrayList<Node>();
    String type;
    String attribute;
    Node (ArrayList<Node> children, String type, String attribute){
        this.children = children;
        this.type = type;
        this.attribute = attribute;
    }
    public void printTree(){
        System.out.println(printFix.tab + "Type: " + this.type + " Attribute:" + this.attribute);
        System.out.println(printFix.tab + "---------------------------------------------------");
        if (this.children == null){
            return;
        }
        System.out.println(printFix.tab + "Children: ");
        printFix.tab.append("\t\t");
        for (Node node: this.children){
            node.printTree();
        }
        printFix.tab.deleteCharAt(printFix.tab.length() - 1);
        printFix.tab.deleteCharAt(printFix.tab.length() - 1);

    }

}
