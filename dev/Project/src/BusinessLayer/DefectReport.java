package BusinessLayer;
import java.util.Date;
import java.util.LinkedList;

public class DefectReport {
    private Date startDate;
    private Date endDate;
    private LinkedList<Item> defectedItems;


    public DefectReport(Date startDate, Date endDate){
        this.startDate = startDate;
        this.endDate = endDate;
        this.defectedItems = new LinkedList<>();
    }

    public void AddDefectedItem(Item item){
        defectedItems.add(item);
    }
}
