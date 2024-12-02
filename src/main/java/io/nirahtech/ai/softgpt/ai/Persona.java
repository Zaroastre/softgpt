package io.nirahtech.ai.softgpt.ai;

import java.util.Collection;

public record Persona(
    String job,
    String role,
    String context,
    Collection<String> goals,
    Collection<String> skills,
    Collection<String> deliverables,
    Collection<String> behavior,
    Collection<String> instructions
) {
    
}
