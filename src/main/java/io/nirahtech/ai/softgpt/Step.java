package io.nirahtech.ai.softgpt;

import java.io.File;
import java.util.UUID;

public record Step(
    UUID id;
    String name;
    String description;
    int order;
    File model;
    StateMachine stateMachine;
) {

    public static final Step create(final String name, final String description, final int order, final File model) {
        final UUID id = UUID.randomUUID();
        final StateMachine stateMachine = null;
        return new Step(id, name, description, order, model, stateMachine);

    }
}
