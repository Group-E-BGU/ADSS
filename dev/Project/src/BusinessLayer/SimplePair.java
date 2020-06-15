package BusinessLayer;

import java.sql.SQLData;

public class SimplePair {
        private java.sql.Date date;
        private Item item;
        public SimplePair(java.sql.Date date, Item item){
            this.date = date;
            this.item = item;
        }
        public java.sql.Date getDate(){ return date; }
        public Item getItem(){ return item; }
        //public void setL(L l){ this.l = l; }
        //public void setR(R r){ this.r = r; }
}
