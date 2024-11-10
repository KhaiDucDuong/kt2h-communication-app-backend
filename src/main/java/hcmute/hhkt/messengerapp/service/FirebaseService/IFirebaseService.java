package hcmute.hhkt.messengerapp.service.FirebaseService;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public interface IFirebaseService {
    String uploadImage(MultipartFile file, String fileName) throws IOException;

    String uploadImage(MultipartFile file, String fileName, String folderName) throws IOException;

    String resizeAndUploadImage(MultipartFile file, String fileName, String folderName) throws IOException;

    String resizeAndUploadImage(MultipartFile file, String fileName, String folderName, int outputWidth, int outputHeight) throws IOException;
}
