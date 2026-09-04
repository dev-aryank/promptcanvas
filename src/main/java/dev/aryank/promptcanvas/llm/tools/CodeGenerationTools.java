package dev.aryank.promptcanvas.llm.tools;

import dev.aryank.promptcanvas.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CodeGenerationTools {

    private final ProjectFileService projectFileService;
    private final Long projectId;

    @Tool(
            name = "read_files",
            description = "Read the content of the files. Only input the file names present inside the FILE_TREE. DO NOT input any path which is not present under the FILE_TREE."
    )
    public List<String> readFiles(
            @ToolParam(description = "List of relative paths (e.g., ['src/App.tsx'])")
            List<String> paths){

        List<String> result = new ArrayList<>();

        for(String path : paths){
            String cleanPath = path.startsWith("/") ? path.substring(1) : path;

            log.info("Requested file: {}", cleanPath);

            String content = projectFileService.getFileContent(projectId, cleanPath).content();

            result.add(String.format(
                    "--- START OF FILE: %s ---\n%s\n--- END OF FILE ---",
                    cleanPath, content
            ));
        }

        return result;

    }

}
