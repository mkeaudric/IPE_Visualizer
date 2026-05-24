import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class InsertLine {
    public void insertTextAtLine(Path filePath, int lineNumber, String textToInsert) throws IOException {
        List<String> lines = Files.readAllLines(filePath); // baca seluruh baris file, simpen
        
        int targetIndex = lineNumber - 1; // line dimulai dari 1, jadiin dari 0

        if (targetIndex >= lines.size()) { // validasi jika baris yang diminta melebihi kapasitas file saat ini
            // jika melebihi, tambahkan baris kosong hingga mencapai target baris tersebut
            while (lines.size() < targetIndex) {
                lines.add("");
            }
            lines.add(textToInsert);
        } else {
            lines.add(targetIndex, textToInsert); // sisipkan teks ke baris yang dituju (baris lama akan bergeser ke bawah)
        }

        Files.write(filePath, lines);
    }

    
}
