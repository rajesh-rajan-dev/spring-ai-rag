package com.example.ragdemo.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures an in-memory vector store.
 *
 * SimpleVectorStore requires no external infrastructure — everything lives
 * in JVM heap. Perfect for demos. For production, swap this bean for
 * PgVectorStore, ChromaVectorStore, or any other Spring AI-supported backend.
 */
@Configuration
public class VectorStoreConfig {

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return new SimpleVectorStore(embeddingModel);
    }
}
