package com.example.hotel.service;

import com.example.hotel.Application;
import com.example.hotel.domain.BorrowedRoom;
import com.ibm.icu.text.Transliterator;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;

@Service
public class QRGenerationService {
    public String createContent(BorrowedRoom record) {

        Hibernate.initialize(record.getRoom());
        Hibernate.initialize(record.getRoom().getRoomType());
        Hibernate.initialize(record.getRoom().getRoomType().getHotel());

        if (record == null) return "";

        BaseFont baseFont=null;
        try {
            baseFont = BaseFont.createFont(Objects.requireNonNull(Application.class.getResource(
                            "/fonts/ChampagneAndLimousinesBoldItalic-dqex.ttf")).toString(),
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.YYYY");

        String content = "Ticket #" + record.getId() + '\n' +
                "Hotel: " + record.getRoom().getRoomType().getHotel().getName() + "\n" +
                "Room: " + record.getRoom().getNumber() + "\n" +
                "From: " + fmt.format(record.getStartDate()) + "\n" +
                "To: " + fmt.format(record.getEndDate());

        content = transliteration(content);

        return content;
    }
    public String createQR(String content, Long id){
        try {
            Document document = new Document(new Rectangle(600, 600));
            String filename = "src/main/resources/qr/qr" + id + ".pdf";
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename, false));
            document.open();
            BarcodeQRCode my_code = new BarcodeQRCode(content, 400, 400, null);
            Image qr_image = my_code.getImage();
            qr_image.setAlignment(Element.ALIGN_CENTER);
            document.add(qr_image);
            document.close();
            return filename;
        } catch (Exception e) {
        }
        return "";
    }

    private String transliteration(String strInCyrillic) {

        String CYRILLIC_TO_LATIN = "Russian-Latin/BGN";
        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
        return toLatinTrans.transliterate(strInCyrillic);
    }
}
