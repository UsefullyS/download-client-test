package de.meondi.downloadclienttest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpMethod;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.Objects;

@SpringBootApplication
public class DownloadClientTestApplication implements CommandLineRunner {

    private final RestTemplate restTemplate;
    @Value("${app.server.url}")
    private String serverUrl;
    @Value("${app.server.fileName}")
    private String fileName;
    @Value("${app.client.folder}")
    private String downloadFolder;

    public DownloadClientTestApplication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(DownloadClientTestApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String url = serverUrl.concat("/api/v1/downloadfile/").concat(fileName);
        Path filePath = Path.of(downloadFolder, fileName);
        File dlFile = restTemplate.execute(
                url,
                HttpMethod.GET,
                null,
                clientHttpResponse -> {
                    File ret2 = new File(filePath.toString());
                    StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret2));
                    return ret2;
                }
        );
        assert Objects.requireNonNull(dlFile).exists();
    }
}
