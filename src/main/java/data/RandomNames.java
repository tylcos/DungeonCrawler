package data;

import java.util.Random;

public class RandomNames {
    private static final Random RAND = new Random();

    private static final String[] USER_NAMES = new String[] {
        "Bishop Walt",
        "Cardinal Guntard",
        "Chancellor Solomon",
        "Chinda The Mammoth",
        "Count Robert",
        "Emperor Trustram",
        "Graidy Longtail",
        "Jeoddrinyg Lord Of The Red",
        "Orruntig The Dark",
        "Prince Consort Ogier",
        "Prince Tericius",
        "Qekog The Voiceless One",
        "Reeve Emericus",
        "Reeve Galien",
        "Sir Sym",
        "Vicar Maynard",
        "Viscount Audemar",
        "Xonem Lord Of Ice"
    };

    public static String getRandomName() {
        return USER_NAMES[RAND.nextInt(USER_NAMES.length)];
    }
}
