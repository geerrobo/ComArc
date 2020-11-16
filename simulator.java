import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class simulator {
    
    
    String path;
    char[][] mc = new char[32][32];
    String mem[] = new String[32];
    int reg[] = new int[8];
    int pc = 0;
    int ins =0;

    public simulator(String file) throws Exception {
        path = file;

        File fr = new File(path);
        Scanner sc = new Scanner(fr);

        
        for (int i = 0; sc.hasNextLine(); i++) {
            mem[i] = sc.nextLine();
            
            int k = 0;

            for(int j=mem[i].length()-1;j >= 0;j--){

                mc[i][j] = mem[i].charAt(k);
                k++;
            }

        }
        PrintState();

        pc =1;
        

        while (true) {
            String op = String.valueOf(mc[pc-1][24]) + String.valueOf(mc[pc-1][23]) + String.valueOf(mc[pc-1][22]);
            System.out.println(op);
            Read(op,pc-1);
            
            PrintState();
            pc++;
            ins++;
        }

    }


    public void Read(String op,int i){

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

                LoadW(rs,rt,of,check);
  
                break;
                
            case "011":

                rs = String.valueOf(mc[i][21]) + String.valueOf(mc[i][20]) + String.valueOf(mc[i][19]);
                rt = String.valueOf(mc[i][18]) + String.valueOf(mc[i][17]) + String.valueOf(mc[i][16]);
                check = String.valueOf(mc[i][15]);

                for (int j = 14; j >=0; j--) {
                    of += String.valueOf(mc[i][j]);
                }

                StoreW(rs,rt,of,check);
                
                break;

            case "100":

                rs = String.valueOf(mc[i][21]) + String.valueOf(mc[i][20]) + String.valueOf(mc[i][19]);
                rt = String.valueOf(mc[i][18]) + String.valueOf(mc[i][17]) + String.valueOf(mc[i][16]);
                

                for (int j = 14; j >=0; j--) {
                    of += String.valueOf(mc[i][j]);
                }

                Beq(rs,rt,of);

                break;

            case "101":

                rs = String.valueOf(mc[i][21]) + String.valueOf(mc[i][20]) + String.valueOf(mc[i][19]);
                rd = String.valueOf(mc[i][18]) + String.valueOf(mc[i][17]) + String.valueOf(mc[i][16]);

                Jalr(rs,rd);
                
                break;
            
            case "110":
                Halt();
                break;
                
            case "111":
                
                break;
            
            default:
                System.exit(1);             //error exception
                break;
        }
        
    }


    public void Add(String rs1,String rs2,String rd){

        int rs1_dec = Integer.parseInt(rs1,2); 
        int rs2_dec = Integer.parseInt(rs2,2);
        int rd_dec = Integer.parseInt(rd,2);

        reg[rd_dec] = reg[rs1_dec] + reg[rs2_dec];

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
        String mem_temp ="";
        
        if(check.equals("1") ){
            of_dec = 32766-of_dec ;
        }


        int a = reg[rs1_dec] + of_dec;

        for (int j = 14; j >=0; j--) {
            mem_temp += String.valueOf(mc[a][j]);
        }

        int mem_dec = Integer.parseInt(mem_temp,2);

        if(mc[a][15] == '1'){
            mem_dec = 32766-mem_dec;
        } 

        reg[rs2_dec] = mem_dec;
        

    }

    public void StoreW(String rs1,String rs2,String of,String check){
        int rs1_dec = Integer.parseInt(rs1,2); 
        int rs2_dec = Integer.parseInt(rs2,2); 
        int of_dec = Integer.parseInt(of,2);

        int x = of_dec+reg[rs1_dec];

        mem[x] = Integer.toBinaryString(reg[rs2_dec]);
        

    }

    public void Beq(String rs1,String rs2,String of){
        int rs1_dec = Integer.parseInt(rs1,2); 
        int rs2_dec = Integer.parseInt(rs2,2); 
        int of_dec = Integer.parseInt(of, 2);

        if(reg[rs1_dec] == reg[rs2_dec]){
            
            if(mc[pc-1][15] == '1'){
                pc = (of_dec-32767)-1 +pc;
            }else{
                pc = of_dec + pc;
            }
        } 
        
    }

    public void Jalr(String rs,String rd){
        int rs_dec = Integer.parseInt(rs,2); 
        int rd_dec = Integer.parseInt(rd,2); 
        reg[rd_dec] = pc+1;
        pc = rs_dec;

    }

    public void Halt(){
        System.out.println("end state"+ 
        "\n machine halted"+
        "\n total of "+ ins+1 +" instructions executed"+
        "\n final state of machine:");

        PrintState();
        System.exit(0);
    }

    public void PrintState(){
        System.out.println("===========================================================================\n");
        System.out.println("pc:"+pc);
        System.out.println("mem:");
        for (int i = 0; mem[i] != null; i++) {
            System.out.println("\n mem["+ i + "] = "+mem[i]);
        }
        System.out.println("\n Reg:");
        for (int i = 0; i < reg.length; i++) {
            System.out.println("\n reg["+ i + "] = " + reg[i]);
        }

    }


    
}

