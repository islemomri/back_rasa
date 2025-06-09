package com.project.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/pdf")
@Tag(name = "pdf-controller", description = "PDF file processing")
public class PdfController {

    @Operation(
        summary = "Vérifier si deux mots existent dans un fichier PDF",
        description = "Cette méthode permet d'envoyer un fichier PDF et de vérifier si deux mots s'y trouvent."
    )
    @PostMapping(value = "/checkWords", consumes = "multipart/form-data")
    public ResponseEntity<?> checkWordsInPdf(
            @Parameter(description = "Fichier PDF à analyser", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "Premier mot à rechercher", required = true)
            @RequestParam("word1") String word1,
            @Parameter(description = "Deuxième mot à rechercher", required = true)
            @RequestParam("word2") String word2) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Le fichier est vide");
        }

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            String normalizedText = normalizeText(text);
            String normalizedWord1 = normalizeText(word1);
            String normalizedWord2 = normalizeText(word2);

            boolean containsWord1 = normalizedText.contains(normalizedWord1);
            boolean containsWord2 = normalizedText.contains(normalizedWord2);

            boolean bothExist = containsWord1 && containsWord2;

            return ResponseEntity.ok(new ResultResponse(bothExist, containsWord1, containsWord2));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors du traitement du PDF");
        }
        
        
        
        
    }
    private String normalizeText(String text) {
        if (text == null) return null;
        
        text = text.replace("’", "'"); // apostrophe typographique
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(text).replaceAll("");

        text = text.replaceAll("\\s+", " "); // remplace tab, newline, espaces multiples, etc.
        return text.toLowerCase().trim();    // minuscule + supprime espaces en début/fin
    }


    public static class ResultResponse {
        private boolean bothExist;
        private boolean word1Found;
        private boolean word2Found;

        public ResultResponse(boolean bothExist, boolean word1Found, boolean word2Found) {
            this.bothExist = bothExist;
            this.word1Found = word1Found;
            this.word2Found = word2Found;
        }

        public boolean isBothExist() {
            return bothExist;
        }

        public boolean isWord1Found() {
            return word1Found;
        }

        public boolean isWord2Found() {
            return word2Found;
        }
        
        
       }
}
