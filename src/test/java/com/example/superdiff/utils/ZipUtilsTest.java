package com.example.superdiff.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ZipUtilsTest {

    @Test
    public void compress() throws Exception {
        ZipUtils.compress("e:/zip/test", "e:/zip/test2.zip", false);
        //ZipUtils2.compress("e:/zip/test", "e:/zip/test2.zip");
    }

    @Test
    public void decompress() throws Exception {
        ZipUtils.decompress("e:/zip/test.zip");
    }
}