import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownParse {



    public static void main(String[] args) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse("this is *Sparta*");
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        renderer.render(document);
    }
}