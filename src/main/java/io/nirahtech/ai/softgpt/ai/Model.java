package io.nirahtech.ai.softgpt.ai;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

public final class Model implements GenerativePretrainedTransformer {
    private final String name;
    private final File file;

    private Consumer<byte[]> onPredictionSuccedded = null;
    private Consumer<Throwable> onPredictionFailed = null;

    public Model(final File file) {
        this.file = file;
        this.name = this.file.getName().split("\\.")[0];
    }

    public final String getName() {
        return name;
    }

    public final File getFile() {
        return file;
    }

    @Override
    public void setOnPredictionSuccessdedEventListener(Consumer<byte[]> eventListener) {
        this.onPredictionSuccedded = eventListener;
    }

    @Override
    public void setOnPredictionFailedEventListener(Consumer<Throwable> eventListener) {
        this.onPredictionFailed = eventListener;
    }

    private final byte[] predictWithTrainedNeuronalNetwork(byte[] prompt) throws IOException {
        // Normalement, ici on va appeller l'API du model (probabblement Ollama 3.1 7B).
        throw new UnsupportedOperationException("Unimplemented method 'chat'");
    }

    @Override
    public byte[] communicate(byte[] prompt) throws IOException {
        byte[] prediction = null;
        try {
            prediction = this.predictWithTrainedNeuronalNetwork(prompt);
        } catch (IOException exception) {
            if (Objects.nonNull(this.onPredictionFailed)) {
                this.onPredictionFailed.accept(exception);
            }
            throw exception;
        }
        if (Objects.nonNull(prediction)) {
            if (Objects.nonNull(this.onPredictionSuccedded)) {
                this.onPredictionSuccedded.accept(prediction);
            }
        }
        return prediction;
    }
}
