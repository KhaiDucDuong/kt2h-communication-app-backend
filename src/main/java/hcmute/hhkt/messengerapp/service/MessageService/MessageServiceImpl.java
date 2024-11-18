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
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
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
        MessageType messageType = MessageType.valueOf(messageDTO.getMessageType());

        if (messageType.equals(MessageType.TEXT) && StringUtils.isBlank(messageDTO.getMessage())) {
            throw new IllegalArgumentException(ExceptionMessage.MESSAGE_IS_BLANK);
        }

        // Tạo đối tượng Message
        Message.MessageBuilder messageBuilder = Message.builder()
                .messageType(messageType)
                .message("")  // Default empty message for non-TEXT types
                .sender(userService.findById(UUID.fromString(messageDTO.getSenderId())));

        // Xử lý nội dung tin nhắn TEXT hoặc IMAGE
        if (messageType.equals(MessageType.TEXT)) {
            messageBuilder.message(messageDTO.getMessage());
        } else if (messageType.equals(MessageType.IMAGE)) {
            // If multiple image URLs are provided, assign them as a list
            if (messageDTO.getImageUrls() == null || messageDTO.getImageUrls().isEmpty()) {
                throw new IllegalArgumentException("Image URL(s) must be provided for image messages.");
            }
            messageBuilder.imageUrls(messageDTO.getImageUrls());
        } else if (messageType.equals(MessageType.IMAGE_AND_TEXT)) {
            messageBuilder.message(messageDTO.getMessage());
            // If multiple image URLs are provided, assign them as a list
            if (messageDTO.getImageUrls() == null || messageDTO.getImageUrls().isEmpty()) {
                throw new IllegalArgumentException("Image URL(s) must be provided for image and text messages.");
            }
            messageBuilder.imageUrls(messageDTO.getImageUrls());
        }

        Message message = messageBuilder.build();

        // Liên kết với cuộc trò chuyện nếu có
        if (!StringUtils.isBlank(messageDTO.getConversationId())) {
            message.setConversation(conversationService.findById(UUID.fromString(messageDTO.getConversationId())));
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
        InputStream serviceAccount = new ClassPathResource(serviceAccountFileName).getInputStream();

//        File serviceAccountFile = ResourceUtils.getFile("classpath:" + serviceAccountFileName);
//        FileInputStream serviceAccount = new FileInputStream(serviceAccountFile);

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
