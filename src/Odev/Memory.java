package Odev;

import java.util.BitSet;

public class Memory {

    // Belleği temsil eden BitSet nesnesi
    private BitSet memoryBitmap;

    // Kullanıcı işlemleri için ayrılan maksimum bellek boyutu
    private int maximumUserSize = 960;

    // Gerçek zamanlı işlemler için ayrılan maksimum bellek boyutu
    private int maximumRealTimeSize = 64;

    // Bellek sınıfının yapıcı metodu
    public Memory() {
        // Bellek dizisini oluşturuyoruz (her eleman bir hafıza bloğunu temsil eder)
        memoryBitmap = new BitSet(1024);

        // Tüm bellek bloklarını boş olarak işaretle
        memoryBitmap.clear();
    }

    // Verilen bir işlem için bellekte yer var mı kontrol eder
    public boolean isAvailable(Process process) {
        int startIndex = getStartingIndex(process);
        return startIndex != -1 && !hasIntersectingBits(startIndex, process.memorySize);
    }

    // Verilen bir işlem için bellekte yer ayırır
    public int allocateMemory(Process process) {
        int beginningIndex = getStartingIndex(process);
        int endIndex = beginningIndex;

        // Bellekte işlem boyutu kadar uygun bir bölgeyi bul
        while (endIndex < memoryBitmap.length() && !hasIntersectingBits(endIndex, process.memorySize)) {
            endIndex++;
        }

        if (endIndex - beginningIndex >= process.memorySize) {
            // Bellekte işlem boyutu kadar alanı işgal et
            setBits(beginningIndex, process.memorySize);
            process.begginingAddress = beginningIndex;
            return beginningIndex;
        }

        return -1; // Uygun bölge bulunamadı
    }

    // Verilen bir işlemin bellekte işgal ettiği alanı serbest bırakır
    public void leaveMemory(Process process) {
        // Bellekte işlem boyutu kadar alanı serbest bırak
        clearBits(process.begginingAddress, process.memorySize);
    }

    // İşlem önceliğine göre başlangıç indeksini belirler
    private int getStartingIndex(Process process) {
        return (process.priority == 0) ? 0 : 64;
    }

    // İki BitSet arasında kesişen bitlerin olup olmadığını kontrol eder
    private boolean hasIntersectingBits(int startIndex, int length) {
        for (int i = 0; i < length; i++) {
            if (memoryBitmap.get(startIndex + i)) {
                return true;
            }
        }
        return false;
    }

    // Belirli bir aralıktaki bitleri set eder
    private void setBits(int startIndex, int length) {
        for (int i = 0; i < length; i++) {
            memoryBitmap.set(startIndex + i);
        }
    }

    // Belirli bir aralıktaki bitleri temizler
    private void clearBits(int startIndex, int length) {
        for (int i = 0; i < length; i++) {
            memoryBitmap.clear(startIndex + i);
        }
    }

    // Verilen bir işlemin alabileceği maksimum bellek boyutunu döndürür
    public int getMaximumMemory(Process process) {
        return (process.priority == 0) ? maximumRealTimeSize : maximumUserSize;
    }
}
