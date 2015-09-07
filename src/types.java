import java.util.*;

public class types {
    public static Map keywords = new HashMap();
    public static Map delimiters = new HashMap();
    public static Map digit = new HashMap();
    public static Map identifier = new HashMap();
    public static Map error = new HashMap();

    private static Integer diapasonForDigit = 501;
    private static Integer diapasonForIdentifier = 1001;
    private static Integer diapasonForError = 1501;

    public static void setKeywords() {
        keywords.put("BEGIN", 401);
        keywords.put("END", 402);
        keywords.put("PROCEDURE", 403);
        keywords.put("CONST", 404);
        keywords.put("FLOAT", 405);
        keywords.put("INTEGER", 406);
    }
    public static void setDelimiters() {
        delimiters.put("(", 0);
        delimiters.put(")", 1);
        delimiters.put("=", 2);
        delimiters.put(";", 3);
        delimiters.put(":", 4);
    }

    public static Boolean containsInDelimiters (char symbol){
        String searchKey = "" + symbol;
        if (delimiters.containsKey(searchKey))
            return true;
        return false;
    }
    private static Boolean isItDigit(String unk) {
        try{
            double value = Double.parseDouble(unk);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    private static void addToDigit(String key){
        digit.put(key, diapasonForDigit);
        diapasonForDigit++;
    }
    private static void addToIdentifier(String key){
        identifier.put(key, diapasonForIdentifier);
        diapasonForIdentifier++;
    }
    private static void addToError(String key){
        error.put(key, diapasonForError);
        diapasonForError++;
    }
    private static Boolean isItError(String unk){
        int i, size = 0;
        String tmp = "";
        for (i = 48;i <= 57; i++ ){
            tmp = "" + ((char)i);
            if (unk.contains(tmp)){
                size++;
            }
        }
        for (i = 65;i <= 90; i++){
            tmp = "" + ((char)i);
            if (unk.contains(tmp)){
                size++;
            }
        }
        tmp = "" + ((char)95);
        String str = "" + unk.charAt(0);
        if (isItDigit(str)) return true;
        if (unk.contains(tmp)){
            size++;
        }
        if (size == unk.length()){
            return false;
        } else {
            return true;
        }
    }

    public static void identAll(Vector<Vector<String>> text){
        for (int i = 0; i < text.size(); i++){
            for (int j = 0; j < text.elementAt(i).size(); j++){
                if (keywords.containsKey(text.elementAt(i).elementAt(j))){
                    System.out.println("keyword, " + keywords.get(text.elementAt(i).elementAt(j)) + ": " + text.elementAt(i).elementAt(j));
                }else{
                    if (delimiters.containsKey(text.elementAt(i).elementAt(j))){
                        System.out.println("delimiter, " + delimiters.get(text.elementAt(i).elementAt(j)) + ": " + text.elementAt(i).elementAt(j));
                    }else{
                        if (isItDigit(text.elementAt(i).elementAt(j))){
                            if (digit.containsKey(text.elementAt(i).elementAt(j))){
                                System.out.println("digit, " + digit.get(text.elementAt(i).elementAt(j)) + ": " + text.elementAt(i).elementAt(j));
                            }else {
                                addToDigit(text.elementAt(i).elementAt(j));
                                System.out.println("digit, " + digit.get(text.elementAt(i).elementAt(j)) + ": " + text.elementAt(i).elementAt(j));
                            }
                        }else{
                            if (isItError(text.elementAt(i).elementAt(j))){
                                addToError(text.elementAt(i).elementAt(j));
                                System.out.println("error: " + text.elementAt(i).elementAt(j));
                            }else {
                                if (identifier.containsKey(text.elementAt(i).elementAt(j))) {
                                    System.out.println("identifier, " + identifier.get(text.elementAt(i).elementAt(j)) + ": " + text.elementAt(i).elementAt(j));
                                } else {
                                        addToIdentifier(text.elementAt(i).elementAt(j));
                                        System.out.println("identifier, " + identifier.get(text.elementAt(i).elementAt(j)) + ": " + text.elementAt(i).elementAt(j));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
