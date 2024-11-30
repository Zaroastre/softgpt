package io.nirahtech.ai.softgpt.ai;

import java.io.IOException;

public interface GenerativePretrainedTransformer extends ArtificialIntelligence {
    byte[] communicate(byte[] prompt) throws IOException;
}
