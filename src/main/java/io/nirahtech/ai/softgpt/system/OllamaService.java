package io.nirahtech.ai.softgpt.system;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class OllamaService extends AbstractSystemctlService {

    private static final String SERVICE_FILE_NAME = "ollama.service";
    private static final String SERVICE_FILE_PATH = "/etc/systemd/system/";

    public OllamaService() {
        super("ollama");
    }

    /**
     * Installe le fichier de service dans /etc/systemd/system,
     * recharge systemd, et active le service.
     */
    public void install() throws IOException {
        // Copie du fichier ollama.service depuis les ressources vers /etc/systemd/system
        Path sourcePath = Path.of(getClass().getClassLoader().getResource(SERVICE_FILE_NAME).getPath());
        Path destinationPath = Path.of(SERVICE_FILE_PATH + SERVICE_FILE_NAME);

        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

        // Recharge systemd pour prendre en compte le nouveau service
        this.reloadDaemon();

        // Active le service pour qu'il démarre au démarrage
        this.enable();

        // Démarre immédiatement le service
        this.start();
    }

    /**
     * Désinstalle le service en le stoppant, le désactivant,
     * et en supprimant le fichier de service.
     */
    public void uninstall() throws IOException {
        // Stoppe le service
        this.stop();

        // Désactive le service
        this.disable();

        // Supprime le fichier ollama.service
        Path serviceFilePath = Path.of(SERVICE_FILE_PATH + SERVICE_FILE_NAME);
        Files.deleteIfExists(serviceFilePath);

        // Recharge systemd pour prendre en compte la suppression
        this.reloadDaemon();
    }
}
