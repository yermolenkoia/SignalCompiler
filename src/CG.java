import java.util.ArrayList;

/**
 * Created by IGOR on 25.05.2015.
 */
public class CG {
    public static ArrayList<String> listing = new ArrayList<String>();
    private static ArrayList<String> reservedIdentifiers = new ArrayList<String>();
//    ArrayList<String> registers = new ArrayList<String>();
    String [] registers = {"EAX", "ECX", "EBX"};
    Integer i = 0;
    private Node tree;

    CG(Node tree){
        this.tree = tree;
 /*       registers.add("EAX");
        registers.add("ECX");
        registers.add("EBX");*/
    }
    public void program(){
        listing.add(".386");
        listing.add("DATA SEGMENT USE16");
        block(tree.children.get(2));
        listing.add("DATA ENDS");
        procedure_identifier(tree.children.get(0));
//        parameters_list(tree.children.get(1), false);
        listing.add("CODE SEGMENT");
        listing.add("NOP");
        listing.add("CODE ENDS");
    }
    public void procedure_identifier(Node procedureIdentifier){
        procId(procedureIdentifier.children.get(0));
    }
    public void parameters_list(Node parametersList, boolean inProc){
        declarations_list(parametersList.children.get(0), inProc);
    }
    public void block(Node block){
        declarations((block.children.get(0)));
    }
    public String identifierForConst(Node ifc){
        if (reservedIdentifiers.contains(ifc.attribute)){
            error(ifc.attribute + " is already defined!");
        }else {
            reservedIdentifiers.add(ifc.attribute);
        }
        return ifc.attribute;
    }
    public void procId(Node procId){
        reservedIdentifiers.add(procId.attribute);
        listing.add(procId.attribute + " PROC");
        parameters_list(tree.children.get(1), true);

        listing.add("RET");
        listing.add(procId.attribute + " ENDP");



    }
    public void identifier(Node identifier, boolean procIn) {
        if (reservedIdentifiers.contains(identifier.attribute)) {
            error(identifier.attribute + "is already defined!");
        }
        reservedIdentifiers.add(identifier.attribute);
        if (procIn == true) {
            listing.add("POP " + registers[i]);
            if ((i+1) != registers.length){
                i++;
            }

        }else {
            listing.add(identifier.attribute + " DD 0");

        }
    }
    public void declarations_list(Node declarationsList, boolean procIn){
//        declaration(declarationsList.children.get(0)); //here
        try {
            declarations_list(declarationsList.children.get(1), procIn);
        }catch (Exception e){}
        declaration(declarationsList.children.get(0), procIn); //here

    }
    public void declaration(Node declaration, boolean procIn){
        variable_identifier(declaration.children.get(0), procIn);
    }
    public void variable_identifier(Node variableIdentifier, boolean procIn){
        identifier(variableIdentifier.children.get(0), procIn);
    }
    public void declarations(Node declarations){
        constants_declarations(declarations.children.get(0));
    }
    public void constants_declarations(Node constantsDeclarations){
        constants_declarations_list(constantsDeclarations.children.get(0));
    }
    public void constants_declarations_list(Node constantsDeclarationsList){
        constants_declaration(constantsDeclarationsList.children.get(0));
        try{
            constants_declarations_list(constantsDeclarationsList.children.get(1));
        }catch (Exception e){}
    }
    public void constants_declaration(Node constantsDeclaration){
        listing.add(constant_identifier(constantsDeclaration.children.get(0)) + " EQU " + constant((constantsDeclaration.children.get(1))));
    }
    public String constant_identifier(Node constantIdentifier){
       return identifierForConst(constantIdentifier.children.get(0));
    }
    public String constant(Node constant){
        return unsigned_integer(constant.children.get(0));
    }
    public String unsigned_integer(Node unsignedInteger){
        return unsignedInteger.attribute;
    }
    public void statement_list(Node statementList){

    }

    private void error(String tmp){
        System.out.println("error");
        System.out.println(tmp);
        System.exit(0);
    }
    public void printListing(){
        for (String i:listing){
            System.out.println(i);
        }
    }
}
