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
public class FileStore
{
    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename)
    {
        return fileDir + filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException
    {
        List<UploadFile> storeFileResult = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles)
        {
            if (!multipartFile.isEmpty())
            {
                UploadFile uploadFile = storeFile(multipartFile);
                storeFileResult.add(uploadFile);
            }
        }
        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException
    {
        if (multipartFile.isEmpty())
        {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        //image.png

        String storeFileName = createStoreFileName(originalFilename);
        //"qwe-qwe-qwe-qqwe.png"

        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(originalFilename, storeFileName);
    }

    private static String createStoreFileName(String originalFilename)
    {
        //서버에 저장하는 파일명
        //"qwe-qwe-qwe-qqwe.png"
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    //확장자 추출
    private static String extractExt(String originalFilename)
    {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
