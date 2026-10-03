package org.genAi.project1;

import org.genAi.project1.client.OllamaClient;
import org.genAi.project1.model.Content;
import org.genAi.project1.model.OllamaResponse;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        String systemPrompt = "You are concise. You give output clear and straightforward";
        String userPrompt = "Which are the best place to see fall color in New England.";
        int maxOutputToken = 500;

        OllamaClient ollamaClient = new OllamaClient();
        OllamaResponse response = ollamaClient.sendRequest(systemPrompt, userPrompt, maxOutputToken);

        System.out.println(response.usage());
        for(Content content : response.content()) {
            if(content.text() != null && !content.text().isEmpty()) {
                System.out.println(content.text());
            }

        }

    }
}