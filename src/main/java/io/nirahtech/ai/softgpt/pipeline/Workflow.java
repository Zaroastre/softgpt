package io.nirahtech.ai.softgpt.pipeline;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import io.nirahtech.ai.softgpt.ai.Persona;

public class Workflow implements Consumer<String> {
    private final Set<Step> steps;
    private StepState state;
    private final AtomicReference<Step> currentStep;
    private Consumer<Step> onCurrentStepChangedEventListener;

    public Workflow() {
        this.steps = new LinkedHashSet<>();
        this.currentStep = new AtomicReference<>();
    }

    /**
     * @param onCurrentStepChangedEventListener the onCurrentStepChangedEventListener to set
     */
    public void setOnCurrentStepChangedEventListener(Consumer<Step> onCurrentStepChangedEventListener) {
        this.onCurrentStepChangedEventListener = onCurrentStepChangedEventListener;
    }

    public void initialize() throws IOException {
        for (Step step : steps) {
            final Persona persona = step.getBusinessExpert().agent().persona();
            step.getBusinessExpert().agent().model().initialize(persona);
        }
    }

    public void addStep(final Step step) {
        Objects.requireNonNull(step);
        this.steps.add(step);
    }

    public void removeStep(final Step step) {
        Objects.requireNonNull(step);
        this.steps.remove(step);
    }

    public final Collection<Step> getSteps() {
        return Collections.unmodifiableCollection(this.steps);
    }

    @Override
    public void accept(final String userPrompt) {
        Objects.requireNonNull(userPrompt);
        this.state = StepState.RUNNING;
        STEPS_LOOP: for (Step step : steps) {
            this.currentStep.set(step);
            if (Objects.nonNull(this.onCurrentStepChangedEventListener)) {
                this.onCurrentStepChangedEventListener.accept(this.currentStep.get());
            }
            try {
                step.run();
            } catch (IOException e) {
                this.state = StepState.FAILED;
                break STEPS_LOOP;
            }
        }
        this.currentStep.set(null);
        if (this.state == StepState.RUNNING) {
            this.state = StepState.SUCCESS;
        }
    }

}
