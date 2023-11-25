package com.example.hotel.service;

import com.example.hotel.Application;
import com.example.hotel.domain.BorrowedRoom;
import com.ibm.icu.text.Transliterator;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class ReportGenerationService {

    private String transliteration(String strInCyrillic) {

        String CYRILLIC_TO_LATIN = "Russian-Latin/BGN";
        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
        return toLatinTrans.transliterate(strInCyrillic);
    }
    public String generatePdf(List<BorrowedRoom> borrowedRooms, String filename) {
        Document document = new Document(PageSize.A4);

        String path = "src/main/resources/report/" + filename;

        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            PdfPTable table = new PdfPTable(6);

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

            table.addCell("#");
            table.addCell(transliteration("Отель"));
            table.addCell(transliteration("Номер"));
            table.addCell(transliteration("Тип номера"));
            table.addCell(transliteration("Даты"));
            table.addCell(transliteration("Статус"));

            int i=1;
            for (BorrowedRoom record : borrowedRooms) {
                table.addCell(String.valueOf(i) + ".");
                table.addCell(transliteration(record.getRoom().getRoomType().getHotel().getName()));
                table.addCell(transliteration(record.getRoom().getNumber()));
                table.addCell(transliteration(record.getRoom().getRoomType().getName()));
                table.addCell(record.getLocalStartDate() + " - " + record.getLocalEndDate());
                table.addCell(transliteration(record.isCancelled()? "Отменен": "Завершен"));
                i++;
            }

            document.add(table);
            document.close();

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }

        return path;
    }
}
