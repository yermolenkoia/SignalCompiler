
import java.util.Vector;

public class lexer {
    private String content;
    public Vector<Vector<String>> s;
    lexer(String content){
        try {
            this.content = content;
            s = new Vector<Vector<String>>();
            System.out.print(content);
            scanner();
            System.out.println();
            types.identAll(s);
        }catch (StringIndexOutOfBoundsException e){
            types.identAll(s);
            System.out.println();
            System.out.print("Comment error");
        }

    }
    public void scanner(){
        int i=0, k=0;
        boolean endOfProgram = false, endOfLine = false, comment = false;
        String tmp = "";
        while (endOfProgram == false) {
            s.add(new Vector<String>());

            while (endOfLine == false) {
                while (((content.charAt(k) != ' ') && (comment == false) ) &&
                        ((content.charAt(k)) != ((char)13)) && ((content.charAt(k)) != ((char)10)) && ((content.charAt(k)) != ((char)9)))
                {
                    if ((content.charAt(k) == '*') && (content.charAt(k+1) == '<')){
                        comment = true;
                    }
                    if (comment==true)
                        break;
                    if (types.containsInDelimiters(content.charAt(k)) != true) {
                        tmp += (content.charAt(k++));
                        if (k >= content.length())
                            break;
                    }else{
                        if (tmp.equals("END")){
                            endOfLine = true;
                            endOfProgram = true;
                        }
                        if (tmp != ""){
                            //tmp = tmp.toUpperCase();
                            s.elementAt(i).add(tmp);
                        }
                        tmp = "" + content.charAt(k++);
                        if (k >= content.length())
                            break;
                        if (tmp != "") {
                            //tmp =tmp.toUpperCase();
                            s.elementAt(i).add(tmp);
                        }
                        tmp = "";
                    }
                }
                //tmp = tmp.toUpperCase();
                if (k >= content.length()) {
                    s.elementAt(i).add(tmp);
                    endOfProgram = true;
                    break;
                }
                if (tmp != "")
                    s.elementAt(i).add(tmp);
                tmp = "";
                if (((content.charAt(k)) == ((char)13)) && (s.elementAt(i).size() != 0)){ //|| ((content.charAt(k)) == ((char)10)) || ((content.charAt(k)) == ((char)9))){
                    endOfLine = true;
                }
                k++;
                if ((content.charAt(k-1) == '>') && (content.charAt(k) == '*')) {
                    comment = false;
                    k = k + 2;
                }
                if (k >= content.length())
                    break;
            }
            endOfLine = false;
            i++;

        }
    }
}
