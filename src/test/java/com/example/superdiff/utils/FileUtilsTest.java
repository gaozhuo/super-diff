package com.example.superdiff.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileUtilsTest {

    @Test
    public void move() throws Exception {
        FileUtils.move("e:\\zip\\jrtt-old\\assets\\ams-spi-services\\com.alibaba.sdk.android.ams.common.spi.FactoryProvider",
                "e:\\zip\\jrtt-new-diff\\assets\\ams-spi-services\\com.alibaba.sdk.android.ams.common.spi.FactoryProvider");
    }
}