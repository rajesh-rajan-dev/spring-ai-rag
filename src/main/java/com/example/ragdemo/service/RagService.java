package com.example.ragdemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.ai.vectorstore.SearchRequest;
/**
 * RagService — the core of the RAG pipeline.
 *
 * How it works:
 *  1. QuestionAnswerAdvisor intercepts the question before it reaches the LLM
 *  2. It embeds the question and performs a similarity search in the VectorStore
 *  3. The top-k matching chunks are injected into the prompt as context
 *  4. The enriched prompt is sent to the LLM, which answers using only the context
 *
 * This approach prevents hallucination — the model is grounded to your document.
 */
@Service
public class RagService {

    private static final Logger log = LoggerFactory.getLogger(RagService.class);

    private final ChatClient chatClient;

    public RagService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder
                // QuestionAnswerAdvisor does the RAG retrieval automatically
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .build();
    }

    /**
     * Ask a question — returns a grounded answer from the ingested document.
     *
     * @param question  the user's natural language question
     * @return          answer grounded in the document context
     */
    public String ask(String question) {
        log.info("🔍 Question received: {}", question);

        String answer = chatClient.prompt()
                .user(question)
                .call()
                .content();

        log.info("💬 Answer: {}", answer);
        return answer;
    }
}
