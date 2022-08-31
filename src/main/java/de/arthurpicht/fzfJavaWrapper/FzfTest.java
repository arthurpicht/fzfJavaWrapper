package de.arthurpicht.fzfJavaWrapper;

import de.arthurpicht.utils.core.collection.Lists;

import java.io.IOException;
import java.util.List;

public class FzfTest {

    public static void main(String[] args) throws IOException {
        List<String> elements = Lists.newArrayList("element1", "element2", "element3");


        Fzf fzf = new Fzf();
        String result = fzf.execute(elements);
    }

}
