package com.xhzb.nursing.test;

import cn.hutool.crypto.digest.BCrypt;

public class PasswordTest {

    public static void main(String[] args) {
        String gensalt = BCrypt.gensalt();
        String password = BCrypt.hashpw("admin123", gensalt);
        System.out.println(password);
    }
}
