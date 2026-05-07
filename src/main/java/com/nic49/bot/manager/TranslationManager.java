package com.nic49.bot.manager;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import java.io.FileInputStream;

public class TranslationManager {
    private Translate translate;

    public TranslationManager() {
        try {
            // Path to the JSON key you downloaded from Google Cloud Console
            String keyPath = "/home/ubuntu/nic49/gcp-key.json";

            this.translate = TranslateOptions.newBuilder()
                    .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(keyPath)))
                    .build()
                    .getService();

            System.out.println("Translation API Authenticated Successfully!");
        } catch (Exception e) {
            System.err.println("Failed to initialize Translation API: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String translateText(String text, String targetLang) {
        if (translate == null) return "Translation service not initialized.";

        var translation = translate.translate(
                text,
                Translate.TranslateOption.targetLanguage(targetLang)
        );
        return translation.getTranslatedText();
    }
}