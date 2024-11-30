package io.nirahtech.ai.softgpt.ai;

import java.io.IOException;
import java.util.Set;

public record Agent(
    Model model,
    String promptPersona,
    Set<String> promtpSentences
) {
 
    public byte[] executePrompt(byte[] inputPrompt) throws IOException {
        return this.model.communicate(inputPrompt);
    }
}
