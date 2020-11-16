import java.io.*;
import java.util.Scanner;

public class compiller {
    static int size = 50;
    String inst[] = { "add", "nand", "lw", "sw", "beq", "jalr" };
    String inst2[] = { "halt", "noop", ".fill" };
    String[][] mem = new String[size][32];
    String[] pc = new String[size];
    String path;

    /**
     * main constructor
     * @param file file directory
     * @throws Exception error exception
     */
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
        printState();
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

    /**
     * assembly to machine code
     * @param str line of assembly
     * @param index number of line
     * @return machine code
     */
    private String toBinary(String str, int index) {
        String temp = "";

        try {
            String[] array = str.split(" ");

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

    /**
     * change decimal to binary
     * @param str decimal
     * @return binary
     */
    private String decToBinary(String str) {
        if (Integer.parseInt(str) < 0) {
            String st = Integer.toBinaryString(Integer.parseInt(str));
            st = st.substring(st.length() - 16, st.length());
            return st;
        } else {
            return Integer.toBinaryString(Integer.parseInt(str));
        }
    }

    /**
     * main function for reading assembly part 2
     * @param str line of assembly
     * @param index number of line
     * @return new line format
     */
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

    /**
     * main function for reading assembly part 1
     * @param str line of assembly
     * @param index number of line
     * @return new line format
     */
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
                            return pc[index].replaceAll(array[i], Integer.toString(j));
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

    /**
     * replace label in regA, regB and offset with index
     * @param str label
     * @param resultIndex index for replace
     */
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

    /**
     * fine index of .fill
     * @param str label
     * @param index row of pc
     * @return index of .fill
     */
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

    /**
     * Is integer?
     * @param str text for check
     * @return true for integer, false for not integer
     */
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * fine value of label in regA, regB or offset
     * @param str label
     * @return value of str
     */
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

    /**
     * Is label = { "add", "nand", "lw", "sw", "beq", "jalr" }
     * @param str label
     * @return true is label and false is not label
     */
    private boolean matchinstruc(String str) {
        for (String s : inst) {
            if (str.matches(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * print present state
     */
    private void printState() {
        System.out.println("==================");
        for (String str : pc) {
            if (str != null)
                System.out.println(str);
        }
    }
}
