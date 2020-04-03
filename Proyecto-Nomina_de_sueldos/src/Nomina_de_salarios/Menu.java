package Nomina_de_salarios;

import com.itextpdf.text.Chunk;
import java.applet.AudioClip;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import org.jfree.chart.*;
import org.jfree.data.category.*;
import org.jfree.chart.plot.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import ds.desktop.notify.DesktopNotify;
import org.jfree.data.general.DefaultPieDataset;

/**
 * (Clase): CLASE QUE CONTIENE TODOS LOS COMPONENTES DE DISEÑO EXCEL GRAFICA
 * ALGORITMO DE IMPRESION CREACION DE CONSTRUCTOR DE JTABLE MENSAJES CON IMAGEN
 * ALGORMITMO GENERAL DE ACUTLIZACCIONES DE MATRIZ, VECTORES Y ARCHIVOS
 *
 */
public class Menu {

    String Database = "jdbc:mysql://127.0.0.1:3306/nomina";
    String UsuarioDB = "root";
    String Clave = "";

    String[] UsuarioaModificarenBD = new String[5];

    /**
     * (String Usuario):Sirve para pedir un Usuario para acceder a la Plataforma
     */
    String Usuario;
    /**
     * (String Contraseña):Sirve para pedir una contraseña para acceder a la
     * Plataforma
     */
    String Contraseña;
    /**
     * (String PreguntaClave):Nos va a servir para pedir un numero telefonico y
     * poder recuperar una contraseña olvidada
     */
    String PreguntaClave;

    public static Menu g = new Menu();
    int cant, cantaux, ModificarCantidad;
    String[] Titulos = {"Nombre", "Puesto", "Sueldo Ord", "Sueldo Ext", "Bonificaciones",
        "Comisiones", "Otros", "TOTAL DEVENGADO", "IGGS", "ISR", "Anticipos", "Desc. Jud", "Otros", "TOTAL DESCUENTOS", "TOTAL LIQUIDADO"};
    float totales[] = new float[13];
    String[] nombre = new String[cantaux + cant];
    String[] puesto = new String[cantaux + cant];
    float Nomina_salariosArray[][] = new float[cantaux + cant][13];
    boolean cerrar = true;

    /**
     * (String Matrix): Llos valores de la nomina en String (int y): posicion
     * (int x): longitud del for para calcular caracteres
     *
     */
    public int gotoxMatriz(String Matrix[][], int x, int y) {
        int NumeroLetras[] = new int[cantaux + 2];
        int NumeroMayor = 0;

        for (int i = x; i < x + 1; i++) {

            for (int j = 0; j < cantaux + 2; j++) {
                char[] caracteres = Matrix[j][i].toCharArray();
                NumeroLetras[j] = caracteres.length;
                if (NumeroLetras[j] >= NumeroMayor) {

                    NumeroMayor = NumeroLetras[j];

                }
            }

        }

        return NumeroMayor - NumeroLetras[y] + 5;

    }

    public int gotoxMatrizParaDB(String Matrix[][], int x, int y, int registros) {
        int NumeroLetras[] = new int[6];
        int NumeroMayor = 0;

        for (int i = x; i < x + 1; i++) {

            for (int j = 0; j < registros; j++) {
                char[] caracteres = Matrix[j][i].toCharArray();
                NumeroLetras[j] = caracteres.length;
                if (NumeroLetras[j] >= NumeroMayor) {

                    NumeroMayor = NumeroLetras[j];

                }
            }

        }

        return NumeroMayor - NumeroLetras[y] + 5;

    }

    /*public int gotox(int x, String vector[],int y){
         int NumeroLetras[] = new int[y];
         int NumeroMayor=0;

         for (int i = 0; i < y; i++) {
             char[] caracteres = vector[i].toCharArray();
             NumeroLetras[i] = caracteres.length;

             if(NumeroLetras[i]>=NumeroMayor ){

                 NumeroMayor = NumeroLetras[i];

             }

         }

         for (int j = 0;j < NumeroMayor-NumeroLetras[x]+5; j++) {
             System.out.print(" ");


         }


         return NumeroMayor-NumeroLetras[x]+5;
     }*/
    /**
     * (String NuevaNomina): llenar la nomina como string con encabezados y
     * totales (NominaFlotante): nomina llena con vlores float
     * (TotalesdeNomina):contiene solo los totales de cada columna
     *
     */
    public void llenarMatriz(String NuevaNomina[][], float NominaFlotante[][], float TotalesdeNomina[]) {
        for (int i = 0; i < 15; i++) {

            NuevaNomina[0][i] = Titulos[i];

        }
        for (int i = 1; i < NuevaNomina.length - 1; i++) {

            NuevaNomina[i][0] = nombre[i];
            NuevaNomina[i][1] = puesto[i];

        }

        for (int i = 1; i < NuevaNomina.length - 1; i++) {

            for (int j = 2; j < 15; j++) {

                NuevaNomina[i][j] = Float.toString(NominaFlotante[i - 1][j - 2]);

            }

            NuevaNomina[NuevaNomina.length - 1][0] = "";
            NuevaNomina[NuevaNomina.length - 1][1] = "TOTALES";

            for (int j = 2; j < 15; j++) {

                NuevaNomina[NuevaNomina.length - 1][j] = Float.toString(TotalesdeNomina[j - 2]);

            }

        }

    }

    /* public void ConvertirMatrizEnVectores( String vector1[], int x, float MatrizFinal[][], String NombredeColumna){
         for(int i = 0; i < 1;i++){

             vector1[i] = NombredeColumna;
         }
         for(int i = 0; i < 13;i++){
             float NumeroLetrasMatriz[] = new float[cantaux];


             for(int j = 1; j < cantaux+1;j++){
                 NumeroLetrasMatriz[j-1] = MatrizFinal[j-1][i];
                 String numero = Float.toString(NumeroLetrasMatriz[j-1]);
                 vector1[j] = numero;


             }
             if(i == x){
                 break;
             }

         }
     }*/
    /**
     * (int n): si se agregaron mas cantidades en la nomina (float Matriz): la
     * matriz acualizada
     *
     *
     */
    public void ImprimirNomina(int n, float Matriz[][]) {
        float auxtotales = 0;
        /* String[] SueldoOrdinario = new String[cantaux+1];
        String[] SueldoExtraOrdinario = new String[cantaux+1];
        String[] Bonificacion = new String[cantaux+1];
        String[] Comision = new String[cantaux+1];
        String[] Otros = new String[cantaux+1];
        String[] totalDEV = new String[cantaux+1];
        String[] Iggs = new String[cantaux+1];
        String[] Isr = new String[cantaux+1];
        String[] Anticipos = new String[cantaux+1];
        String[] Descuentos = new String[cantaux+1];
        String[] OtrosDescuentos = new String[cantaux+1];
        String[] TotalDesc = new String[cantaux+1];
        String[] TotalLiquido  = new String[cantaux+1];

        ConvertirMatrizEnVectores(SueldoOrdinario,0,Matriz,"Sueldo Ord");
        ConvertirMatrizEnVectores( SueldoExtraOrdinario,1,Matriz,"Sueldo Ext" );
        ConvertirMatrizEnVectores( Bonificacion,2,Matriz,"Bonificaciones");
        ConvertirMatrizEnVectores(Comision,3 ,Matriz,"Comision");
        ConvertirMatrizEnVectores( Otros,4,Matriz, "Otros");
        ConvertirMatrizEnVectores(totalDEV ,5,Matriz,"TOTAL DEVENGADO");
        ConvertirMatrizEnVectores( Iggs ,6,Matriz,"IGGS");
        ConvertirMatrizEnVectores(Isr,7,Matriz,"ISR");
        ConvertirMatrizEnVectores(Anticipos,8 ,Matriz,"Anticipos");
        ConvertirMatrizEnVectores(Descuentos,9,Matriz,"Descuentos" );
        ConvertirMatrizEnVectores( OtrosDescuentos ,10,Matriz,"Otros");
        ConvertirMatrizEnVectores(TotalDesc,11,Matriz,"TOTAL DESCUENTOS" );
        ConvertirMatrizEnVectores( TotalLiquido ,12 ,Matriz,"TOTAL LIQUIDADO");*/

 /* for(int i = 0; i < cantaux+1; i++){

            System.out.print("\033[37m. "+"\033[37m "+nombre[i]);gotox(i,nombre,cantaux+n);
            System.out.print("\033[37m"+puesto[i]);gotox(i,puesto,cantaux+n);
            System.out.print("\033[37m "+SueldoOrdinario[i]);gotox(i,SueldoOrdinario,cantaux+n);
            System.out.print("\033[37m "+SueldoExtraOrdinario[i]); gotox(i,SueldoExtraOrdinario,cantaux+n);
            System.out.print("\033[37m "+Bonificacion[i]);gotox(i,Bonificacion,cantaux+n);
            System.out.print("\033[37m "+Comision[i]);gotox(i,Comision,cantaux+n);
            System.out.print("\033[37m "+ Otros[i]); gotox(i, Otros,cantaux+n);
            System.out.print("\033[37m "+totalDEV[i]);gotox(i,totalDEV,cantaux+n);
            System.out.print("\033[37m "+Iggs[i]);gotox(i,Iggs,cantaux+n);
            System.out.print("\033[37m "+Isr[i]);gotox(i,Isr,cantaux+n);
            System.out.print("\033[37m "+Anticipos[i]);gotox(i,Anticipos,cantaux+n);
            System.out.print("\033[37m "+Descuentos[i]);gotox(i,Descuentos,cantaux+n);
            System.out.print("\033[37m "+OtrosDescuentos[i]);gotox(i,OtrosDescuentos,cantaux+n);
            System.out.print("\033[37m "+TotalDesc[i]);gotox(i,TotalDesc,cantaux+n);
            System.out.println("\033[37m "+TotalLiquido[i]);

        }*/

 /*for(int i = 0; i < cantaux+n+1; i++){
                Esc.print(nombre[i]);
                for(int j = 0; j < gotox(i,nombre,cantaux);j++){
                    Esc.print(" ");
                }
                Esc.print(puesto[i]);
                for(int j = 0; j < gotox(i,puesto,cantaux);j++){
                    Esc.print(" ");

                }
                Esc.print(SueldoOrdinario[i]);

                for(int j = 0; j < gotox(i,SueldoOrdinario,cantaux);j++){
                    Esc.print(" ");

                }
                Esc.print(SueldoExtraOrdinario[i]);

                for(int j = 0; j < gotox(i,SueldoExtraOrdinario,cantaux);j++){
                    Esc.print(" ");

                }
                Esc.print(Bonificacion[i]);

                for(int j = 0; j < gotox(i,Bonificacion,cantaux);j++){
                    Esc.print(" ");

                }
                Esc.print(Comision[i]);

                for(int j = 0; j < gotox(i,Comision,cantaux);j++){
                    Esc.print(" ");

                }
                Esc.print(Otros[i]);

                for(int j = 0; j < gotox(i,Otros,cantaux);j++){
                    Esc.print(" ");

                }
                Esc.print(totalDEV[i]);


                for(int j = 0; j < gotox(i,totalDEV,cantaux);j++){
                    Esc.print(" ");

                }
                Esc.print(Iggs[i]);

                for(int j = 0; j < gotox(i,Iggs,cantaux);j++){
                    Esc.print(" ");

                }
                Esc.print(Isr[i]);

                for(int j = 0; j < gotox(i,Isr,cantaux);j++){
                    Esc.print(" ");

                }
                Esc.print(Anticipos[i]);



                for(int j = 0; j < gotox(i,Anticipos,cantaux);j++){
                    Esc.print(" ");

                }
                Esc.print(Descuentos[i]);



                for(int j = 0; j < gotox(i,Descuentos,cantaux);j++){
                    Esc.print(" ");

                }
                Esc.print(OtrosDescuentos[i]);


                for(int j = 0; j < gotox(i,OtrosDescuentos,cantaux);j++){
                    Esc.print(" ");

                }
                Esc.print(TotalDesc[i]);

                for(int j = 0; j < gotox(i,TotalDesc,cantaux);j++){
                    Esc.print(" ");

                }
                Esc.println(TotalLiquido[i]);

               if(i ==0){
                     Esc.println("");
               }

            }*/
        String[][] Nominastring = new String[cantaux + 2][15];

        if (cantaux > 0) {
            System.out.println("\033[33m<< " + "\033[37m---PLANILLA---" + "\033[33m >>");
           
            llenarMatriz(Nominastring, Matriz, totales);

            for (int i = 0; i < 15; i++) {
                System.out.print("\033[33m" + Nominastring[0][i]);
                for (int l = 0; l < gotoxMatriz(Nominastring, i, 0); l++) {
                    System.out.print(" ");

                }
            }
            System.out.println("");
            for (int i = 1; i < cantaux + 2; i++) {

                for (int j = 0; j < 15; j++) {

                    if (i == cantaux + 1) {
                        if (j == 7) {
                            System.out.print("\033[32m" + Nominastring[i][j]);
                        } else if (j == 13) {
                            System.out.print("\033[31m" + Nominastring[i][j]);
                        } else if (j == 14) {
                            System.out.print("\033[36m" + Nominastring[i][j]);
                        } else {
                            System.out.print("\033[37m" + Nominastring[i][j]);
                        }

                    } else {
                        System.out.print("\033[37m" + Nominastring[i][j]);
                    }

                    for (int l = 0; l < gotoxMatriz(Nominastring, j, i); l++) {
                        System.out.print(" ");

                    }

                }
                System.out.println("");
            }

            for (int j = 0; j < 265; j++) {

                System.out.print("\033[35m-");
            }
            System.out.println("");

            System.out.print("\033[33m TOTALES: ");
            for (int j = 0; j < 13; j++) {
                System.out.print("\033[34m  " + totales[j] + " ");

            }

        } else {
            MensajeNO();
        }
        try {

            PrintWriter Esc = new PrintWriter("Nomina_de_sueldos.txt");

            for (int i = 0; i < 107; i++) {
                Esc.print("-");
            }

            Esc.print("PLANILLA");
            for (int i = 0; i < 135; i++) {
                Esc.print("-");
            }
            Esc.println("");

            for (int i = 0; i < cantaux + 2; i++) {

                for (int j = 0; j < 15; j++) {

                    Esc.print("" + Nominastring[i][j]);

                    for (int k = 0; k < gotoxMatriz(Nominastring, j, i); k++) {
                        Esc.print(" ");
                    }

                }
                Esc.println("");
            }

            for (int j = 0; j < 250; j++) {

                Esc.print("-");
            }

            Esc.println("\nTOTALES: ");
            for (int j = 2; j < 15; j++) {
                Esc.print(Titulos[j]);

                Esc.println(":  " + totales[j - 2] + "");

            }
            String[] Bitacora = new String[5];

            try {
                FileReader Leer = new FileReader("Usuario_Bitacora.txt");
                BufferedReader Buffer = new BufferedReader(Leer);

                String temp = "";
                String BfRead;

                while ((BfRead = Buffer.readLine()) != null) {

                    for (int j = 0; j < 5; j++) {

                        Bitacora[j] = BfRead;
                        BfRead = Buffer.readLine();

                    }

                }

            } catch (Exception err) {

            }
            Esc.println("\nUSUARIO QUE REGISTRO NOMINA: ");
            for (int j = 0; j < 5; j++) {
                Esc.println(Bitacora[j]);

            }

            Esc.close();
        } catch (Exception err) {

        }

    }

    /**
     * (LeerEmpleadosArchivo): llena la matriz y vectores los valores existentes
     * en el archivo y las cantidades
     *
     */
    public void LeerEmpleadosArchivo() {
 float auxtotales = 0;
        try {
            FileReader Leer = new FileReader("Cantidad.txt");
            BufferedReader Buffer = new BufferedReader(Leer);

            String temp = "";
            String BfRead;

            while ((BfRead = Buffer.readLine()) != null) {

                temp = BfRead;
            }

            cantaux = Integer.parseInt(temp);

        } catch (Exception err) {
            cantaux = 0;

        }

        nombre = new String[cantaux + 1];
        puesto = new String[cantaux + 1];
        Nomina_salariosArray = new float[cantaux][13];

        for (int i = 0; i < 1; i++) {
            nombre[i] = "NOMBRE";
            puesto[i] = "PUESTO";

        }

        try {
            FileReader Leer = new FileReader("Nomina.txt");
            BufferedReader Buffer = new BufferedReader(Leer);

            int aux1 = 0, aux2 = 0;
            String BfRead;

            while ((BfRead = Buffer.readLine()) != null) {

                if ((aux1 == cantaux - 2) && (aux2 == 13 - 1)) {

                    break;
                }
                for (int i = 1; i < cantaux + 1; i++) {

                    nombre[i] = BfRead;
                    BfRead = Buffer.readLine();
                    puesto[i] = BfRead;
                    BfRead = Buffer.readLine();

                    for (int j = 0; j < 13; j++) {

                        Nomina_salariosArray[i - 1][j] = Float.parseFloat(BfRead);
                        BfRead = Buffer.readLine();

                        if ((i == cantaux - 2) && (j == 13 - 1)) {
                            aux1 = cantaux - 2;
                            aux2 = 13 - 1;

                        }

                    }

                }

            }
            if (cantaux > 0) {
           
            for (int i = 0; i < 13; i++) {

                for (int j = 0; j < cantaux; j++) {

                    auxtotales = auxtotales + Nomina_salariosArray[j][i];
                }
                totales[i] = auxtotales;
                auxtotales = 0;
            }
            }

        } catch (Exception err) {

        }

    }

    /**
     * (menu): El menu Principal 1. Altas 2.Table 3. Cambios 4.Elminar
     * 5.Graficar
     *
     */
    public void menu(String UsuarioqueAccedio, String ContraseñaqueAccedio, String admin, String clave) {
        int op = 0;
        int opsion = 0;
        int opsionAmodificar = 0;
        int Tema = 0;
        do {
            LeerEmpleadosArchivo();

            System.out.println(" ");
            Icon icon = new ImageIcon(getClass().getResource("/Imagenes/3.png"));
            boolean letra;
            do {
                try {
                    letra = true;
                    op = Integer.parseInt((String) JOptionPane.showInputDialog(null, "Proyeto realizado parala la administracion de una planilla de sueldos\n Implementacion de JAVA + MySQL\n Excel, PDF, Codigos QR, Google Gmail, Notificaciones,\n Temas, Archivos planos, Graficas\n\n" + "<< PLATAFORMA >>\n\n" + "1. INGRESE DATOS Y VALORES SOLICITADOS/MOSTRAR PLANILLA DE SUELDOS" + "\n" + "2. MOSTRAR NOMINA / GENERAR EXCEL" + "\n" + "3."
                            + " MODIFICAR DATOS" + "\n" + "4. ELIMINAR DATOS" + "\n" + "5. GRAFICA DE BARRAS" + "\n" +"6. GRAFICA DE PASTEL" + "\n" + "7. AJUSTES DE CUENTA/USUARIO" + "\n" + "8. TEMA" + "\n" + "9. CERRA SESION" + "\n" + "\n"
                            + "DIGITE LA OPCION: ", "MENU", JOptionPane.INFORMATION_MESSAGE, icon, null, ""));
                    /* op = Integer.parseInt((String) JOptionPane.showInputDialog(null,  "<< PLATAFORMA >>\n\n" + "1. INGRESE DATOS Y VALORES SOLICITADOS/MOSTRAR PLANILLA DE SUELDOS" + "\n" + "2. MOSTRAR NOMINA / GENERAR EXCEL" + "\n" + "3."
                            + " MODIFICAR DATOS" + "\n" + "4. ELIMINAR DATOS" + "\n" + "5. GRAFICAR" + "\n" + "6. AJUSTES DE CUENTA/USUARIO" + "\n" + "7. TEMA" + "\n" + "8. CERRA SESION" + "\n" + "\n"
                            + "DIGITE LA OPCION: ", "MENU", JOptionPane.INFORMATION_MESSAGE, icon, null, ""));*/

                } catch (Exception e) {
                    Icon error = new ImageIcon(Main.class.getResource("/Imagenes/ErrorNum.png"));
                    JOptionPane.showMessageDialog(null, "EL DATO INGRESADO ES INCORRECTO", "ERROR", JOptionPane.INFORMATION_MESSAGE, error);
                    letra = false;
                }
            } while (letra != true);

            switch (op) {
                case 1:
                    Icon Ingresar = new ImageIcon(getClass().getResource("/Imagenes/Altas.png"));

                    DesktopNotify.showDesktopMessage("¿Que le Permite esta opcion?",
                            "Esta opcion le permite agregar empleados a la planilla cada tendra un archivo plano y su propio codigo QR,"
                            + " tanto que podra ingresar sus percepsiones y deducciones. "
                            + "se calcula de manera automatizada los porcentajes, podra ingresar la cantidad de empleados que desee, todo sera conservado en archivos, base de datos, etc.",
                            DesktopNotify.INFORMATION, 8000L);

                    do {
                        try {
                            letra = true;
                            cant = Integer.parseInt((String) JOptionPane.showInputDialog(null, "DIGITE CANTIDAD DE EMPLEADOS: ", "ALTAS", JOptionPane.INFORMATION_MESSAGE, Ingresar, null, ""));

                        } catch (Exception e) {
                            Icon error = new ImageIcon(Main.class.getResource("/Imagenes/ErrorNum.png"));
                            JOptionPane.showMessageDialog(null, "EL DATO INGRESADO ES INCORRECTO", "ERROR", JOptionPane.INFORMATION_MESSAGE, error);
                            letra = false;
                        }
                    } while (letra != true);
                    ModificarCantidad = cant;
                    Nomina_sueldos Nomina = new Nomina_sueldos(cant, cantaux);
                    Nomina.IngresoDatos(Nomina_salariosArray, nombre, puesto);
                    break;
                case 2:

                    ImprimirNomina(0, Nomina_salariosArray);
                    DesktopNotify.showDesktopMessage("¿Que le Permite esta opcion?",
                            "Esta opcion le permite visualizar lo empleados de la planilla,"
                            + " tanto que podra ver sus percepsiones y deducciones. "
                            + "tambien se generar un excel con la tabla, podra ver la cantidad de empleados en consola y en la tabla, y su total liquidado, etc.",
                            DesktopNotify.INFORMATION, 8000L);
                    String[][] NominaParaFormato = new String[cantaux + 2][15];
                    String ruta = "Nomina_formato.xls";
                    llenarMatriz(NominaParaFormato, Nomina_salariosArray, totales);
                    g.generarExcel(NominaParaFormato, ruta);
                    if (cantaux > 0) {
                        Tabla mostrar = new Tabla(NominaParaFormato, cantaux);
                    }

                    break;
                case 3:
                    if (cantaux > 0) {
                        DesktopNotify.showDesktopMessage("¿Que le Permite esta opcion?",
                                "Esta opcion le permite modificar los empleados registrados en planilla,"
                                + " tanto que podra modificar sus percepsiones y deducciones. "
                                + " podra modificar la cantidad de empleados que desee, todo sera conservado en archivos, base de datos, etc.",
                                DesktopNotify.INFORMATION, 8000L);
                        Nomina_sueldos Modificar = new Nomina_sueldos(ModificarCantidad, cantaux);

                        Modificar.ModificarDatos(Nomina_salariosArray, nombre, puesto);
                    } else {
                        MensajeNO();
                    }

                    break;
                case 4:
                    Icon Eliminaricon = new ImageIcon(getClass().getResource("/Imagenes/Bajas.png"));
                    if (cantaux > 0) {
                        DesktopNotify.showDesktopMessage("¿Que le Permite esta opcion?",
                                "Esta opcion le permite eliminar empleados en la planilla,"
                                + " tanto que podra quitar todos los registros hechos por el nombre de la persona"
                                + "todo sera conservado en archivos, base de datos, etc.",
                                DesktopNotify.INFORMATION, 8000L);

                        DesktopNotify.showDesktopMessage("Advertencia", "Dependiento la cantidad de Empleados a eliminar no ingrese un valor superior a los registros actuales, si no desea eliminar digite 0 ", DesktopNotify.WARNING, 10000L);

                        do {

                            do {
                                try {
                                    letra = true;
                                    cant = Integer.parseInt((String) JOptionPane.showInputDialog(null, "DIGITE CANTIDAD DE EMPLEADOS: ", "BAJAS", JOptionPane.INFORMATION_MESSAGE, Eliminaricon, null, ""));

                                } catch (Exception e) {
                                    Icon error = new ImageIcon(Main.class.getResource("/Imagenes/ErrorNum.png"));
                                    JOptionPane.showMessageDialog(null, "EL DATO INGRESADO ES INCORRECTO", "ERROR", JOptionPane.INFORMATION_MESSAGE, error);
                                    letra = false;
                                }
                            } while (letra != true);

                        } while (cant > cantaux);
                        Nomina_sueldos Eliminar = new Nomina_sueldos(cant, cantaux);
                        Eliminar.EliminarDatos(Nomina_salariosArray, nombre, puesto);
                    } else {
                        MensajeNO();
                    }
                    break;
                case 5:
                    if (cantaux > 0) {
                        DesktopNotify.showDesktopMessage("¿Que le Permite esta opcion?",
                                "Esta opcion le permite visualizar una grafica de barras de todos los totales,"
                                + " tanto que podra ver los totales de cada percepsion y deduccion. "
                                + " si usted a modificado, ingresado, eliminado se actualizara automaticamente la grafica, todo sera conservado en archivos, base de datos, etc.",
                                DesktopNotify.INFORMATION, 8000L);
                        Graficar(totales);
                    } else {
                        MensajeNO();
                    }
                    break;
                     case 6:
                    if (cantaux > 0) {
                        DesktopNotify.showDesktopMessage("¿Que le Permite esta opcion?",
                                "Esta opcion le permite visualizar una grafica de pastel de todos los totales,"
                                + " tanto que podra ver los totales de cada percepsion y deduccion. "
                                + " si usted a modificado, ingresado, eliminado se actualizara automaticamente la grafica, todo sera conservado en archivos, base de datos, etc.",
                                DesktopNotify.INFORMATION, 8000L);
                        GraficaCircular(totales);
                    } else {
                        MensajeNO();
                    }
                    break;
                    
                case 7:
                    Icon pass = new ImageIcon(getClass().getResource("/Imagenes/Pass.png"));
                    Icon mail = new ImageIcon(getClass().getResource("/Imagenes/Mail.png"));
                    Icon usuario = new ImageIcon(getClass().getResource("/Imagenes/Username.png"));
                    DesktopNotify.showDesktopMessage("¿Que le Permite esta opcion?",
                            "Se desplegara un sub menu con opciones de usuario,"
                            + " El administrador podra ingresar, modificar, eliminar, mostrar usuarios"
                            + "cada accion o modificacion funciona con el ID de cada usuario, todo sera conservado en archivos, base de datos, etc.",
                            DesktopNotify.INFORMATION, 8000L);
                    do {
                        do {
                            try {
                                letra = true;
                                opsion = Integer.parseInt((String) JOptionPane.showInputDialog(null, "USUARIOS \n\n" + "1. CREAR USUARIOS" + "\n" + "2. MODIFICAR USUARIO" + "\n" + "3. ELIMINAR USUARIO" + "\n" + "4. MOSTRAR USUARIOS / GENERAR PDF" + "\n" + "5. REGRESAR:"
                                        + "\n" + "\n" + "DIGITE LA OPCION: ", "USUARIOS", JOptionPane.INFORMATION_MESSAGE, usuario, null, ""));

                            } catch (Exception e) {
                                Icon error = new ImageIcon(Main.class.getResource("/Imagenes/ErrorNum.png"));
                                JOptionPane.showMessageDialog(null, "EL DATO INGRESADO ES INCORRECTO", "ERROR", JOptionPane.INFORMATION_MESSAGE, error);
                                letra = false;
                            }
                        } while (letra != true);

                        switch (opsion) {
                            case 1:
                                Icon ingresado = new ImageIcon(Main.class.getResource("/Imagenes/Ingresado.png"));
                                if (UsuarioqueAccedio.equals(admin) && ContraseñaqueAccedio.equals(clave)) {
                                    registro();
                                    JOptionPane.showMessageDialog(null, "INGRESADO CON EXITO", "USUARIO INGRESADO", JOptionPane.INFORMATION_MESSAGE, ingresado);

                                } else {
                                    DesktopNotify.showDesktopMessage("Fallo", "No tiene los permisos para esta opcion.", DesktopNotify.FAIL, 5000L);
                                    JOptionPane.showMessageDialog(null, "SOLO EL ADMINISTRADOR PUEDE REGISTRAR USUARIOS");

                                }
                                break;

                            case 2:
                                Icon modificado = new ImageIcon(Main.class.getResource("/Imagenes/Cambios.png"));
                                String UsuarioAModificar = (String) JOptionPane.showInputDialog(null, "DIGITE EL ID DE USUARIO: ", "USER", JOptionPane.INFORMATION_MESSAGE, usuario, null, "");

                                if (BuscarRegistroDB(UsuarioAModificar) == false) {
                                    Icon ID = new ImageIcon(Main.class.getResource("/Imagenes/ErrorNum.png"));
                                    JOptionPane.showMessageDialog(null, "EL ID INGRESADO NO EXISTE", "USUARIO NO EXISTE", JOptionPane.INFORMATION_MESSAGE, ID);

                                } else {

                                    do {
                                        do {
                                            try {
                                                letra = true;
                                                opsionAmodificar = Integer.parseInt((String) JOptionPane.showInputDialog(null, "MODIFICAR \n\n" + "1. NOMBRE DE USUARIO" + "\n" + "2. CONTRASEÑA" + "\n" + "3. CORREO " + "\n" + "4. REGRESAR" + "\n" + "\n" + "DIGITE LA OPCION: ", "USUARIOS", JOptionPane.INFORMATION_MESSAGE, usuario, null, ""));
                                            } catch (Exception e) {
                                                Icon error = new ImageIcon(Main.class.getResource("/Imagenes/ErrorNum.png"));
                                                JOptionPane.showMessageDialog(null, "EL DATO INGRESADO ES INCORRECTO", "ERROR", JOptionPane.INFORMATION_MESSAGE, error);
                                                letra = false;
                                            }
                                        } while (letra != true);

                                        if (opsionAmodificar == 1) {
                                            String NuevoUser = (String) JOptionPane.showInputDialog(null, "DIGITE EL NUEVO NOMBRE DE USUARIO: ", "USER", JOptionPane.INFORMATION_MESSAGE, usuario, null, "");
                                            modificar(opsionAmodificar, NuevoUser, UsuarioAModificar);
                                            JOptionPane.showMessageDialog(null, "MODIFICADO CON EXITO", "USUARIO MODIFICADO", JOptionPane.INFORMATION_MESSAGE, modificado);

                                        }
                                        if (opsionAmodificar == 2) {
                                            String NuevoPass = (String) JOptionPane.showInputDialog(null, "DIGITE LA NUEVA CONTRASEÑA DE USUARIO: ", "PASSWORD", JOptionPane.INFORMATION_MESSAGE, pass, null, "");
                                            modificar(opsionAmodificar, NuevoPass, UsuarioAModificar);
                                            JOptionPane.showMessageDialog(null, "MODIFICADO CON EXITO", "USUARIO MODIFICADO", JOptionPane.INFORMATION_MESSAGE, modificado);
                                        }
                                        if (opsionAmodificar == 3) {
                                            String NuevoCorreo = (String) JOptionPane.showInputDialog(null, "DIGITE LA NUEVA CONTRASEÑA DE USUARIO: ", "CORREO", JOptionPane.INFORMATION_MESSAGE, mail, null, "");
                                            modificar(opsionAmodificar, NuevoCorreo, UsuarioAModificar);
                                            JOptionPane.showMessageDialog(null, "MODIFICADO CON EXITO", "USUARIO MODIFICADO", JOptionPane.INFORMATION_MESSAGE, modificado);
                                        }

                                    } while (opsionAmodificar != 4);
                                }

                                break;

                            case 3:
                                if (UsuarioqueAccedio.equals(admin) && ContraseñaqueAccedio.equals(clave)) {
                                    String UsuarioAEliminar = (String) JOptionPane.showInputDialog(null, "DIGITE EL ID DE USUARIO: ", "USER", JOptionPane.INFORMATION_MESSAGE, usuario, null, "");

                                    if (UsuarioAEliminar.equals("1")) {
                                        JOptionPane.showMessageDialog(null, "EL USUARIO ADMINISTRADOR NO SE PUEDE ELIMINAR - CONTACTE CON EL DESAROLLADOR");
                                    } else {
                                        if (BuscarRegistroDB(UsuarioAEliminar) == false) {
                                            Icon ID = new ImageIcon(Main.class.getResource("/Imagenes/ErrorNum.png"));
                                            JOptionPane.showMessageDialog(null, "EL ID INGRESADO NO EXISTE", "USUARIO NO EXISTE", JOptionPane.INFORMATION_MESSAGE, ID);
                                        } else {
                                            eliminar(UsuarioAEliminar);
                                            Icon eliminado = new ImageIcon(Main.class.getResource("/Imagenes/Eliminado.png"));
                                            JOptionPane.showMessageDialog(null, "ELIMINADO CON EXITO", "USUARIO NO EXISTE", JOptionPane.INFORMATION_MESSAGE, eliminado);
                                        }

                                    }
                                } else {
                                    DesktopNotify.showDesktopMessage("Fallo", "No tiene los permisos para esta opcion.", DesktopNotify.FAIL, 5000L);
                                    JOptionPane.showMessageDialog(null, "SOLO EL ADMINISTRADOR PUEDE REGISTRAR USUARIOS");

                                }

                                break;
                            case 4:
                                if (UsuarioqueAccedio.equals(admin) && ContraseñaqueAccedio.equals(clave)) {
                                    mostrarUsers();
                                } else {
                                    DesktopNotify.showDesktopMessage("Fallo", "No tiene los permisos para esta opcion.", DesktopNotify.FAIL, 5000L);
                                    JOptionPane.showMessageDialog(null, "SOLO EL ADMINISTRADOR PUEDE REGISTRAR USUARIOS");

                                }

                                break;

                        }
                    } while (opsion != 5);

                    break;
                case 8:

                    Icon tema = new ImageIcon(getClass().getResource("/Imagenes/Tema.png"));
                    DesktopNotify.showDesktopMessage("¿Que le Permite esta opcion?",
                            "Se desplegara un sub menu donde encontrara dos opsiones,"
                            + " dopra escoger entre dos temas disponibles"
                            + "ligh mode o dark mode",
                            DesktopNotify.INFORMATION, 8000L);
                    do {
                        try {
                            letra = true;
                            Tema = Integer.parseInt((String) JOptionPane.showInputDialog(null, "DIGITE EL TEMA QUE DESEA APLICAR \n" + "1. LIGTH MODE\n" + "2. DARK MODE\n\n", "TEMA", JOptionPane.INFORMATION_MESSAGE, tema, null, ""));

                        } catch (Exception e) {
                            Icon error = new ImageIcon(Main.class.getResource("/Imagenes/ErrorNum.png"));
                            JOptionPane.showMessageDialog(null, "EL DATO INGRESADO ES INCORRECTO", "ERROR", JOptionPane.INFORMATION_MESSAGE, error);
                            letra = false;
                        }
                    } while (letra != true);
                    if (Tema == 1) {
                        UIManager.put("OptionPane.background", new Color(235, 235, 235));
                        UIManager.put("Panel.background", new Color(238, 238, 238));
                        UIManager.put("Button.background", new Color(235, 235, 235));
                        UIManager.put("Button.foreground", new Color(27, 27, 25));
                        UIManager.put("OptionPane.messageForeground", new Color(31, 31, 31));

                    } else if ((Tema == 2)) {
                        UIManager.put("OptionPane.background", new Color(24, 24, 24));
                        UIManager.put("Panel.background", new Color(19, 19, 19));
                        UIManager.put("Button.background", new Color(235, 235, 235));
                        UIManager.put("Button.foreground", new Color(27, 27, 25));
                        UIManager.put("OptionPane.messageForeground", new Color(166, 166, 166));

                    }

                    break;
                case 9:
                    DesktopNotify.showDesktopMessage("Cerrar sesion con Éxito", "Nos vemos: " + UsuarioqueAccedio, DesktopNotify.SUCCESS, 5000L);

                    break;
            }
        } while (op != 9);
    }

    /**
     * (float Totales): los totales los grafica de cada columna de la nomina
     *
     */
   public void Graficar(float Totales[]) {
        DefaultCategoryDataset data = new DefaultCategoryDataset();
     
        final String TO = "SUELDO ORDINARIO";
        final String TE = "SUELDO EXTRAORDINARIO";
        final String TB = "BONIFICACIONES";
        final String TC = "COMISIONES";
        final String TOTROS = "OTROS";
        final String TDEV = "TOTAL DEVENGADO";
        final String TIGGS = "IGGS";
        final String TISR = "ISR";
        final String TA = "ANTICIPOS";
        final String TDJ = "DESCUENTOS JUDICIALES";
        final String TOTROSDESC = "OTROS DESCUENTOS";
        final String TDESC = "TOTAL DESCUENTOS";
       
        
        data.addValue(Totales[0], TO, "SUELDO ORDINARIO");
        data.addValue(Totales[1], TE, "SUELDO EXTRAORDINARIO");
        data.addValue(Totales[2], TB, "BONIFICACIONES");
        data.addValue(Totales[3], TC, "COMISIONES");
        data.addValue(Totales[4], TOTROS, "OTROS");
        data.addValue(Totales[5], TDEV, "TOTAL DEVENGADO");
        data.addValue(Totales[6], TIGGS, "IGGS");
        data.addValue(Totales[7], TISR, "ISR");
        data.addValue(Totales[8], TA, "ANTICIPOS");
        data.addValue(Totales[9], TDJ, "DESCUENTOS JUDICIALES");
        data.addValue(Totales[10], TOTROSDESC, "OTROS DESCUENTOS");
        data.addValue(Totales[11], TDESC, "TOTAL DESCUENTOS");
    
        JFreeChart grafica = ChartFactory.createBarChart3D("NOMINA DE SUELDOS", "PERCEPCIONES / DEDEUCCIONES", "Y", data, PlotOrientation.VERTICAL, true, true, false);
        
        ChartPanel contenedor = new ChartPanel(grafica);
        JFrame ventana = new JFrame("Grafica");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.add(contenedor);
        ventana.setSize(700, 600);
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        Icon Grafica = new ImageIcon(getClass().getResource("/Imagenes/Grafica.png"));
        JOptionPane.showMessageDialog(null, "ACEPTAR PARA CERRAR GRAFICA ", "GRAFICA", JOptionPane.INFORMATION_MESSAGE, Grafica);
        ventana.setVisible(false);

    }
public void GraficaCircular(float Totales[]) {
       
        DefaultPieDataset dataset = new DefaultPieDataset();
        final String TO = "SUELDO ORDINARIO";
        final String TE = "SUELDO EXTRAORDINARIO";
        final String TB = "BONIFICACIONES";
        final String TC = "COMISIONES";
        final String TOTROS = "OTROS";
        final String TDEV = "TOTAL DEVENGADO";
        final String TIGGS = "IGGS";
        final String TISR = "ISR";
        final String TA = "ANTICIPOS";
        final String TDJ = "DESCUENTOS JUDICIALES";
        final String TOTROSDESC = "OTROS DESCUENTOS";
        final String TDESC = "TOTAL DESCUENTOS";
        dataset.setValue("SUELDO ORDINARIO", new Double(Totales[0]));
        dataset.setValue("SUELDO EXTRAORDINARIO", new Double(Totales[1]));
        dataset.setValue("BONIFICACIONES", new Double(Totales[2]));
        dataset.setValue("COMISIONES", new Double(Totales[3]));
        dataset.setValue( "OTROS", new Double(Totales[4]));
        dataset.setValue( "TOTAL DEVENGADO", new Double(Totales[5]));
        dataset.setValue( "IGGS", new Double(Totales[6]));
        dataset.setValue( "ISR", new Double(Totales[7]));
        dataset.setValue( "ANTICIPOS", new Double(Totales[8]));
        dataset.setValue( "DESCUENTOS JUDICIALES", new Double(Totales[9]));
        dataset.setValue( "OTROS DESCUENTOS", new Double(Totales[10]));
        dataset.setValue( "TOTAL DESCUENTOS", new Double(Totales[11]));
        
        JFreeChart grafica1 = ChartFactory.createPieChart3D("NOMINA DE SUELDOS",  dataset, true, true, false);
        ChartPanel contenedor = new ChartPanel(grafica1);
        JFrame ventana = new JFrame("Grafica");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.add(contenedor);
        ventana.setSize(700, 600);
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        Icon Grafica = new ImageIcon(getClass().getResource("/Imagenes/Grafica.png"));
        JOptionPane.showMessageDialog(null, "ACEPTAR PARA CERRAR GRAFICA ", "GRAFICA", JOptionPane.INFORMATION_MESSAGE, Grafica);
        ventana.setVisible(false);

    }
    /**
     * (String entrada): la nomina a ingresar a un documento excel (String
     * ruta): la ruta que tendra
     */
    public void generarExcel(String[][] entrada, String ruta) {
        try {
            WorkbookSettings conf = new WorkbookSettings();
            conf.setEncoding("ISO-8859-1");
            WritableWorkbook woorbook = Workbook.createWorkbook(new File(ruta), conf);

            WritableSheet sheet = woorbook.createSheet("RESULTADO", 0);
            WritableFont h = new WritableFont(WritableFont.COURIER, 12, WritableFont.NO_BOLD);
            WritableCellFormat hFormat = new WritableCellFormat(h);

            for (int i = 0; i < entrada.length; i++) // filas
            {
                for (int j = 0; j < entrada[i].length; j++) // columnas
                {

                    sheet.addCell(new jxl.write.Label(j, i, entrada[i][j], hFormat));

                }
            }
            woorbook.write();
            woorbook.close();

        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);

        } catch (WriteException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * (MensajeNo): si no hay registro tira mensaje que no hay
     *
     */
    public void registro() {

        /**
         * (NombreAux):Sirve para confirmar la contraseña
         */
        String NombreAux;
        /**
         * (Asteriscos):Nos va a servir para que cuando el usuario ingrese su
         * contraseña despues cuando acceda a la plataforma muestre la
         * contraseña en forma de asteriscos
         */
        String Asteriscos;

        /**
         * (Icon pass):Agregamos una Imagen a nuestro JOptionPane
         */
        Icon pass = new ImageIcon(getClass().getResource("/Imagenes/Password.png"));
        /**
         * (Icon user):Agregamos una Imagen a nuestro JOptionPane
         */
        Icon user = new ImageIcon(getClass().getResource("/Imagenes/User.png"));
        Icon mail = new ImageIcon(getClass().getResource("/Imagenes/Mail.png"));
        /**
         * Bucle do while creado para evitar que se ingrese un mismo nombre la
         * hora de registrar
         */
        do {
            /**
             * (Usuario):Le pide al usuario un Usuario (parentComponent):Agregar
             * un componente(Ejemplo:un objeto) en este caso no utilizamos un
             * componente por lo que colocamos un null (message):Ingresamos un
             * String en este caso nuestros (DIGITE EL NOMBRE DE USUARIO:)
             * (title):Ingresamos un String como Titulo en este caso es (User)
             * (icon):Ingresamos una imagen (selectionValues):Pide un objeto en
             * este caso nosotros no le ponemos por lo que es(null)
             * (initialSelectionValue):Pide un objeto en este caso nosotros no
             * le ponemos por lo que es(null)
             */
            Usuario = (String) JOptionPane.showInputDialog(null, "DIGITE EL NOMBRE DE USUARIO: ", "USER", JOptionPane.INFORMATION_MESSAGE, user, null, "");

            /**
             * Condicional que contiene: (BuscarUser):Metodo Creado en clase
             * (Login) (NombreAuxiliar):Pude ser cualquier String en este caso
             * es(Usuario).
             *
             * Si esta funcion devuelve un (true) muestra lo que hay adentro del
             * if
             */
            if (BuscarRegistroPorNombre(Usuario) == true) {
                /**
                 * (Usuario):Le pide al usuario un Usuario
                 * (parentComponent):Agregar un componente(Ejemplo:un objeto) en
                 * este caso no utilizamos un componente por lo que colocamos un
                 * null (message):Ingresamos un String en este caso es USUARIO
                 * YA REGISTRADO DIGITE DE NUEVO EL USUARIO M
                 */
                JOptionPane.showMessageDialog(null, "USUARIO YA REGISTRADO DIGITE DE NUEVO EL USUARIO");
            }

        } /**
         * (BuscarUser):Metodo Creado en clase (Login) (NombreAuxiliar):Pude ser
         * cualquier String en este caso es(Usuario).
         *
         * Si esta funcion devuelve un (true) sale de nuestro bucle do while
         */
        while (BuscarRegistroPorNombre(Usuario) == true);

        /**
         * (Contraseña):Le pide al usuario un Usuario (parentComponent):Agregar
         * un componente(Ejemplo:un objeto) en este caso no utilizamos un
         * componente por lo que colocamos un null (message):Ingresamos un
         * String en este caso nuestros (DIGITE LA CONTRASEÑA:)
         * (title):Ingresamos un String como Titulo en este caso es (PASSWORD)
         * (icon):Ingresamos una imagen (selectionValues):Pide un objeto en este
         * caso nosotros no le ponemos por lo que es(null)
         * (initialSelectionValue):Pide un objeto en este caso nosotros no le
         * ponemos por lo que es(null)
         */
        Contraseña = (String) JOptionPane.showInputDialog(null, "DIGITE LA CONTRASEÑA: ", "PASSWORD", JOptionPane.INFORMATION_MESSAGE, pass, null, "");
        /**
         * (char[] caracteres):Este vector nos va a servir para convertir mi
         * (contraseña) a asteriscos
         */
        char[] caracteres = Contraseña.toCharArray();

        /**
         * Bucle for nos va a servir para que llenar mi vector (caracteres[])
         * del tamaño de la cantidad de letras (caracteres)que tiene mi
         * (contraseña).
         *
         */
        for (int i = 0; i < caracteres.length; i++) {
            caracteres[i] = '*';

        }
        /**
         * ( Asteriscos = String.valueOf(caracteres)):Sirve para que
         * (Asteriscos) solo guarde los caracteres de (caracteres[])
         */
        Asteriscos = String.valueOf(caracteres);

        do {
            /**
             * (NombreAux):Le pide al usuario un String que va a ser la
             * confirmacion de mi contraseña (parentComponent):Agregar un
             * componente(Ejemplo:un objeto) en este caso no utilizamos un
             * componente por lo que colocamos un null (message):Ingresamos un
             * String en este caso nuestros (CONFIRME LA CONTRASEÑA:)
             * (title):Ingresamos un String como Titulo en este caso es
             * (PASSWORD) (icon):Ingresamos una imagen (selectionValues):Pide un
             * objeto en este caso nosotros no le ponemos por lo que es(null)
             * (initialSelectionValue):Pide un objeto en este caso nosotros no
             * le ponemos por lo que es(null)
             */
            NombreAux = (String) JOptionPane.showInputDialog(null, "CONFIRME LA CONTRASEÑA: ", "PASSWORD", JOptionPane.INFORMATION_MESSAGE, pass, null, "");

            /**
             * (!NombreAux.equals(Contraseña)):Si la contraseña es diferente el
             * do while hace que vuelva a pedir la contraseña , sale del do
             * while hasta que las contraseñas sean iguales.
             */
        } while (!NombreAux.equals(Contraseña));

        /**
         * Muestra un JOptionPane los parametros: (parentComponent):Agregar un
         * componente(Ejemplo:un objeto) en este caso no utilizamos un
         * componente por lo que colocamos un null (message):Ingresamos un
         * String en este caso nuestros (USER:) (title):Ingresamos un String
         * como Titulo en este caso es (PASSWORD)
         */
        JOptionPane.showMessageDialog(null, "USER: " + Usuario + "\n" + "PASSWORD: " + Asteriscos);
        /**
         * (PreguntaClave):Le pide al usuario un String que va a ser la
         * confirmacion de mi contraseña (parentComponent):Agregar un
         * componente(Ejemplo:un objeto) en este caso no utilizamos un
         * componente por lo que colocamos un null (message):Ingresamos un
         * String en este caso nuestros (RESPONDA LA SIGUIENTE PREGUNTA: ¿CUAL
         * ES SU NUMERO DE TELEFONO?:) (title):Ingresamos un String como Titulo
         * en este caso es (PREGUNTA CLAVE) (icon):Ingresamos una imagen
         * (selectionValues):Pide un objeto en este caso nosotros no le ponemos
         * por lo que es(null) (initialSelectionValue):Pide un objeto en este
         * caso nosotros no le ponemos por lo que es(null)
         */
        PreguntaClave = (String) JOptionPane.showInputDialog(null, "RESPONDA LA SIGUIENTE PREGUNTA: ¿CUAL ES SU CORREO ELECTRONICO? ", "PREGUNTA CLAVE", JOptionPane.INFORMATION_MESSAGE, mail, null, "");

        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
            Connection cn = DriverManager.getConnection(Database, UsuarioDB, Clave);
            PreparedStatement pst = cn.prepareStatement("insert into Usuarios values(?,?,?,?,?,?)");
            pst.setString(1, "0");
            pst.setString(2, Usuario);
            pst.setString(3, Contraseña);
            pst.setString(4, hostname);
            pst.setString(5, addr.getHostAddress());
            pst.setString(6, PreguntaClave);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            PrintWriter Esc = new PrintWriter("Usuarios.txt");
            String[] reciboPalabraUsuario = new String[5];
            Connection c = DriverManager.getConnection(Database, UsuarioDB, Clave);
            String query = "select * from Usuarios";
            PreparedStatement consulta = c.prepareStatement(query);
            ResultSet resultado = consulta.executeQuery();

            int columna_1 = resultado.findColumn("Username");
            int columna_2 = resultado.findColumn("Password_Usuario");
            int columna_3 = resultado.findColumn("HostUsuario");
            int columna_4 = resultado.findColumn("IP");
            int columna_5 = resultado.findColumn("Correo");

            while (resultado.next()) {
                reciboPalabraUsuario[0] = resultado.getString(columna_1);
                reciboPalabraUsuario[1] = resultado.getString(columna_2);
                reciboPalabraUsuario[2] = resultado.getString(columna_3);
                reciboPalabraUsuario[3] = resultado.getString(columna_4);
                reciboPalabraUsuario[4] = resultado.getString(columna_5);

                Esc.println("USER: " + reciboPalabraUsuario[0]);

                Esc.println("PASSWORD: " + reciboPalabraUsuario[1]);

                Esc.println("HOST: " + reciboPalabraUsuario[2]);

                Esc.println("IP LOCAL: " + reciboPalabraUsuario[3]);

                Esc.println("MAIL: " + reciboPalabraUsuario[4] + "\n");

            }

            Esc.close();

        } catch (Exception err) {

        }
    }

    public void modificar(int opsion, String dato, String UsuarioaModificar) {
        String numero = UsuarioaModificar;

        GuardardatoBD(numero);

        try {

            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
            Connection cn = DriverManager.getConnection(Database, UsuarioDB, Clave);
            PreparedStatement pst = cn.prepareStatement("update Usuarios set ID =?, Username = ?, Password_Usuario = ?, HostUsuario =?, IP = ?, Correo = ? where ID = " + numero);

            pst.setString(1, numero);
            if (opsion == 1) {

                pst.setString(2, dato);
                pst.setString(3, UsuarioaModificarenBD[1]);
                pst.setString(4, hostname);
                pst.setString(5, addr.getHostAddress());
                pst.setString(6, UsuarioaModificarenBD[4]);
            }
            if (opsion == 2) {
                pst.setString(2, UsuarioaModificarenBD[0]);
                pst.setString(3, dato);
                pst.setString(4, hostname);
                pst.setString(5, addr.getHostAddress());
                pst.setString(6, UsuarioaModificarenBD[4]);
            }

            if (opsion == 3) {
                pst.setString(2, UsuarioaModificarenBD[0]);
                pst.setString(3, UsuarioaModificarenBD[1]);
                pst.setString(4, hostname);
                pst.setString(5, addr.getHostAddress());
                pst.setString(6, dato);
            }
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            PrintWriter Esc = new PrintWriter("Usuarios.txt");
            String[] reciboPalabraUsuario = new String[5];
            Connection c = DriverManager.getConnection(Database, UsuarioDB, Clave);
            String query = "select * from Usuarios";
            PreparedStatement consulta = c.prepareStatement(query);
            ResultSet resultado = consulta.executeQuery();

            int columna_1 = resultado.findColumn("Username");
            int columna_2 = resultado.findColumn("Password_Usuario");
            int columna_3 = resultado.findColumn("HostUsuario");
            int columna_4 = resultado.findColumn("IP");
            int columna_5 = resultado.findColumn("Correo");

            while (resultado.next()) {
                reciboPalabraUsuario[0] = resultado.getString(columna_1);
                reciboPalabraUsuario[1] = resultado.getString(columna_2);
                reciboPalabraUsuario[2] = resultado.getString(columna_3);
                reciboPalabraUsuario[3] = resultado.getString(columna_4);
                reciboPalabraUsuario[4] = resultado.getString(columna_5);

                Esc.println("USER: " + reciboPalabraUsuario[0]);

                Esc.println("PASSWORD: " + reciboPalabraUsuario[1]);

                Esc.println("HOST: " + reciboPalabraUsuario[2]);

                Esc.println("IP LOCAL: " + reciboPalabraUsuario[3]);

                Esc.println("MAIL: " + reciboPalabraUsuario[4] + "\n");

            }

            Esc.close();

        } catch (Exception err) {

        }

    }

    public void eliminar(String UsuarioaEliminar) {
        try {
            Connection cn = DriverManager.getConnection(Database, UsuarioDB, Clave);
            PreparedStatement pst = cn.prepareStatement("delete from Usuarios where ID = ?");

            pst.setString(1, UsuarioaEliminar);
            pst.executeUpdate();

        } catch (Exception e) {
        }
        try {
            PrintWriter Esc = new PrintWriter("Usuarios.txt");
            String[] reciboPalabraUsuario = new String[5];
            Connection c = DriverManager.getConnection(Database, UsuarioDB, Clave);
            String query = "select * from Usuarios";
            PreparedStatement consulta = c.prepareStatement(query);
            ResultSet resultado = consulta.executeQuery();

            int columna_1 = resultado.findColumn("Username");
            int columna_2 = resultado.findColumn("Password_Usuario");
            int columna_3 = resultado.findColumn("HostUsuario");
            int columna_4 = resultado.findColumn("IP");
            int columna_5 = resultado.findColumn("Correo");

            while (resultado.next()) {
                reciboPalabraUsuario[0] = resultado.getString(columna_1);
                reciboPalabraUsuario[1] = resultado.getString(columna_2);
                reciboPalabraUsuario[2] = resultado.getString(columna_3);
                reciboPalabraUsuario[3] = resultado.getString(columna_4);
                reciboPalabraUsuario[4] = resultado.getString(columna_5);

                Esc.println("USER: " + reciboPalabraUsuario[0]);

                Esc.println("PASSWORD: " + reciboPalabraUsuario[1]);

                Esc.println("HOST: " + reciboPalabraUsuario[2]);

                Esc.println("IP LOCAL: " + reciboPalabraUsuario[3]);

                Esc.println("MAIL: " + reciboPalabraUsuario[4] + "\n");

            }

            Esc.close();

        } catch (Exception err) {

        }
    }

    public boolean BuscarUser(String NombreAuxiliar) {
        try {
            /**
             * (FileReader Leer):Sirve para crear un nuevo archivo para leer en
             * este caso es (Leer) con un parametro: (fileName):Se le da un
             * nombre al nuevo archivo creado en este caso es (Usuarios.txt)
             */
            FileReader Leer = new FileReader("Usuarios.txt");
            /**
             * (BufferedReader Buffer):Sirve para leer una secuencia de
             * caracteres con un parametro: (FileReader):En este caso utilizamos
             * el archivo de lectura llamado (Leer)
             *
             */
            BufferedReader Buffer = new BufferedReader(Leer);

            /**
             * (String temp):Nos va a servir para Guardar el nombre de el
             * archivo a leer en este caso de mi (Buffer), por que necesitamos
             * saber si el nombre que el usuario ingreso existe en mi archivo en
             * este caso (Leer)
             */
            String temp = " ";
            /**
             * (String BfRead):Nos va a servir parapara poder ir leyendo mi
             * usuario y contraseña
             */
            String BfRead;

            /**
             * Bucle While que nos va a decir primero que ((BfRead =
             * Buffer.readLine())):Nos Indica que mi String (BfRead) nos va a
             * leer siempre la primera linea de mi archivo y segundo que
             * ((BfRead = Buffer.readLine()) != null):El blucle va a seguir
             * hasta que mi que archivo este vacio osea hasta que mi archivo ya
             * no tenga ningun caracter , mi archivo este vacio osea hasta que
             * mi archivo llegue a (null)
             */
            while ((BfRead = Buffer.readLine()) != null) {
                /**
                 * (temp):Como se dijo antes va a contener la primera linea de
                 * mi archivo osea mi nombre de (Usuario)
                 */
                temp = BfRead;
                /**
                 * Condicional que nos sirve para verificar que el usuario
                 * ingrese el Usuario es el mismo que el Usuario registrado en
                 * mi archivo(Leer) osea que (temp = Usuario)
                 */
                if (temp.equals(NombreAuxiliar)) {
                    //JOptionPane.showMessageDialog(null,"NOMBRE ENCONTRADO");
                    /**
                     * Si se cumple nuestro condicional significa que el nombre
                     * ingresado y el nombre de mi Arhivo(Leer) son los mismos,
                     * por lo que la funcion nos retorna un (true)
                     */
                    return true;
                } /**
                 * (else):Si mi condicion no se cumple.
                 */
                else {
                    /**
                     * Ciclo for que nos va a servir para saltar 5 espacios
                     * abajo para llegar de nuevo a leer mi (Usuario) , se
                     * preguntaran porque se saltan 5 espacios? La razon es por
                     * que asi podemos hacer que salte a que lea un Usuario, sin
                     * que pase por los otros Strings que no son necesarios para
                     * ingresar a nuestra plataforma.
                     *
                     * Ejemplo: Nuestro documento en este caso (Usuarios.txt)
                     * tiene tiene guardados 5 Strings que son:
                     * 1.Usuario,2.Contraseña,3.IP,4.Host,5.Telefono. Entonces
                     * como se guardarian en nuestro archivos? Se guardarian
                     * verticalmente asi:
                     *
                     * Usuario Contraseña IP Host Telefono Usuario Contraseña
                     *
                     * El for entonces sirve para esto si se dan cuenta solo
                     * necesitamos 1 parametros (Usuario) por lo que los demas
                     * no nos sirven en este caso, entonces para evitar que mi
                     * (Buffer) pase por los parametros que no neesitamos
                     * hacemos 5 saltos de linea en mi archivo hasta llergar de
                     * nuevo a (Usuario) , asi se repite este buble hasta que el
                     * archivo este vacio
                     *
                     */
                    for (int i = 0; i < 5; i++) {
                        /**
                         * (BfRead):Adentro de este for sirve para leer
                         * rapidamente los parametros que no deseamos obtener en
                         * este caso.
                         */
                        BfRead = Buffer.readLine();
                    }
                }

            }

        } /**
         * (try y catch): Sirve para que cuando un proceso entre a mi try y
         * encuentre un error o excepcion en el catch nos muestre el error
         */
        catch (Exception err) {
            /**
             * (return false):Si mi Programa encuntra algun problema que me
             * retorne un (false) como que si no hubiera encontrado mi usuario
             */
            return false;
        }
        /**
         * (return false):Si mi Programa no encuntra mi usuario en el registro
         * (Leer) wur retorne un (false)
         */
        return false;
    }

    public void MensajeNO() {

        Icon icon = new ImageIcon(getClass().getResource("/Imagenes/NohayRegistros.png"));
        JOptionPane.showMessageDialog(null, "SIN REGISTROS\n" + "POR FAVOR INGRESE DATA ", "PLANILLA VACIA", JOptionPane.INFORMATION_MESSAGE, icon);

    }

    public void GuardardatoBD(String IDaModificar) {

        try {

            Connection c = DriverManager.getConnection(Database, UsuarioDB, Clave);
            String query = "select * from Usuarios where ID = ?";
            PreparedStatement consulta = c.prepareStatement(query);
            consulta.setString(1, IDaModificar);
            ResultSet resultado = consulta.executeQuery();

            int columna_1 = resultado.findColumn("Username");
            int columna_2 = resultado.findColumn("Password_Usuario");
            int columna_3 = resultado.findColumn("HostUsuario");
            int columna_4 = resultado.findColumn("IP");
            int columna_5 = resultado.findColumn("Correo");

            while (resultado.next()) {
                UsuarioaModificarenBD[0] = resultado.getString(columna_1);
                UsuarioaModificarenBD[1] = resultado.getString(columna_2);
                UsuarioaModificarenBD[2] = resultado.getString(columna_3);
                UsuarioaModificarenBD[3] = resultado.getString(columna_4);
                UsuarioaModificarenBD[4] = resultado.getString(columna_5);

            }

        } catch (Exception err) {

        }
    }

    public Boolean BuscarRegistroDB(String Usuario_Basededatos) {

        try {
            Connection cn = DriverManager.getConnection(Database, UsuarioDB, Clave);
            PreparedStatement pst = cn.prepareStatement("select * from Usuarios where ID = ?");
            pst.setString(1, Usuario_Basededatos);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                return true;

            } else {

                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean BuscarRegistroPorNombre(String Usuario_Basededatos) {

        try {
            Connection cn = DriverManager.getConnection(Database, UsuarioDB, Clave);
            PreparedStatement pst = cn.prepareStatement("select * from Usuarios where Username = ?");
            pst.setString(1, Usuario_Basededatos);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                return true;

            } else {

                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void mostrarUsers() {
        int Registros = CantidadDeRegistros();
        String[][] reciboPalabraUsuario = new String[Registros + 1][6];
        String[] Titulos = {"ID", "Usuarios", "Password", "Host", "IP", "Correo Electronico"};
        try {

            Connection c = DriverManager.getConnection(Database, UsuarioDB, Clave);
            String query = "select * from Usuarios";
            PreparedStatement consulta = c.prepareStatement(query);
            ResultSet resultado = consulta.executeQuery();

            int columna_0 = resultado.findColumn("ID");
            int columna_1 = resultado.findColumn("Username");
            int columna_2 = resultado.findColumn("Password_Usuario");
            int columna_3 = resultado.findColumn("HostUsuario");
            int columna_4 = resultado.findColumn("IP");
            int columna_5 = resultado.findColumn("Correo");

            reciboPalabraUsuario[0][0] = Titulos[0];
            reciboPalabraUsuario[0][1] = Titulos[1];
            reciboPalabraUsuario[0][2] = Titulos[2];
            reciboPalabraUsuario[0][3] = Titulos[3];
            reciboPalabraUsuario[0][4] = Titulos[4];
            reciboPalabraUsuario[0][5] = Titulos[5];
            int filas = 1;
            while (resultado.next()) {
                reciboPalabraUsuario[filas][0] = resultado.getString(columna_0);
                reciboPalabraUsuario[filas][1] = resultado.getString(columna_1);
                reciboPalabraUsuario[filas][2] = resultado.getString(columna_2);
                reciboPalabraUsuario[filas][3] = resultado.getString(columna_3);
                reciboPalabraUsuario[filas][4] = resultado.getString(columna_4);
                reciboPalabraUsuario[filas][5] = resultado.getString(columna_5);
                filas++;

            }
            System.out.println(" ");
            System.out.println("\033[33m<< " + "\033[37m---USUARIOS---" + "\033[33m >>");
            for (int i = 0; i < Registros + 1; i++) {

                for (int j = 0; j < 6; j++) {
                    if (i == 0) {
                        System.out.print("\033[33m" + reciboPalabraUsuario[i][j]);

                    } else {
                        if (j == 0) {
                            System.out.print("\033[35m" + reciboPalabraUsuario[i][j]);
                        } else {
                            System.out.print("\033[37m" + reciboPalabraUsuario[i][j]);
                        }

                    }

                    for (int l = 0; l < gotoxMatrizParaDB(reciboPalabraUsuario, j, i, Registros + 1); l++) {
                        System.out.print(" ");

                    }

                }
                System.out.println("");
            }

            for (int j = 0; j < 265; j++) {

                System.out.print("\033[34m-");
            }
            System.out.println("");
            Matriz EjecutarTabla = new Matriz(Titulos, reciboPalabraUsuario, Registros, 6);
            generarPDF(reciboPalabraUsuario, Registros);
        } catch (Exception err) {

        }
    }

    public int CantidadDeRegistros() {
        int CantidadDeRegistros = 0;
        try {

            Connection c = DriverManager.getConnection(Database, UsuarioDB, Clave);
            String query = "select * from Usuarios";
            PreparedStatement consulta = c.prepareStatement(query);
            ResultSet resultado = consulta.executeQuery();
            ResultSet AuxParaCantidadRegistros = resultado;

            while (AuxParaCantidadRegistros.next()) {
                CantidadDeRegistros++;
            }
        } catch (Exception e) {

        }
        return CantidadDeRegistros;
    }

    public void generarPDF(String[][] Contenido, int Registros) {
        String[] Parafo = new String[6];

        try {
            FileOutputStream archivo = new FileOutputStream("Registros_Usuarios.pdf");
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);

            doc.open();
            doc.add(CabezaPDF("USUARIOS"));

            doc.add(new Paragraph(""));

            for (int i = 0; i < Registros + 1; i++) {

                for (int j = 0; j < 6; j++) {
                    Parafo[j] = Contenido[i][j];

                    for (int l = 0; l < gotoxMatrizParaDB(Contenido, j, i, Registros + 1) - 2; l++) {
                        Parafo[j] = Parafo[j] + " ";
                    }
                }

                doc.add(CuerpoPDF(Parafo[0] + Parafo[1] + Parafo[2] + Parafo[3] + Parafo[4] + Parafo[5]));

            }
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));

            Image logo = Image.getInstance("Logo.png");
            logo.scaleAbsolute(128, 128);
            logo.setAlignment(Element.ALIGN_CENTER);
            doc.add(logo);

            doc.close();
        } catch (Exception e) {

        }

    }

    public Paragraph CabezaPDF(String titulo) {
        Font fuente = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);

        Paragraph linea = new Paragraph();
        Chunk c = new Chunk();
        linea.setAlignment(Element.ALIGN_CENTER);
        c.append(titulo);
        c.setFont(fuente);
        linea.add(c);
        return linea;
    }

    public Paragraph CuerpoPDF(String titulo) {
        Font fuente = new Font(Font.FontFamily.COURIER, 9, Font.NORMAL);
        Paragraph linea = new Paragraph();
        Chunk c = new Chunk();
        linea.setAlignment(Element.ALIGN_CENTER);
        c.append(titulo);
        c.setFont(fuente);
        linea.add(c);
        return linea;
    }

    public Paragraph FinalPDF(String titulo) {
        Font fuente = new Font(Font.FontFamily.COURIER, 9, Font.ITALIC);
        Paragraph linea = new Paragraph();
        Chunk c = new Chunk();
        linea.setAlignment(Element.ALIGN_CENTER);
        c.append(titulo);
        c.setFont(fuente);
        linea.add(c);
        return linea;
    }

}
