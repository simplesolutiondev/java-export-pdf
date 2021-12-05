import com.lowagie.text.DocumentException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;

public class PdfFileExporter {

    public void exportPdfFile(String templateFileName, Map<String, Object> data, String pdfFileName) {
        String htmlContent = generateHtml(templateFileName, data);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(pdfFileName);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(fileOutputStream, false);
            renderer.finishPDF();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private String generateHtml(String templateFileName, Map<String, Object> data) {
        TemplateEngine templateEngine = createTemplateEngine();
        Context context = new Context();
        context.setVariables(data);
        String htmlContent = templateEngine.process(templateFileName, context);
        return htmlContent;
    }

    private TemplateEngine createTemplateEngine() {
        ClassLoaderTemplateResolver pdfTemplateResolver = new ClassLoaderTemplateResolver();
        pdfTemplateResolver.setPrefix("pdf-templates/");
        pdfTemplateResolver.setSuffix(".html");
        pdfTemplateResolver.setTemplateMode("HTML5");
        pdfTemplateResolver.setCharacterEncoding("UTF-8");
        pdfTemplateResolver.setOrder(1);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(pdfTemplateResolver);
        return templateEngine;
    }

}