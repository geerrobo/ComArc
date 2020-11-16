import java.io.File;
import java.io.FileWriter;

public class app {
    public static void main(String[] args) throws Exception {
        // compiller x = new compiller("C:\\Users\\geerc\\Documents\\ComArc\\assembly.txt");
        // simulate y = new simulate("C:\\Users\\geerc\\Documents\\ComArc\\");

        writeMultiAssembly(6, 3);
        compiller multi = new compiller("C:\\Users\\geerc\\Documents\\ComArc\\multi.txt");

    }

    public static void writeMultiAssembly(int x, int y) {
        String[] mul = {"lw 0 1 "+Integer.toString(x),"lw 0 2 "+Integer.toString(y),"lw 0 3 0","add 3 3 1","add 4 "+Integer.toString(x)+" "+Integer.toString(x),"beq 3 2 7","beq 0 0 3","halt"};
        try {
            File myObj = new File("C:\\Users\\geerc\\Documents\\ComArc\\multi.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
            }
            FileWriter myWriter = new FileWriter("C:\\Users\\geerc\\Documents\\ComArc\\multi.txt");
            for (String s : mul) {
                if (s == null)
                    break;
                myWriter.write(s + "\n");
            }
            myWriter.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}