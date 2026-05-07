package com.nic49.bot.manager;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class TranslationManager {
    private final Translate translate = TranslateOptions.getDefaultInstance().getService();

    public String translateText(String text, String targetLang) {
        Translation translation = translate.translate(
                text,
                Translate.TranslateOption.targetLanguage(targetLang)
        );
        return translation.getTranslatedText();
    }
}