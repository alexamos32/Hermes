package ca.unb.mobiledev.hermes;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownRender {

    MarkdownRender(){

    }

    public String render(String input){
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        Node document = parser.parse(input == null? "" : input);
        return renderer.render(document);
    }
}
