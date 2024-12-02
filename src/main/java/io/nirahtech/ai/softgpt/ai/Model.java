package io.nirahtech.ai.softgpt.ai;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import io.github.ollama4j.models.chat.OllamaChatRequest;
import io.github.ollama4j.models.chat.OllamaChatRequestBuilder;
import io.github.ollama4j.models.chat.OllamaChatResult;

public final class Model implements GenerativePretrainedTransformer {
    private final String name;
    private final File file;

    private Consumer<byte[]> onPredictionSuccedded = null;
    private Consumer<Throwable> onPredictionFailed = null;

    private final OllamaAPI ollamaAPI;

    public Model(final File file) {
        this.file = file;
        this.name = this.file.getName().split("\\.")[0];
        this.ollamaAPI = new OllamaAPI();
    }

    private final Optional<io.github.ollama4j.models.response.Model> findRequiredInstalledModel() throws IOException {
        Optional<io.github.ollama4j.models.response.Model> foundedModelToSearch = Optional.empty();
        try {
            foundedModelToSearch = this.ollamaAPI.listModels().stream().filter(model -> model.getName().equalsIgnoreCase(this.name)).findFirst();
        } catch (OllamaBaseException | IOException | InterruptedException | URISyntaxException exception) {
            throw new IOException(exception);
        }
        return foundedModelToSearch;
    }

    public final void initialize(final Persona persona) throws IOException {
        try {
            final var foundedModelToSearch = this.findRequiredInstalledModel();
            if (foundedModelToSearch.isEmpty()) {
                this.ollamaAPI.pullModel(this.name);
            }
            this.setupModelWithSpecificPersona( persona);
        } catch (OllamaBaseException | IOException | InterruptedException | URISyntaxException exception) {
            throw new IOException(exception);
        }
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

    private final void setupModelWithSpecificPersona(final Persona persona) throws IOException {
        final var foundedSearchedModel = this.findRequiredInstalledModel();
        if (foundedSearchedModel.isPresent()) {
            final var model = foundedSearchedModel.get();
            OllamaChatRequestBuilder builder = OllamaChatRequestBuilder.getInstance(model.getModel());
            OllamaChatRequest requestModel = builder
                    .withMessage(
                            OllamaChatMessageRole.SYSTEM,
                            persona.toString())
                    .build();

            try {
                ollamaAPI.chat(requestModel);
            } catch (OllamaBaseException | IOException | InterruptedException exception) {
                throw new IOException(exception);
            }

        } else {
            throw new IOException("Missing Ollama model: " + this.name);
        }
    }

    private final byte[] predictWithTrainedNeuronalNetwork(byte[] prompt) throws IOException {
        final var foundedSearchedModel = this.findRequiredInstalledModel();
        if (foundedSearchedModel.isPresent()) {
            final var model = foundedSearchedModel.get();
            OllamaChatRequestBuilder builder = OllamaChatRequestBuilder.getInstance(model.getModel());
            OllamaChatRequest requestModel = builder
                    .withMessage(
                            OllamaChatMessageRole.USER,
                            new String(prompt, StandardCharsets.UTF_8))
                    .build();

            OllamaChatResult chatResult;
            try {
                chatResult = ollamaAPI.chat(requestModel);
            } catch (OllamaBaseException | IOException | InterruptedException exception) {
                throw new IOException(exception);
            }
            return  chatResult.getResponse().trim().getBytes();

        } else {
            throw new IOException("Missing Ollama model: " + this.name);
        }
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
