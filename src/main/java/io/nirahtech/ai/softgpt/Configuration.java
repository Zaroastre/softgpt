package io.nirahtech.ai.softgpt;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public record Configuration(
    WorkflowConfiguration workflow
) {

    public static final Configuration load() {
        Yaml yaml = new Yaml();
        final Map<String, Object> loadedYaml = yaml.load(Configuration.class.getResourceAsStream("/workflow.yaml"));
        Configuration configuration = new Configuration(new WorkflowConfiguration(List.of()));
        return configuration;
 
    }

    public record WorkflowConfiguration(
        List<StepConfgiration> steps
    ) {
    }

    public record StepConfgiration(
        String name,
        String description,
        String model,
        File personaFile,
        List<SentenceConfiguration> sentences
    ) {
    }

    public record SentenceConfiguration(
        String text,
        boolean validationRequired,
        String validator,
        List<String> collaboratesWith
    ) {
    }
}
