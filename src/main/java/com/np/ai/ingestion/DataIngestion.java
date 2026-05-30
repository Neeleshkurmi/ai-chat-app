package com.np.ai.ingestion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class DataIngestion {

    private final VectorStore vectorStore;
    private final EmbeddingModel embeddingModel;
    private final TokenTextSplitter splitter;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "pdf", "docx", "txt", "java", "py", "js", "ts"
    );

    public int ingest(MultipartFile file, UUID chatId) throws IOException {
        log.info("ingestion method called.....");

        String ext = getExtension(file);

        List<Document> docs = switch (ext){
            case "pdf", "docx", "txt" -> new TikaDocumentReader(file.getResource()).get();
            case "java", "py", "js" -> readCodeFile(file, chatId.toString());
            default -> throw new IllegalArgumentException("Unsupported type");
        };

        List<Document> chunks = splitter.apply(docs);

        chunks.forEach(d -> d.getMetadata().putAll(Map.of(
                "chatId", chatId,
                "filename", getSafeFileName(file),
                "type", ext
        )));

        log.info("adding data in database.......");
        vectorStore.add(chunks);

        log.info("ingestion successful");
        return chunks.size();
    }

    public List<Document> readCodeFile(MultipartFile file, String chatId) throws IOException {

        try (var reader = new BufferedReader((new InputStreamReader(file.getInputStream())))){
            StringBuilder sb = new StringBuilder();
            String line;
            int lineNum = 1;

            while((line = reader.readLine())!= null){
                sb.append(lineNum++).append(": ").append(line).append("\n");
            }

            Document doc = new Document(sb.toString());
            doc.getMetadata().put("language", getExtension(file));

            return List.of(doc);
        }
    }

    public String getExtension(MultipartFile file){
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());

        if(ext == null || ext.isBlank()){
            throw new IllegalArgumentException("File has no Extension");
        }
        ext = ext.toLowerCase();

        if(!ALLOWED_EXTENSIONS.contains(ext)){
            throw new IllegalArgumentException("Unsupported file type: "+ext);
        }
        return ext;
    }

    private String getSafeFileName(MultipartFile file) {
        String name = file.getOriginalFilename();
        return name!=null ? name : "unknown";
    }
}
