package io.nirahtech.ai.softgpt.pipeline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

import io.nirahtech.ai.softgpt.ai.Agent;
import io.nirahtech.ai.softgpt.ai.BusinessExpert;
import io.nirahtech.ai.softgpt.ai.Model;
import io.nirahtech.ai.softgpt.ai.Persona;
import io.nirahtech.ai.softgpt.ai.Sentence;

public final class WorkflowLoader {

    private static final Logger LOGGER = Logger.getLogger(WorkflowLoader.class.getSimpleName());

    private WorkflowLoader() { }

    public static final Workflow load() throws IOException {
        LOGGER.info("Workflow is loading...");
        final Workflow workflow = new Workflow();
        final Yaml yaml = new Yaml();
        final Map<String, Object> loadedYaml = yaml.load(WorkflowLoader.class.getResourceAsStream("/workflow.yaml"));
        final Map<String, Object> workflowConfiguration = (Map<String, Object>) loadedYaml.get("workflow");
        final List<Map<String, Object>> stepsConfiguration = (List<Map<String, Object>>) workflowConfiguration.get("steps");
        stepsConfiguration.forEach(stepConfiguration -> {
            LOGGER.info("A new step is detected in the configuration...");
            final List<Map<String, Object>> sentencesConfiguration = (List<Map<String, Object>>) stepConfiguration.get("sentences");
            final List<Sentence> sentences = new ArrayList<>();
            sentencesConfiguration.forEach(sentenceConfiguration -> {
                final String text = (String) sentenceConfiguration.get("text");
                final boolean validationRequired = (boolean) sentenceConfiguration.get("validationRequired");
                final String validator = (String) sentenceConfiguration.get("validator");
                final Sentence sentence = new Sentence(text, Optional.empty(), List.of());
                sentences.add(sentence);
            });
            final String name = (String) stepConfiguration.get("name");
            final String description = (String) stepConfiguration.get("description");
            final String modelName = (String) stepConfiguration.get("model");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(WorkflowLoader.class.getResourceAsStream("/"+((String) stepConfiguration.get("personaFile")).split("/")[1])))) {
                final String personaFileContent = reader.lines().collect(Collectors.joining("\r\n"));
                final Model model = new Model(modelName);
                final Persona persona = new Persona(null, null, null, null, List.of(), List.of(), List.of(), List.of(), personaFileContent);
                final Agent agent = new Agent(model, persona, sentences);
                final BusinessExpert businessExpert = new BusinessExpert(agent);
                final Step step = new Step(UUID.randomUUID(), name, description, 0, businessExpert, null);
                workflow.addStep(step);
                LOGGER.info("A new step was added into the workflow.");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        LOGGER.info("Workflow is successfully loaded.");
        LOGGER.info(String.format("Total available steps: %s", workflow.getSteps().size()));
        return workflow;
 
    }
}
