package io.nirahtech.ai.softgpt.ai;

import java.util.HashMap;
import java.util.Map;

public record Job(
    String name
) {
    private static final Map<String, Job> CACHE = new HashMap<>();
    

    public static final Job of(final String name) {
        final String key = name.toLowerCase();
        if (!CACHE.containsKey(key)) {
            CACHE.put(key, new Job(name));
        }
        return CACHE.get(key);
    }
}
