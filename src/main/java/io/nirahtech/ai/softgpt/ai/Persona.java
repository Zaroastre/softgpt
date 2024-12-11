package io.nirahtech.ai.softgpt.ai;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public record Persona(
    Job job,
    String role,
    String context,
    Collection<String> goals,
    Collection<String> skills,
    Collection<Document> deliverables,
    Collection<String> behavior,
    Collection<String> instructions,
    String rowData
) {
    
    public static final Persona load(final String persona) {
        final Yaml yaml = new Yaml();
        final Map<String, Object> content = (Map<String, Object>) yaml.load(persona);
        String job = null;
        String role = null;
        String context = null;
        List<String> goals = new ArrayList<>();
        List<String> skills = new ArrayList<>();
        List<Document> deliverables = new ArrayList<>();
        List<String> behavior = new ArrayList<>();
        List<String> instructions = new ArrayList<>();
        String rowData = persona;
        return new Persona(Job.of(job), role, context, goals, skills, deliverables, behavior, instructions, rowData);
    }

    public static final Persona load(final File personaFile) throws IOException {
        return load(Files.readString(personaFile.toPath()));
    }
    
}
