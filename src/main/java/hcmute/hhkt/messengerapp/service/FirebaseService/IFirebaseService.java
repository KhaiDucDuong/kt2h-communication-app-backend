package hcmute.hhkt.messengerapp.service.FirebaseService;


import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface IFirebaseService {
    String uploadImage(MultipartFile file, String fileName) throws IOException;

    String uploadImage(MultipartFile file, String fileName, String folderName) throws IOException;

    String resizeAndUploadImage(MultipartFile file, String fileName, String folderName) throws IOException;

    String resizeAndUploadImage(MultipartFile file, String fileName, String folderName, int outputWidth, int outputHeight) throws IOException;
}
