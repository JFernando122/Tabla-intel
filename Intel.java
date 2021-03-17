import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Intel{
    
    public static char[] mergeArr(char[] A, char[] B){
        char[] C = new char[4];
        C[0] = A[0];
        C[1] = A[1];
        C[2] = B[0];
        C[3] = B[1];
        return C;
    }

    public static int HextoDec(String data){
		if (data.length() == 1) {
			try{
				int dec = Integer.parseInt(data);
				return dec;
			}catch (Exception e) {
				if (data.equals("A"))return 10;
				if (data.equals("B"))return 11;
				if (data.equals("C"))return 12;
				if (data.equals("D"))return 13;
				if (data.equals("E"))return 14;
				if (data.equals("F"))return 15;
			}
		}
		if(data.length() == 2)
			return 	HextoDec(String.valueOf(data.charAt(0)))*16 + HextoDec(String.valueOf(data.charAt(1)));
		if(data.length() == 4){
			return HextoDec(data.substring(0,2))*16*16 + HextoDec(data.substring(2));
		}
		System.out.println("Data length invalid " + data);
		return -1;
	}

	public static String DectoHex(int data){
		if (data < 16) {
			switch (data) {
				case 15: return "F";
				case 14: return "E";
				case 13: return "D";
				case 12: return "C";
				case 11: return "B";
				case 10: return "A";
				default: return String.valueOf(data);
			}
		}
		return DectoHex(data/16) + DectoHex(data % 16);
	}

    public static String fill(String string, int number,boolean start){
		if(string.length() >= number) return string;
		String filler = "";
		for(int i = 0; i < number - string.length(); i++)
			filler += "0";
		if(start) return filler + string;
		return string + filler;
	}

    public static void main(String args[]){
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            File Archivo = new File("main.hex");
            Scanner scanner = new Scanner(Archivo);
            fichero = new FileWriter("main.txt");
            pw = new PrintWriter(fichero);
            pw.println("  direccion  tipo   dato\n");
            while (scanner.hasNextLine()) {
              String linea = scanner.nextLine();
              if(linea.equals(":00000001FF"))
                break;
                int tamano = (linea.length() - 5 ) / 2;
            char[] arr = linea.toCharArray();
            char[][] caracteres = new char[tamano][2];
            for(int i = 3, j = 0; i < (linea.length() - 3 ); i += 2, j++){
                caracteres[j][0] = arr[i];
                caracteres[j][1] = arr[i + 1];
            }
            String direccionInicial = String.valueOf(mergeArr(caracteres[0],caracteres[1]));
            String tipo = String.valueOf(caracteres[2]);
            if(tipo.equals("00")){
                for(int i = 3; i < tamano; i++){
                    int direccion = HextoDec(direccionInicial);
                    direccion += (i - 3);
                    String dato = String.valueOf(caracteres[i]);
                    pw.println("  0x" + fill(DectoHex(direccion),4,true) + "      " + tipo + "     " + dato);
                }
            }else if(tipo.equals("04")){
                String dato = String.valueOf(mergeArr(caracteres[3], caracteres[4]));
                pw.println("  0x" + direccionInicial + "      " + tipo + "     " + dato);
            }
        }
        scanner.close();
        } catch (Exception e) {
            System.err.println("Ha ocurrido un problema");
            e.printStackTrace();
        }finally {
            try {
            if (null != fichero)
               fichero.close();
            } catch (Exception e2) {
               e2.printStackTrace();
            }
        }
    }
}