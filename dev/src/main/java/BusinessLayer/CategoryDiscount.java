package BusinessLayer;

import java.util.Date;

public class CategoryDiscount extends Discount {

    private Category category;

    public CategoryDiscount(int id,Category cat, java.sql.Date start, java.sql.Date end, int perc) {
        super(id,start,end,perc);
        category = cat;
    }

    @Override
    public boolean validCategoryDiscount(String name) {
        if (name.equals(category.getName())) {
            java.sql.Date now = new java.sql.Date((new Date()).getTime());
            if (getStartDate().getTime() < now.getTime() && now.getTime() < getEndDate().getTime()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String withDiscount() {
        return category.getName() +" : "+getPercentage()+"% Discount until "+getEndDate() +"\n";
    }

    @Override
    public boolean validItemDiscount(String name) {
        return false;
    }
}
