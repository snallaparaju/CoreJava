package encryptdecrypt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {
//    private final static String fileLocation = "C:\\SelfDevelopment\\Certification\\";

    public static void main(String[] args) throws IOException {
        Map<String, String> stringStringMap = parseArguments(args);

        String mode = stringStringMap.get("mode");
        int key = Integer.parseInt(stringStringMap.get("key"));
        String algo = stringStringMap.getOrDefault("alg", "shift");
        String outFile = stringStringMap.get("out");
        String fileName = stringStringMap.get("in");

        switch (algo) {
            case "shift" -> {
                switch (mode) {
                    case "enc" -> encodeForShiftAlgo(key, fileName, outFile);
                    case "dec" -> decodeForShiftAlgo(key, fileName, outFile);
                }
            }
            case "unicode" -> encryptionUnicode(mode, key, fileName, outFile);

        }
    }


    public static void encodeForShiftAlgo(int key, String inputFileName, String outPutFileName) throws IOException {
        String stringBuilder = "";
        Path path = Paths.get(inputFileName);
        String rawData = Files.readString(path);
        for (int i = 0; i < rawData.length(); i++) {
            char rawchar = rawData.charAt(i);
            if (rawchar >= 97 && rawchar <= 122) {
                if (rawchar + key > 122) {
                    int overAsciiValue = (rawchar + key) - 123;
                    rawchar = (char) (97 + overAsciiValue);
                    stringBuilder += (rawchar);
                } else {
                    rawchar = (char) (rawchar + key);
                    stringBuilder += (rawchar);
                }
            } else if (rawchar >= 65 && rawchar <= 90) {
                if (rawchar + key > 90) {
                    int overAsciiValue = (rawchar + key) - 91;
                    rawchar = (char) (65 + overAsciiValue);
                    stringBuilder += (rawchar);
                } else {
                    rawchar = (char) (rawchar + key);
                    stringBuilder += (rawchar);
                }
            } else {
                stringBuilder += (rawchar);
            }
        }
        Path outPath = Paths.get(outPutFileName);
        Files.writeString(outPath, stringBuilder);
    }

    public static void decodeForShiftAlgo(int key, String inputFileName, String outPutFileName) throws IOException {
        Path path = Paths.get(inputFileName);
        String rawData = Files.readString(path);
        String stringBuilder = "";
        for (int i = 0; i < rawData.length(); i++) {
            char rawChar = rawData.charAt(i);
            if ((rawChar >= 'a' && rawChar <= 'z') || (rawChar >= 'A' && rawChar <= 'Z')) {
                int keyAdjusted = key % 26; // Ensure key is within range of 26 for alphabet wrap-around
                char base = (Character.isLowerCase(rawChar)) ? 'a' : 'A';
                int shifted = rawChar - base - keyAdjusted;
                if (shifted < 0) {
                    shifted += 26; // Wrap around if shifted is negative
                }
                char shiftedChar = (char) (base + shifted);
                stringBuilder += (shiftedChar);
            } else {
                stringBuilder += (rawChar); // Append unchanged for non-alphabetic characters
            }
        }
        Path outPath = Paths.get(outPutFileName);
        Files.writeString(outPath, stringBuilder);
    }


    public static void encryptionUnicode(String mode, int key, String inputFileName, String outPutFileName) throws IOException {
        Path path = Paths.get( inputFileName);
        String rawData = Files.readString(path);
        String encyptedData = "";
        for (int i = 0; i < rawData.length(); i++) {
            char rawChar = rawData.charAt(i);
            if (mode.equals("enc"))
                encyptedData += ((char) (rawChar + key));
            else
                encyptedData += ((char) (rawChar - key));
        }
        Path outPath = Paths.get(outPutFileName);
        Files.writeString(outPath, encyptedData);
    }

    private static Map<String, String> parseArguments(String[] args) {
        Map<String, String> arguments = new HashMap<>();
        String currentKey = null;
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;
        boolean algSpecified = false; // Flag to check if -alg argument is specified

        for (String arg : args) {
            if (arg.startsWith("-")) {
                if (currentKey != null && !currentKey.equals("alg")) {
                    arguments.put(currentKey, currentValue.toString().trim());
                    currentValue.setLength(0);
                }
                currentKey = arg.substring(1); // Remove leading "-"
                if (currentKey.equals("alg")) {
                    algSpecified = true;
                }
            } else if (arg.startsWith("\"")) {
                inQuotes = true;
                currentValue.append(arg.substring(1));
            } else if (arg.endsWith("\"")) {
                inQuotes = false;
                currentValue.append(" ").append(arg.substring(0, arg.length() - 1));
                arguments.put(currentKey, currentValue.toString().trim());
                currentValue.setLength(0);
                currentKey = null;
            } else if (inQuotes) {
                currentValue.append(" ").append(arg);
            } else {
                currentValue.append(arg);
                arguments.put(currentKey, currentValue.toString().trim());
                currentValue.setLength(0);
                currentKey = null;
            }
        }

        // If currentKey is still set and currentValue has data, put it in arguments
        if (currentKey != null && !currentKey.equals("alg") && currentValue.length() > 0) {
            arguments.put(currentKey, currentValue.toString().trim());
        }

        // Default -alg to "shift" if not specified
        if (!algSpecified) {
            arguments.put("alg", "shift");
        }

        return arguments;
    }
}