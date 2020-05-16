package BusinessLayer;

import DataAccesslayer.*;
import InterfaceLayer.InterfaceContract;
import InterfaceLayer.InterfaceOrder;
import InterfaceLayer.InterfaceSupplier;
import InterfaceLayer.SystemManager;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

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
    private int itemId; //was static
    private DALItemRecord dalItemRecord;

    public static Store createInstance(String email) {
        if (storeInstance != null) {
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
    Map.InitializeDB();
    list_of_Suplier=new LinkedList<Supplier>();
    LinkedList<DALSupplier> Suppliers=Map.ReadAllSupplier();
    for (DALSupplier s : Suppliers
        ) {
            Supplier sp = new Supplier(s.Name, s.ID, s.Address, s.Bank, s.Branch, s.BankNumber, s.Payments, s.ContactsID_Name, s.ContactsID_number);
            DALContract DC = Map.ReadContract(s.ID);
            if (DC != null) {
                Contract c = new Contract(DC.Suplaier_ID, DC.FixeDays, DC.Dayes, DC.leading, DC.ProductIDSupplier_Name, DC.ItemsID_ItemsIDSupplier, DC.productIDSupplier_Price);
                sp.setContract(c);
                NumOfProduct += c.getItemsID_ItemsIDSupplier().size();
            }
            DALWrotequantities DW = Map.ReadWorte(s.ID);
            if (DW != null) {
                Wrotequantities w = new Wrotequantities(DW.Suplaier_ID, DW.ItemsID_Amount, DW.ItemsID_Assumption);
                sp.setWorte(w);
            }
            list_of_Suplier.add(sp);
        }
    LinkedList<DALOrder> Orders=Map.ReadAllInvetation();
    list_of_Order=new LinkedList<Order>();
    for (DALOrder Do: Orders
        ) {
            Order or=new Order(Do.ID_Vendor,Do.ID_Inventation,Do.Auto,Do.Day,Do.ItemsID_ItemsIDVendor,Do.ItemsID_NumberOfItems,Do.TotalPrice);
            list_of_Order.add(or);
        }
    NumOfOrder=Orders.size();
        itemRecords = new HashMap<>();
        categories = new HashMap<>();
        discounts = new LinkedList<>();
        defects = new LinkedList<>();
        itemId = 0;
        dalItemRecord = new DALItemRecord();
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
        Map.WriteSupplier(name, id,address, bank,branch,bankNumber,payments,contacts_id,contacts_number);
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
        Map.DeleteSupplier(id);
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
        Map.EditSupplier(name, id, address, bank,branch,bankNumber,payments,contacts_id_name,contacts_number);
        return "Done";
    }

    public String AddContract(int suplaier_id, boolean fixeDays, LinkedList<Integer> days, boolean leading, Map<Integer,Integer>  ItemsID_ItemsIDSupplier,
                              Map<Integer, String> productIDVendor_name, Map<Integer, Double> productIDVendor_price) {

        NumOfProduct=NumOfProduct+ItemsID_ItemsIDSupplier.size();
        for (Supplier s:list_of_Suplier
        ) {
                Contract con=new Contract(suplaier_id,fixeDays,days,leading,productIDVendor_name,ItemsID_ItemsIDSupplier,productIDVendor_price);
                s.setContract(con);
                Map.WriteContract(suplaier_id,fixeDays,days,leading,productIDVendor_name,ItemsID_ItemsIDSupplier,productIDVendor_price);
                return "Done";
            }

        return "The supplier is not exists in the system";
    }

    public String AddWrite(int suplaier_id,Map<Integer, Integer> itemsID_amount, Map<Integer, Double> itemsID_assumption) {
        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==suplaier_id){
                Wrotequantities W=new Wrotequantities(suplaier_id,itemsID_amount,itemsID_assumption);
                s.setWorte(W);
                Map.WriteWrote(suplaier_id,itemsID_amount,itemsID_assumption);
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
            list_of_Order.add(O);
            Map.WriteOrder(id_suplaier, NumOfOrder,false,day, LocalDate.now(),null,ProductID_IDSupplier, ProductIDSupplier_numberOfItems, TotalPrice.get(),"Waiting");

            NumOfOrder++;
            return NumOfOrder-1;
        }
        return -1;

    }


    public void ChangeOrder(int id_order, int id_suplaier, LinkedList<Integer> day, java.util.Map<Integer, Integer> itemsIDVendor_numberOfItems) {
        Supplier sup=null;
        for (Supplier s: list_of_Suplier
        ) {
            if(s.getID()==id_suplaier){
                sup=s;
            }
        }
        if(sup!=null){
            Map<Integer, Integer> ProductID_IDSupplier = new ConcurrentHashMap<Integer, Integer>();
            for (Map.Entry<Integer,Integer> e : itemsIDVendor_numberOfItems.entrySet()) {
                int Id_Product = sup.GetIdProduct(e.getKey());
                ProductID_IDSupplier.put(Id_Product, e.getKey());
            }
            for (Order o:list_of_Order
            ) {
                if (o.getID_Invitation() == id_order) {
                    o.ChangeOrder(id_order, id_suplaier, day, ProductID_IDSupplier, itemsIDVendor_numberOfItems);
                    AtomicReference<Double> TotalPrice = new AtomicReference<>((double) 0);
                    for (Map.Entry<Integer, Integer> e : o.getItemsID_NumberOfItems().entrySet()) {
                        double Price = sup.getPric(e.getKey(), e.getValue()); //todo check if works
                        TotalPrice.set(TotalPrice.get() + Price);
                    }
                    o.setTotalPrice(TotalPrice.get());
                }
            }
        }
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
                    IdSoplier = s.getID();
                    sup=s;
                }
            }
        }
        LinkedList<Integer> day=new LinkedList<Integer>();
        day.add(-1);//todo which day?
        Map<Integer,Integer> ProductID_ProductID_IDSupplier=new ConcurrentHashMap<Integer, Integer>();
        Map<Integer,Integer> ProductIDSupplier_numberOfItems=new ConcurrentHashMap<Integer, Integer>();
        ProductID_ProductID_IDSupplier.put(IdProduct,Id_p_sup);
        ProductIDSupplier_numberOfItems.put(Id_p_sup,amount);
        Order O = new Order(IdSoplier, NumOfOrder,true, day, ProductID_ProductID_IDSupplier, ProductIDSupplier_numberOfItems, FinalPrice);
        if(!sup.getContract().isLeading()){
            Trans.Lead(O);
        }
        list_of_Order.add(O);
        NumOfOrder++;
        Map.WriteOrder(IdProduct, NumOfOrder,true,day, LocalDate.now(),null,ProductID_ProductID_IDSupplier, ProductIDSupplier_numberOfItems, FinalPrice,"Waiting");
        return "Done";//todo ?? return done?
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
                    Map.DeleteContract(suplaier_id);
                    Map.WriteContract(suplaier_id, fixeDays, days, leading, productIDVendor_name, s.getContract().getItemsID_ItemsIDSupplier(), producttemsIDVendor_price);
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
                    Map.DeleteWrote(suplaier_id);
                    Map.WriteWrote(suplaier_id,itemsID_amount,itemsID_assumption);
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
            InterfaceSupplier I=new InterfaceSupplier(s.getName(),s.getID(),s.getBank(),s.getBranch(),s.getBankNumber(),s.getPayments(),s.getContactsID_Name(),s.getContactsID_number());
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
        int id=Map.getProductId(product_name,category,subcategory,sub_subcategory,manufacturer);
        if(id==-1){
            id=NumOfProduct++;
            Map.AddProdudt(id,product_name,category,subcategory,sub_subcategory,manufacturer);
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





    public void initializeItems() {
        ItemRecord itemRecord1 = new ItemRecord("milk Tnova 3%",1,3,1,3,4,1,"tnova");
        itemRecord1.addItem(new Item(itemId++, new java.sql.Date(2020-1900,4-1,19)));
        itemRecord1.addItem(new Item(itemId++, new java.sql.Date(2020-1900,4-1,19)));
        itemRecord1.addItem(new Item(itemId++, new java.sql.Date(2020-1900,4-1,20)));
        itemRecord1.addItem(new Item(itemId++, new java.sql.Date(2020-1900,4-1,20)));
        itemRecords.put("milk Tnova 3%",itemRecord1);

        ItemRecord itemRecord2 = new ItemRecord("white bread",2,3,2,3,5,2,"dganit");
        itemRecord2.addItem(new Item(itemId++, new java.sql.Date(2020-1900,5-1,19)));
        itemRecord2.addItem(new Item(itemId++, new java.sql.Date(2020-1900,5-1,19)));
        itemRecord2.addItem(new Item(itemId++, new java.sql.Date(2020-1900,5-1,20)));
        itemRecord2.addItem(new Item(itemId++, new java.sql.Date(2020-1900,5-1,20)));
        itemRecord2.addItem(new Item(itemId++, new java.sql.Date(2020-1900,5-1,20)));
        itemRecords.put("white bread",itemRecord2);

        ItemRecord itemRecord3 = new ItemRecord("coffee Elite",3,2,0,2,2,3,"elite");
        itemRecord3.addItem(new Item(itemId++, new java.sql.Date(2020-1900,8-1,20)));
        itemRecord3.addItem(new Item(itemId++, new java.sql.Date(2020-1900,8-1,20)));
        itemRecords.put("coffee Elite",itemRecord3);

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

        categories.put("Dairy",category1);
        categories.put("Milk",subCat1);
        categories.put("1 liter",subsubcat1);

        Category category2 = new Category(Category.CategoryRole.MainCategory,"Bread and pastry");
        Category subcat2 = new Category(Category.CategoryRole.SubCategory,"Bread");
        Category subsubcat2 = new Category(Category.CategoryRole.SubSubCategory,"750 gr");
        addItemToCategory(itemRecords.get("white bread") ,category2);
        addItemToCategory(itemRecords.get("white bread") ,subcat2);
        addItemToCategory(itemRecords.get("white bread") ,subsubcat2);

        categories.put("Bread and pastry",category2);
        categories.put("Bread",subcat2);
        categories.put("750 gr",subsubcat2);
        Category category3 = new Category(Category.CategoryRole.MainCategory,"Drinks");
        Category subcat3 = new Category(Category.CategoryRole.SubCategory,"Coffee powder");
        Category subsubcat3 = new Category(Category.CategoryRole.SubSubCategory,"500 gr");
        addItemToCategory(itemRecords.get("coffee Elite") ,category3);
        addItemToCategory(itemRecords.get("coffee Elite") ,subcat3);
        addItemToCategory(itemRecords.get("coffee Elite") ,subsubcat3);

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

    public void initializeDiscounts() {
        CategoryDiscount cd1 = new CategoryDiscount(categories.get("Drinks"),
                new java.sql.Date(2020-1900,4-1,20),
                new java.sql.Date(2020-1900,5-1,20),20);
        CategoryDiscount cd2 = new CategoryDiscount(categories.get("Dairy"),
                new java.sql.Date(2020-1900,5-1,20),
                new java.sql.Date(2020-1900,5-1,20),30);
        discounts.add(cd1);
        discounts.add(cd2);
        ItemDiscount id =new ItemDiscount(itemRecords.get("white bread"),
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
                ItemDiscount d = new ItemDiscount(ir, beginDate, endDate, percentage);
                Price p = ir.getCurrPrice();
                int beforeDiscount = p.getStorePrice();
                int afterDiscount = (beforeDiscount/100) * percentage;
                Price discountedPrice = new Price(p.getRetailPrice() , afterDiscount);
                ir.addPrice(discountedPrice);
                discounts.add(d);
                return "The discount was added succesfully";
            }
        }
        return "No such item";
    }

    public String addNewCategoryDiscount(String categoryName, int percentage, java.sql.Date beginDate, java.sql.Date endDate){
        if(!(percentage>=1 && percentage<=100)){
            return "Discount percentage must be a number between 1-100";
        }
        for( Category cat: categories.values()){
            if (cat.getName().equals(categoryName)){         //checks if there is a category with the given name
                CategoryDiscount d = new CategoryDiscount(cat, beginDate, endDate, percentage);
                for (ItemRecord itemRec: cat.getItemRecords() ){
                    Price p = itemRec.getCurrPrice();
                    int beforeDiscount = p.getStorePrice();
                    int afterDiscount = (beforeDiscount/100) * percentage;
                    Price discountedPrice = new Price(p.getRetailPrice() , afterDiscount);
                    itemRec.addPrice(discountedPrice);
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
            return "No such item in inventory";
        }
        return  ir.getAmounts();
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
            ir.setTotalAmount(total);
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
            return "No such category";
        }
        for (Discount discount: discounts) {
            if(discount.validCategoryDiscount(category))
                return discount.withDiscount() + category1.items();
        }
        return category+" : \n"+ category1.items();
    }

    public String itemForReport(ItemRecord record) {
        String itemStr = record.getName() + " : shelf amount " + record.getShelfAmount() + " storage amount "+ record.getStorageAmount()+" ";
        String main = "main category ";
        String sub = "sub category ";
        String subsub = "sub sub category " ;
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
        String report = "";
        for (Category category:categories.values()) {
            report = report + getInventoryReport(category.getName())+ "\n";
        }
        return report;
    }

    public String printDefectedReport(java.sql.Date beginDate, java.sql.Date endDate){
        String report = "";
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

    public InterfaceOrder getOrderDetails(int done) {
        return null;//todo
    }
}
