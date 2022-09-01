package de.arthurpicht.fzfJavaWrapper;

import de.arthurpicht.utils.core.collection.Lists;

import java.util.List;

public class FzfDemo {

    public static void main(String[] args) {
        List<String> elements = Lists.newArrayList("element1", "element2", "element3");

        Fzf fzf = Fzf.builder()
                .withPrompt("Input>")
                .withTac()
                .withNoSort()
                .build();

        String result = fzf.execute(elements);
        System.out.println("Your choice: " + result);
    }

}
