package io.nirahtech.ai.softgpt.ai;

import java.util.function.Consumer;

public interface ArtificialIntelligence {
    void setOnPredictionSuccessdedEventListener(Consumer<byte[]> eventListener);
    void setOnPredictionFailedEventListener(Consumer<Throwable> eventListener);
}
