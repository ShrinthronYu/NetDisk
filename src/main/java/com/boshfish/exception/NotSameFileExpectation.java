package com.boshfish.exception;

public class NotSameFileExpectation extends Exception {
    public NotSameFileExpectation() {
        super("File MD5 Different");
    }
}