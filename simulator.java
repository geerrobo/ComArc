import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class simulator {
    
    
    String path;
    char[][] mc = new char[32][32];
    String mem[] = new String[32];
    int reg[] = new int[8];
    int pc = 0;

    public simulator(String file) throws Exception {
        path = file;

        File fr = new File(path);
        Scanner sc = new Scanner(fr);

        
        for (int i = 0; sc.hasNextLine(); i++) {
            mem[i] = sc.nextLine();
            int bin1 = Integer.parseInt(mem[i]); 
            String bin2 = Integer.toBinaryString(bin1);
            int k = 0;

            for(int j=bin2.length()-1;j >= 0;j--){

                mc[i][j] = bin2.charAt(k);
                k++;
            }

        }
        PrintState();

        pc =1;

        while (pc<3) {
            String op = String.valueOf(mc[pc-1][24]) + String.valueOf(mc[pc-1][23]) + String.valueOf(mc[pc-1][22]);
            System.out.println(op);
            pc = Read(op,pc-1);
            pc++;
            PrintState();
        }

    }


    public int Read(String op,int i){
        // String op = String.valueOf(mc[0][24]) + String.valueOf(mc[0][23]) + String.valueOf(mc[0][22]);
        // op = "110";
        // System.out.println(op);
        String rs,rt,rd;
        String of = "";
        String check = "";
        
        switch (op) {
            case "000":
                rs = String.valueOf(mc[i][21]) + String.valueOf(mc[i][20]) + String.valueOf(mc[i][19]);
                rt = String.valueOf(mc[i][18]) + String.valueOf(mc[i][17]) + String.valueOf(mc[i][16]);
                rd = String.valueOf(mc[i][2]) + String.valueOf(mc[i][1]) + String.valueOf(mc[i][0]);

                Add(rs,rt,rd);

                break;

            case "001":

                rs = String.valueOf(mc[i][21]) + String.valueOf(mc[i][20]) + String.valueOf(mc[i][19]);
                rt = String.valueOf(mc[i][18]) + String.valueOf(mc[i][17]) + String.valueOf(mc[i][16]);
                rd = String.valueOf(mc[i][2]) + String.valueOf(mc[i][1]) + String.valueOf(mc[i][0]);

                Nand(rs,rt,rd);
                
                break;
            
            case "010":
                rs = String.valueOf(mc[i][21]) + String.valueOf(mc[i][20]) + String.valueOf(mc[i][19]);
                rt = String.valueOf(mc[i][18]) + String.valueOf(mc[i][17]) + String.valueOf(mc[i][16]);
                check = String.valueOf(mc[i][15]);
                
                for (int j = 14; j >=0; j--) {
                    of += String.valueOf(mc[i][j]);
                }
                // System.out.println("of:"+of);
                LoadW(rs,rt,of,check);
  
                break;
                
            case "011":

                rs = String.valueOf(mc[i][21]) + String.valueOf(mc[i][20]) + String.valueOf(mc[i][19]);
                rt = String.valueOf(mc[i][18]) + String.valueOf(mc[i][17]) + String.valueOf(mc[i][16]);
                check = String.valueOf(mc[i][15]);

                for (int j = 14; j >=0; j--) {
                    of += String.valueOf(mc[i][j]);
                }
                // System.out.println("of:"+of);
                StoreW(rs,rt,of,check);
                
                break;

            case "100":

                rs = String.valueOf(mc[i][21]) + String.valueOf(mc[i][20]) + String.valueOf(mc[i][19]);
                rt = String.valueOf(mc[i][18]) + String.valueOf(mc[i][17]) + String.valueOf(mc[i][16]);
                check = String.valueOf(mc[i][15]);

                for (int j = 14; j >=0; j--) {
                    of += String.valueOf(mc[i][j]);
                }
                // System.out.println("of:"+of);
                return Beq(rs,rt,of,check);

            case "101":

                rs = String.valueOf(mc[i][21]) + String.valueOf(mc[i][20]) + String.valueOf(mc[i][19]);
                rd = String.valueOf(mc[i][18]) + String.valueOf(mc[i][17]) + String.valueOf(mc[i][16]);
                
                // System.out.println("of:"+of);
                Jalr(rs,rd);
                
                break;
            
            case "110":
                Halt();
                break;
                
            case "111":
                
                break;
            
            default:
                break;
        }
        return i+1;
    }


    public void Add(String rs1,String rs2,String rd){

        int rs1_dec = Integer.parseInt(rs1,2); 
        int rs2_dec = Integer.parseInt(rs2,2);
        int rd_dec = Integer.parseInt(rd,2);

        reg[rd_dec] = reg[rs1_dec] + reg[rs2_dec];
        // System.out.println(reg[rd_dec]);

    }

    public void Nand(String rs1,String rs2,String rd){

        int rs1_dec = Integer.parseInt(rs1,2); 
        int rs2_dec = Integer.parseInt(rs2,2);
        int rd_dec = Integer.parseInt(rd,2);

        reg[rd_dec] = ~(rs1_dec & rs2_dec);


    }

    public void LoadW(String rs1,String rs2,String of,String check){
        int rs1_dec = Integer.parseInt(rs1,2); 
        int rs2_dec = Integer.parseInt(rs2,2); 
        int of_dec = Integer.parseInt(of,2);
        
        

        if(check.equals("1")){
            of_dec = of_dec-32766 ;
        }

        int a = Integer.parseInt(mem[of_dec]);
        System.out.println(a);

        reg[rs2_dec] = reg[rs1_dec] + a;

        // System.out.println(reg[rs2_dec]);

    }

    public void StoreW(String rs1,String rs2,String of,String check){

    }

    public int Beq(String rs1,String rs2,String of,String check){
        int rs1_dec = Integer.parseInt(rs1,2); 
        int rs2_dec = Integer.parseInt(rs2,2); 
        int of_dec = Integer.parseInt(of, 2);

        if(reg[rs1_dec] == reg[rs2_dec]){
            
            if(check.equals("1")){
                of_dec = (of_dec-32766) +pc;
            }else{
                of_dec = of_dec + pc;
            }
        }
        // System.out.println("++++++++++++"+of_dec);
        return of_dec;
    }

    public int Jalr(String rs,String rd){
        int rs_dec = Integer.parseInt(rs,2); 
        int rd_dec = Integer.parseInt(rd,2); 
        reg[rd_dec] = pc+1;

        return reg[rs_dec];


    }

    public void Halt(){
        PrintState();
        System.exit(0);
    }

    public void PrintState(){
        System.out.println("===========================================================================\n");
        System.out.println("pc:"+pc);
        System.out.println("mem:");
        for (int i = 0; mem[i] != null; i++) {
            System.out.println("\n mem["+ i + "]"+mem[i]);
        }
        System.out.println("\n Reg:");
        for (int i = 0; i < reg.length; i++) {
            System.out.println("\n reg["+ i + "]" + reg[i]);
        }

    }


    
}

