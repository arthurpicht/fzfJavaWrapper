package de.arthurpicht.fzfJavaWrapper.demo;

import de.arthurpicht.fzfJavaWrapper.Fzf;
import de.arthurpicht.fzfJavaWrapper.FzfParboiled;
import de.arthurpicht.fzfJavaWrapper.exception.InvalidSelectionException;
import de.arthurpicht.utils.core.collection.Lists;

import java.util.List;

public class FzfDemo {

    public static void main(String[] args) {
        List<String> elements = Lists.newArrayList("element1", "element2", "element3");

//        Fzf fzf = Fzf.builder()
//                .withPrompt("Input>")
//                .withTac()
//                .withNoSort()
//                .build();

        Fzf fzf = FzfParboiled.inline20();

        try {
            String result = fzf.execute(elements);
            System.out.println("Selected element: " + result);
        } catch (InvalidSelectionException e) {
            System.out.println(e.getMessage());
        }

    }

}
