package com.example.ragdemo.controller;

import com.example.ragdemo.service.RagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ChatController — exposes the RAG pipeline over HTTP.
 *
 * Endpoints:
 *   POST /ask        — ask a question about the ingested document
 *   GET  /health     — simple liveness check
 */
@RestController
public class ChatController {

    private final RagService ragService;

    public ChatController(RagService ragService) {
        this.ragService = ragService;
    }

    /**
     * Ask a question.
     *
     * Request body (JSON):
     * {
     *   "question": "What is Spring AI?"
     * }
     *
     * Example curl:
     * curl -X POST http://localhost:8080/ask \
     *   -H "Content-Type: application/json" \
     *   -d '{"question": "What is Spring AI?"}'
     */
    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> ask(@RequestBody Map<String, String> body) {
        String question = body.get("question");

        if (question == null || question.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Request body must contain a non-empty 'question' field."));
        }

        String answer = ragService.ask(question);
        return ResponseEntity.ok(Map.of("question", question, "answer", answer));
    }

    /**
     * Health check.
     *
     * curl http://localhost:8080/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "Spring AI RAG Demo"));
    }
}
