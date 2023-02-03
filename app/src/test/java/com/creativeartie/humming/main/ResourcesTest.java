package com.creativeartie.humming.main;

import java.io.*;
import java.net.*;

import org.junit.jupiter.api.*;

import com.google.common.io.*;

class ResourcesTest {
    @Test
    void test() {
        URL url = Resources.getResource("data/text.css");
        System.out.println(url.getPath());
        File f = new File(url.getPath());
        Assertions.assertTrue(f.canRead());
    }
}