package dev.aryank.promptcanvas.mapper;

import dev.aryank.promptcanvas.dto.chat.ChatResponse;
import dev.aryank.promptcanvas.entity.ChatMessage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    List<ChatResponse> fromListOfChatMessage(List<ChatMessage> chatMessageList);
}
