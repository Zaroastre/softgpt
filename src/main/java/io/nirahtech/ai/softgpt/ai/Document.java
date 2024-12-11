package io.nirahtech.ai.softgpt.ai;

import java.io.File;

public record Document(
    File file,
    String content
) {
    
}
