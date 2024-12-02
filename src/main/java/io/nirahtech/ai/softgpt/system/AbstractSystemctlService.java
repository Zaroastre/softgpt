package io.nirahtech.ai.softgpt.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSystemctlService implements SystemctlService {

    protected final String serviceName;
    protected boolean useBecomeMethod = false;
    protected final String becomeMethod;

    protected AbstractSystemctlService(final String serviceName) {
        this.serviceName = serviceName;
        this.becomeMethod = "sudo";
    }

    protected final String executeSystemCommand(final List<String> commandWithArguments) throws IOException {
        final List<String> command = new ArrayList<>();
        if (this.useBecomeMethod) {
            command.add(this.becomeMethod);
        }
        command.addAll(commandWithArguments);
        System.out.println(command);
        final ProcessBuilder processBuilder = new ProcessBuilder(command);
        final Process process = processBuilder.start();

        try (
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))
        ) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = outputReader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }

            StringBuilder errorOutput = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append(System.lineSeparator());
            }

            final int exitCode = process.waitFor();
            if (exitCode != SUCCESS_EXIT_CODE && exitCode != BAD_USAGE_EXIT_CODE) {
                System.err.println(errorOutput);
                System.err.println(output);
                throw new IOException("Command failed with exit code " + exitCode + ": " + errorOutput.toString());
                // throw new IOException("Command failed with exit code " + exitCode + ": " + output.toString());
            }

            return output.toString();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IOException("Command execution interrupted", exception);
        }
    }

    @Override
    public void start() throws IOException {
        final List<String> commandWithArguments = List.of(
            SYSTEM_COMMAND,
            "start",
            this.serviceName
        );
        this.executeSystemCommand(commandWithArguments);
    }

    @Override
    public void stop() throws IOException {
        final List<String> commandWithArguments = List.of(
            SYSTEM_COMMAND,
            "stop",
            this.serviceName
        );
        this.executeSystemCommand(commandWithArguments);
    }

    @Override
    public void restart() throws IOException {
        final List<String> commandWithArguments = List.of(
            SYSTEM_COMMAND,
            "restart",
            this.serviceName
        );
        this.executeSystemCommand(commandWithArguments);
    }

    @Override
    public void reload() throws IOException {
        final List<String> commandWithArguments = List.of(
            SYSTEM_COMMAND,
            "reload",
            this.serviceName
        );
        this.executeSystemCommand(commandWithArguments);
    }

    @Override
    public String getStatus() throws IOException {
        final List<String> commandWithArguments = List.of(
            SYSTEM_COMMAND,
            "status",
            this.serviceName
        );
        return this.executeSystemCommand(commandWithArguments);
    }

    @Override
    public void enable() throws IOException {
        final List<String> commandWithArguments = List.of(
            SYSTEM_COMMAND,
            "start",
            this.serviceName
        );
        this.executeSystemCommand(commandWithArguments);
    }

    @Override
    public void disable() throws IOException {
        final List<String> commandWithArguments = List.of(
            SYSTEM_COMMAND,
            "disable",
            this.serviceName
        );
        this.executeSystemCommand(commandWithArguments);
    }

    @Override
    public boolean isInstalled() throws IOException {
        final List<String> commandWithArguments = List.of(
            SYSTEM_COMMAND,
            "list-unit-files"
        );
        String output = this.executeSystemCommand(commandWithArguments);
        return output.contains(this.serviceName);
    }

    @Override
    public boolean isRunning() throws IOException {
        final List<String> commandWithArguments = List.of(
            SYSTEM_COMMAND,
            "is-active",
            this.serviceName
        );
        String output = this.executeSystemCommand(commandWithArguments).trim();
        return "active".equals(output);
    }

    @Override
    public void useSudoBecomeMethod(boolean useBecomeMethod) {
        this.useBecomeMethod = useBecomeMethod;
    }
    
}
