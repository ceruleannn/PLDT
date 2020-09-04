package com.hyhello.priceless.base;

/**
 *
 */
public class RemoveANSIEscape {
    public static void main(String[] args) {
        String s = "    # download-with: ^[[4myou-get --itag=160 [URL]^[[0m\n".replaceAll("\\^\\[\\[[;\\d]*m", "");
        System.out.println(s);
    }
}
