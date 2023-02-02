package com.translation;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.cloud.translate.v3.GlossaryName;
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextGlossaryConfig;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.TranslationServiceClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TranslationController {

    @PostMapping("/translate")
    public String translate(@RequestBody String input){
        Translate translate = TranslateOptions.getDefaultInstance().getService();
        Translation translation = translate.translate(
                input,
                Translate.TranslateOption.sourceLanguage("en"),
                Translate.TranslateOption.targetLanguage("de"));
        System.out.println(translation.getTranslatedText());
        return translation.getTranslatedText();
    }



    @PostMapping("/translate-with-glossary")
    // Translates a given text using a glossary.
    public String translateTextWithGlossary(@RequestBody String input)
            throws IOException {

        String projectId = "532750601276";
        String sourceLanguage = "en";
        String targetLanguage = "fr";
        String glossaryId = "glossary-2";
        String result = "";
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            // Supported Locations: `global`, [glossary location], or [model location]
            // Glossaries must be hosted in `us-central1`
            // Custom Models must use the same location as your model. (us-central1)
            String location = "us-central1";
            LocationName parent = LocationName.of(projectId, location);

            GlossaryName glossaryName = GlossaryName.of(projectId, location, glossaryId);
            TranslateTextGlossaryConfig glossaryConfig =
                    TranslateTextGlossaryConfig.newBuilder().setGlossary(glossaryName.toString()).build();

            // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
            TranslateTextRequest request =
                    TranslateTextRequest.newBuilder()
                            .setParent(parent.toString())
                            .setMimeType("text/plain")
                            .setSourceLanguageCode(sourceLanguage)
                            .setTargetLanguageCode(targetLanguage)
                            .addContents(input)
                            .setGlossaryConfig(glossaryConfig)
                            .build();

            TranslateTextResponse response = client.translateText(request);

            // Display the translation for each input text provided
            for (com.google.cloud.translate.v3.Translation translation : response.getGlossaryTranslationsList()) {
                String translatedText = translation.getTranslatedText();
                System.out.printf("Translated text: %s\n", translation.getTranslatedText());
                result = result + translatedText;
            }
        }
        return result;
    }



}
