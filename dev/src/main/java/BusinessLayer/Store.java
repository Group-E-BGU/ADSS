package BusinessLayer;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import DataAccesslayer.*;
import InterfaceLayer.*;

public class Store {

    private static Store storeInstance;

    private String email_ID;
    private List<Supplier> list_of_Suplier;
    private List<Order> list_of_Order;
    private int NumOfOrder;
    private int NumOfProduct=0;
    private Transportrans Trans;
    private static Mapper Map;
    private HashMap<String,ItemRecord> itemRecords;
    private HashMap<String,Category> categories;
    private LinkedList<Discount> discounts;
    private LinkedList<SimplePair> defects;
    private static int itemId;
    private DALItemRecord dalItemRecord;
    private DALCategory dalCategory;
    private DALDiscount dalDiscount;
    private DALPrice dalPrice;
    private MapperSupplier MapSupplier;
    private MapperContact MapContact;
    private MapperContract MapContract;
    private MapperDays MapDays;
    private MapperItemRecord_Supplier MapIRS;
    private MapperProductOrder MapPO;
    private MapperWrotequantities MapWorte;
    private MapperProductToSupplier MapPTS;
    private MapperOrder MapOrder;
    private MapperStore MapStore;

    public static Store createInstance(String email) {
        if (storeInstance == null) {
            storeInstance = new Store(email);
        }
        return storeInstance;
    }

    public static Store getInstance() {
        if (storeInstance != null) {
            return storeInstance;
        }
        return null;
    }

    private Store(String email) {
    Map=new Mapper();
    email_ID = email;
    list_of_Suplier=new LinkedList<Supplier>();
    list_of_Order=new LinkedList<Order>();
    NumOfOrder=0;
    NumOfProduct=0;
    Trans=new Transportrans();
    itemRecords=new HashMap<String,ItemRecord>();
    categories=new HashMap<String,Category>();
    discounts=new LinkedList<Discount>();
    defects=new LinkedList<SimplePair>();
    itemId=0;
    dalItemRecord = new DALItemRecord();
    dalCategory = new DALCategory();
    dalDiscount = new DALDiscount();
    MapSupplier=new MapperSupplier();
    MapContact=new MapperContact();
    MapContract=new MapperContract();
    MapPO=new MapperProductOrder();
    MapDays=new MapperDays();
    MapWorte=new MapperWrotequantities();
    MapIRS=new MapperItemRecord_Supplier();
    MapPTS=new MapperProductToSupplier();
    MapOrder=new MapperOrder();
    MapStore=new MapperStore();
    }

    public String getEmail_ID(){
        return email_ID;
    }

    public String addItemRecord(String name, int minAmount, int shelfNumber, String manufacture) {
        if (itemRecords.containsKey(name) || dalItemRecord.getItemRecord(name,email_ID) != null) {
            return "This product name already exists";
        }
        else{
            ItemRecord ir = new ItemRecord(name,itemId++,minAmount,0,0,0,shelfNumber,manufacture);
            itemRecords.put(name,ir);
            dalItemRecord.InsertItemRecord(name,ir.getId(),minAmount,0,0,0,shelfNumber,manufacture,email_ID);
            return name+" added successfully";
       }
    }

    public String AddSuplier(String name, int id,String address, String bank, String branch, int bankNumber,
                             String payments, java.util.Map<Integer, String> contacts_id, Map<Integer, Integer> contacts_number) {
        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==id){
                return "The supplier already exists in the system";
            }
        }
        Supplier sup=new Supplier(name, id,address, bank,branch,bankNumber,payments,contacts_id,contacts_number);
        list_of_Suplier.add(sup);
        MapSupplier.WriteSupplier(name, id,address, bank,branch,bankNumber,payments,email_ID);
        for (Map.Entry<Integer,Integer> E: contacts_number.entrySet()
        ) {
            MapContact.WriteContact(email_ID,id,contacts_id.get(E.getKey()),E.getKey(),E.getValue());
        }
        return "Done";
    }

    public String Delete(int id) {
        boolean exit=false;
        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==id){
                exit=true;
                list_of_Suplier.remove(s);
            }
        }
        if(!exit){
            return "The supplier is not exists in the system";
        }
        MapSupplier.DeleteSupplier(id,email_ID);
        return "Done";
    }

    public String EditSuplier(String name, int id,String address, String bank, String branch, int bankNumber, String payments, Map<Integer, String> contacts_id_name, Map<Integer, Integer> contacts_number) {
        boolean exit=false;
        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==id){
                exit=true;
                s.setName(name);
                s.setBank(bank);
                s.setBranch(branch);
                s.setBankNumber(bankNumber);
                s.setPayments(payments);
                s.setContactsID_Name(contacts_id_name);
                s.setContactsID_number(contacts_number);
            }
        }
        if(!exit){
            return "The supplier is not exists in the system";
        }
        MapSupplier.EditSupplier(name, id, address, bank,branch,bankNumber,payments,email_ID);
        for (Map.Entry<Integer,Integer> E: contacts_number.entrySet()
             ) {
        MapContact.DeleteContact(email_ID,id,E.getKey());
        MapContact.WriteContact(email_ID,id,contacts_id_name.get(E.getKey()),E.getKey(),E.getValue());
        }
        return "Done";
    }

    public String AddContract(int suplaier_id, boolean fixeDays, LinkedList<Integer> days, boolean leading, Map<Integer,Integer>  ItemsID_ItemsIDSupplier,
                              Map<Integer, String> productIDVendor_name, Map<Integer, Double> productIDVendor_price) {

        for (Supplier s:list_of_Suplier
        ) {
            Contract con=new Contract(suplaier_id,fixeDays,days,leading,productIDVendor_name,ItemsID_ItemsIDSupplier,productIDVendor_price);
            s.setContract(con);
            MapContract.WriteContract(suplaier_id,fixeDays,leading,email_ID);
            for (int day:days
                 ) {
                MapDays.WriteDayToContract(email_ID,suplaier_id,day);
            }
            for (Map.Entry<Integer,Integer> IDS_IDV: ItemsID_ItemsIDSupplier.entrySet()
                 ) {
                MapPTS.WriteProductToSupplier(email_ID,suplaier_id,IDS_IDV.getKey(),IDS_IDV.getValue(),productIDVendor_price.get(IDS_IDV.getKey()),productIDVendor_name.get(IDS_IDV.getKey()));
            }
                return "Done";
            }

        return "The supplier is not exists in the system";
    }

    public String AddWrite(int suplaier_id, Map<Integer, Integer> itemsID_amount, Map<Integer, Double> itemsID_assumption) {
        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==suplaier_id){
                Wrotequantities W=new Wrotequantities(suplaier_id,itemsID_amount,itemsID_assumption);
                s.setWorte(W);
                MapWorte.WriteWrote(email_ID,suplaier_id,itemsID_amount,itemsID_assumption);
                return "Done";
            }
        }
        return "The supplier is not exists in the system";
    }

    public int MakeOrder(int id_suplaier, LinkedList<Integer> day, Map<Integer, Integer> ProductIDSupplier_numberOfItems) {
        Supplier sup=null;//todo check mayby it not necessary
        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==id_suplaier){
                sup=s;
            }
        }

        if(sup!=null&&sup.getContract()!=null) {
            Map<Integer, Integer> ProductID_IDSupplier = new ConcurrentHashMap<Integer, Integer>();
            AtomicReference<Double> TotalPrice = new AtomicReference<>((double) 0);
            for (Map.Entry<Integer,Integer> e : ProductIDSupplier_numberOfItems.entrySet()) {
                int Id_Product = sup.GetIdProduct(e.getKey());
                ProductID_IDSupplier.put(Id_Product, e.getKey());
                double Price =sup.getPric(e.getKey(), e.getValue()); //todo check if works
                TotalPrice.set(TotalPrice.get()+Price);
                //Sale = (100 - Sale) / 100;
                //double Price = sup.GetPricProduct(id_suplaier, Id);
                //TotalPrice.set(TotalPrice.get() + num * Price * Sale);
            }

            Order O = new Order(id_suplaier, NumOfOrder,false, day, ProductID_IDSupplier, ProductIDSupplier_numberOfItems, TotalPrice.get());
            if(!sup.getContract().isLeading()){
                Trans.Lead(O);
            }

            Map<Integer, Integer> ProductIDSupplier_IDStore = new ConcurrentHashMap<Integer, Integer>();
            for (Map.Entry<Integer,Integer> e:ProductID_IDSupplier.entrySet()
                 ) {
                ProductIDSupplier_IDStore.put(e.getValue(),e.getKey());
            }
            list_of_Order.add(O);
            MapOrder.WriteOrder(email_ID,id_suplaier, NumOfOrder,false, day, java.sql.Date.valueOf(LocalDate.now()),null, TotalPrice.get(),"Waiting");
            for (Map.Entry<Integer,Integer> E:ProductID_IDSupplier.entrySet()
                 ) {
                MapPO.WriteProductOrder(email_ID,NumOfOrder,ProductIDSupplier_IDStore,ProductIDSupplier_numberOfItems);
            }
            NumOfOrder++;
            return NumOfOrder-1;
        }
        return -1;

    }

    public String ChangeOrder(int id_order, int id_suplaier, LinkedList<Integer> day, java.util.Map<Integer, Integer> itemsIDVendor_numberOfItems) {
        Supplier sup=null;
        for (Supplier s: list_of_Suplier
        ) {
            if(s.getID()==id_suplaier){
                sup=s;
            }
        }
        Order order=null;
        if(sup!=null){
            Map<Integer, Integer> ProductID_IDSupplier = new ConcurrentHashMap<Integer, Integer>();
            for (Map.Entry<Integer,Integer> e : itemsIDVendor_numberOfItems.entrySet()) {
                int Id_Product = sup.GetIdProduct(e.getKey());
                ProductID_IDSupplier.put(Id_Product, e.getKey());
            }

            for (Order o:list_of_Order
            ) {
                if (o.getID_Invitation() == id_order) {
                    order=o;
                    o.ChangeOrder(id_order, id_suplaier, day, ProductID_IDSupplier, itemsIDVendor_numberOfItems);
                    AtomicReference<Double> TotalPrice = new AtomicReference<>((double) 0);
                    for (Map.Entry<Integer, Integer> e : o.getItemsID_NumberOfItems().entrySet()) {
                        double Price = sup.getPric(e.getKey(), e.getValue()); //todo check if works
                        TotalPrice.set(TotalPrice.get() + Price);
                    }
                    o.setTotalPrice(TotalPrice.get());
                }
            }
            MapOrder.DeleteOrder(email_ID,id_order);
            Map<Integer, Integer> ProductIDSupplier_IDStore = new ConcurrentHashMap<Integer, Integer>();
            for (Map.Entry<Integer,Integer> e:ProductID_IDSupplier.entrySet()
            ) {
                ProductIDSupplier_IDStore.put(e.getValue(),e.getKey());
            }
            MapOrder.WriteOrder(email_ID,id_suplaier, NumOfOrder,false, day, java.sql.Date.valueOf(LocalDate.now()),null ,order.getTotalPrice(),"Waiting");
            for (Map.Entry<Integer,Integer> E:ProductID_IDSupplier.entrySet()
            ) {
                MapPO.WriteProductOrder(email_ID,id_order,ProductIDSupplier_IDStore, itemsIDVendor_numberOfItems);
            }
        }
        return null;
    }

    public String AutomaticProductOrdering(int IdProduct, int amount){
        int IdSoplier= -1;
        int Id_p_sup=-1;
        Supplier sup=null;
        double FinalPrice= Integer.MAX_VALUE;
        for (Supplier s:list_of_Suplier
        ) {
            Id_p_sup=s.GetIdProduct(IdProduct);
            if(Id_p_sup!=-1) {
                double price = s.getPric(IdProduct, amount);
                if (price < FinalPrice) {
                    FinalPrice = price;
                    sup=s;
                    IdSoplier=sup.getID();
                }
            }
        }
        if(sup!=null) {
            LinkedList<Integer> day = new LinkedList<Integer>();
            day.add(-1);//todo which day?
            Map<Integer, Integer> ProductID_ProductID_IDSupplier = new ConcurrentHashMap<Integer, Integer>();
            Map<Integer, Integer> ProductIDSupplier_numberOfItems = new ConcurrentHashMap<Integer, Integer>();
            ProductID_ProductID_IDSupplier.put(IdProduct, Id_p_sup);
            ProductIDSupplier_numberOfItems.put(Id_p_sup, amount);
            Order O = new Order(IdSoplier, NumOfOrder, true, day, ProductID_ProductID_IDSupplier, ProductIDSupplier_numberOfItems, FinalPrice);
            if (!sup.getContract().isLeading()) {
                Trans.Lead(O);
            }
            list_of_Order.add(O);

            Map<Integer, Integer> ProductIDSupplier_IDStore = new ConcurrentHashMap<Integer, Integer>();
            for (Map.Entry<Integer,Integer> e:ProductID_ProductID_IDSupplier.entrySet()
            ) {
                ProductIDSupplier_IDStore.put(e.getValue(),e.getKey());
            }
            MapOrder.WriteOrder(email_ID,NumOfOrder, NumOfOrder,false, day, java.sql.Date.valueOf(LocalDate.now()),null ,O.getTotalPrice(),"Waiting");
            for (Map.Entry<Integer,Integer> E:ProductID_ProductID_IDSupplier.entrySet()
            ) {
                MapPO.WriteProductOrder(email_ID,NumOfOrder,ProductIDSupplier_IDStore, ProductIDSupplier_numberOfItems);
            }NumOfOrder++;
            return "Done";//todo ?? return done?
        }
        return "problem with the create autoOrder";
    }

    public String EditContract(int suplaier_id, boolean fixeDays, LinkedList<Integer> days, boolean leading,Map<Integer,Integer>  ItemsID_ItemsIDSupplier, Map<Integer, String> productIDVendor_name, Map<Integer, Double> producttemsIDVendor_price) {
        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==suplaier_id){
                if(s.getContract()!=null) {
                    s.getContract().setDayes(days);
                    s.getContract().setLeading(leading);
                    s.getContract().setFixeDays(fixeDays);
                    s.getContract().setItemsID_ItemsIDSupplier(ItemsID_ItemsIDSupplier);
                    s.getContract().setProductIDVendor_Name(productIDVendor_name);
                    s.getContract().setProductIDVendor_Price(producttemsIDVendor_price);
                    MapContract.DeleteContract(suplaier_id,email_ID);
                    MapDays.DeleteDayFromContract(email_ID,suplaier_id);
                    MapContract.WriteContract(suplaier_id,fixeDays,leading,email_ID);
                    for (int day:days
                    ) {
                        MapDays.WriteDayToContract(email_ID,suplaier_id,day);
                    }
                    for (Map.Entry<Integer,Integer> IDS_IDV: ItemsID_ItemsIDSupplier.entrySet()
                    ) {
                        MapPTS.DeleteProductFromSupplier(email_ID,suplaier_id,IDS_IDV.getValue());
                        MapPTS.WriteProductToSupplier(email_ID,suplaier_id,IDS_IDV.getKey(),IDS_IDV.getValue(),producttemsIDVendor_price.get(IDS_IDV.getValue()),productIDVendor_name.get(IDS_IDV.getValue()));
                    }
                    return "Done";
                }
                else
                    return "The Supplier haven't a contract";
            }
        }
        return "The supplier is not exists in the system";
    }

    public String EditWrite(int suplaier_id, Map<Integer, Integer> itemsID_amount, Map<Integer, Double> itemsID_assumption) {
        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==suplaier_id){
                if(s.getWorte()!=null) {
                    s.getWorte().setItemsID_Amount(itemsID_amount);
                    s.getWorte().setItemsID_Assumption(itemsID_assumption);
                    MapWorte.DeleteWrote(suplaier_id,email_ID);
                    MapWorte.WriteWrote(email_ID, suplaier_id,itemsID_amount,itemsID_assumption);
                    return "Done";
                }
                else
                    return "The Supplier haven't a 'Wrote quantities'";
            }
        }
        return "The supplier is not exists in the system";
    }

    public String CheckSuplierExit(int id) {
        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==id){
                return "Exit";
            }
        }
        return "Not Exit";
    }

    public String CheckSAgreementExit(int suplaier_id) {
        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==suplaier_id) {
                if (s.getContract() != null) {
                    return "Done";
                }
            }
        }
        return "The supplier haven't Agreement";
    }

    public String CheckSWortExit(int suplaier_id) {
        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==suplaier_id){
                if(s.getWorte()!=null) {
                    return "Done";
                }
            }
        }
        return "The supplier haven't Agreement";
    }

    public String ChangOrderStatus(int id_order) {
        for (Order O:list_of_Order
        ) {
            if(O.getID_Invitation()==id_order){
                O.setStatus("Arrived");
                O.setArrivedatime(LocalDate.now());
                return "Done";
            }
        }
        return "The Order is not exist in the system";
    }

    public LinkedList<InterfaceSupplier> GetSupliers() {
        LinkedList<InterfaceSupplier> list=new LinkedList<InterfaceSupplier>();
        for (Supplier s:list_of_Suplier
        ) {
            Contract c=s.getContract();
            InterfaceContract contract=new InterfaceContract(c.getSuplaier_ID(),c.isFixeDays(),c.getDayes(),c.isLeading(),c.getProductIDVendor_Name(),c.getItemsID_ItemsIDSupplier(),c.getProductIDVendor_Price());
            Wrotequantities worte=s.getWorte();
            InterfaceWrotequantities w=null;
            if(worte!=null) {
                w=new InterfaceWrotequantities(worte.getSuplaier_ID(),worte.getItemsID_Amount(),worte.getItemsID_Assumption());
            }
            InterfaceSupplier I=new InterfaceSupplier(s.getName(),s.getID(),s.getBank(),s.getBranch(),s.getBankNumber(),s.getPayments(),s.getContactsID_Name(),s.getContactsID_number(),contract,w);
            list.add(I);
        }
        return list;
    }

    public LinkedList<InterfaceContract> GetContract() {
        LinkedList<InterfaceContract> list=new LinkedList<InterfaceContract>();
        for (Supplier s:list_of_Suplier
        ) {
            Contract c=s.getContract();
            if (c!=null) {
                InterfaceContract I = new InterfaceContract(c.getSuplaier_ID(),c.isFixeDays(),c.getDayes(),c.isLeading(),c.getProductIDVendor_Name(),c.getItemsID_ItemsIDSupplier(),c.getProductIDVendor_Price());
                list.add(I);
            }
        }
        return list;
    }

    public boolean CheckTheDay(int id_suplaier, int day) {
        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==id_suplaier){
                    return s.CheckTheDay(day);
            }
        }
        return false;
    }

    public boolean CheckProductexist(int id_suplaier, int product_id) {
        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==id_suplaier){
                return s.CheckProductexist(product_id);

            }
        }
        return false;
    }

    public int FindId_P_Store(String product_name, String category, String subcategory, String sub_subcategory, String manufacturer) {
        int id=MapIRS.getProductId(email_ID,category,subcategory,sub_subcategory,product_name,manufacturer);
        if(id==-1){
            id=NumOfProduct;
            MapIRS.WriteItemRecord_Supplier(email_ID,id,category,subcategory,sub_subcategory,product_name);
            NumOfProduct++;
        }
        return id;
    }

    public String CheckAbleToChangeOrder(int id_order) {
        for (Order o:list_of_Order
        ) {
            if(o.getID_Invitation()==id_order){
                return o.CheckAbleToChangeOrder();
            }
        }
        return "the order is not exist in the system";
    }

    public void RemoveProduct(int id_order, int product_id) {
        for (Order o:list_of_Order
        ) {
            if(o.getID_Invitation()==id_order){
                o.RemoveProduct(product_id);
            }
        }
    }
    @SuppressWarnings("deprecation")

    public void initializeItems() {
        ItemRecord itemRecord1 = new ItemRecord("milk Tnova 3%",1,3,1,3,4,1,"tnova");
        itemRecord1.addItem(new Item(itemId++, new java.sql.Date(2020-1900,4-1,19)));
        itemRecord1.addItem(new Item(itemId++, new java.sql.Date(2020-1900,4-1,19)));
        itemRecord1.addItem(new Item(itemId++, new java.sql.Date(2020-1900,4-1,20)));
        itemRecord1.addItem(new Item(itemId++, new java.sql.Date(2020-1900,4-1,20)));
        itemRecords.put("milk Tnova 3%",itemRecord1);
        dalItemRecord.InsertItemRecord(itemRecord1.getName(),itemRecord1.getId(),3,1,3,2,1,"tnova",email_ID);

        ItemRecord itemRecord2 = new ItemRecord("white bread",2,3,2,3,5,2,"dganit");
        itemRecord2.addItem(new Item(itemId++, new java.sql.Date(2020-1900,5-1,19)));
        itemRecord2.addItem(new Item(itemId++, new java.sql.Date(2020-1900,5-1,19)));
        itemRecord2.addItem(new Item(itemId++, new java.sql.Date(2020-1900,5-1,20)));
        itemRecord2.addItem(new Item(itemId++, new java.sql.Date(2020-1900,5-1,20)));
        itemRecord2.addItem(new Item(itemId++, new java.sql.Date(2020-1900,5-1,20)));
        itemRecords.put("white bread",itemRecord2);
        dalItemRecord.InsertItemRecord(itemRecord2.getName(),itemRecord2.getId(),3,2,3,2,1,"dganit",email_ID);

        ItemRecord itemRecord3 = new ItemRecord("coffee Elite",3,2,0,2,2,3,"elite");
        itemRecord3.addItem(new Item(itemId++, new java.sql.Date(2020-1900,8-1,20)));
        itemRecord3.addItem(new Item(itemId++, new java.sql.Date(2020-1900,8-1,20)));
        itemRecords.put("coffee Elite",itemRecord3);
        dalItemRecord.InsertItemRecord(itemRecord3.getName(),itemRecord3.getId(),2,0,2,2,3,"elite",email_ID);


        itemRecord1.addPrice(new Price(80,120));
        itemRecord2.addPrice(new Price(90,130));
        itemRecord3.addPrice(new Price(100,135));

    }

    public void initializeCategories() {
        Category category1 = new Category(Category.CategoryRole.MainCategory,"Dairy");
        Category subCat1 = new Category(Category.CategoryRole.SubCategory,"Milk");
        Category subsubcat1 = new Category(Category.CategoryRole.SubSubCategory,"1 liter");
        addItemToCategory(itemRecords.get("milk Tnova 3%") ,category1);
        addItemToCategory(itemRecords.get("milk Tnova 3%") ,subCat1);
        addItemToCategory(itemRecords.get("milk Tnova 3%") ,subsubcat1);
        dalCategory.InsertCategory(category1.getName(),1,email_ID);
        dalCategory.InsertCategory(subCat1.getName(),2,email_ID);
        dalCategory.InsertCategory(subsubcat1.getName(),3,email_ID);


        categories.put("Dairy",category1);
        categories.put("Milk",subCat1);
        categories.put("1 liter",subsubcat1);

        Category category2 = new Category(Category.CategoryRole.MainCategory,"Bread and pastry");
        Category subcat2 = new Category(Category.CategoryRole.SubCategory,"Bread");
        Category subsubcat2 = new Category(Category.CategoryRole.SubSubCategory,"750 gr");
        addItemToCategory(itemRecords.get("white bread") ,category2);
        addItemToCategory(itemRecords.get("white bread") ,subcat2);
        addItemToCategory(itemRecords.get("white bread") ,subsubcat2);
        dalCategory.InsertCategory(category2.getName(),1,email_ID);
        dalCategory.InsertCategory(subcat2.getName(),2,email_ID);
        dalCategory.InsertCategory(subsubcat2.getName(),3,email_ID);

        categories.put("Bread and pastry",category2);
        categories.put("Bread",subcat2);
        categories.put("750 gr",subsubcat2);
        Category category3 = new Category(Category.CategoryRole.MainCategory,"Drinks");
        Category subcat3 = new Category(Category.CategoryRole.SubCategory,"Coffee powder");
        Category subsubcat3 = new Category(Category.CategoryRole.SubSubCategory,"500 gr");
        addItemToCategory(itemRecords.get("coffee Elite") ,category3);
        addItemToCategory(itemRecords.get("coffee Elite") ,subcat3);
        addItemToCategory(itemRecords.get("coffee Elite") ,subsubcat3);
        dalCategory.InsertCategory(category3.getName(),1,email_ID);
        dalCategory.InsertCategory(subcat3.getName(),2,email_ID);
        dalCategory.InsertCategory(subsubcat3.getName(),3,email_ID);

        categories.put("Drinks",category3);
        categories.put("Coffee powder",subcat3);
        categories.put("500 gr",subsubcat3);
    }

    private void addItemToCategory(ItemRecord itemRecord, Category cat) {
        for (Category category: categories.values()) {
            if(category.getRole().equals(cat.getRole()) && category.getItemRecords().contains(itemRecord))
                return;
        }
        cat.addItem(itemRecord);
    }
    @SuppressWarnings("deprecation")
    public void initializeDiscounts() {
        CategoryDiscount cd1 = new CategoryDiscount(1,categories.get("Drinks"),
                new java.sql.Date(2020-1900,4-1,20),
                new java.sql.Date(2020-1900,5-1,20),20);
        CategoryDiscount cd2 = new CategoryDiscount(2,categories.get("Dairy"),
                new java.sql.Date(2020-1900,5-1,20),
                new java.sql.Date(2020-1900,5-1,20),30);
        discounts.add(cd1);
        discounts.add(cd2);
        ItemDiscount id =new ItemDiscount(3,itemRecords.get("white bread"),
                new java.sql.Date(2020-1900,4-1,20),
                new java.sql.Date(2020-1900,5-1,20),15);
        discounts.add(id);
    }

    public String addItemDiscount(String name, int percentage, java.sql.Date beginDate, java.sql.Date endDate){
        if(!(percentage>=1 && percentage<=100)){
            return "Discount percentage must be a number between 1-100";
        }
        for( ItemRecord ir: itemRecords.values()){
            if (ir.getName().equals(name)){         //checks if there is an item record with the given name
                ItemDiscount d = new ItemDiscount(dalDiscount.getMaxId()+1,ir, beginDate, endDate, percentage);
                dalDiscount.InsertItemDiscount(d.getId(), d.getPercentage(), d.getStartDate(), d.getEndDate(), ir.getId(), this.getEmail_ID());
                Price p = ir.getCurrPrice();
                int beforeDiscount = p.getStorePrice();
                int afterDiscount = (beforeDiscount/100) * percentage;
                Price discountedPrice = new Price(p.getRetailPrice() , afterDiscount);
                dalPrice.InsertPrice(discountedPrice.getId(), ir.getId(), discountedPrice.getStorePrice() , discountedPrice.getRetailPrice());
                //ir.addPrice(discountedPrice);
                discounts.add(d);
                return "The discount was added succesfully";
            }
        }
        return "No such item";
    }

    /*
     if (itemRecords.containsKey(name) || dalItemRecord.getItemRecord(name,email_ID) != null) {
            return "This product name already exists";
        }
        else{
            ItemRecord ir = new ItemRecord(name,itemId++,minAmount,0,0,0,shelfNumber,manufacture);
            itemRecords.put(name,ir);
            dalItemRecord.InsertItemRecord(name,ir.getId(),minAmount,0,0,0,shelfNumber,manufacture,email_ID);
            return name+" added successfully";
       }
     */

    public String addNewCategoryDiscount(String categoryName, int percentage, java.sql.Date beginDate, java.sql.Date endDate){
        if(!(percentage>=1 && percentage<=100)){
            return "Discount percentage must be a number between 1-100";
        }
        for( Category cat: categories.values()){
            if (cat.getName().equals(categoryName)){         //checks if there is a category with the given name
                CategoryDiscount d = new CategoryDiscount(dalDiscount.getMaxId()+1,cat, beginDate, endDate, percentage);
                dalDiscount.InsertCategoryDiscount(d.getId(), categoryName, d.getPercentage(), d.getStartDate(), d.getEndDate(), this.getEmail_ID());
                for (ItemRecord itemRec: cat.getItemRecords() ){
                    Price p = itemRec.getCurrPrice();
                    int beforeDiscount = p.getStorePrice();
                    int afterDiscount = (beforeDiscount/100) * percentage;
                    Price discountedPrice = new Price(p.getRetailPrice() , afterDiscount);
                    dalPrice.InsertPrice(discountedPrice.getId(), itemRec.getId(), discountedPrice.getStorePrice() , discountedPrice.getRetailPrice());
                    //itemRec.addPrice(discountedPrice);


                }
                discounts.add(d);
                return "The discount was added successfully";
            }
        }
        return "No such item";
    }

    public int incrementAndGetItemID(){
        return itemId++;
    }

    public String setDefectedItem(String name, int id){
        for( ItemRecord ir: itemRecords.values()){
            if (ir.getName().equals(name)){         //checks if there is an item record with the given name
                LinkedList<Item> itemsList = ir.getItems();
                for (Item item: itemsList){
                    if(item.getId()==id){
                        item.setDefective(true);
                        java.sql.Date currDate = new java.sql.Date((new Date()).getTime());
                        dalItemRecord.updateDefect(item.getId(), ir.getId(), currDate, this.email_ID);
                        defects.add(new SimplePair(currDate, item));
                        return  "Defected item was added";
                    }
                }
                return "No such item ID";
            }
        }
        return "No such item";
    }

    public String setNewPrice(String name, int price , int retailPrice){
        for( ItemRecord ir: itemRecords.values()) {
            if (ir.getName().equals(name)) {         //checks if there is an item record with the given name
                Price p = ir.getCurrPrice();
                Price newPr = new Price(retailPrice , price);
                ir.addPrice(newPr);
                return "added successfully";
            }
        }
        return "No such item";
    }

    public String getItemAmountsByName(String name) {
        ItemRecord ir = itemRecords.get(name);
        if (ir == null){
            ir = dalItemRecord.getItemRecord(name,email_ID);
            itemRecords.put(name,ir);
            if(ir == null)
                return "No such item in inventory";
        }
        return  ir.getAmounts();
    }
    @SuppressWarnings("deprecation")

    //amounts should contain exp date
    public String addAmounts(String name, String amounts) {
        ItemRecord ir = itemRecords.get(name);
        if(amounts.contains("-"))
            return "Can't set amounts to a negative number";
        String[] split = amounts.split("\\s+");
        try {
            int storage = Integer.parseInt(split[0]);
            int shelf = Integer.parseInt(split[1]);
            int total = storage + shelf;
            String edate = split[2];
            if(edate.length()!=10)
                return "Please enter valid arguments";
            java.sql.Date expdate = new java.sql.Date(Integer.parseInt(edate.substring(6))-1900,Integer.parseInt(edate.substring(3,5))-1,Integer.parseInt(edate.substring(0,2)));
            ir.setStorageAmount(storage);
            ir.setShelfAmount(shelf);
            ir.setTotalAmount(total,expdate);
            return ir.getAmounts();

        } catch (Exception e) {
            return "Action failed due to invalid input";
        }
    }

    public String getItemIdsByName(String name) {
        String toRet = name+" ids int store : ";
        List<Integer> ids = dalItemRecord.geItemIdsByName(name,email_ID);
        for (Integer id:ids) {
            toRet = toRet + id + "\n";
        }
        return toRet;
    }

    public String removeItem(String name,int id) {
        if(dalItemRecord.DeleteItem(name,id,email_ID)) {
            ItemRecord ir = itemRecords.get(name);
            if (ir == null) {
                ir = dalItemRecord.getItemRecord(name, email_ID);
                itemRecords.put(name, ir);
            }
            ir.removeItem(id);
            return "item deleted successfully";

        }
        return "item could not be deleted";
    }


    public String removeItemFromShelf(String name,int id) {
        if(dalItemRecord.DeleteItem(name,id,email_ID)) {
            ItemRecord ir = itemRecords.get(name);
            if (ir == null) {
                ir = dalItemRecord.getItemRecord(name, email_ID);
                itemRecords.put(name, ir);
            }
            ir.removeItemFromShelf(id);
            return "item deleted successfully";

        }
        return "item could not be deleted";
    }

    public String setNewAmounts(String name, String amounts) {
        ItemRecord ir = itemRecords.get(name);
        if(amounts.contains("-"))
            return "Can't set amounts to a negative number";
        String[] split = amounts.split("\\s+");
        try {
            int storage = Integer.parseInt(split[0]);
            int shelf = Integer.parseInt(split[1]);
            int total = storage + shelf;
            ir.setStorageAmount(storage);
            ir.setShelfAmount(shelf);
            ir.setTotalAmount(total,null);
            return ir.getAmounts();

        } catch (Exception e) {
            return "Action failed due to invalid input";
        }
    }

    public String moveToShelf(String name, String amount) {
        ItemRecord ir = itemRecords.get(name);
        try {
            return ir.moveToShelf(Integer.parseInt(amount));
        } catch (Exception e) {
            return "Action failed due to invalid input";
        }
    }

    public String subtract(String name, String amount) {
        ItemRecord ir = itemRecords.get(name);
        try {
            return ir.subtractFromShelf(Integer.parseInt(amount));
        } catch (Exception e) {
            return "Action failed due to invalid input";
        }
    }

    public String getInventoryReport(String category) {
        Category category1 = categories.get(category);
        if (category1 == null){
            category1 = dalCategory.getCategory(category,email_ID);
            if (category1 == null)
                return "No such category";
        }
        loadItemRecordsOfCategory(category1);
        loadCategoryDiscount(category1);
        for (Discount discount: discounts) {
            if(discount.validCategoryDiscount(category))
                return discount.withDiscount() + category1.items(this);
        }
        return category+" : \n"+ category1.items(this);
    }

    private void loadCategoryDiscount(Category c) {
        List<CategoryDiscount> l = dalDiscount.getCategoryDiscounts(c,email_ID);
        for (CategoryDiscount cd:l) {
            boolean inList = false;
            for (Discount d:discounts) {
                if(d.getId() == cd.getId()) {
                    inList = true;
                    break;
                }
            }
            if(!inList)
                discounts.add(cd);
        }
    }

    private void loadItemRecordsOfCategory(Category c) {
        List<ItemRecord> l = dalItemRecord.getItemRecordByCategoryName(c.getName(),email_ID);
        for (ItemRecord ir:l) {
            boolean inList = false;
            for (ItemRecord ir2: c.getItemRecords()) {
                if(ir.getId() == ir2.getId()) {
                    inList = true;
                    break;
                }
            }
            if(!inList)
                c.addItem(ir);
        }
    }

    private void loadCategoryOfItem(ItemRecord record){
        List<Category> l = dalCategory.getCategoryOfItem(record.getId(),email_ID);
        for (Category c:l) {
            boolean inList = false;
            for (Category c2: categories.values()) {
                if(c.getName().equals(c2.getName())) {
                    inList = true;
                    break;
                }
            }
            if(!inList)
                categories.put(c.getName(),c);
                c.addItem(record);//addItem is safe
        }
    }

    public String itemForReport(ItemRecord record) {
        String itemStr = record.getName() + " : shelf amount " + record.getShelfAmount() + " storage amount "+ record.getStorageAmount()+" ";
        String main = "main category ";
        String sub = "sub category ";
        String subsub = "sub sub category " ;
        loadCategoryOfItem(record);
        for (Category category:categories.values()) {

            if(category.getItemRecords().contains(record)){
                if(category.isMain())
                    main = main + category.getName()+" ";
                else if(category.isSub())
                    sub = sub + category.getName()+" ";
                else if(category.isSubSub())
                    subsub = subsub + category.getName()+" ";
            }
        }
        itemStr += main+" "+sub+" "+subsub+" "+record.getPrices()+" ";
        for (Discount discount: discounts) {
            if(discount.validItemDiscount(record.getName()))
                itemStr += discount.withDiscount()+" ";
        }
        return itemStr + "\n";

    }

    public String getAllInventoryReport() {
        List<Category> l = dalCategory.getAllCategories(email_ID);
        for (Category c:l) {
            if(!categories.containsKey(c.getName()))
                categories.put(c.getName(),c);
        }
        String report = "";
        for (Category category:categories.values()) {
            report = report + getInventoryReport(category.getName())+ "\n";
        }
        return report;
    }

    public String printDefectedReport(java.sql.Date beginDate, java.sql.Date endDate){

        return dalItemRecord.printDefectedItems(beginDate, endDate, this.email_ID);
    /*    String report ="";
        for(SimplePair pair: defects){
            if(pair.getDate().compareTo(beginDate)>=0 && pair.getDate().compareTo(endDate)<=0){
                report = report + pair.getItem().toString() ;
            }
            for (ItemRecord ir: itemRecords.values()) {
                if(ir.getItems().contains(pair.getItem())) {
                    report = report + " shelf: " + ir.getShelfNumber() +" Item: "+ir.getName()+ "\n";
                }
            }
        }
        for (ItemRecord ir: itemRecords.values()) {
            for (Item item: ir.getItems()) {
                if(!item.isDefective()) {
                    if (item.getExpirationDate().getTime() > beginDate.getTime() && item.getExpirationDate().getTime() < endDate.getTime()) {
                        report = report + item.toString() + " shelf: " + ir.getShelfNumber() + " Item: " + ir.getName() + "\n";
                    }
                }
            }
        }
        return report;
*/
    }

    public void sendWarning(ItemRecord itemRecord) {
        SystemManager.sendWarning("Making new order of "+itemRecord.getName()+" after reaching total amount of : "+itemRecord.getTotalAmount()+" " +
                " while min amount is : " +itemRecord.getMinAmount()+ "\n");
    }

    public int getPrice(String itemRecord) {
        return itemRecords.get(itemRecord).getCurrPrice().getStorePrice();
    }

    public boolean isDefective(String itemRecord, int itemId) {
        for (Item item: itemRecords.get(itemRecord).getItems()) {
            if(item.getId() == itemId)
                return item.isDefective();

        }
        return false;
    }

    public int getItemDiscount(String name){
        for (Discount d:discounts) {
            if(d.validItemDiscount(name))
                return d.getPercentage();
        }
        return 0;
    }

    public int getCategoryDiscount(String name) {
        for (Discount d:discounts) {
            if(d.validCategoryDiscount(name))
                return d.getPercentage();
        }
        return 0;
    }

    public int getShelfAmount(String name) {
        return itemRecords.get(name).getShelfAmount();
    }

    public int getStorageAmount(String name) {
        return itemRecords.get(name).getStorageAmount();
    }

    public int getTotalAmount(String name) {
        return itemRecords.get(name).getTotalAmount();
    }

    public void addItemRecord(ItemRecord itemRecord) {
        itemRecords.put(itemRecord.getName(),itemRecord);
    }

    //Just for tests!
    public List<Order> getList_of_Order() {
        return this.list_of_Order;
    }

    //Just fot tests!
    public List<Supplier> getList_of_Suplier() {
        return this.list_of_Suplier;
    }

    public void createAutomaticOrder(int itemId, int amount) {
        //TODO: make order with best supplier
    }

    public void AddToStore(java.util.Map productID_amount, java.util.Map productID_date) {
        //Todo Add the store the product with that ID
    }

    public InterfaceOrder getOrderDetails(int orderID) {
        InterfaceOrder ord=null;
        for (Order o:list_of_Order
             ) {
            if(o.getID_Invitation()==orderID);
            ord=new InterfaceOrder(o.getID_Vendor(),o.getID_Invitation(),o.getDay(),o.getOrderDate(),o.getArrivalTime(),o.getItemsID_ItemsIDVendor(),o.getItemsID_NumberOfItems(),o.getTotalPrice(),o.getStatus());
        }
        return ord;
    }

    public InterfaceSupplier GetTheCyeeperSuplier(int produdtId, int amount) {
        int Id_p_sup=-1;
        Supplier sup=null;
        double FinalPrice= Integer.MAX_VALUE;
        for (Supplier s:list_of_Suplier
        ) {
            Id_p_sup=s.GetIdProduct(produdtId);
            if(Id_p_sup!=-1) {
                double price = s.getPric(produdtId, amount);
                if (price < FinalPrice) {
                    FinalPrice = price;
                    sup=s;
                }
            }
        }
        if(sup!=null){
            Contract c=sup.getContract();
            InterfaceContract contract=new InterfaceContract(c.getSuplaier_ID(),c.isFixeDays(),c.getDayes(),c.isLeading(),c.getProductIDVendor_Name(),c.getItemsID_ItemsIDSupplier(),c.getProductIDVendor_Price());
            Wrotequantities worte=sup.getWorte();
            InterfaceWrotequantities w=null;
            if(worte!=null) {
                w=new InterfaceWrotequantities(worte.getSuplaier_ID(),worte.getItemsID_Amount(),worte.getItemsID_Assumption());
            }
            InterfaceSupplier s=new InterfaceSupplier(sup.getName(),sup.getID(),sup.getBank(),sup.getBranch(),sup.getBankNumber(),sup.getPayments(),sup.getContactsID_Name(),sup.getContactsID_number(),contract,w);
            return s;
        }
        return null;
        }

    public int GetSupplierID_PerOrder(int id_order) {
        for (Order o:list_of_Order
             ) {
            if(o.getID_Invitation()==id_order){
                return o.getID_Vendor();
            }
        }
       return -1;
    }

    public LinkedList<InterfaceOrder> GetOrderDetails() {
     LinkedList<InterfaceOrder> orders=new LinkedList<InterfaceOrder>();

        for (Order o:list_of_Order
        ) {
            if (o.getDay().contains(1)) {//todo check the date!!
                InterfaceOrder ord = new InterfaceOrder(o.getID_Vendor(), o.getID_Invitation(), o.getDay(), o.getOrderDate(), o.getArrivalTime(), o.getItemsID_ItemsIDVendor(), o.getItemsID_NumberOfItems(), o.getTotalPrice(), o.getStatus());
            }
        }
    return orders;
    }

    public void Logout(){
        MapStore.DeleteStore(email_ID);
        MapStore.WriteStore(email_ID,itemId,NumOfProduct,NumOfOrder);
 }
}
