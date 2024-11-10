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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService{
    @Value("${firebase.service-account-filename}")
    private String serviceAccountFileName;

    private final MessageRepository messageRepository;
    private final IConversationService conversationService;
    private final IUserService userService;
    @Override
    public Message createMessage(MessageDTO messageDTO) {
        // Kiểm tra và xác định kiểu tin nhắn
        if (messageDTO.getMessageTypes() == null || messageDTO.getMessageTypes().isEmpty()) {
            throw new IllegalArgumentException("Message types must not be empty");
        }

        Message message = new Message();
        message.setSender(userService.findById(UUID.fromString(messageDTO.getSenderId())));
        message.setConversation(conversationService.findById(UUID.fromString(messageDTO.getConversationId())));

        // Kiểm tra các loại tin nhắn và xử lý
        for (String messageTypeString : messageDTO.getMessageTypes()) {
            MessageType messageType = MessageType.valueOf(messageTypeString.toUpperCase());

            // Nếu là văn bản
            if (messageType == MessageType.TEXT) {
                if (messageDTO.getMessage() == null || messageDTO.getMessage().isEmpty()) {
                    throw new IllegalArgumentException("Message content cannot be empty for TEXT message");
                }
                message.setMessage(messageDTO.getMessage());
            }

            // Nếu là hình ảnh
            if (messageType == MessageType.IMAGE) {
                if (messageDTO.getImageUrl() == null || messageDTO.getImageUrl().isEmpty()) {
                    throw new IllegalArgumentException("Image URL cannot be empty for IMAGE message");
                }
                message.setImageUrl(messageDTO.getImageUrl());
            }
        }

        // Lưu tin nhắn vào cơ sở dữ liệu
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
        File serviceAccountFile = ResourceUtils.getFile("classpath:" + serviceAccountFileName);
        FileInputStream serviceAccount = new FileInputStream(serviceAccountFile);

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId("hkt-e0d9b") // Your project ID
                .build()
                .getService();

        Bucket bucket = storage.get("hkt-e0d9b.appspot.com");

        String fileName = "images/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        bucket.create(fileName, file.getInputStream(), file.getContentType());

        // Mã hóa đường dẫn file
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());

        return "https://firebasestorage.googleapis.com/v0/b/hkt-e0d9b.appspot.com/o/" + encodedFileName + "?alt=media";
    }


}
