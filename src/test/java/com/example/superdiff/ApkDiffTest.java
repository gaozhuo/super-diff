package com.example.superdiff;

import org.junit.Test;

import static org.junit.Assert.*;

public class ApkDiffTest {

    @Test
    public void diff() throws Exception {
        ApkDiff.diff("e:/zip/jrtt-new.apk","e:/zip/jrtt-old.apk","");
    }
}