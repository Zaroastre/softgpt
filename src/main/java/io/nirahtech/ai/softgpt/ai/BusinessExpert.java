package io.nirahtech.ai.softgpt.ai;

import java.io.IOException;

public record BusinessExpert(
    Agent agent
) {

    public byte[] listenAndRespond(byte[] sentence) throws IOException {
        return this.agent.executePrompt(sentence);
    }

}
