package io.nirahtech.ai.softgpt.pipeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

import io.nirahtech.ai.softgpt.ai.Agent;
import io.nirahtech.ai.softgpt.ai.BusinessExpert;
import io.nirahtech.ai.softgpt.ai.Model;
import io.nirahtech.ai.softgpt.ai.Persona;
import io.nirahtech.ai.softgpt.ai.Sentence;

public final class WorkflowLoader {
    private WorkflowLoader() { }

    public static final Workflow load() throws IOException {
        final Workflow workflow = new Workflow();
        final Yaml yaml = new Yaml();
        final Map<String, Object> loadedYaml = yaml.load(WorkflowLoader.class.getResourceAsStream("/workflow.yaml"));
        final Map<String, Object> workflowConfiguration = (Map<String, Object>) loadedYaml.get("workflow");
        final List<Map<String, Object>> stepsConfiguration = (List<Map<String, Object>>) workflowConfiguration.get("steps");
        stepsConfiguration.forEach(stepConfiguration -> {
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
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        return workflow;
 
    }
}
