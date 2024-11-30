package io.nirahtech.ai.softgpt;

import java.util.function.Consumer;

public interface ArtificialIntelligence {
    void setOnPredictionSuccessdedEventListener(Consumer<byte[]> eventListener);
    void setOnPredictionFailedEventListener(Consumer<Throwable> eventListener);
    void useContextToPredict(byte[] context);
    void predict();
}