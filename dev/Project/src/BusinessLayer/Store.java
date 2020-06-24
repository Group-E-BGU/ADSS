package BusinessLayer;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import DataAccesslayer.*;

public class Store {

    private static Store storeInstance;
    private MapperItemRecord mapperItemRecord;
    private MapperCategory mapperCategory;
    private MapperDiscount mapperDiscount;
    private MapperPrice mapperPrice;
    private MapperSupplier MapSupplier;
    private MapperContract MapContract;
    private MapperItemRecord_Supplier MapIRS;
    private MapperWrotequantities MapWorte;
    private MapperOrder MapOrder;
    private MapperStore MapStore;
    private String address;
    private int NumOfOrder=0;
    private List<Supplier> list_of_Suplier;
    private List<Order> list_of_Order;
    private Transportrans Trans;
    private static Mapper Map;
    private HashMap<String,ItemRecord> itemRecords;
    private HashMap<String,Category> categories;
    private LinkedList<Discount> discounts;
    private LinkedList<SimplePair> defects;
    private static int itemId;


    public static Store createInstance(String email) {
        storeInstance = new Store(email);
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
    mapperCategory = new MapperCategory();
    mapperItemRecord = new MapperItemRecord();
    mapperDiscount = new MapperDiscount();
    mapperPrice = new MapperPrice();
    MapSupplier=new MapperSupplier();
    MapContract=new MapperContract();
    MapWorte=new MapperWrotequantities();
    MapIRS=new MapperItemRecord_Supplier();
    MapOrder=new MapperOrder();
    MapStore=new MapperStore();
    address = email;
    list_of_Suplier=new LinkedList<Supplier>();
    list_of_Order=new LinkedList<Order>();
    NumOfOrder=MapStore.getNumOfOrder(email);
    Trans = new Transportrans();
    itemRecords=new HashMap<String,ItemRecord>();
    categories=new HashMap<String,Category>();
    discounts=new LinkedList<Discount>();
    defects=new LinkedList<SimplePair>();
    itemId = mapperItemRecord.getMaxItemRecordId();

    }

    public String getAddress(){
        return address;
    }

    public String addItemRecord(String name, int minAmount, int shelfNumber, String manufacture) {
        if (itemRecords.containsKey(name) || mapperItemRecord.getItemRecord(name, address) != null) {
            return "This product name already exists";
        }
        else{
            ItemRecord ir = new ItemRecord(name,itemId++,minAmount,0,0,0,shelfNumber,manufacture);
            itemRecords.put(name,ir);
            mapperItemRecord.InsertItemRecord(name,ir.getId(),minAmount,0,0,0,shelfNumber,manufacture, address);
            // add the new product to Products table
            (new ProductDAO()).save(new Product(name,"" + itemId, 1));
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
        Supplier sup=new Supplier(name, id, address, bank,branch,bankNumber,payments,contacts_id,contacts_number);
        list_of_Suplier.add(sup);
        MapSupplier.WriteSupplier(name, id,address, bank,branch,bankNumber,payments,this.address,contacts_id,contacts_number);
        return "Done";
    }

    public String Delete(int id) {
        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==id){
                list_of_Suplier.remove(s);
                MapOrder.DeleteOrder_Supplier(address,id);
                MapSupplier.DeleteSupplier(id, address);
                return "Done";
            }
        }

        Supplier s=MapSupplier.GetSupplier(id, address);
        if(s!=null){
            MapOrder.DeleteOrder_Supplier(address,id);
            MapSupplier.DeleteSupplier(id, address);
            return "Done";
        }
        return "The Supplier is not exist in the system";
    }

    public MapperSupplier getMapSupplier() {
        return MapSupplier;
    }

    public MapperContract getMapContract() {
        return MapContract;
    }

    public MapperItemRecord_Supplier getMapIRS() {
        return MapIRS;
    }

    public MapperWrotequantities getMapWorte() {
        return MapWorte;
    }

    public MapperOrder getMapOrder() {
        return MapOrder;
    }

    public MapperStore getMapStore() {
        return MapStore;
    }

    public String EditSuplier(String name, int id, String address, String bank, String branch, int bankNumber, String payments, Map<Integer, String> contacts_id_name, Map<Integer, Integer> contacts_number) {

        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==id){
                s.setName(name);
                s.setBank(bank);
                s.setBranch(branch);
                s.setBankNumber(bankNumber);
                s.setAddress(address);
                s.setPayments(payments);
                s.setContactsID_Name(contacts_id_name);
                s.setContactsID_number(contacts_number);
                MapSupplier.UpdateSupplier(name, id, address, bank,branch,bankNumber,payments, this.address,contacts_id_name,contacts_number);
              return "Done";
            }
        }
        Supplier s=MapSupplier.GetSupplier(id, this.address);
        if(s!=null){
            MapSupplier.UpdateSupplier(name, id, address, bank,branch,bankNumber,payments, this.address,contacts_id_name,contacts_number);
            return "Done";
        }
        return "The supplier is not exist in the system";
    }

    public String AddContract(int suplaier_id, boolean fixeDays, LinkedList<Integer> days, boolean leading, Map<Integer,Integer>  ItemsID_ItemsIDSupplier,
                              Map<Integer, String> productIDVendor_name, Map<Integer, Double> productIDVendor_price) {
        for (Supplier s:list_of_Suplier
        ) {
            if (s.getID() == suplaier_id) {
                Contract con = new Contract(suplaier_id, fixeDays, days, leading, productIDVendor_name, ItemsID_ItemsIDSupplier, productIDVendor_price);
                s.setContract(con);
                MapContract.WriteContract(suplaier_id,fixeDays,leading, address,days,ItemsID_ItemsIDSupplier,productIDVendor_name,productIDVendor_price);
                return "Done";
            }
        }
            Supplier s=MapSupplier.GetSupplier(suplaier_id, address);
            if(s!=null){
                list_of_Suplier.add(s);
                MapContract.WriteContract(suplaier_id,fixeDays,leading, address,days,ItemsID_ItemsIDSupplier,productIDVendor_name,productIDVendor_price);
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
                MapWorte.WriteWrote(address,suplaier_id,itemsID_amount,itemsID_assumption);
                return "Done";
            }
        }
            Supplier s=MapSupplier.GetSupplier(suplaier_id, address);
            if(s!=null){
                list_of_Suplier.add(s);
                Wrotequantities W=new Wrotequantities(suplaier_id,itemsID_amount,itemsID_assumption);
                s.setWorte(W);
                MapWorte.WriteWrote(address,suplaier_id,itemsID_amount,itemsID_assumption);
                return "Done";
            }
        return "The supplier is not exists in the system";
    }

    public int MakeOrder(int id_suplaier, LinkedList<Integer> day, Map<Integer, Integer> ProductIDSupplier_numberOfItems) {
        Supplier sup=null;
        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==id_suplaier){
                sup=s;
            }
        }
        if(sup==null){
            sup=MapSupplier.GetSupplier(id_suplaier, address);
            if(sup!=null){
                list_of_Suplier.add(sup);
            }
        }
        if(sup!=null&&sup.getContract()!=null) {
            Map<Integer, Integer> ProductID_IDSupplier = new ConcurrentHashMap<Integer, Integer>();
            AtomicReference<Double> TotalPrice = new AtomicReference<>((double) 0);
            for (Map.Entry<Integer,Integer> e : ProductIDSupplier_numberOfItems.entrySet()) {
                int Id_Product = sup.GetIdProduct(e.getKey());
                ProductID_IDSupplier.put(Id_Product, e.getKey());
                double Price =sup.getPric(e.getKey(), e.getValue());
                TotalPrice.set(TotalPrice.get()+Price);
            }
            Order O = new Order(id_suplaier, NumOfOrder,false, day, ProductID_IDSupplier, ProductIDSupplier_numberOfItems, TotalPrice.get());
            Map<Integer, Integer> ProductIDSupplier_IDStore = new ConcurrentHashMap<Integer, Integer>();
            for (Map.Entry<Integer,Integer> e:ProductID_IDSupplier.entrySet()
                 ) {
                ProductIDSupplier_IDStore.put(e.getValue(),e.getKey());
            }
            list_of_Order.add(O);
            MapOrder.WriteOrder(address,id_suplaier, NumOfOrder,false, day, java.sql.Date.valueOf(LocalDate.now()),null, TotalPrice.get(),"Waiting",ProductIDSupplier_IDStore,ProductIDSupplier_numberOfItems);
            NumOfOrder++;
            return NumOfOrder-1;
        }
        return -1;

    }

    public InterfaceOrder ChangeOrder(int id_order, int id_suplaier, LinkedList<Integer> day, java.util.Map<Integer, Integer> itemsIDVendor_numberOfItems) {
        Supplier sup = MapSupplier.GetSupplier(id_suplaier, address);
       InterfaceOrder ord=null;
        if (sup != null) {
            Order order = MapOrder.GetOrder(id_order, address);
            if (order != null) {
                LinkedList<Integer> OldDays = order.getDay();

                Map<Integer, Integer> ProductID_IDSupplier = new ConcurrentHashMap<Integer, Integer>();
                for (Map.Entry<Integer, Integer> e : itemsIDVendor_numberOfItems.entrySet()) {
                    int Id_Product = sup.GetIdProduct(e.getKey());
                    ProductID_IDSupplier.put(Id_Product, e.getKey());
                }

                Map<Integer, Integer> ProductIDSupplier_IDStore = new ConcurrentHashMap<Integer, Integer>();
                for (Map.Entry<Integer, Integer> e : ProductID_IDSupplier.entrySet()
                ) {
                    ProductIDSupplier_IDStore.put(e.getValue(), e.getKey());
                }
                Double tot=order.getTotalPrice();
                AtomicReference<Double> TotalPrice = new AtomicReference<>(tot);
                for (Map.Entry<Integer, Integer> e : order.getItemsID_NumberOfItems().entrySet()) {
                    double Price = sup.getPric(e.getKey(), e.getValue());
                    TotalPrice.set(TotalPrice.get() + Price);
                }

                for (Order o : list_of_Order
                ) {
                    if (o.getID_Invitation() == id_order) {
                        o.ChangeOrder(id_order, id_suplaier,OldDays, day, ProductID_IDSupplier, itemsIDVendor_numberOfItems);
                        o.setTotalPrice(TotalPrice.get());
                    }
                }
                MapOrder.UpdateOrder(address, id_order,OldDays, day, ProductIDSupplier_IDStore, itemsIDVendor_numberOfItems);
                order = MapOrder.GetOrder(id_order, address);
                ord=new InterfaceOrder( order.getID_Vendor(),order.getID_Invitation(),order.getDay(),order.getOrderDate(),order.getArrivalTime(),order.getItemsID_ItemsIDVendor(),order.getItemsID_NumberOfItems(),order.getTotalPrice(),order.getStatus());
                return ord;
            }
            return ord;
        }
            return ord;
        }

    public String AutomaticProductOrdering(int IdProduct, int amount){
        int IdSoplier= -1;
        int Id_p_sup=-1;
        Supplier sup=null;
        double FinalPrice= Integer.MAX_VALUE;
        LinkedList<Supplier> suppliers=MapSupplier.GetSuppliers(address);
        for (Supplier s:suppliers
        ) {
            Id_p_sup=s.GetIdProductPerStore(IdProduct);
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
            day.add(LocalDate.now().getDayOfWeek().getValue()+2);//todo! check!

            Map<Integer, Integer> ProductStoreID_IDSupplier = new ConcurrentHashMap<Integer, Integer>();
            Map<Integer, Integer> ProductIDSupplier_numberOfItems = new ConcurrentHashMap<Integer, Integer>();
            ProductStoreID_IDSupplier.put(IdProduct, Id_p_sup);
            ProductIDSupplier_numberOfItems.put(Id_p_sup, amount);

            Order O = new Order(IdSoplier, NumOfOrder, true, day, ProductStoreID_IDSupplier, ProductIDSupplier_numberOfItems, FinalPrice);
            list_of_Order.add(O);

            Map<Integer, Integer> ProductIDSupplier_IDStore = new ConcurrentHashMap<Integer, Integer>();
            for (Map.Entry<Integer,Integer> e:ProductStoreID_IDSupplier.entrySet()
            ) {
                ProductIDSupplier_IDStore.put(e.getValue(),e.getKey());
            }
            if(!sup.getContract().isLeading()){
               //
            }
            MapOrder.WriteOrder(address, sup.getID(),NumOfOrder,true, day, java.sql.Date.valueOf(LocalDate.now()),java.sql.Date.valueOf(LocalDate.now()) ,O.getTotalPrice(),"Waiting",ProductIDSupplier_IDStore,ProductIDSupplier_numberOfItems);
            NumOfOrder++;
            return "Done";
        }
        return "problem with the create autoOrder";
    }

    public String EditContract(int suplaier_id, boolean fixeDays, LinkedList<Integer> days, boolean leading,Map<Integer,Integer>  ItemsID_ItemsIDSupplier, Map<Integer, String> productIDVendor_name, Map<Integer, Double> producttemsIDVendor_price) {
        Supplier sup = MapSupplier.GetSupplier(suplaier_id, address);
        if (sup != null) {
            Contract c = MapContract.getContract(suplaier_id, address);
            if (c != null) {
                for (Supplier s : list_of_Suplier
                ) {
                    if (s.getID() == suplaier_id) {
                        if (s.getContract() != null) {
                            s.getContract().setDayes(days);
                            s.getContract().setLeading(leading);
                            s.getContract().setFixeDays(fixeDays);
                            s.getContract().setItemsID_ItemsIDSupplier(ItemsID_ItemsIDSupplier);
                            s.getContract().setProductIDVendor_Name(productIDVendor_name);
                            s.getContract().setProductIDVendor_Price(producttemsIDVendor_price);
                            MapContract.UpdateContract(suplaier_id, fixeDays, leading, address, days);
                            return "Done";
                        }
                    }
                }
                MapContract.UpdateContract(suplaier_id, fixeDays, leading, address, days);
                return "Done";
            }
            return "The contract is not exist in the system";
        }
        return "The Supplier haven't a contract";
    }

    public String EditWrite(int suplaier_id, Map<Integer, Integer> itemsID_amount, Map<Integer, Double> itemsID_assumption) {
        Supplier sup = MapSupplier.GetSupplier(suplaier_id, address);
        if (sup != null) {
            Wrotequantities c = MapWorte.GetWrotequantities(suplaier_id, address);
            if (c != null) {
                for (Supplier s : list_of_Suplier
                ) {
                    if (s.getID() == suplaier_id) {
                        if (s.getWorte() != null) {
                            s.getWorte().setItemsID_Amount(itemsID_amount);
                            s.getWorte().setItemsID_Assumption(itemsID_assumption);
                            MapWorte.DeleteWrote(suplaier_id, address);
                            MapWorte.WriteWrote(address, suplaier_id, itemsID_amount, itemsID_assumption);
                            return "Done";
                        }
                    }
                }
                MapWorte.DeleteWrote(suplaier_id, address);
                MapWorte.WriteWrote(address, suplaier_id, itemsID_amount, itemsID_assumption);
                return "Done";
            }
                    return "The Supplier haven't a 'Wrote quantities'";
                }
        return "The supplier is not exists in the system";
    }

    public String CheckSuplierExit(int id) {
        Supplier s=MapSupplier.GetSupplier(id, address);
             if(s!=null){
                return "Exist";
            }
        return "Not Exist";
    }

    public String CheckSAgreementExit(int suplaier_id) {
        Supplier s=MapSupplier.GetSupplier(suplaier_id, address);
        if(s!=null){
            Contract c=MapContract.getContract(suplaier_id, address);
            if(c!=null){
                    return "Done";
                }
            }
        return "The supplier haven't Agreement";
    }

    public String CheckSWortExit(int suplaier_id) {
        Supplier s=MapSupplier.GetSupplier(suplaier_id, address);
        if(s!=null) {
            Wrotequantities c = MapWorte.GetWrotequantities(suplaier_id, address);
            if (c != null) {
                return "Done";
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

    public boolean CheckTheDay(int id_suplaier, int day) {
        for (Supplier s:list_of_Suplier
        ) {
            if(s.getID()==id_suplaier){
                    return s.CheckTheDay(day);
            }
        }
        Supplier s=MapSupplier.GetSupplier(id_suplaier, address);
        if(s!=null){
            return s.CheckTheDay(day);
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
        Supplier s=MapSupplier.GetSupplier(id_suplaier, address);
        if(s!=null){
            return s.CheckProductexist(product_id);
        }
        return false;
    }

    public int FindId_P_Store(String product_name, String category, String subcategory, String sub_subcategory, String manufacturer, int minAmount, int shelfNumber) {
        int id=MapIRS.getProductId(address,product_name,category,subcategory,sub_subcategory,manufacturer);
        ItemRecord ir = itemRecords.get(product_name);
        if(ir == null)
            ir = mapperItemRecord.getItemRecord(product_name, address);
        if(id==-1) {
            id = itemId++;
            MapIRS.WriteItemRecord_Supplier(address, id, category, subcategory, sub_subcategory, product_name,manufacturer);
            ir = new ItemRecord(product_name, id, minAmount, 0, 0, 0, shelfNumber, manufacturer);
            mapperItemRecord.InsertItemRecord(product_name, id, minAmount, 0, 0, 0, shelfNumber, manufacturer, address);
            // add the new product to Products table
            (new ProductDAO()).save(new Product(product_name, ""+ id, 1));
        }
        Category main = categories.get(category);
        if(main == null) {
            main = mapperCategory.getCategory(category, address);
            if (main == null) {
                main = new Category(Category.CategoryRole.MainCategory, category);
                categories.put(category, main);
                mapperCategory.InsertCategory(category, 1, address);
            }
        }
            main.addItem(ir); //addItem is safe


        Category sub = categories.get(subcategory);
        if(sub == null) {
            sub = mapperCategory.getCategory(subcategory, address);
            if (sub == null) {
                sub = new Category(Category.CategoryRole.SubCategory, subcategory);
                categories.put(subcategory, sub);
                mapperCategory.InsertCategory(subcategory, 2, address);
            }
        }
            sub.addItem(ir);


        Category subsub = categories.get(sub_subcategory);
        if(subsub == null) {
            subsub = mapperCategory.getCategory(sub_subcategory, address);
            if (subsub == null) {
                subsub = new Category(Category.CategoryRole.SubSubCategory, sub_subcategory);
                categories.put(sub_subcategory, subsub);
                mapperCategory.InsertCategory(sub_subcategory, 3, address);
            }
        }
            subsub.addItem(ir);



        return id;
    }

    public String CheckAbleToChangeOrder(int id_order) {
        for (Order o:list_of_Order
        ) {
            if(o.getID_Invitation()==id_order){
                return o.CheckAbleToChangeOrder();
            }
        }
        Order o=MapOrder.GetOrder(id_order, address);
        if(o!=null){
            list_of_Order.add(o);
            return o.CheckAbleToChangeOrder();
        }
        return "the order is not exist in the system";
    }

    public void RemoveProduct(int id_order, int product_id) {
        MapOrder.DeleteProductOrder(address,id_order,product_id);
        for (Order o:list_of_Order
        ) {
            if(o.getID_Invitation()==id_order){
                o.RemoveProduct(product_id);
            }
          }
       }

    private void addItemToCategory(ItemRecord itemRecord, Category cat) {
        for (Category category: categories.values()) {
            if(category.getRole().equals(cat.getRole()) && category.getItemRecords().contains(itemRecord))
                return;
        }
        cat.addItem(itemRecord);
    }

    public String addItemDiscount(String name, int percentage, java.sql.Date beginDate, java.sql.Date endDate){
        if(!(percentage>=1 && percentage<=100)){
            return "Discount percentage must be a number between 1-100";
        }
        ItemRecord ir = itemRecords.get(name);
        if (ir == null) {
            ir = mapperItemRecord.getItemRecord(name, address);
            itemRecords.put(name,ir);
        }
        if(ir != null){
                ItemDiscount d = new ItemDiscount(mapperDiscount.getMaxId()+1,ir, beginDate, endDate, percentage);
                mapperDiscount.InsertItemDiscount(d.getId(), d.getPercentage(), d.getStartDate(), d.getEndDate(), ir.getId(), this.getAddress());

                discounts.add(d);
                return "The discount was added succesfully";

        }
        return "No such item";
    }


    public String addNewCategoryDiscount(String categoryName, int percentage, java.sql.Date beginDate, java.sql.Date endDate){
        if(!(percentage>=1 && percentage<=100)){
            return "Discount percentage must be a number between 1-100";
        }
        Category c = categories.get(categoryName);
        if (c == null) {
            c = mapperCategory.getCategory(categoryName, address);
            categories.put(categoryName,c);
        }
        if(c != null){
            CategoryDiscount d = new CategoryDiscount(mapperDiscount.getMaxId()+1,c, beginDate, endDate, percentage);
                mapperDiscount.InsertCategoryDiscount(d.getId(), categoryName, d.getPercentage(), d.getStartDate(), d.getEndDate(), this.getAddress());
                discounts.add(d);
                return "The discount was added successfully";

        }
        return "No such item";
    }

    public String setDefectedItem(String name, int id){
        for( ItemRecord ir: itemRecords.values()){
            if (ir.getName().equals(name)){         //checks if there is an item record with the given name
                ir.addItem(mapperItemRecord.getItemById(id));
                LinkedList<Item> itemsList = ir.getItems();
                for (Item item: itemsList){
                    if(item.getId()==id){
                        item.setDefective(true);
                        java.sql.Date currDate = new java.sql.Date((new Date()).getTime());
                        mapperItemRecord.updateDefect(item.getId(), ir.getId(), currDate, this.address);
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
        ItemRecord ir = itemRecords.get(name);
        if (ir == null) {
            ir = mapperItemRecord.getItemRecord(name, address);
            itemRecords.put(name,ir);
        }
        if(ir != null){
                Price newPr = new Price(retailPrice , price);
                ir.addPrice(newPr);
                return "added successfully";

        }
        return "No such item";
    }

    public String getItemAmountsByName(String name) {
        ItemRecord ir = itemRecords.get(name);
        if (ir == null){
            ir = mapperItemRecord.getItemRecord(name, address);
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
        List<Integer> ids = mapperItemRecord.geItemIdsByName(name, address);
        for (Integer id:ids) {
            toRet = toRet + id + "\n";
        }
        return toRet;
    }

    public String removeItem(String name,int id) {
        if(mapperItemRecord.DeleteItem(name,id, address)) {
            ItemRecord ir = itemRecords.get(name);
            if (ir == null) {
                ir = mapperItemRecord.getItemRecord(name, address);
                itemRecords.put(name, ir);
            }
            ir.removeItem(id);
            return "item deleted successfully";

        }
        return "item could not be deleted";
    }

    public String removeItemFromShelf(String name,int id) {
        if(mapperItemRecord.DeleteItem(name,id, address)) {
            ItemRecord ir = itemRecords.get(name);
            if (ir == null) {
                ir = mapperItemRecord.getItemRecord(name, address);
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
            category1 = mapperCategory.getCategory(category, address);
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
    private void loadItemDiscount(ItemRecord i) {
        List<ItemDiscount> l = mapperDiscount.getItemDiscount(i, address);
        if(l != null) {

            for (ItemDiscount cd : l) {
                boolean inList = false;
                for (Discount d : discounts) {
                    if (d.getId() == cd.getId()) {
                        inList = true;
                        break;
                    }
                }
                if (!inList)
                    discounts.add(cd);
            }
        }
    }

    private void loadCategoryDiscount(Category c) {
        List<CategoryDiscount> l = mapperDiscount.getCategoryDiscounts(c, address);
        if(l != null) {

            for (CategoryDiscount cd : l) {
                boolean inList = false;
                for (Discount d : discounts) {
                    if (d.getId() == cd.getId()) {
                        inList = true;
                        break;
                    }
                }
                if (!inList)
                    discounts.add(cd);
            }
        }
    }

    private void loadItemRecordsOfCategory(Category c) {
        List<ItemRecord> l = mapperItemRecord.getItemRecordByCategoryName(c.getName(), address);
        if(l != null) {
            for (ItemRecord ir : l) {
                boolean inList = false;
                for (ItemRecord ir2 : c.getItemRecords()) {
                    if (ir.getId() == ir2.getId()) {
                        inList = true;
                        break;
                    }
                }
                if (!inList)
                    c.addItem(ir);
            }
        }
    }

    private void loadCategoryOfItem(ItemRecord record){
        List<Category> l = mapperCategory.getCategoryOfItem(record.getId(), address);
        for (Category c:l) {
            boolean inList = false;
            for (Category c2: categories.values()) {
                if(c.getName().equals(c2.getName())) {
                    inList = true;
                    break;
                }
            }
            if(!inList || categories.containsKey(c.getName())) {
                categories.put(c.getName(), c);
            }
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

            if(category.containsRecId(record)){
                if(category.isMain())
                    main = main + category.getName()+" ";
                else if(category.isSub())
                    sub = sub + category.getName()+" ";
                else if(category.isSubSub())
                    subsub = subsub + category.getName()+" ";
            }
        }
        itemStr += main+" "+sub+" "+subsub+" "+record.getPrices()+" ";
        loadItemDiscount(record);
        for (Discount discount: discounts) {
            if(discount.validItemDiscount(record.getName()))
                itemStr += discount.withDiscount()+" ";
        }
        return itemStr + "\n";

    }

    public String getAllInventoryReport() {
        List<Category> l = mapperCategory.getAllCategories(address);
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

        return mapperItemRecord.printDefectedItems(beginDate, endDate, this.address);
    }

    public void sendWarning(ItemRecord itemRecord) {
        BLService.sendWarning("Making new order of "+itemRecord.getName()+" after reaching total amount of : "+itemRecord.getTotalAmount()+" " +
                " while min amount is : " +itemRecord.getMinAmount()+ "\n");
        AutomaticProductOrdering(itemRecord.getId(),itemRecord.getMinAmount()*2);
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
        InterfaceOrder ord = null;
        Order o = null;
            o=MapOrder.GetOrder(orderID, address);
            if(o!=null){
                ord=new InterfaceOrder(o.getID_Vendor(),o.getID_Invitation(),o.getDay(),o.getOrderDate(),o.getArrivalTime(),o.getItemsID_ItemsIDVendor(),o.getItemsID_NumberOfItems(),o.getTotalPrice(),o.getStatus());
            }
        return ord;
    }

    public int GetSupplierID_PerOrder(int id_order) {
        for (Order o:list_of_Order
        ) {
            if(o.getID_Invitation()==id_order){
                return o.getID_Vendor();
            }
        }
        Order s=MapOrder.GetOrder(id_order, address);
        if(s!=null){
            list_of_Order.add(s);
            return s.getID_Vendor();
        }
        return -1;
    }

    public LinkedList<InterfaceSupplier> GetSupliers() {
        LinkedList<InterfaceSupplier> list=new LinkedList<InterfaceSupplier>();
        LinkedList<Supplier> suppliers=MapSupplier.GetSuppliers(address);
        for (Supplier s:suppliers
        ) {
            InterfaceContract contract=null;
            InterfaceWrotequantities w=null;
            Contract c=MapContract.getContract(s.getID(), address);
            if(c!=null) {
                contract = new InterfaceContract(c.getSuplaier_ID(), c.isFixeDays(), c.getDayes(), c.isLeading(), c.getProductIDVendor_Name(), c.getItemsID_ItemsIDSupplier(), c.getProductIDVendor_Price());
            }
            Wrotequantities worte=MapWorte.GetWrotequantities(s.getID(), address);
            if(worte!=null) {
                w=new InterfaceWrotequantities(worte.getSuplaier_ID(),worte.getItemsID_Amount(),worte.getItemsID_Assumption());
            }
            InterfaceSupplier I=new InterfaceSupplier(s.getName(),s.getID(),s.getAddress(),s.getBank(),s.getBranch(),s.getBankNumber(),s.getPayments(),s.getContactsID_Name(),s.getContactsID_number(),contract,w);
            list.add(I);
        }
        return list;
    }

    public LinkedList<InterfaceContract> GetContract() {
        LinkedList<InterfaceContract> list=new LinkedList<InterfaceContract>();
        LinkedList<Supplier> suppliers=MapSupplier.GetSuppliers(address);
        for (Supplier s:suppliers
        ) {
            Contract c=MapContract.getContract(s.getID(), address);
            if (c!=null) {
                InterfaceContract I = new InterfaceContract(c.getSuplaier_ID(),c.isFixeDays(),c.getDayes(),c.isLeading(),c.getProductIDVendor_Name(),c.getItemsID_ItemsIDSupplier(),c.getProductIDVendor_Price());
                list.add(I);
            }
        }
        return list;
    }

    public InterfaceSupplier GetTheCyeeperSuplier(int produdtId, int amount) {
        int Id_p_sup=-1;
        Supplier sup=null;
        double FinalPrice= Integer.MAX_VALUE;
        LinkedList<Supplier> suppliers=MapSupplier.GetSuppliers(address);
        for (Supplier s:suppliers
        ) {
            Id_p_sup=s.GetIdProductPerStore(produdtId);
            if(Id_p_sup!=-1) {
                double price = s.getPric(produdtId, amount);
                if (price < FinalPrice) {
                    FinalPrice = price;
                    sup=s;
                }
            }
        }
        if(sup!=null) {
            Contract c = MapContract.getContract(sup.getID(), address);
            if (c != null) {
                InterfaceContract contract = new InterfaceContract(c.getSuplaier_ID(), c.isFixeDays(), c.getDayes(), c.isLeading(), c.getProductIDVendor_Name(), c.getItemsID_ItemsIDSupplier(), c.getProductIDVendor_Price());
                Wrotequantities worte = MapWorte.GetWrotequantities(sup.getID(), address);
                InterfaceWrotequantities w = null;
                if (worte != null) {
                    w = new InterfaceWrotequantities(worte.getSuplaier_ID(), worte.getItemsID_Amount(), worte.getItemsID_Assumption());
                }
                InterfaceSupplier s = new InterfaceSupplier(sup.getName(), sup.getID(),sup.getAddress(), sup.getBank(), sup.getBranch(), sup.getBankNumber(), sup.getPayments(), sup.getContactsID_Name(), sup.getContactsID_number(), contract, w);
                return s;
            }
        }
        return null;
        }

    public LinkedList<InterfaceOrder> GetOrderDetails() {
     LinkedList<InterfaceOrder> orders=new LinkedList<InterfaceOrder>();
     int today = LocalDate.now().getDayOfWeek().getValue()+1;
     LinkedList<Integer> ooo=MapOrder.GetOrdersId(today, address);
        for (int order: ooo
             ) {
            Order o=MapOrder.GetOrder(order, address);
            InterfaceOrder ord = new InterfaceOrder(o.getID_Vendor(), o.getID_Invitation(), o.getDay(), o.getOrderDate(), o.getArrivalTime(), o.getItemsID_ItemsIDVendor(), o.getItemsID_NumberOfItems(), o.getTotalPrice(), o.getStatus());
              orders.add(ord);
        }
    return orders;
    }

    public void Logout(){
        MapStore.UpdateStore(address,itemId,NumOfOrder);
 }
}
