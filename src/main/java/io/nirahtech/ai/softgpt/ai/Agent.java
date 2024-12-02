package io.nirahtech.ai.softgpt.ai;

import java.io.IOException;
import java.util.Collection;

public record Agent(
    Model model,
    Persona persona,
    Collection<Sentence> sentences
) {
 
    public byte[] executePrompt(byte[] inputPrompt) throws IOException {
        return this.model.communicate(inputPrompt);
    }
}
