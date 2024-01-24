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
    public String extractExt(String filename){
        int index = filename.lastIndexOf(".");
        return filename.substring(index+1);
    }

    public String createStoreFileName(String originalFileName){
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFileName);
        return uuid+"."+ext;
    }

    public String getFullPath(String fileName){
        return fileDir+fileName;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);
        String fullPath = getFullPath(storeFileName);
        multipartFile.transferTo(new File(fullPath));
        return new UploadFile(originalFileName,storeFileName);
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()){
                UploadFile uploadFile = storeFile(multipartFile);
                storeFileResult.add(uploadFile);
            }
        }
        return storeFileResult;
    }
}
