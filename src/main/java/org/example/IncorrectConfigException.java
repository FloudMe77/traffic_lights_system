package org.example;

public class IncorrectConfigException extends Exception{
    private final String message;
    public IncorrectConfigException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("Config is not correct, %s", message);
    }
}
