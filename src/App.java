import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;

public class App {
    public static void main(String[] args) throws Exception {

        String caminho = "jquery-3.6.0.js";
        String arqString = readLineByLineJava11(caminho);
        String semComentario = arqString.replaceAll("(?s:/\\*.*?\\*/)|//.*", "");
        String semEspaco = semComentario.replaceAll("(?s)\\s+|/\\*.*?\\*/|//[^\\r\\n]*", " ");
        String ajuda = semEspaco
                .replaceAll("(?<=.)(\\,|\\\"|\\(|\\)|\\'|\\;|\\.|\\{|\\}|\\:|\\++|\\-|\\!|\\?|\\[|\\])(|[a-z])", " ");
       
                List<Token> lstToken = new ArrayList<Token>();

        // Quebrar string em tokens
        StringTokenizer st = new StringTokenizer(ajuda);


        //Instanciar token
        Token token;

        int contador = -1;
        while (st.hasMoreTokens()) {
            contador++;
            String x = st.nextToken();
            token = new Token();
            token.id = contador;
            token.value = x;
            token.type = validaString(x);
            lstToken.add(token);
            System.out.println(token.getId() + ", " + token.getValue() + ", " + token.getType() + ", " + "\n");
        }

        try (PrintWriter saida = new PrintWriter("saida.csv")) {
            for (int i = 0; i < lstToken.size(); i++) {
                token = new Token();

                token = lstToken.get(i);

                String linha = token.getId() + ", " + token.getValue() + ", " + token.getType() + ", " + "\n";

                saida.println(linha);
            }
        }
    
    }

    private static String validaString(String x) {
        String[] palavrasResevadas = { "window", "document", "use", "strict", "abstract", "arguments", "await",
                "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "debugger",
                "default", "delete", "do", "double", "else", "enum", "eval", "export", "extends", "false", "final",
                "finally", "float", "for", "function", "goto", "if", "implements", "import", "in", "instanceof", "int",
                "interface", "let", "long", "native", "new", "null", "package", "private", "protected", "public",
                "return", "short", "static", "super", "switch", "synchronized", "this", "throw", "throws", "transient",
                "true", "try", "typeof", "var", "void", "volatile", "while", "with", "yield" };
        String[] comparador = { "==", "===", "!==", "!=", ">", "=>", "=<", "<", "!", "&&", "||", "?" };
        String[] operador = { "+", "-", "++", "*", "/", "=" };

        for (String a : palavrasResevadas) {
            if (a.equals(x))
                return "palavra-reservada";
        }

        for (String a : comparador) {
            if (a.equals(x))
                return "comparador";
        }

        for (String a : operador) {
            if (a.equals(x))
                return "operador";
        }

        if (x.matches("[0-9]\\.[0-9]"))
            return "float";

        if (x.matches("[0-9]"))
            return "inteiro";

        if (x.matches("(true|false)"))
            return "boolean";

        if (x.matches(
                "(?i)string|int|array|date|list|JSON|float|boolean|var|null|undefined|function|bigint|char|symbol|math"))
            return "tipo-de-dado";

        if (x.matches(".$"))
            return "char";

        if (x.matches("[a-z][a-z]+[A-Z][a-z]+"))
            return "funcao";

        if (x.matches("[a-z][A-Z][a-z]+"))
            return "variavel";

        return "string";
    }


    private static String readLineByLineJava11(String caminho) {
        StringBuilder conteudoSB = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(caminho), StandardCharsets.UTF_8)) {
            stream.forEach(s -> conteudoSB.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            ;
        }
        return conteudoSB.toString();
    }
}
