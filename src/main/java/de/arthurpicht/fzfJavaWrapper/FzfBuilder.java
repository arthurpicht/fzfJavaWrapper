package de.arthurpicht.fzfJavaWrapper;

import java.nio.file.Path;

import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNull;

public class FzfBuilder {

    private boolean noSort;
    private boolean tac;
    private Integer heightLines;
    private Integer heightPercent;
    private Boolean layoutReverse;
    private Boolean layoutReverseList;
    private String prompt;
    private Path tempParentDir;

    private Path fzfBinary;

    /**
     * Set the <i>'--no-sort'</i> flag: Do not sort the result.
     *
     * @return current instance of {@link FzfBuilder}
     */
    public FzfBuilder withNoSort() {
        this.noSort = true;
        return this;
    }

    /**
     * Set the <i>'--tac'</i> flag: Reverse the order of the input.
     *
     * @return current instance of {@link FzfBuilder}
     */
    public FzfBuilder withTac() {
        this.tac = true;
        return this;
    }

    /**
     * Set the <i>--height=HEIGHT</i> tag. Display fzf window below the cursor with the given height instead of using
     * the full screen. Specified as number of lines.
     *
     * @param heightLines height as number of lines
     *
     * @return current instance of {@link FzfBuilder}
     */
    public FzfBuilder withHeightLines(int heightLines) {
        if (this.heightPercent != null) throw new IllegalStateException("Height is already defined.");
        this.heightLines = heightLines;
        return this;
    }

    /**
     * Set the <i>--height=HEIGHT</i> tag. Display fzf window below the cursor with the given height instead of using
     * the full screen. Specified as percentage.
     *
     * @param heightPercentage height as a percentage
     *
     * @return current instance of {@link FzfBuilder}
     */
    public FzfBuilder withHeightPercentage(int heightPercentage) {
        if (this.heightLines != null) throw new IllegalStateException("Height is  already defined.");
        this.heightPercent = heightPercentage;
        return this;
    }

    /**
     * Set the <i>--layout=reverse</i> flag. Display from the top of the screen.
     * Default is: Display from the bottom of the screen.
     *
     * @return current instance of {@link FzfBuilder}
     */
    public FzfBuilder withLayoutReverse() {
        if (this.layoutReverseList != null) throw new IllegalStateException("Layout is already defined.");
        this.layoutReverse = true;
        return this;
    }

    /**
     * Set the <i>--layout=reverse-list</i> flag. Display from the top of the screen, prompt at the bottom.
     * Default is: Display from the bottom of the screen.
     *
     * @return current instance of {@link FzfBuilder}
     */
    public FzfBuilder withLayoutReverseList() {
        if (this.layoutReverse != null) throw new IllegalStateException("Layout is already defined.");
        this.layoutReverseList = true;
        return this;
    }

    /**
     * Set the <i>--prompt=STR</i> flag. Input prompt (default: '>').
     *
     * @param prompt prompt string
     *
     * @return current instance of {@link FzfBuilder}
     */
    public FzfBuilder withPrompt(String prompt) {
        assertArgumentNotNull("prompt", prompt);
        this.prompt = prompt;
        return this;
    }

    /**
     * Use specified directory as parent directory for temporary files. Must exist. Default is user home.
     *
     * @param tempParentDir parent directory for temporary files
     * @return current instance of {@link FzfBuilder}
     */
    public FzfBuilder withTempParentDir(Path tempParentDir) {
        assertArgumentNotNull("tempParentDir", tempParentDir);
        this.tempParentDir = tempParentDir;
        return this;
    }

    /**
     * Path to the fzf binary to call. Default is calling fzf from path.
     *
     * @param fzfBinary path to fzf binary
     * @return current instance of {@link FzfBuilder}
     */
    public FzfBuilder withFzfBinary(Path fzfBinary) {
        assertArgumentNotNull("fzfBinary", fzfBinary);
        this.fzfBinary = fzfBinary;
        return this;
    }

    public Fzf build() {
        if (this.layoutReverse == null) this.layoutReverse = false;
        if (this.layoutReverseList == null) this.layoutReverseList = false;

        return new Fzf(
                this.noSort,
                this.tac,
                this.heightLines,
                this.heightPercent,
                this.layoutReverse,
                this.layoutReverseList,
                this.prompt,
                this.tempParentDir,
                this.fzfBinary
        );
    }

}
