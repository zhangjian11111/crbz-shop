package cn.lili.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class encodePass {
    public static void main(String[] args) {
        String old = new BCryptPasswordEncoder().encode("12345");

        boolean matches = new BCryptPasswordEncoder().matches("d47eac8c4650f75f1a1a8a6f60e58590", "$2a$10$.WkWuw3FhTG7Y9tZWL//yOytEkalQcVVb1Hp/guCOPVTaf7qKQc7m");
        System.out.println(old);
        System.out.println(matches);
    }
}
