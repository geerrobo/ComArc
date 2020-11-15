import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class simulator {
    
    
    String path;
    char[][] mc = new char[32][32];
    String temp[] = new String[32];

    public simulator(String file) throws Exception {
        path = file;

        File fr = new File(path);
        Scanner sc = new Scanner(fr);

        for (int i = 0; sc.hasNextLine(); i++) {
            temp[i] = sc.nextLine();
            int k = 0;

            for(int j=temp[i].length()-1;j >= 0;j--){
                mc[i][j] = temp[i].charAt(k);
                k++;
            }

            String op = String.valueOf(mc[i][24]) + String.valueOf(mc[i][23]) + String.valueOf(mc[i][22]);

            Read(op,i);

            

        }

        

    }


    public void Read(String op,int i){
        // String op = String.valueOf(mc[0][24]) + String.valueOf(mc[0][23]) + String.valueOf(mc[0][22]);
        op = "010";
        System.out.println(op);
        String rs,rt,rd;
        String of = "";
        
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
                for (int j = 15; j >=0; j--) {
                    of += String.valueOf(mc[i][j]);
                }
                // System.out.println("of:"+of);
                LoadW(rs,rt,of);
  
                break;
                
            case "011":

                rs = String.valueOf(mc[i][21]) + String.valueOf(mc[i][20]) + String.valueOf(mc[i][19]);
                rt = String.valueOf(mc[i][18]) + String.valueOf(mc[i][17]) + String.valueOf(mc[i][16]);
                for (int j = 15; j >=0; j--) {
                    of += String.valueOf(mc[i][j]);
                }
                // System.out.println("of:"+of);
                StoreW(rs,rt,of);
                
                break;

            case "100":

                rs = String.valueOf(mc[i][21]) + String.valueOf(mc[i][20]) + String.valueOf(mc[i][19]);
                rt = String.valueOf(mc[i][18]) + String.valueOf(mc[i][17]) + String.valueOf(mc[i][16]);
                for (int j = 15; j >=0; j--) {
                    of += String.valueOf(mc[i][j]);
                }
                // System.out.println("of:"+of);
                Beq(rs,rt,of);
                
                break;

            case "101":

                rs = String.valueOf(mc[i][21]) + String.valueOf(mc[i][20]) + String.valueOf(mc[i][19]);
                rd = String.valueOf(mc[i][18]) + String.valueOf(mc[i][17]) + String.valueOf(mc[i][16]);
                
                // System.out.println("of:"+of);
                Jalr(rs,rd);
                
                break;
            
            case "110":
                
                break;
                
            case "111":
                
                break;
            
            default:
                break;
        }
    }


    public void Add(String rs1,String rs2,String rd){

        int rs1_dec = Integer.parseInt(rs1,2); 
        int rs2_dec = Integer.parseInt(rs2,2); ;

        System.out.println(rs1_dec);

    }

    public void Nand(String rs1,String rs2,String rd){

        int rs1_dec = Integer.parseInt(rs1,2); 
        int rs2_dec = Integer.parseInt(rs2,2);

        System.out.println(rs1_dec);

    }

    public void LoadW(String rs1,String rs2,String of){

    }

    public void StoreW(String rs1,String rs2,String of){

    }

    public void Beq(String rs1,String rs2,String of){

    }

    public void Jalr(String rs,String rd){

    }

    
}

