package com.project.CyShare.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class PasswordGenerator
{
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";

    PasswordGenerator() {}

    public String generate(int length)
    {
        if (length <= 0)
        {
            return "";
        }

        StringBuilder password = new StringBuilder(length);
        Random random = new Random(System.nanoTime());

        List<String> characters = new ArrayList<>(3);
        characters.add(LOWERCASE);
        characters.add(UPPERCASE);
        characters.add(NUMBERS);
        for (int i = 0; i < length; i++)
        {
            String character = characters.get(random.nextInt(characters.size()));
            int position = random.nextInt(character.length());
            password.append(character.charAt(position));
        }

        return new String(password);
    }

}


