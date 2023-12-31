package Odev;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import Odev.Dispatcher;

public class Main {

	public static void main(String[] args) {
		
		Dispatcher dispatcher = new Dispatcher();

		// burada giris.txt okunarak her satır dispatcherin process listesine ekleniyor
        try (BufferedReader br = new BufferedReader(new FileReader("giris.txt"))) {
            String satir;
            int id = 0;
            while ((satir = br.readLine()) != null) {
     
                String[] lineStr = satir.split(", ");
                Process process = new Process();
                process.arriveTime = Integer.parseInt(lineStr[0]) ;
                process.priority = Integer.parseInt(lineStr[1]) ;
                process.burstTime = Integer.parseInt(lineStr[2]) ;
                process.memorySize = Integer.parseInt(lineStr[3]) ;
                process.printers = Integer.parseInt(lineStr[4]) ;
                process.scanner = Integer.parseInt(lineStr[5]) ;
                process.modem = Integer.parseInt(lineStr[6]) ;
                process.cdDrivers = Integer.parseInt(lineStr[7]);
                process.id=id;
                id++;
                dispatcher.processList.add(process);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("Pid   varış   öncelik   cpu   MBytes      prn    scn   modem      cd     status");
        System.out.println("===============================================================================");
        
        //dispatcher başlatılıyor
        dispatcher.program();
        
	}

}
