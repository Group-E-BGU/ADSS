package BL;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WorkerDeal {

    private int worker_id;
    private Date start_date;
    private String bank_address;
    private double salary;
    private List<String> work_conditions;    // ask what is a work condition ?


    public WorkerDeal(int worker_id , Date start_date, double salary, String bank_address, List<String> work_conditions) {
        this.start_date = start_date;
        this.salary = salary;
        this.work_conditions = work_conditions;
        this.bank_address = bank_address;
        this.worker_id = worker_id;
    }


    public String toString()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String deal = "worker id : "+worker_id+'\n';
        deal = deal + "start date : "+dateFormat.format(start_date)+'\n';
        deal = deal + "bank address : "+bank_address+'\n';
        deal = deal+ "salary : "+salary+'\n';
        if(work_conditions!=null)
        {
            deal = deal + "working conditions : "+work_conditions.toString()+'\n';
        }
        return deal;
    }

    public Date getStart_date() {
        return start_date;
    }

    public double getSalary() {
        return salary;
    }

    public List<String> getWork_conditions() {
        return work_conditions;
    }

    public int getWorker_id() {
        return worker_id;
    }

    public String getBankAddress()
    {
        return bank_address;
    }

    public void setBankAddress(String address)
    {
        this.bank_address = address;
    }

    public void setSalary(double salary)
    {
        this.salary = salary;
    }
}
