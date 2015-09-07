
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        types.setKeywords();
        types.setDelimiters();
        try {
            String content = new String(Files.readAllBytes(Paths.get("output.txt")));
            lexer scanner = new lexer(content);
            System.out.println("\n");
            parser pars = new parser(scanner.s, content, types.keywords, types.delimiters, types.digit, types.identifier, types.error);
            pars.program().printTree();
            CG codeGenerator = new CG(pars.program());
            codeGenerator.program();
            codeGenerator.printListing();
        } catch (IOException e){
            System.out.println("File not found");
        }
    }
}

