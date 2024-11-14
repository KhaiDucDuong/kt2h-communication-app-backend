package hcmute.hhkt.messengerapp.service.FirebaseService;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import hcmute.hhkt.messengerapp.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class FirebaseServiceImpl implements IFirebaseService{
    @Value("${firebase.service-account-filename}")
    private String serviceAccountFileName;
    @Value("${firebase.project-id}")
    private String firebaseProjectId;
    @Value("${firebase.bucket-url}")
    private String bucketUrl;
    @Value("${firebase.file-starter-url}")
    private String fileStarterUrl;
    private final String IMAGE_FOLDER_NAME = "images/";
    private final Integer DEFAULT_IMG_HEIGHT = 500, DEFAULT_IMG_WIDTH = 500;

    /**
     * Uploads an image file to Firebase storage, with optional resizing.
     *
     * @param file The MultipartFile representing the image file to upload.
     * @param fileName The name to assign to the uploaded file.
     * @param doResize A flag indicating whether to resize the image.
     * @param outputWidth The width for resizing the image if resizing is enabled.
     * @param outputHeight The height for resizing the image if resizing is enabled.
     * @return The URL of the uploaded image file.
     * @throws IOException if the file name is blank, the image file is invalid, or resizing fails.
     */
    private String uploadImageToFirebase(MultipartFile file, String fileName, boolean doResize, int outputWidth, int outputHeight) throws IOException {
        if(StringUtils.isBlank(fileName)){
            throw new IllegalArgumentException(ExceptionMessage.FILE_NAME_BLANK);
        }

        if(!ImageUtil.isImgValid(file)){
            throw new IOException(ExceptionMessage.IMAGE_INVALID);
        }

        Storage storage = getStorage();
        Bucket bucket = storage.get(bucketUrl);

        String filePathAndName = IMAGE_FOLDER_NAME + fileName;

        InputStream inputStream = doResize ? resizeImage(file, outputWidth, outputHeight) : file.getInputStream();

        bucket.create(filePathAndName, inputStream, file.getContentType());
        String encodedFileName = URLEncoder.encode(filePathAndName, StandardCharsets.UTF_8);

        return fileStarterUrl + encodedFileName + "?alt=media";
    }

    /**
     * Uploads an image file to Firebase storage in the default "images/" folder without resizing.
     *
     * @param file The MultipartFile representing the image file to upload.
     * @param fileName The name to assign to the uploaded file.
     * @return The URL of the uploaded image file.
     * @throws IOException if the file name is blank, the image file is invalid
     */
    @Override
    public String uploadImage(MultipartFile file, String fileName) throws IOException {
        return uploadImageToFirebase(file, fileName, false, 0, 0); //last 2 params don't affect the img because we don't resize
    }

    /**
     * Uploads an image file to a specified subfolder within the "images/" folder without resizing.
     *
     * @param file The MultipartFile representing the image file to upload.
     * @param fileName The name to assign to the uploaded file.
     * @param folderName The name of the subfolder inside "images/" where the file should be uploaded.
     * @return The URL of the uploaded image file.
     * @throws IOException if the file path or folder name is invalid.
     */
    @Override
    public String uploadImage(MultipartFile file, String fileName, String folderName) throws IOException {
        //check if the last char is '/' and length is > 2
        if(!StringUtils.contains(folderName.substring(folderName.length() - 1), "/") || !(folderName.length() > 2)){
            throw new IllegalArgumentException(ExceptionMessage.FIREBASE_FILE_PATH_INVALID);
        }

        String filePathAndName = folderName + fileName;
        return uploadImageToFirebase(file, filePathAndName, false, 0, 0); //last 2 params don't affect the img because we don't resize
    }

    /**
     * Resizes an image file and uploads it to a specified subfolder within the "images/" folder.
     *
     * @param file The MultipartFile representing the image file to upload.
     * @param fileName The name to assign to the uploaded file.
     * @param folderName The name of the subfolder inside "images/" where the file should be uploaded.
     * @return The URL of the uploaded image file.
     * @throws IOException if the file path, folder name, or resizing process is invalid.
     */
    @Override
    public String resizeAndUploadImage(MultipartFile file, String fileName, String folderName) throws IOException {
        //check if the last char is '/' and length is > 2
        if(!StringUtils.contains(folderName.substring(folderName.length() - 1), "/") || !(folderName.length() > 2)){
            throw new IllegalArgumentException(ExceptionMessage.FIREBASE_FILE_PATH_INVALID);
        }

        String filePathAndName = folderName + fileName;
        return uploadImageToFirebase(file, filePathAndName, true, DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT);
    }

    /**
     * Resizes an image file to specified dimensions and uploads it to a specified subfolder within the "images/" folder.
     *
     * @param file The MultipartFile representing the image file to upload.
     * @param fileName The name to assign to the uploaded file.
     * @param folderName The name of the subfolder inside "images/" where the file should be uploaded.
     * @param outputWidth The width for resizing the image.
     * @param outputHeight The height for resizing the image.
     * @return The URL of the uploaded image file.
     * @throws IOException if the file path, folder name, or resizing process is invalid.
     */
    @Override
    public String resizeAndUploadImage(MultipartFile file, String fileName, String folderName, int outputWidth, int outputHeight) throws IOException {
        //check if the last char is '/' and length is > 2
        if(!StringUtils.contains(folderName.substring(folderName.length() - 1), "/") || !(folderName.length() > 2)){
            throw new IllegalArgumentException(ExceptionMessage.FIREBASE_FILE_PATH_INVALID);
        }

        String filePathAndName = folderName + fileName;
        return uploadImageToFirebase(file, filePathAndName, true, outputWidth, outputHeight);
    }

    private Storage getStorage() throws IOException {
        InputStream serviceAccount = new ClassPathResource(serviceAccountFileName).getInputStream();

//        File serviceAccountFile = ResourceUtils.getFile("classpath:" + serviceAccountFileName);
//        FileInputStream serviceAccount = new FileInputStream(serviceAccountFile);

        return StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId(firebaseProjectId)
                .build()
                .getService();
    }

    private InputStream resizeImage(MultipartFile file, int outputWidth, int outputHeight) throws IOException {
        InputStream resizedInputStream = ImageUtil.resizeImage(file, outputWidth, outputHeight);

        if(resizedInputStream == null){
            throw new IOException(ExceptionMessage.RESIZE_IMAGE_ERROR);
        }
        return resizedInputStream;
    }
}
