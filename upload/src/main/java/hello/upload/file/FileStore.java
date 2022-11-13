package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    // 여러개의 파일이 들어올때
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    // 단일파일이 들어올떄
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        // 파일의 이름을 가져온다.
        String originalFilename = multipartFile.getOriginalFilename();
        // uuid를 붙인 파일을 가져온다.
        String storeFileName = createStoreFileName(originalFilename);

        // 파일을 스토리지에 저장한다.
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        // 기존파일명과 uuid를 붙인 파일명을 uploadFile에 담아서 return 한다.
        return new UploadFile(originalFilename, storeFileName);
    }

    // 확장자를 uuid와 붙여서 파일명을 return한다.
    private String createStoreFileName(String originalFilename) {
        // 확장자를 가져온다.
        String ext = extractExt(originalFilename);

        // 서버에 저장하는 파일명
        String uuid = UUID.randomUUID().toString();
        // uuid와 함께 학장자를 붙여서 파일을 만들어준다.

        return uuid + "." + ext;
    }

    // 파일의 확장자를 가져온다.
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }


}
