package io.nirahtech.ai.softgpt.pipeline;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

import io.nirahtech.ai.softgpt.ai.BusinessExpert;
import io.nirahtech.ai.softgpt.ai.Sentence;

public final class Step {

    private static final Logger LOGGER = Logger.getLogger(Step.class.getSimpleName());

    private final UUID id;
    private final String name;
    private final String description;
    private final int order;
    private final BusinessExpert businessExpert;
    private final Step approvationStep;
    private final Collection<Runnable> onStateChangedEventListeners;
    private StepState stepState;

    public Step(
        final UUID id,
        final String name,
        final String description,
        final int order,
        final BusinessExpert businessExpert,
        final Step approvationStep
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.order = order;
        this.businessExpert = businessExpert;
        this.approvationStep = approvationStep;
        this.stepState = StepState.PENDING;
        this.onStateChangedEventListeners = new LinkedHashSet<>();
    }

    /**
     * @param onStateChangedEventListener the onStateChangedEventListener to set
     */
    public void addOnStateChangedEventListener(Runnable onStateChangedEventListener) {
        LOGGER.info("A new event listener must be aded to the step when its state will change.");
        if (Objects.nonNull(onStateChangedEventListener)) {
            this.onStateChangedEventListeners.add(onStateChangedEventListener);
            LOGGER.info("A new event listener was added to the step when its state will be updated.");
        }
    }
    
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getOrder() {
        return order;
    }

    public BusinessExpert getBusinessExpert() {
        return businessExpert;
    }

    public Step getApprovationStep() {
        return approvationStep;
    }

    public StepState getStepState() {
        return stepState;
    }

    private final boolean validateWithParent(byte[] output) throws IOException {
        if (this.approvationStep != null) {
            LOGGER.info("The step need approval by the parent...");
            // Construction du prompt
            final String prompt = String.format(
                    "Le résultat suivant a été produit par '%s' (étape %d) :\n%s\n\n"
                            + "En tant que '%s', veuillez évaluer si ce résultat correspond aux attentes définies dans vos exigences. "
                            + "Répondez uniquement par 'OK' si le résultat est conforme, ou décrivez les points de non-conformité dans votre réponse.",
                    this.name, this.order, new String(output),
                    this.approvationStep.name);

            // Conversion du prompt en byte array
            final byte[] promptBytes = prompt.getBytes();

            // Appel au BusinessExpert du parent pour validation
            final byte[] parentFeedback = this.approvationStep.businessExpert.listenAndRespond(promptBytes);

            // Validation basée sur la réponse du parent
            final String feedback = new String(parentFeedback).trim();
            return feedback.equalsIgnoreCase("OK"); // Validation si réponse "OK"
        }
        return true; // Pas de parent = validation automatique
    }

    private final void triggeOnStateChangedEventListerners() {
        LOGGER.info("Trigging all event listeners because state was changed...");
        this.onStateChangedEventListeners.forEach(eventListener -> {
            eventListener.run();
        });
    }

    public void run() throws IOException {
        final int maxCorrection = 5;
    
        for (Sentence sentence : this.businessExpert.agent().sentences()) {
            int currentCorrection = 0;
            boolean isValid = false;
            String correctionOrder = null;

            try {
                do {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (Objects.isNull(correctionOrder)) {
                        stringBuilder.append(sentence.text());
                    } else {
                        stringBuilder.append(correctionOrder).append(" ").append(sentence.text());
                    }
    
                    byte[] inputPrompt = stringBuilder.toString().getBytes();
    
                    // Demander au BusinessExpert d'exécuter le prompt
                    byte[] output = this.businessExpert.listenAndRespond(inputPrompt);
    
                    // Valider le résultat avec le parent (si présent)
                    isValid = validateWithParent(output);
    
                    if (!isValid) {
                        System.err.printf("Step '%s' (ordre %d) failed : parent agent '%s' does not valdate the result.%n", this.name, this.order, this.approvationStep.name);
                        currentCorrection++;
                        
                        // Ajout du message de régénération en cas d'échec
                        if (Objects.isNull(correctionOrder)) {
                            correctionOrder = "Le résultat généré ne répond pas aux exigences attendues et ne correspond pas tout à fait à nos attentes. Merci de le régénérer en prenant en compte les points suivants : ";
                        }
    
                        if (currentCorrection < maxCorrection) {
                            System.err.printf("Étape '%s' (ordre %d) échouée : Il faut regénérer un nouveau résultat.%n", this.name, this.order);
                        }
    
                    }
                    
                } while ((!isValid) && (currentCorrection < maxCorrection));
    
                if (isValid) {
                    System.out.printf("Étape '%s' (ordre %d) validée avec succès.%n", this.name, this.order);
                    this.stepState = StepState.SUCCESS;
                    this.triggeOnStateChangedEventListerners();
                } else {
                    this.stepState = StepState.FAILED;
                    this.triggeOnStateChangedEventListerners();
                    System.err.printf("Étape '%s' (ordre %d) échouée : le parent '%s' n'a pas validé le résultat.%n", this.name, this.order, this.approvationStep.name);
                }
    
            } catch (IOException e) {
                this.stepState = StepState.FAILED;
                this.triggeOnStateChangedEventListerners();
                System.err.printf("Erreur lors de l'exécution de l'étape '%s' (ordre %d) : %s%n", this.name, this.order, e.getMessage());
                throw e;
            }
        }
    }
    
}
