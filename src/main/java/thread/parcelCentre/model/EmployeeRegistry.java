package thread.parcelCentre.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EmployeeRegistry {
    private final CopyOnWriteArrayList<String> employees;

    public EmployeeRegistry() {
        this.employees = new CopyOnWriteArrayList<>();
    }

    public void subscribe(String employee){
       if (employee.isBlank() || employee == null )return;
       employees.addIfAbsent(employee);
    }

    public void unsubscribe(String employee) {
        if (employee != null) {
            employees.remove(employee);
        }
    }
    public void notifyEmployees(Parcel parcel){
        employees.forEach(e-> System.out.println(String.format("%s : Завершена сортировка посылки: %s",e,parcel)));
    }

    public List<String> getEmployees(){
        return List.copyOf(employees);
    }

}
