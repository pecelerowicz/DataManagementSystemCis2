package gov.ncbj.nomaten.datamanagementbackend.service.support;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static gov.ncbj.nomaten.datamanagementbackend.constants.Constants.*;

@Service
public class ZipService {
    public Path zipFile(String fileNameWithPath) {
        Path sourceFilePath = FileSystems.getDefault().getPath(STORAGE, TEM, fileNameWithPath);
        String zippedFileName = changeExtensionToZip(sourceFilePath.getFileName().toString());
        Path zipFilePath = FileSystems.getDefault().getPath(STORAGE, ZIP, zippedFileName);
        try {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {
                ZipEntry zipEntry = new ZipEntry(sourceFilePath.getFileName().toString());
                zipOutputStream.putNextEntry(zipEntry);
                Files.copy(sourceFilePath, zipOutputStream);
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipFilePath;
    }

    private String changeExtensionToZip(String fileNameWithExtension) {
        if(!fileNameWithExtension.contains(".")) {
            return fileNameWithExtension;
        } else if(fileNameWithExtension.split("\\.").length > 2) {
            throw new RuntimeException("Invalid file name");
        } else {
            return fileNameWithExtension.split("\\.")[0].concat(".zip");
        }
    }
}
