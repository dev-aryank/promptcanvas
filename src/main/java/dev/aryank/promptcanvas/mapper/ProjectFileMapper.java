package dev.aryank.promptcanvas.mapper;

import dev.aryank.promptcanvas.dto.project.FileNode;
import dev.aryank.promptcanvas.entity.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectFileMapper {

    List<FileNode> toListOfFileNodes(List<ProjectFile> projectFileList);
}
