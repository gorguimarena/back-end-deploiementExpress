package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Image;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import javax.imageio.ImageIO;

public class PDFUtils {

    // Génération du QR Code
    public static byte[] generateQRCode(String data, int width, int height) throws Exception {
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // Création du QR code
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hints);

        // Convertir le BitMatrix en BufferedImage
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                image.setRGB(i, j, matrix.get(i, j) ? 0x000000 : 0xFFFFFF);
            }
        }

        // Convertir BufferedImage en byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        return baos.toByteArray();
    }

    // Ajouter le QR code dans le PDF en bas de la page
    public static void addQRCodeToPDF(Document document, String data) throws Exception {
        // Générer le QR code
        byte[] qrCodeBytes = generateQRCode(data, 150, 150);

        // Convertir les bytes en image iText
        Image qrCodeImage = Image.getInstance(qrCodeBytes);
        qrCodeImage.scaleToFit(150, 150);

        // Placer l'image à la position souhaitée (en bas de la page)
        qrCodeImage.setAbsolutePosition(100, 50); // Ajustez les coordonnées selon vos besoins

        // Ajouter l'image à votre document
        document.add(qrCodeImage);
    }

    // Ajouter une image à partir d'un fichier ou du chemin de l'image
    public static void addImageToPDF(Document document, String imagePath) throws IOException, DocumentException {
        // Charger l'image à partir du chemin
        Image image = Image.getInstance(imagePath);
        image.scaleToFit(100, 100);  // Ajustez la taille selon vos besoins
        image.setAbsolutePosition(200, 50); // Positionner l'image dans le PDF
        document.add(image);
    }

    // Exemple d'utilisation dans la génération de votre PDF
    public static void generatePdfWithQRCode(Demande demande, Object documentInfos) throws DocumentException, IOException {
        Document document = new Document();
        // Logic for PDF creation (add title, paragraphs, etc.)

        // Exemple de texte à ajouter au document
        document.add(new Paragraph("Délivré par MairieExpress"));

        // Ajouter un QR code avec la date de délivrance et d'expiration (3 mois après la date)
        String qrCodeData = "Date de délivrance: " + new java.util.Date() + "\nDate d'expiration: " + getExpirationDate();

        // Gérer les exceptions ici
        try {
            addQRCodeToPDF(document, qrCodeData);
            addImageToPDF(document, "src/main/resources/static/signatureMairieExpress.png");
        } catch (Exception e) {
            e.printStackTrace();
            // Gestion d'erreur si nécessaire
        }

        // Finaliser la génération du PDF
        document.close();
    }

    // Méthode pour calculer la date d'expiration (3 mois après la date actuelle)
    private static String getExpirationDate() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.add(java.util.Calendar.MONTH, 3); // Ajouter 3 mois à la date actuelle
        java.util.Date expirationDate = calendar.getTime();
        return expirationDate.toString();
    }
}
