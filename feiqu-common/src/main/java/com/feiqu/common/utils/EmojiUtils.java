package com.feiqu.common.utils;

import com.vdurmont.emoji.EmojiParser;

/**
 * Created by chenweidong on 2018/1/10.
 */
public class EmojiUtils {

//    private static final Logger logger = LoggerFactory.getLogger(EmojiUtils.class);

    private EmojiUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Replaces the emoji's unicode occurrences by one of their alias
     * (between 2 ':').
     *
     * @param input the string to parse
     *
     * @return the string with the emojis replaced by their alias.
     */
    public static String toAliases(String input) {
        return EmojiParser.parseToAliases(input);
    }

    /**
     * Replaces the emoji's aliases (between 2 ':') occurrences and the html
     * representations by their unicode.
     *
     * @param input the string to parse
     *
     * @return the string with the emojis replaced by their alias.
     */
    public static String toUnicode(String input) {
        return EmojiParser.parseToUnicode(input);
    }


}
