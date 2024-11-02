package hcmute.hhkt.messengerapp.configuration;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @Bean
    public FirebaseApp firebaseInit() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("C:/Users/Dell.MM/hoang/kt2h-communication-app-backend/src/main/resources/hkt-e0d9b-firebase-adminsdk-6b37m-3bbc9aeeeb.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://hkt-e0d9b-default-rtdb.firebaseio.com/")
                .build();

        return FirebaseApp.initializeApp(options);
    }
}
