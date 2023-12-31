package Odev;

import java.util.LinkedList;
import java.util.Queue;
import Odev.Process;
import Odev.Memory;



public class Dispatcher {
	
	public Queue<Process> processList;
	public Queue<Process> realTimeQueue;
	public LinkedList<Process> userQueue;
	public Queue<Process> priority1;
    public Queue<Process> priority2;
    public Queue<Process> priority3;
    // burada kaynaklar atanıyor
    public int systemCdDriver = 2;
    public int usingCdDriver = 0;
    public int systemPrinter = 2;
    public int usingPrinter = 0;
    public int systemModem = 1;
    public int usingModem = 0;
    public int systemScanner = 1;
    public int usingScanner = 0;
    public int dispatcherTimer=0;
    public Memory memory;
    
    // yapıcıda geri beslemeli kuyruklar, process listesi, gerçek zamanlı ve kullanıcı kuyrukları , memory bellekte oluşturuluyor
    Dispatcher(){
    	this.processList=new LinkedList<>();
        this.priority1 = new LinkedList<>();
        this.priority2 = new LinkedList<>();
        this.priority3 = new LinkedList<>();
        this.realTimeQueue  = new LinkedList<>();
        this.userQueue = new LinkedList<>();
        this.memory= new Memory();
    }
    
    // mainde çalışacak program fonksiyonu
    
    
    // çalışmak için hazır mı fonksiyonu
    public  boolean isReadyToWork(Process process){
        return  process.scanner <= this.systemScanner
                && process.printers <= this.systemPrinter
                && process.modem <= this.systemModem
                && process.cdDrivers <= this.systemCdDriver
                && process.memorySize <= memory.getMaximumMemory(process);
    }

    // yeterli kaynak var mı kontrolü
    public boolean isEnoughSourceProcess(Process process){
        return process.modem <= systemModem - usingModem
                && memory.isAvailable(process)
                && process.scanner <= systemScanner - usingScanner
                && process.printers <= systemPrinter - usingPrinter
                && process.cdDrivers <= systemCdDriver - usingCdDriver;
    }
    


    
    
}