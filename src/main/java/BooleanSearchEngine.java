import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    private Map<String, List<PageEntry>> results; // слово - список файл/страница/количество

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        results = new HashMap<>();

        File[] fs = pdfsDir.listFiles();

        for (File curFile : fs){
            String pdf = curFile.getName();
            var doc = new PdfDocument(new PdfReader(curFile));
            for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                PdfPage page = doc.getPage(i);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");

                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    freqs.put(word.toLowerCase(), freqs.getOrDefault(word.toLowerCase(), 0) + 1);
                }

                for (Map.Entry<String, Integer> entry : freqs.entrySet()) {
                    PageEntry pageEntry = new PageEntry(pdf, i, entry.getValue());
                    String word = entry.getKey();
                    List listPageEntry = results.getOrDefault(word, new ArrayList<>());
                    listPageEntry.add(pageEntry);
                    results.put(word, listPageEntry);
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> result = results.getOrDefault(word, Collections.emptyList());
        result.sort(PageEntry::compareTo);
        return result;
    }
}
