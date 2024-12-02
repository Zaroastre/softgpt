package io.nirahtech.ai.softgpt.system;

import java.io.IOException;

public interface Service {
    void start() throws IOException;
    void restart() throws IOException;
    void stop() throws IOException;
    void reload() throws IOException;
    String getStatus() throws IOException;
    void enable() throws IOException;
    void disable() throws IOException;
    boolean isInstalled() throws IOException;
    boolean isRunning() throws IOException;
    void useSudoBecomeMethod(final boolean useBecomeMethod);

}
