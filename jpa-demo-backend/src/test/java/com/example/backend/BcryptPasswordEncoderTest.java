package com.example.backend;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptPasswordEncoderTest {

    public static void main(String[] args) {
	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	    String inputText = "johndoe@user_P@ssw0rd";
	    String encodedString = encoder.encode(inputText);
	    System.out.println("Encoding '" + inputText + "' ==> " + encodedString);
    }
}
