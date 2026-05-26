package com.np.ai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AiApplicationTests {

    @Autowired
    private VectorStore vectorStore;

	@Test
	void contextLoads() {
	}

    @Test
    void saveData(){
        System.out.println("started adding data");
        vectorStore.add(documents.stream().map(Document::new).toList());
        System.out.println("added data in the store");
    }


    List<String> documents = List.of(
            // Technology & AI
            "Artificial intelligence is transforming modern healthcare systems.",
            "Python is the preferred language for data science.",
            "Cloud computing allows scalable storage and processing power.",
            "Large language models generate human-like text responses.",
            "Cybersecurity protocols protect sensitive data from breaches.",

            // Nature & Animals
            "Deep ocean trenches harbor unique, undiscovered marine life.",
            "Honeybees play a crucial role in global pollination.",
            "The Amazon rainforest produces a large share of oxygen.",
            "Migrating birds travel thousands of miles every year.",
            "Volcanic eruptions reshape local landscapes and ecosystems.",

            // Daily Life & Wellness
            "A balanced diet includes vegetables, proteins, and grains.",
            "Regular aerobic exercise improves cardiovascular health.",
            "Morning meditation helps reduce daily stress and anxiety.",
            "Drinking enough water is essential for metabolic function.",
            "Quality sleep restores energy and sharpens mental focus.",

            // Space & Science
            "Mars remains a primary target for human exploration.",
            "Black holes possess gravitational pulls that trap light.",
            "The James Webb telescope captures deep space images.",
            "Quantum computing utilizes qubits for complex calculations.",
            "Renewable energy sources like solar power reduce emissions."
    );


}
