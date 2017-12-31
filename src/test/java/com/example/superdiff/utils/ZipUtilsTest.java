package com.example.superdiff.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ZipUtilsTest {

    @Test
    public void compress() throws Exception {
        ZipUtils.compress("e:/zip/111.txt", "e:/zip/111.zip",false);
    }

    @Test
    public void decompress() throws Exception {
        ZipUtils.decompress("e:/zip/test.zip");
    }
}