package com.example.ragdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Spring AI RAG Demo.
 *
 * What this app does:
 *  1. On startup, reads a plain-text document from src/main/resources/docs/
 *  2. Splits it into chunks and stores embeddings in an in-memory vector store
 *  3. Exposes POST /ask — send a question, get a grounded answer from the document
 *
 * No database setup needed. Just set your OpenAI API key and run.
 */
@SpringBootApplication
public class RagDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RagDemoApplication.class, args);
    }
}
