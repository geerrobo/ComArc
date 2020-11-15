import java.io.*;
import java.util.Scanner;

public class compiller {
    String inst[] = { "add", "nand", "lw", "sw", "beq", "jalr" };
    String inst2[] = { "halt", "noop", ".fill" };
    int[][] mem = new int[50][];
    String[] pc = new String[50];
    String path;

    public compiller(String file) throws Exception {
        path = file;

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
        for (int i = 0; i < inst.length; i++) {
            if (pc[i] == null)
                break;
            pc[i] = translate2(pc[i], i);
        }

        printState();
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

        if (array.length <= 2)
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

            /*
             * switch (array[0]) { case "add": array[0] = "000"; break;
             * 
             * case "nand": array[0] = "001"; break;
             * 
             * case "lw": array[0] = "010"; break;
             * 
             * case "sw": array[0] = "011"; break;
             * 
             * case "beq": array[0] = "100"; break;
             * 
             * case "jalr": array[0] = "101"; break;
             * 
             * case "halt": array[0] = "110"; break;
             * 
             * case "noop": array[0] = "111"; break;
             * 
             * default: break; }
             */
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

    private void printState() {
        System.out.println("==================");
        for (String str : pc) {
            if (str != null)
                System.out.println(str);
        }
    }
}
