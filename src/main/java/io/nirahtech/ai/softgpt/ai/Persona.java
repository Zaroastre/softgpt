package io.nirahtech.ai.softgpt.ai;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

public record Persona(
    String job,
    String role,
    String context,
    Collection<String> goals,
    Collection<String> skills,
    Collection<String> deliverables,
    Collection<String> behavior,
    Collection<String> instructions,
    String rowData
) {
    @Override
    public final String toString() {
        return "";
    }

    public static final Persona load(final String persona) {
        return new Persona(null, null, null, null, null, null, null, null, persona);
    }

    public static final Persona load(final File personaFile) throws IOException {
        Files.readString(personaFile.toPath());
        return load(Files.readString(personaFile.toPath()));
    }
    
}
