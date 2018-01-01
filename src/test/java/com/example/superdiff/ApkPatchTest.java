package com.example.superdiff;

import org.junit.Test;

import static org.junit.Assert.*;

public class ApkPatchTest {

    @Test
    public void patch() throws Exception {
        ApkPatch.patch("e:/zip/jrtt-old.apk","e:/zip/jrtt-new-diff.zip","e:/zip/jrtt-new2.apk");
    }
}