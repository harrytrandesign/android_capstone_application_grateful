package com.htdwps.grateful.Util;

/**
 * Created by HTDWPS on 6/6/18.
 */
public class EmojiSelectUtil {

    private static final String EMOJI_ON_FIRE_STRING = "On Fire";
    private static final String EMOJI_HAPPY_STRING = "Happy";
    private static final String EMOJI_IN_LOVE_STRING = "In Love";
    private static final String EMOJI_COOL_STRING = "Cool";
    private static final String EMOJI_DEEP_THOUGHTS_STRING = "Deep Thoughts";
    private static final String EMOJI_AGONY_STRING = "Agony";
    private static final String EMOJI_EXHAUSTED_STRING = "Exhausted";
    private static final String EMOJI_NERVOUS_STRING = "Nervous";
    private static final String EMOJI_SAD_STRING = "Sad";
    private static final String EMOJI_FURIOUS_STRING = "Furious";
    private static final String EMOJI_POOPED_STRING = "Pooped";
    private static final String EMOJI_HUNDRED_STRING = "100%";
    private static final String EMOJI_CHACHING_STRING = "Cha-Ching";
    private static final String EMOJI_BLESSED_STRING = "Blessed";
    private static final String EMOJI_STRESSED_STRING = "Stressed";
    private static final String EMOJI_HURT_STRING = "Hurt";

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

    // Values: 0x1F525, 0x1F603, 0x1F60D, 0x1F60E, 0x1F914, 0x1F623, 0x1F62A, 0x1F613, 0x1F61F, 0x1F620, 0x1F4A9, 0x1F4AF, 0x1F4B0, 0x1F47C
    public static int[] emojiIconCodePoint = new int[]{
            0x1F525, 0x1F603, 0x1F60D, 0x1F60E,
            0x1F914, 0x1F623, 0x1F62A, 0x1F613,
            0x1F61F, 0x1F620, 0x1F4A9, 0x1F4AF,
            0x1F4B0, 0x1F47C, 0x1F625, 0x1F915
    };

    public static String[] emojiExpressionTextValue = new String[]{
            EMOJI_ON_FIRE_STRING,
            EMOJI_HAPPY_STRING,
            EMOJI_IN_LOVE_STRING,
            EMOJI_COOL_STRING,
            EMOJI_DEEP_THOUGHTS_STRING,
            EMOJI_AGONY_STRING,
            EMOJI_EXHAUSTED_STRING,
            EMOJI_NERVOUS_STRING,
            EMOJI_SAD_STRING,
            EMOJI_FURIOUS_STRING,
            EMOJI_POOPED_STRING,
            EMOJI_HUNDRED_STRING,
            EMOJI_CHACHING_STRING,
            EMOJI_BLESSED_STRING,
            EMOJI_STRESSED_STRING,
            EMOJI_HURT_STRING
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

    public static int emojiStringConvertToInt(String name) {

        switch (name) {
            case EMOJI_ON_FIRE_STRING:
                return 0;
            case EMOJI_HAPPY_STRING:
                return 1;
            case EMOJI_IN_LOVE_STRING:
                return 2;
            case EMOJI_COOL_STRING:
                return 3;
            case EMOJI_DEEP_THOUGHTS_STRING:
                return 4;
            case EMOJI_AGONY_STRING:
                return 5;
            case EMOJI_EXHAUSTED_STRING:
                return 6;
            case EMOJI_NERVOUS_STRING:
                return 7;
            case EMOJI_SAD_STRING:
                return 8;
            case EMOJI_FURIOUS_STRING:
                return 9;
            case EMOJI_POOPED_STRING:
                return 10;
            case EMOJI_HUNDRED_STRING:
                return 11;
            case EMOJI_CHACHING_STRING:
                return 12;
            case EMOJI_BLESSED_STRING:
                return 13;
            case EMOJI_STRESSED_STRING:
                return 14;
            case EMOJI_HURT_STRING:
                return 15;
            default:
                return 0;
        }
    }

    public static String emojiIntConvertToString(int value) {

        String feeling = "";

        switch (value) {
            case 0:
                feeling = EMOJI_ON_FIRE_STRING;
                break;
            case 1:
                feeling = EMOJI_HAPPY_STRING;
                break;
            case 2:
                feeling = EMOJI_IN_LOVE_STRING;
                break;
            case 3:
                feeling = EMOJI_COOL_STRING;
                break;
            case 4:
                feeling = EMOJI_DEEP_THOUGHTS_STRING;
                break;
            case 5:
                feeling = EMOJI_AGONY_STRING;
                break;
            case 6:
                feeling = EMOJI_EXHAUSTED_STRING;
                break;
            case 7:
                feeling = EMOJI_NERVOUS_STRING;
                break;
            case 8:
                feeling = EMOJI_SAD_STRING;
                break;
            case 9:
                feeling = EMOJI_FURIOUS_STRING;
                break;
            case 10:
                feeling = EMOJI_POOPED_STRING;
                break;
            case 11:
                feeling = EMOJI_HUNDRED_STRING;
                break;
            case 12:
                feeling = EMOJI_CHACHING_STRING;
                break;
            case 13:
                feeling = EMOJI_BLESSED_STRING;
                break;
            case 14:
                feeling = EMOJI_STRESSED_STRING;
                break;
            case 15:
                feeling = EMOJI_HURT_STRING;
                break;
        }

        return feeling;
    }

}
