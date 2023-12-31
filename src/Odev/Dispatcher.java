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
    
    // processin kullandığı bütün kaynakları bırakma fonksiyonu
    public void leaveAllSource(Process process) {
    	memory.leaveMemory(process);
    	usingScanner-= process.scanner;
    	usingModem-= process.modem;
    	usingPrinter-= process.printers;
    	usingCdDriver-=process.cdDrivers;
    }

    // bütün processler tamamlandı mı kontrolü için fonksiyon
    public boolean isComletedAllProcess() {
        return dispatcherTimer != 0
                && userQueue.isEmpty()
                && realTimeQueue.isEmpty()
                && priority1.isEmpty()
                && priority2.isEmpty()
                && priority3.isEmpty()
                && processList.isEmpty();
    }

    // geri beslemeli kuyruğa ekleme fonksiyonu
    public void addMultiLevelQueue(Process process){
        int priority = process.priority;
         if (priority == 1){
             priority1.add(process);
         }
        else if (priority == 2){
             priority2.add(process); 
         }
        else{
             priority3.add(process);
         }
        process.queueTime=this.dispatcherTimer;
    }


    // kaynak tahsis etme fonksiyonu
    public void allocateProcessResources(Process process){
    	usingModem += process.modem;
    	usingPrinter += process.printers;
    	usingCdDriver += process.cdDrivers;
    	usingScanner += process.scanner;
    	memory.allocateMemory(process);
    }

    // zaman aşımı kontrolu yapılan fonksiyon
    public void overTime() {
        realTimeQueue.removeIf(process -> {
            if (this.dispatcherTimer == process.queueTime + 20) {
            	System.out.println(process.id + "\tHATA - Proses zaman aşımı (20 sn de tamamlanamadı)");
                this.leaveAllSource(process);
                return true;  
            }
            return false;  
        });
        priority1.removeIf(process -> {
        	if (this.dispatcherTimer == process.queueTime + 20) {
            	System.out.println(process.id + "\tHATA - Proses zaman aşımı (20 sn de tamamlanamadı)");
                this.leaveAllSource(process);
                return true;  
            }
            return false;  
        });
        priority2.removeIf(process -> {
        	if (this.dispatcherTimer == process.queueTime + 20) {
            	System.out.println(process.id + "\tHATA - Proses zaman aşımı (20 sn de tamamlanamadı)");
                this.leaveAllSource(process);
                return true;  
            }
            return false;  
        });
        priority3.removeIf(process -> {
        	if (this.dispatcherTimer == process.queueTime + 20) {
            	System.out.println(process.id + "\tHATA - Proses zaman aşımı (20 sn de tamamlanamadı)");
                this.leaveAllSource(process);
                return true;  
            }
            return false;  
        });
        userQueue.removeIf(process -> {
        	if (this.dispatcherTimer == process.queueTime + 20) {
            	System.out.println(process.id + "\tHATA - Proses zaman aşımı (20 sn de tamamlanamadı)");
                this.leaveAllSource(process);
                return true;  
            }
            return false;  
        });
    }

    public void suspend(){
        if(!priority1.isEmpty()){
        	 Process process = priority1.peek();
        	 process.status="ASKIYA ALINDI";
        	 System.out.println(process.id + "\t    " + process.arriveTime + "\t\t" + process.priority + "\t  " + process.memorySize + "\t   " + process.printers + "\t\t" + process.scanner + "\t   " + process.modem + "\t  "+ process.cdDrivers + "\t" + process.status);
        }
        else if(! priority2.isEmpty()){
            Process process = priority2.remove();  
            priority1.add(process);
            process.status="ASKIYA ALINDI";
            System.out.println(process.id + "\t    " + process.arriveTime + "\t\t" + process.priority + "\t  " + process.memorySize + "\t   " + process.printers + "\t\t" + process.scanner + "\t   " + process.modem + "\t  "+ process.cdDrivers + "\t" + process.status);
        }
        else if(!priority3.isEmpty()){
            Process process = priority3.remove();  
            priority2.add(process);
            process.status="ASKIYA ALINDI";
            System.out.println(process.id + "\t    " + process.arriveTime + "\t\t" + process.priority + "\t  " + process.memorySize + "\t   " + process.printers + "\t\t" + process.scanner + "\t   " + process.modem + "\t  "+ process.cdDrivers + "\t" + process.status);
        }
    }
    
    public void processScheduling() {
    	if(!realTimeQueue.isEmpty()){
            Process firstComeProcess = realTimeQueue.peek();
            firstComeProcess.workTime++;
            
            // burada ilk gelen process ilk çalıştırılır
            firstComeProcess.status="RUNNING";
            System.out.println(firstComeProcess.id + "\t    " + firstComeProcess.arriveTime + "\t\t" + firstComeProcess.priority + "\t  " + firstComeProcess.memorySize + "\t   " + firstComeProcess.printers + "\t\t" + firstComeProcess.scanner + "\t   " + firstComeProcess.modem + "\t  "+ firstComeProcess.cdDrivers + "\t" + firstComeProcess.status);

            // burada patlama zamanı ve çalışma zamanı eşitliğini kontrol eder
            if(firstComeProcess.burstTime == firstComeProcess.workTime){
                firstComeProcess.status="COMPLETED";
                System.out.println(firstComeProcess.id + "\t    " + firstComeProcess.arriveTime + "\t\t" + firstComeProcess.priority + "\t  " + firstComeProcess.memorySize + "\t   " + firstComeProcess.printers + "\t\t" + firstComeProcess.scanner + "\t   " + firstComeProcess.modem + "\t  "+ firstComeProcess.cdDrivers + "\t" + firstComeProcess.status);
                Process process = realTimeQueue.remove(); 
                this.leaveAllSource(firstComeProcess); 
            }
        }
        else if (!priority1.isEmpty()){
            Process process = priority1.remove();
            process.workTime++;
            process.status="RUNNING";
            System.out.println(process.id + "\t    " + process.arriveTime + "\t\t" + process.priority + "\t  " + process.memorySize + "\t   " + process.printers + "\t\t" + process.scanner + "\t   " + process.modem + "\t  "+ process.cdDrivers + "\t" + process.status);

            if(!(process.burstTime == process.workTime)){
                priority2.add(process);
            }
            else{
            	leaveAllSource(process);
                process.status="COMPLETED";
                System.out.println(process.id + "\t    " + process.arriveTime + "\t\t" + process.priority + "\t  " + process.memorySize + "\t   " + process.printers + "\t\t" + process.scanner + "\t   " + process.modem + "\t  "+ process.cdDrivers + "\t" + process.status);
            }
        }
        else if (!priority2.isEmpty()) {
        	Process process = priority2.remove();
            process.workTime++;
            process.status="RUNNING";
            System.out.println(process.id + "\t    " + process.arriveTime + "\t\t" + process.priority + "\t  " + process.memorySize + "\t   " + process.printers + "\t\t" + process.scanner + "\t   " + process.modem + "\t  "+ process.cdDrivers + "\t" + process.status);

            if(!(process.burstTime == process.workTime)){
                priority3.add(process);
            }
            else{
            	leaveAllSource(process);
                process.status="COMPLETED";
                System.out.println(process.id + "\t    " + process.arriveTime + "\t\t" + process.priority + "\t  " + process.memorySize + "\t   " + process.printers + "\t\t" + process.scanner + "\t   " + process.modem + "\t  "+ process.cdDrivers + "\t" + process.status);
            }

        }
        else if (!priority3.isEmpty()) {
        	Process process = priority3.remove();
            process.workTime++; 
            process.status="RUNNING";
            System.out.println(process.id + "\t    " + process.arriveTime + "\t\t" + process.priority + "\t  " + process.memorySize + "\t   " + process.printers + "\t\t" + process.scanner + "\t   " + process.modem + "\t  "+ process.cdDrivers + "\t" + process.status);

            if(!(process.burstTime == process.workTime)){
                priority3.add(process);
            }
            else{
            	leaveAllSource(process);
                process.status="COMPLETED";
                System.out.println(process.id + "\t    " + process.arriveTime + "\t\t" + process.priority + "\t  " + process.memorySize + "\t   " + process.printers + "\t\t" + process.scanner + "\t   " + process.modem + "\t  "+ process.cdDrivers + "\t" + process.status);
            }
        }
    }

    
    
}