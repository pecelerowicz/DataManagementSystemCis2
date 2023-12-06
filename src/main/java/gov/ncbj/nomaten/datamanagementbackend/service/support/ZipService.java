package gov.ncbj.nomaten.datamanagementbackend.service.support;

import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipService {
    public void zipFile(Path sourcePath, Path outputPath) {
        try {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(outputPath))) {
                ZipEntry zipEntry = new ZipEntry(sourcePath.getFileName().toString());
                zipOutputStream.putNextEntry(zipEntry);
                Files.copy(sourcePath, zipOutputStream);
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void zipFiles(List<Path> sourcePaths, Path outputPath) {
        try {
            zipFilesPrivate(sourcePaths, outputPath);
            System.out.println("Files zipped successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void zipFilesPrivate(List<Path> filesToZip, Path zipFilePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath.toFile());
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            byte[] buffer = new byte[1024];

            for (Path filePath : filesToZip) {
                if (!Files.exists(filePath)) {
                    System.out.println("File not found: " + filePath);
                    continue;
                }

                ZipEntry zipEntry = new ZipEntry(filePath.toString());
                zos.putNextEntry(zipEntry);

                try (InputStream fis = Files.newInputStream(filePath)) {
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                }

                zos.closeEntry();
            }
        }
    }

    public void zipFolder(Path sourcePath, Path outputPath) {
        try {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(outputPath))) {
                Files.walk(sourcePath)
                        .filter(path -> !Files.isDirectory(path))
                        .forEach(path -> {
                            ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(path).toString());
                            try {
                                zipOutputStream.putNextEntry(zipEntry);
                                Files.copy(path, zipOutputStream);
                                zipOutputStream.closeEntry();
                            } catch (IOException e) {
                                e.printStackTrace(); // Handle the exception according to your requirements
                            }
                        });
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your requirements
        }
    }

}
