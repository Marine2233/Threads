package thread.HWThread.ProectPractic;

public class AirplaneApp {
    public static void main(String[] args) {
        Airplane sochi = new Airplane("B-147","Sochi");
        Airplane gelendzhik = new Airplane("SU-783","Gelendzhik");
        Airplane vladivostok = new Airplane("B-747","Vladivostok");

        new Thread(sochi).start();
        new Thread(gelendzhik).start();
        new Thread(vladivostok).start();
    }
}
