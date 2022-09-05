package de.arthurpicht.fzfJavaWrapper;

public class FzfParboiled {

    public static Fzf fullScreenBottom(String prompt) {
        return Fzf.builder()
                .withPrompt(prompt)
                .withTac()
                .withNoSort()
                .build();
    }

    public static Fzf inline40(String prompt) {
        return Fzf.builder()
                .withPrompt(prompt)
                .withHeightPercentage(40)
                .withLayoutReverse()
                .build();
    }

    public static Fzf inline40() {
        return Fzf.builder()
                .withHeightPercentage(40)
                .withLayoutReverse()
                .build();
    }

    public static Fzf inline20(String prompt) {
        return Fzf.builder()
                .withPrompt(prompt)
                .withHeightPercentage(20)
                .withLayoutReverse()
                .build();
    }

    public static Fzf inline20() {
        return Fzf.builder()
                .withHeightPercentage(20)
                .withLayoutReverse()
                .build();
    }

}
