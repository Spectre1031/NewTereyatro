package com.example.myapplication.data

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslationRepository @Inject constructor() {

    /**
     * @param text       The source text (in English or Tagalog).
     * @param targetLang "en" or "tl" — the language you want back.
     */
    suspend fun translate(text: String, targetLang: String): String {

        val sourceCode = if (targetLang == "en")
            TranslateLanguage.TAGALOG
        else
            TranslateLanguage.ENGLISH

        val targetCode = if (targetLang == "en")
            TranslateLanguage.ENGLISH
        else
            TranslateLanguage.TAGALOG


        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceCode)
            .setTargetLanguage(targetCode)
            .build()

        val translator = Translation.getClient(options)


        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        return try {
            translator.downloadModelIfNeeded(conditions).await()
            translator.translate(text).await()
        } catch (e: Exception) {
            Log.w("TranslationRepo", "Translation failed, returning original", e)
            text
        } finally {
            translator.close()
        }
    }
}
