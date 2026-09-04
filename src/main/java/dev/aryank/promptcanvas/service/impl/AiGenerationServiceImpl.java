package dev.aryank.promptcanvas.service.impl;

import dev.aryank.promptcanvas.llm.PromptUtils;
import dev.aryank.promptcanvas.llm.advisors.FileTreeContextAdvisor;
import dev.aryank.promptcanvas.llm.tools.CodeGenerationTools;
import dev.aryank.promptcanvas.security.AuthUtil;
import dev.aryank.promptcanvas.service.AiGenerationService;
import dev.aryank.promptcanvas.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiGenerationServiceImpl implements AiGenerationService {

    private final ChatClient chatClient;
    private final AuthUtil authUtil;
    private final ProjectFileService projectFileService;
    private final FileTreeContextAdvisor  fileTreeContextAdvisor;

    private static final Pattern FILE_TAG_PATTERN = Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>", Pattern.DOTALL);

    @PreAuthorize("@security.canEditProject(#projectId)")
    @Override
    public Flux<String> streamResponse(String userMessage, Long projectId) {
        Long userId = authUtil.getCurrentUserId();
        createChatSessionIfNotExists(projectId, userId);

        Map<String, Object> advisorParams = Map.of(
                "userId", userId,
                "projectId", projectId
        );

        StringBuilder fullResponseBuffer = new StringBuilder();

        CodeGenerationTools codeGenerationTools = new CodeGenerationTools(projectFileService, projectId);

        return chatClient.prompt()
                .system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userMessage)
                .tools(codeGenerationTools)
                .advisors(advisorSpec -> {
                            advisorSpec.params(advisorParams);
                            advisorSpec.advisors(fileTreeContextAdvisor);
                        }
                )
                .stream()
                .chatResponse()
                .doOnNext(response -> {
                    String content = response.getResult().getOutput().getText();

                    fullResponseBuffer.append(content);
                })
                .doOnComplete(() -> {
                    Schedulers.boundedElastic().schedule(() -> {
                        parseAndSaveFiles(fullResponseBuffer.toString(), projectId);
                    });

                })
                .doOnError(error -> log.error("Error during streaming for projectID: {}", projectId))
                .map(response -> response.getResult().getOutput().getText());
    }

    private void parseAndSaveFiles(String fullResponse, Long projectId) {
//        String dummy = """
//                <message> I am going to read the files and generate the code </message>
//                <file path="src/App.jsx">
//                    import App from './App.jsx'
//                    .....
//                </file>
//                <message> I am going to read the files and generate the code </message>
//                <file path="src/App.jsx">
//                    import App from './App.jsx'
//                    .....
//                </file>
//                """; dummy string for reference. This is how we will get the output from the llm, we have to parse it and store it.

        Matcher matcher = FILE_TAG_PATTERN.matcher(fullResponse);
        while (matcher.find()) {
            String filePath = matcher.group(1);
            String fileContent = matcher.group(2).trim();

            projectFileService.saveFile(projectId, filePath, fileContent);
        }
    }

    private void createChatSessionIfNotExists(Long projectId, Long userId) {

    }
}
