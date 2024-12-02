package io.nirahtech.ai.softgpt.ai;

import java.util.Collection;
import java.util.Optional;

public record Sentence(
    String text,
    Optional<Agent> validator,
    Collection<Agent> collaborator
) implements Prompt {
    
}
