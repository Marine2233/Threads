package thread.classWork.HWforThreadAndJackson;


public class OrderMonitor implements Runnable {
    private OrderStatistics statistics;
    private JsonOrderProcessor processor;
    private volatile boolean running = true;

    public OrderMonitor(JsonOrderProcessor processor, OrderStatistics statistics) {
        this.processor = processor;
        this.statistics = statistics;
    }

    @Override
    public void run() {
        System.out.println("==========" + "Order monitor STARTED." + "==========");

        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000);

                System.out.println("Свободных слотов: " + processor.getProcessingSlots().availablePermits());
                System.out.println("Обработано: " + statistics.getProcessedOrders().get());
                System.out.println("Ошибок: " + statistics.getFailedOrders().get());
                System.out.println("Отклонено: " + statistics.getRejectedOrders().get());
                System.out.println("Выручка: " + statistics.getTotalRevenue().get());
                System.out.println("Готовых заказов в списке: " + processor.getCompletedOrders().size());
                System.out.println("----------------------------------------");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n" + "=".repeat(10) + " RESULT " + "=".repeat(10));
        System.out.println("Успешно обработано: " + statistics.getProcessedOrders().get());
        System.out.println("Ошибок JSON (Отклонено): " + statistics.getRejectedOrders().get());
        System.out.println("Сбоев обработки (Ошибок): " + statistics.getFailedOrders().get());
        System.out.println("Готовых заказов в списке: " + processor.getCompletedOrders().size());
        System.out.println("Общая выручка: " + statistics.getTotalRevenue().get());
        System.out.println("Total input json lines: "+(statistics.getProcessedOrders().get() + statistics.getFailedOrders().get() + statistics.getRejectedOrders().get()));
        System.out.println("=".repeat(28) + "\n");
    }
        public synchronized void stop () {
            running = false;
        }
    }

