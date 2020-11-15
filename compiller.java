import java.io.*;
import java.util.Scanner;

public class compiller {
    String inst[] = { "add", "nand", "lw", "sw", "beq", "jalr" };
    String inst2[] = { "halt", "noop", ".fill" };
    int[][] mem = new int[50][];
    String[] pc = new String[50];
    String path;

    public compiller(String file) throws Exception {
        path = file + "assembly.txt";

        File fr = new File(path);
        Scanner sc = new Scanner(fr);

        for (int i = 0; sc.hasNextLine(); i++) {
            pc[i] = sc.nextLine();
        }
        for (int i = 0; i < pc.length; i++) {
            if (pc[i] == null)
                break;
            pc[i] = translate1(pc[i], i);
        }
        for (int i = 0; i < pc.length; i++) {
            if (pc[i] == null)
                break;
            pc[i] = translate2(pc[i], i);
        }
        for (int i = 0; i < pc.length; i++) {
            if (pc[i] == null)
                break;
            pc[i] = toBinary(pc[i], i);
        }

        printState();

        try {
            File myObj = new File(file + "machine.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter(file + "machine.txt");
            for (String s : pc) {
                if (s == null)
                    break;
                myWriter.write(s + "\n");
            }
            myWriter.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String toBinary(String str, int index) {
        String temp = "";

        try {
            String[] array = str.split(" ");

            System.out.println(array[0]);

            switch (array[0]) {
                case "add":
                    array[0] = "000";
                    array[1] = decToBinary(array[1]);
                    array[2] = decToBinary(array[2]);
                    array[3] = decToBinary(array[3]);
                    while (array[3].length() < 16) {
                        array[3] = "0" + array[3];
                    }
                    break;

                case "nand":
                    array[0] = "001";
                    array[1] = decToBinary(array[1]);
                    array[2] = decToBinary(array[2]);
                    array[3] = decToBinary(array[3]);
                    while (array[3].length() < 16) {
                        array[3] = "0" + array[3];
                    }
                    break;

                case "lw":
                    array[0] = "010";
                    array[1] = decToBinary(array[1]);
                    array[2] = decToBinary(array[2]);
                    array[3] = decToBinary(array[3]);
                    while (array[3].length() < 16) {
                        array[3] = "0" + array[3];
                    }
                    break;

                case "sw":
                    array[0] = "011";
                    array[1] = decToBinary(array[1]);
                    array[2] = decToBinary(array[2]);
                    array[3] = decToBinary(array[3]);
                    while (array[3].length() < 16) {
                        array[3] = "0" + array[3];
                    }
                    break;

                case "beq":
                    array[0] = "100";
                    array[1] = decToBinary(array[1]);
                    array[2] = decToBinary(array[2]);
                    array[3] = decToBinary(array[3]);
                    while (array[3].length() < 16) {
                        array[3] = "0" + array[3];
                    }
                    break;

                case "jalr":
                    array[0] = "101";
                    array[1] = decToBinary(array[1]);
                    array[2] = decToBinary(array[2]);
                    array[3] = decToBinary(array[3]);
                    while (array[3].length() < 16) {
                        array[3] = "0" + array[3];
                    }
                    break;

                case "halt":
                    array[0] = "110";
                    while (array[0].length() < 25) {
                        array[0] += "0";
                    }
                    break;

                case "noop":
                    array[0] = "111";
                    while (array[0].length() < 25) {
                        array[0] += "0";
                    }
                    break;

                default:
                    for (int i = 0; i < array.length; i++) {
                        if (array[i].equals(".fill")) {
                            for (int j = 0; j <= i; j++) {
                                array[j] = "0";
                            }
                            array[i + 1] = decToBinary(array[i + 1]);
                        }
                    }
                    break;
            }

            for (String s : array) {
                while (s.length() < 3) {
                    s = "0" + s;
                }
                temp += s;
            }

            while (temp.length() < 32) {
                temp = 0 + temp;
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return temp;
    }

    private String decToBinary(String str) {
        if (Integer.parseInt(str) < 0) {
            String st = Integer.toBinaryString(Integer.parseInt(str));
            st = st.substring(st.length() - 16, st.length());
            return st;
        } else {
            return Integer.toBinaryString(Integer.parseInt(str));
        }
    }

    private String translate2(String str, int index) {
        String[] array = str.split(" ");

        if (array.length <= 2)
            return str;

        if (matchinstruc(array[1])) {
            String temp = "";
            for (int i = 1; i < array.length; i++) {
                temp += array[i] + " ";
            }
            return temp;
        }

        return str;
    }

    private String translate1(String str, int index) {
        String[] array = str.split(" ");

        if (array.length <= 1)
            return str;

        // normal form
        if (matchinstruc(array[0])) {
            // System.out.println("matchinstruc");
            for (int i = 1; i < array.length; i++) {
                if (isInteger(array[i])) {
                    int temp = Integer.parseInt(array[i]);
                    array[i] = Integer.toBinaryString(temp);
                } else {
                    for (int j = 0; j < pc.length; j++) {
                        if (pc[j] == null)
                            break;
                        if (pc[j].matches(array[i] + " .fill (.*)")) {
                            return pc[index].replaceAll(array[i], findValue(array[i]));
                        }
                    }
                }
            }
        } else if (array[1].equals(".fill")) { // variable after .fill
            if (isInteger(array[2])) {

            } else {
                String resultIndex = Integer.toString(fineIndex(array[2], index));
                replaceStringIndex(array[2], resultIndex);
                array[2] = resultIndex;

                str = "";
                for (String s : array) {
                    str += s + " ";
                }
                return str;
            }
        } else {
            for (String s : array) {
                if (s.equals("halt")) {
                    return "halt";
                }
            }
        }
        return str;
    }

    private void replaceStringIndex(String str, String resultIndex) {
        for (int i = 0; i < pc.length; i++) {
            if (pc[i] == null)
                break;
            String[] array = pc[i].split(" ");
            for (int j = 1; j < array.length; j++) {
                if (array[j].equals(str)) {
                    int finalIndex = Integer.parseInt(resultIndex) - 1 - i;
                    array[j] = Integer.toString(finalIndex);
                    pc[i] = "";
                    for (String s : array) {
                        pc[i] += s + " ";
                    }
                }
            }
        }
    }

    private int fineIndex(String str, int index) {
        for (int i = 0; i < pc.length; i++) {
            if (pc[i] == null)
                break;
            if (pc[i].matches(str + " (.*)")) {
                return i;
            }
        }
        return 0;
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String findValue(String str) {
        for (int i = 0; i < pc.length; i++) {
            if (pc[i] == null)
                break;
            if (pc[i].matches(str + " .fill (.*)")) {
                String[] array = pc[i].split(" ");
                // System.out.println(str + " = " + array[2]);
                return array[2];
            }
        }
        return str;
    }

    private boolean matchinstruc(String str) {
        for (String s : inst) {
            if (str.matches(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchinstruc2(String str) {
        for (String s : inst2) {
            if (str.matches(s)) {
                return true;
            }
        }
        return false;
    }

    private void printState() {
        System.out.println("==================");
        for (String str : pc) {
            if (str != null)
                System.out.println(str);
        }
    }
}
