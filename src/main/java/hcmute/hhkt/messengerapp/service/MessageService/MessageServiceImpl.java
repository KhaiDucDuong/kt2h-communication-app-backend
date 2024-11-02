package hcmute.hhkt.messengerapp.service.MessageService;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import hcmute.hhkt.messengerapp.Response.MessageResponse;
import hcmute.hhkt.messengerapp.Response.Meta;
import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.domain.Conversation;
import hcmute.hhkt.messengerapp.domain.Message;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.domain.enums.MessageType;
import hcmute.hhkt.messengerapp.dto.MessageDTO;
import hcmute.hhkt.messengerapp.repository.MessageRepository;
import hcmute.hhkt.messengerapp.service.ConversationService.IConversationService;
import hcmute.hhkt.messengerapp.service.UserService.IUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService{
    private final MessageRepository messageRepository;
    private final IConversationService conversationService;
    private final IUserService userService;
    @Override
    public Message createMessage(MessageDTO messageDTO) {
        MessageType messageType = MessageType.valueOf(messageDTO.getMessageType());

        if (messageType.equals(MessageType.TEXT) && StringUtils.isBlank(messageDTO.getMessage())) {
            throw new IllegalArgumentException(ExceptionMessage.MESSAGE_IS_BLANK);
        }

        // Tạo đối tượng Message
        Message.MessageBuilder messageBuilder = Message.builder()
                .messageType(messageType)
                .sender(userService.findById(UUID.fromString(messageDTO.getSenderId())));

        // Xử lý nội dung tin nhắn TEXT hoặc IMAGE
        if (messageType.equals(MessageType.TEXT)) {
            messageBuilder.message(messageDTO.getMessage());
        } else if (messageType.equals(MessageType.IMAGE)) {
            messageBuilder.imageUrl(messageDTO.getImageUrl());
        }

        Message message = messageBuilder.build();

        // Liên kết với cuộc trò chuyện nếu có
        if (!StringUtils.isBlank(messageDTO.getConversationId())) {
            message.setConversation(conversationService.findById(UUID.fromString(messageDTO.getConversationId())));
        }

        return messageRepository.save(message);
    }



    @Override
    public ResultPaginationResponse getConversationMessages(Conversation conversation, Pageable pageable) {
        Page<Message> messagePage = messageRepository.findMessagesByConversationAndIsDeletedIsFalse(conversation, pageable);
        Meta meta = Meta.builder()
                .page(messagePage.getNumber() + 1)
                .pageSize(messagePage.getSize())
                .pages(messagePage.getTotalPages())
                .total(messagePage.getTotalElements())
                .build();

        return ResultPaginationResponse.builder()
                .meta(meta)
                .result(MessageResponse.fromMessageList(messagePage.getContent()))
                .build();
    }

    @Override
    public Message updateMessage(MessageDTO messageDTO) {
        return null;
    }

    @Override
    public Message findMessageById(UUID messageId) {
        return null;
    }

    @Override
    public void deleteMessage(Message message) {

    }

    public String uploadImage(MultipartFile file) throws IOException {
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream("C:/Users/Dell.MM/hoang/kt2h-communication-app-backend/src/main/resources/hkt-e0d9b-firebase-adminsdk-6b37m-3bbc9aeeeb.json")))
                .setProjectId("hkt-e0d9b") // Your project ID
                .build()
                .getService();

        Bucket bucket = storage.get("hkt-e0d9b.appspot.com");

        String fileName = "images/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        bucket.create(fileName, file.getInputStream(), file.getContentType());

        return "https://firebasestorage.googleapis.com/v0/b/hkt-e0d9b.appspot.com/o/" + fileName + "?alt=media";
    }

}
