package io.nirahtech.ai.softgpt;

import java.util.function.Consumer;

public interface GenerativePretrainedTransformer {
    void setOnAnswerEventListener(Consumer<byte[]> eventListener);
    void setOnFailureEventListener(Consumer<Throwable> eventListener);
    void chat(byte[] prompt);
}
