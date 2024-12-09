package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class PDFUtils {

    public static void addQRCodeWithDetails(Document document) throws Exception {
        String qrData = generateQRContent();
        byte[] qrCodeBytes = generateQRCode(qrData, 150, 150);
        if (qrCodeBytes == null || qrCodeBytes.length == 0) {
            throw new Exception("Erreur lors de la génération du QR code.");
        }

        Image qrCodeImage = Image.getInstance(qrCodeBytes);
        qrCodeImage.scaleToFit(150, 150);
        document.add(qrCodeImage);
    }

    public static void addImageToPDF(Document document, String imagePath) throws Exception {
        Image image = Image.getInstance(imagePath);
        image.scaleToFit(200, 100); // Redimensionner l'image si nécessaire
        image.setAlignment(Image.ALIGN_CENTER); // Centrer l'image
        document.add(image);
    }

    private static String generateQRContent() {
        String deliveryDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String expirationDate = LocalDate.now().plusMonths(3).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return "Délivré par MairieExpress\n" +
                "Date de délivrance: " + deliveryDate + "\n" +
                "Date d'expiration: " + expirationDate;
    }

    private static byte[] generateQRCode(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

        BufferedImage qrImage = toBufferedImage(qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints));
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(qrImage, "png", baos);
            return baos.toByteArray();
        }
    }

    private static BufferedImage toBufferedImage(com.google.zxing.common.BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0x000000 : 0xFFFFFF);
            }
        }
        return image;
    }
}
