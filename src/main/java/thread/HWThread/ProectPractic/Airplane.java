package thread.HWThread.ProectPractic;

public class Airplane implements Runnable{
    private final String flightNumber;
    private final String destination;
    private final String[] steps = {
            "Проверка двигателя",
            "Посадка пассажиров",
            "Загрузка багажа",
            "Выход на взлётную полосу",
            "Взлёт"
    };

    public Airplane(String flightNumber, String destination) {
        this.flightNumber = flightNumber;
        this.destination = destination;
    }

    @Override
    public void run() {
        for (String step : steps) {
            System.out.printf("Рейс %s (%s) -> %s\n", flightNumber, destination, step);

            try {
                Thread.sleep(500 );
            } catch (InterruptedException e) {
                System.out.printf("Подготовка рейса %s прервана!\n", flightNumber);
                Thread.currentThread().interrupt();
                return;
            }
        }
        System.out.printf("Рейс %s успешно улетел в город %s!\n", flightNumber, destination);
    }
}
