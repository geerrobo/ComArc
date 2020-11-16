import java.io.*;
import java.util.Scanner;

public class simulate {
    static int size = 50;
    String inst[] = { "add", "nand", "lw", "sw", "beq", "jalr" };
    String inst2[] = { "halt", "noop", ".fill" };
    String[][] mem = new String[size][4];
    String[] pc = new String[size];
    String path;

    public simulate(String file) throws Exception {
        path = file + "machine.txt";

        File fr = new File(path);
        Scanner sc = new Scanner(fr);

        for (int i = 0; sc.hasNextLine(); i++) {
            pc[i] = sc.nextLine();
        }

        for (int i = 0; i < pc.length && pc[i] != null; i++) {
            for (int j = 0; j < 4; j++) {
                mem[i][j] = getTag(pc[i], j);
            }
        }
    }

    private String getTag(String str, int pos) {
        str = reverseString(str);
        if (pos == 0) {
            return getOpcode(str);
        } else {
            String opCode = getOpcode(str);
            if (opCode == "000" || opCode == "001") { // R-type
                switch (pos) {
                    case 1:
                        return getCode(str, 19, 21);

                    case 2:
                        return getCode(str, 16, 18);

                    case 3:
                        return getCode(str, 0, 2);

                    default:
                        break;
                }
            } else if (opCode == "010" || opCode == "011" || opCode == "100") { // I-type
                switch (pos) {
                    case 1:
                        return getCode(str, 19, 21);

                    case 2:
                        return getCode(str, 16, 18);

                    case 3:
                        return getCode(str, 0, 15);

                    default:
                        break;
                }
            } else if (opCode == "101") { // J-type
                switch (pos) {
                    case 1:
                        return getCode(str, 19, 21);

                    case 2:
                        return getCode(str, 16, 18);

                    default:
                        break;
                }
            } else return null;
        }

        return null;
    }

    private String getCode(String str, int begin, int end) {
        String temp = "";
        for (int i = begin; i <= end; i++) {
            temp += str.charAt(i);
        }
        return temp;
    }

    private String getOpcode(String str) {
        String temp = "";
        for (int i = 22; i <= 24; i++) {
            temp += str.charAt(i);
        }
        return temp;
    }

    private String reverseString(String str) {
        // getBytes() method to convert string
        // into bytes[].
        byte[] strAsByteArray = str.getBytes();

        byte[] result = new byte[strAsByteArray.length];

        // Store result in reverse order into the
        // result byte[]
        for (int i = 0; i < strAsByteArray.length; i++) {
            result[i] = strAsByteArray[strAsByteArray.length - i - 1];
        }

        return new String(result);
    }
}
