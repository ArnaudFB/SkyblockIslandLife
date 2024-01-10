package fr.nono74210.skyblockislandlife.utils.patterns;

import fr.nono74210.skyblockislandlife.utils.language.IridiumColorAPI;

import java.util.regex.Matcher;

public class OldHBPattern implements Pattern {

    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\{#[0-9a-fA-F]{6}}");

    @Override
    public String process(String string) {
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            String hexCode = matcher.group();
            String color = hexCode.substring(2, 8);

            string = string.replace(matcher.group(), IridiumColorAPI.getColor(color) + "");
        }

        return string;
    }
}