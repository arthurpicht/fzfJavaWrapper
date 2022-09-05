package de.arthurpicht.fzfJavaWrapper.exception;

public class InvalidSelectionException extends Exception {

    public InvalidSelectionException() {
        super("fzf selection is empty or was aborted by user.");
    }

}
