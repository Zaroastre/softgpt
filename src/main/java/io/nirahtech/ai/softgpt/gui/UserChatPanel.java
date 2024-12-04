package io.nirahtech.ai.softgpt.gui;

import java.awt.BorderLayout;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class UserChatPanel extends JPanel {

    private final JTextArea userText;
    private final JButton sendButton;

    private Consumer<String> onSendTextEventListener;

    /**
     * @param onSendTextEventListener the onSendTextEventListener to set
     */
    public void setOnSendTextEventListener(Consumer<String> onSendTextEventListener) {
        this.onSendTextEventListener = onSendTextEventListener;
    }

    public UserChatPanel() {
        super(new BorderLayout());
        this.userText = new JTextArea();
        this.sendButton = new JButton("Send");
        this.add(this.userText, BorderLayout.CENTER);
        this.add(this.sendButton, BorderLayout.EAST);

        this.sendButton.addActionListener((event) -> {
            if (Objects.nonNull(this.onSendTextEventListener)) {
                final String userPrompt = this.userText.getText();
                if (!userPrompt.isBlank()) {
                    this.onSendTextEventListener.accept(userPrompt.strip());
                } else {
                    System.err.println("No text specified");
                }
            } else {
                System.out.println("No event specified");
            }
        });
    }
    
}
