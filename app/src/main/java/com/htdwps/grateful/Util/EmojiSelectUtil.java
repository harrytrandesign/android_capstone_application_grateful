package com.htdwps.grateful.Util;

/**
 * Created by HTDWPS on 6/6/18.
 */
public class EmojiSelectUtil {

    private static final int EMOJI_ON_FIRE = 0x1F525;
    private static final int EMOJI_HAPPY = 0x1F603;
    private static final int EMOJI_IN_LOVE = 0x1F60D;
    private static final int EMOJI_COOL = 0x1F60E;
    private static final int EMOJI_DEEP_THOUGHTS = 0x1F914;
    private static final int EMOJI_AGONY = 0x1F623;
    private static final int EMOJI_EXHAUSTED = 0x1F62A;
    private static final int EMOJI_NERVOUS = 0x1F613;
    private static final int EMOJI_SADNESS = 0x1F61F;
    private static final int EMOJI_FURIOUS = 0x1F620;
    private static final int EMOJI_POOPED = 0x1F4A9;
    private static final int EMOJI_100 = 0x1F4AF;
    private static final int EMOJI_MONEY = 0x1F4B0;
    private static final int EMOJI_BLESSED = 0x1F47C;

    public static int[] emojiIconCodePoint = new int[]{
            0x1F525, 0x1F603, 0x1F60D, 0x1F60E,
            0x1F914, 0x1F623, 0x1F62A, 0x1F613,
            0x1F61F, 0x1F620, 0x1F4A9, 0x1F4AF,
            0x1F4B0, 0x1F47C, 0x1F625, 0x1F915
    };
    //      0x1F525, 0x1F603, 0x1F60D, 0x1F60E, 0x1F914, 0x1F623, 0x1F62A, 0x1F613, 0x1F61F, 0x1F620, 0x1F4A9, 0x1F4AF, 0x1F4B0, 0x1F47C

    public static String[] emojiExpressionTextValue = new String[]{
            "On Fire",
            "Happy",
            "In Love",
            "Cool",
            "Deep Thoughts",
            "Agony",
            "Exhausted",
            "Nervous",
            "Sad",
            "Furious",
            "Pooped",
            "100%",
            "Cha-Ching",
            "Blessed",
            "Stressed",
            "Hurt"
    };

    public static String[] emojiForSpinnerDropdown = new String[]{
            String.valueOf(Character.toChars(emojiIconCodePoint[0])) + " - " + emojiExpressionTextValue[0],
            String.valueOf(Character.toChars(emojiIconCodePoint[1])) + " - " + emojiExpressionTextValue[1],
            String.valueOf(Character.toChars(emojiIconCodePoint[2])) + " - " + emojiExpressionTextValue[2],
            String.valueOf(Character.toChars(emojiIconCodePoint[3])) + " - " + emojiExpressionTextValue[3],
            String.valueOf(Character.toChars(emojiIconCodePoint[4])) + " - " + emojiExpressionTextValue[4],
            String.valueOf(Character.toChars(emojiIconCodePoint[5])) + " - " + emojiExpressionTextValue[5],
            String.valueOf(Character.toChars(emojiIconCodePoint[6])) + " - " + emojiExpressionTextValue[6],
            String.valueOf(Character.toChars(emojiIconCodePoint[7])) + " - " + emojiExpressionTextValue[7],
            String.valueOf(Character.toChars(emojiIconCodePoint[8])) + " - " + emojiExpressionTextValue[8],
            String.valueOf(Character.toChars(emojiIconCodePoint[9])) + " - " + emojiExpressionTextValue[9],
            String.valueOf(Character.toChars(emojiIconCodePoint[10])) + " - " + emojiExpressionTextValue[10],
            String.valueOf(Character.toChars(emojiIconCodePoint[11])) + " - " + emojiExpressionTextValue[11],
            String.valueOf(Character.toChars(emojiIconCodePoint[12])) + " - " + emojiExpressionTextValue[12],
            String.valueOf(Character.toChars(emojiIconCodePoint[13])) + " - " + emojiExpressionTextValue[13],
            String.valueOf(Character.toChars(emojiIconCodePoint[14])) + " - " + emojiExpressionTextValue[14],
            String.valueOf(Character.toChars(emojiIconCodePoint[15])) + " - " + emojiExpressionTextValue[15]
    };

    public static String expressionTextStringEmojiType(int value) {

        String feeling = "";

        switch (value) {
            case 0:
                feeling = "On Fire";
                break;
            case 1:
                feeling = "Happy";
                break;
            case 2:
                feeling = "In Love";
                break;
            case 3:
                feeling = "Cool";
                break;
            case 4:
                feeling = "Deep Thoughts";
                break;
            case 5:
                feeling = "Agony";
                break;
            case 6:
                feeling = "Exhausted";
                break;
            case 7:
                feeling = "Nervous";
                break;
            case 8:
                feeling = "Sad";
                break;
            case 9:
                feeling = "Furious";
                break;
            case 10:
                feeling = "Pooped";
                break;
            case 11:
                feeling = "100%";
                break;
            case 12:
                feeling = "Cha-Ching";
                break;
            case 13:
                feeling = "Blessed";
                break;
            case 14:
                feeling = "Stressed";
                break;
            case 15:
                feeling = "Hurt";
                break;
        }

        return feeling;
    }

    public static int emojiIconPicker(int value) {

        //        switch (value) {
//
//            case 0:
//
//                emoji = EMOJI_ON_FIRE;
//
//                break;
//
//            case 1:
//
//                emoji = EMOJI_HAPPY;
//
//                break;
//
//            case 2:
//
//                break;
//
//            case 3:
//
//                break;
//
//            case 4:
//
//                break;
//
//            case 5:
//
//                break;
//
//            case 6:
//
//                break;
//
//            case 7:
//
//                break;
//
//            case 8:
//
//                break;
//
//            case 9:
//
//                break;
//
//            case 10:
//
//                break;
//
//            case 11:
//
//                break;
//
//            case 12:
//
//                break;
//
//            case 13:
//
//                break;
//
//
//        },

        return emojiIconCodePoint[value];
    }

}
