package com.translation;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
