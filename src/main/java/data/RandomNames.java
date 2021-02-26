package data;

import java.util.Random;

public class RandomNames {
    private static final Random rand = new Random();

    private static final String[] userNames = new String[]{
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
            "Xonem Lord Of Ice",
    };


    public static String getRandomName() {
        return userNames[rand.nextInt(userNames.length)];
    }
}
