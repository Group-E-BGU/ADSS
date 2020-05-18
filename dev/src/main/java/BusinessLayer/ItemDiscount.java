package BusinessLayer;

import java.util.Date;

public class ItemDiscount extends Discount {

    private ItemRecord itemRecord;

    public ItemDiscount(int id,ItemRecord itemRecord, java.sql.Date start, java.sql.Date end, int perc) {
        super(id,start, end, perc);
        this.itemRecord = itemRecord;
    }

    @Override
    public boolean validCategoryDiscount(String name) {
        return false;
    }

    @Override
    public String withDiscount() {
        return getPercentage()+"% discount until "+ getEndDate();
    }

    @Override
    public boolean validItemDiscount(String name) {
        if (name.equals(itemRecord.getName())) {
            java.sql.Date now = new java.sql.Date(new Date().getTime());
            if (getStartDate().getTime() < now.getTime() && now.getTime() < getEndDate().getTime()) {
                return true;
            }
        }
        return false;
    }
}
