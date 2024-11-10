package hcmute.hhkt.messengerapp.util;

import hcmute.hhkt.messengerapp.constant.ExceptionMessage;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ImageUtil {
    private static String[] ALLOWED_FILE_TYPES = {"image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"};
    private static Long MAX_FILE_SIZE = (long) (2 * 1024 * 1024); //2 MBs
    public static boolean isImgNull(MultipartFile value) {
        if(Objects.requireNonNull(value.getOriginalFilename()).isEmpty() ||
                value.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isImgValid(MultipartFile value) {
        if(isImgNull(value)) {
            return false;
        }

        if(value.getSize() > MAX_FILE_SIZE)
            return false;

        try{
            BufferedImage image = ImageIO.read(value.getInputStream());
            if(image == null){
                return false;
            }
        } catch (IOException e) {
            return false;
        }

        for (String type : ALLOWED_FILE_TYPES) {
            if(type.equals(value.getContentType())) {
                return true;
            }
        }
        return false;
    }

    public static String getFileExtension(MultipartFile value){
        return Objects.requireNonNull(value.getContentType()).substring(6);
    }

    public static InputStream resizeImage(MultipartFile value, int width, int height) {
        if(width <= 0 || height <= 0){
            throw new IllegalArgumentException("Width and height cannot not be less than 1");
        }

        try{
            BufferedImage sourceImage = ImageIO.read(value.getInputStream());

            Image thumbnail = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage bufferedThumbnail = new BufferedImage(thumbnail.getWidth(null),
                    thumbnail.getHeight(null),
                    BufferedImage.TYPE_INT_RGB);
            bufferedThumbnail.getGraphics().drawImage(thumbnail, 0, 0, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedThumbnail, getFileExtension(value), baos);
            return new ByteArrayInputStream(baos.toByteArray());
        } catch(IOException e){
            return null;
        }
    }
}
