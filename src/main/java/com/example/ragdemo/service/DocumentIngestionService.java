package com.example.ragdemo.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;

import java.util.List;

/**
 * DocumentIngestionService — runs once at startup.
 *
 * Pipeline:
 *   1. Read raw text from the classpath resource
 *   2. Split into overlapping chunks (TokenTextSplitter)
 *   3. Embed each chunk via OpenAI text-embedding-3-small
 *   4. Store vectors in the SimpleVectorStore
 *
 * To use a different document, swap the file in src/main/resources/docs/
 * and update the path in application.properties (rag.document.path).
 */
@Service
public class DocumentIngestionService {

    private static final Logger log = LoggerFactory.getLogger(DocumentIngestionService.class);

    private final VectorStore vectorStore;

    // Resolves to src/main/resources/docs/sample.txt by default
    @Value("classpath:${rag.document.path:docs/sample.txt}")
    private Resource documentResource;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void ingestDocument() {
        log.info("📄 Loading document: {}", documentResource.getFilename());

        // Step 1 — Read the raw text file
        //TextReader reader = new TextReader(documentResource);

        PagePdfDocumentReader reader = new PagePdfDocumentReader(documentResource);
        List<Document> rawDocs = reader.get();

        // Step 2 — Split into chunks of ~500 tokens with 100-token overlap
        //           Overlap ensures context isn't lost at chunk boundaries
        TokenTextSplitter splitter = new TokenTextSplitter(500, 100, 5, 10000, true);
        List<Document> chunks = splitter.apply(rawDocs);

        log.info("✂️  Split into {} chunks — embedding and storing...", chunks.size());

        // Step 3 & 4 — Embed and store (Spring AI handles this in one call)
        vectorStore.add(chunks);

        log.info("✅ Ingestion complete. Vector store is ready.");
    }
}
