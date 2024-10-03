import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Do you want to decompile image to bytecode? (yes/no)");
        String answer = scanner.nextLine();

        String projectDir = System.getProperty("user.dir");
        File imageFile = Paths.get(projectDir, "src", "img", "My-Image.png").toFile();
        String outputBinaryFile = Paths.get(projectDir, "src", "output", "image.bin").toString();
        File outputDir = new File(Paths.get(projectDir, "src", "output").toString());

        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        if (answer.equalsIgnoreCase("yes")) {
            convertImageToBinaryString(imageFile, outputBinaryFile);
        }
        else{
            return;
        }

        System.out.println("Do you want to recover image from bytecode? (yes/no)");
        answer = scanner.nextLine();

        String outputImageFile = Paths.get(projectDir, "src", "output", "restored_image.png").toString();

        if (answer.equalsIgnoreCase("yes")) {
            convertBinaryStringToImage(outputBinaryFile, outputImageFile);
        }
        else{
            return;
        }

        scanner.close();
    }

    // Decompile
    private static void convertImageToBinaryString(File imageFile, String outputBinaryFile) throws IOException {
        byte[] fileContent = Files.readAllBytes(imageFile.toPath());
        StringBuilder binaryString = new StringBuilder();

        for (byte b : fileContent) {
            binaryString.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }

        try (FileOutputStream fos = new FileOutputStream(outputBinaryFile)) {
            fos.write(binaryString.toString().getBytes());
            System.out.println("Image decompiled> " + outputBinaryFile);
        }
    }

    // Compile
    private static void convertBinaryStringToImage(String inputBinaryFile, String outputImageFile) throws IOException {
        File binaryFile = new File(inputBinaryFile);
        byte[] binaryContent = Files.readAllBytes(binaryFile.toPath());

        String binaryString = new String(binaryContent);
        byte[] fileContent = new byte[binaryString.length() / 8];

        for (int i = 0; i < fileContent.length; i++) {
            String byteString = binaryString.substring(i * 8, (i + 1) * 8);
            fileContent[i] = (byte) Integer.parseInt(byteString, 2);
        }

        try (FileOutputStream fos = new FileOutputStream(outputImageFile)) {
            fos.write(fileContent);
            System.out.println("Image restored> " + outputImageFile);
        }
    }
}