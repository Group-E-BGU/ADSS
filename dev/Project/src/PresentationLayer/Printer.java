package PresentationLayer;

import BusinessLayer.*;
import javafx.util.Pair;
import BusinessLayer.User.UserType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;

public class Printer {

    static BLService blService = BLService.getInstance();

    public static void printAllWorkingTypes() {
        int number = 1;
        for (WorkPolicy.WorkingType workingType : WorkPolicy.WorkingType.values()) {
            System.out.println(number + ") " + workingType.toString());
            number++;
        }
    }

    public static void printAllUserTypes() {
        int number = 1;
        for (User.UserType userType : User.UserType.values()) {
            System.out.println(number + ") " + userType.toString());
            number++;
        }
    }

    public static void printWorkingShifts(Worker w) {

        if (w.getWorker_shifts().isEmpty())
            System.out.println(w.getName() + " has no working shifts.");


        else for (Integer shift_id : w.getWorker_shifts()) {
            Shift shift = blService.getShift(shift_id);
            System.out.println(shift.toString());
        }
    }

    public static void printSchedule(int worker_id) {
        Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> worker_schedule = Workers.getInstance().getAllWorkers().get(worker_id).getSchedule();
        List<Pair<DayOfWeek, Shift.ShiftTime>> schedules_date = new LinkedList<>(worker_schedule.keySet());
        schedules_date.sort((o1, o2) -> {

            if ((o1.getKey().getValue() + 1) % 8 < (o2.getKey().getValue() + 1) % 8)
                return -1;

            if (o1.getKey().equals(o2.getKey())) {
                if (o1.getValue().equals(o2.getValue()))
                    return 0;
                if (o1.getValue() == Shift.ShiftTime.Morning)
                    return -1;
                return 1;
            }
            return 0;
        });
        for (Pair<DayOfWeek, Shift.ShiftTime> p : schedules_date) {
            System.out.println(p.getKey() + " , " + p.getValue().toString() + " : " + worker_schedule.get(p));
        }
    }

    public static void printMainMenu(Map<Integer, MenuOption> options) {

        //    initMenu(userType);

        int i = 1;
        for (MenuOption option : options.values()) {
            System.out.println(i + ") " + option.getOption_text());
            i++;
        }

 /*
        System.out.println("1) View Workers");
        System.out.println("2) View Shifts");
        System.out.println("3) View Deliveries");
        System.out.println("4) View Addresses");
        System.out.println("5) View Trucks");
        System.out.println("6) View Products");
        System.out.println("7) Add a new supplier");
        System.out.println("8) Add an agreement to supplier");
        System.out.println("9) Adding \"Quantity Write\" to supplier");
        System.out.println("10) Make a Fix order");
        System.out.println("11) Display the items in the super");
        System.out.println("12) Display sll the supplier's details");
        System.out.println("13) Update Order Status");
        System.out.println("14) Edit supplier details");
        System.out.println("15) Edit supplier's arrangement");
        System.out.println("16) Edit \"Write Quantities\" of supplier");
        System.out.println("17) Delete supplier");
        System.out.println("18) Change item amount");
        System.out.println("19) Move from storage to shelf");
        System.out.println("20) Subtract from shelf");
        System.out.println("21) Print inventory report");
        System.out.println("22) Enter defected item");
        System.out.println("23) Print defective report");
        System.out.println("24) Enter new discount");
        System.out.println("25) Enter new price");
        System.out.println("26) Update DetailsOrder");
        System.out.println("27) Check the Cheaper Supplier for specific product");
        System.out.println("28) Logout");
*/
    }

    public static Map<Integer, MenuOption> initMenu(UserType userType) {

        Map<Integer, MenuOption> allowed_options = new HashMap<>();

        for (Map.Entry<Integer, MenuOption> entry : InitializeData.menu_options.entrySet()) {
            if (userType.equals(UserType.Master) || Arrays.asList(entry.getValue().getAllowed_users()).contains(userType))
                allowed_options.put(entry.getKey(), entry.getValue());
        }

        return allowed_options;
    }

    public static void printWorkersView() {


    /*
        String workers_string = "";
        for (Worker worker : blService.getAllWorkers().values()) {
            workers_string = workers_string + worker.toString() + '\n';
        }
        if (workers_string.equals("")) {
            System.out.println(workers_string);

        } else {
            workers_string = workers_string.substring(0, workers_string.length() - 1);
            System.out.println(workers_string + '\n');
        }

    */


        if (blService.getAllWorkers().isEmpty()) {
            System.out.println("no Workers in the dataBase!!!!!");
        } else {
            List<String> headersList = Arrays.asList("ID", "NAME", "JOB");
            List<List<String>> rowsList = new LinkedList<>();

            for (Worker worker : blService.getAllWorkers().values()) {
                List<String> worker_details = Arrays.asList(String.valueOf(worker.getId()), worker.getName(), worker.getType().toString());
                rowsList.add(worker_details);
            }

            String tableString = objectToTableString(headersList, rowsList, 10);
            System.out.println(tableString);
        }


        System.out.println("1) Register a worker");
        System.out.println("2) select a worker");
        System.out.println("3) return\n");
    }

    public static void printShiftsView() {


        if (blService.getAllShifts().isEmpty()) {
            System.out.println("no shifts in the dataBase!!!!!");
        } else {

            for (Integer shift_id : blService.getAllShifts().keySet()) {
                printShift(shift_id);
            }

        }

    /*
        for (Shift shift : blService.getAllShifts().values()) {
            System.out.println(shift.toString());
            border();
        }
    */

        System.out.println("1) select a shift");
        System.out.println("2) create a shift");
        System.out.println("3) return");
    }

    public static void PrintWorkerView(int worker_id) {
        Worker w = blService.getWorker(worker_id);
        if (w == null) {
            System.out.println("Error : no worker with such id");
            return;
        }

        /*
        System.out.println("Worker name : " + w.getName());
        System.out.println("Worker id : " + w.getId());
        System.out.println("job : " + w.getType().toString() + "\n");

         */

        printWorkerTable(worker_id);

        System.out.println("1) Print Schedule");
        System.out.println("2) Print contract");
        System.out.println("3) Print working shifts");
        System.out.println("4) Edit worker info");
        System.out.println("5) Return");
    }

    public static void printWorkers(List<Integer> workers_ids) {

        for (Integer id : workers_ids) {
            Worker worker = blService.getWorker(id);
            if (worker == null) {
                System.out.println("Error while printing the worker with the id : " + id);
            } else {
                System.out.println("Worker name : " + worker.getName());
                System.out.println("Worker id : " + worker.getId());
                System.out.println("job : " + worker.getType().toString());
                if (worker.getType() == WorkPolicy.WorkingType.Driver) {
                    System.out.println("License : " + ((Driver) worker).getLicense() + '\n');
                } else {
                    System.out.println("\n");
                }

            }
        }

    }

    public static void printEditWorkerMenu() {
        System.out.println("1) Edit worker name");
        System.out.println("2) Edit worker id");
        System.out.println("3) Edit worker bank address");
        System.out.println("4) Edit worker salary");
        System.out.println("5) Edit schedule");
        System.out.println("6) Return");
    }
//------------------------------------ Shifts ---------------------------------//

    public static void printAvailableWorkers(int shift_id) {
        Shift shift = blService.getShift(shift_id);
        List<Integer> available_workers = blService.getAvailableWorkers(shift.getShiftDate(), shift.getShiftTime());
        printWorkers(available_workers);
    }

    public static void printShiftView(int shift_id) {
        //    Shift shift = blService.getShift(shift_id);
        printShift(shift_id);
        //    System.out.println(shift.toString());
        System.out.println("1) print available workers for this shift");
        System.out.println("2) return");
    }
//------------------------------------ Addresses ---------------------------------//

    public static void printAddressesView() {

        //printAllAddresses();
        printAddresses(new LinkedList<>(blService.getAllAddresses().keySet()));
        System.out.println("1) Add an address");
        System.out.println("2) Return");

    }

/*    public static void printAllAddresses() {
        Map<String, Address> addresses_map = blService.getAllAddresses();

        String addresses = "";

        for (Address address : addresses_map.values()) {
            addresses += "Location : " + address.getLocation() + "\n" +
                    "Contact Name : " + address.getContactName() + "\n" +
                    "Phone Number : " + address.getPhoneNumber() + "\n\n";
        }

        System.out.println(addresses);
    }

 */

//------------------------------------ Trucks ---------------------------------//

    public static void printTrucksView() {

        printAllTrucks();
        System.out.println("1) Add a truck");
        System.out.println("2) Return");
    }


    public static void printAllTrucks() {

        if (blService.getAllTrucks().isEmpty()) {
            System.out.println("No trucks in the database");
            return;
        }

        List<String> headersList = Arrays.asList("SERIAL NUMBER", "MODEL", "WEIGHT", "MAX ALLOWED WEIGHT");
        List<List<String>> rowsList = new LinkedList<>();

        for (Truck truck : blService.getAllTrucks().values()) {
            List<String> truck_details = Arrays.asList(truck.getSerialNumber(), truck.getModel(), String.valueOf(truck.getWeight()), String.valueOf(truck.getMaxAllowedWeight()));
            rowsList.add(truck_details);
        }

        String tableString = objectToTableString(headersList, rowsList, 10);
        System.out.println(tableString);

    }


    public static void printTrucks(List<String> available_trucks) {

        String trucks = "";
        for (String truck_serial : available_trucks) {
            Truck truck = blService.getTruck(truck_serial);
            if (truck == null) {
                System.out.println("Error in printing a truck with serial number : " + truck_serial + '\n');
            } else {
                trucks += "Truck serial number : " + truck_serial + "\n" +
                        "Model : " + truck.getModel() + "\n" +
                        "Weight : " + truck.getWeight() + "\n" +
                        "Max allowed weight : " + truck.getMaxAllowedWeight() + "\n\n";
            }

        }

        System.out.println(trucks);
    }

//------------------------------------ Products ---------------------------------//

    public static void printProductsView() {

        printAllProducts();
        System.out.println("1) Add a product");
        System.out.println("2) Return");

    }

    public static void printAllProducts() {
        Map<String, Product> products_map = blService.getAllProducts();

        String products = "";

        for (Product product : products_map.values()) {
            products += "Product Name : " + product.getName() + "\n" +
                    "CN : " + product.getCN() + "\n" +
                    "Weight : " + product.getWeight() + "\n\n";
        }

        System.out.println(products);
    }

    public static void printDeliveriesView() {


        if (blService.getAllDeliveries().isEmpty()) {
            System.out.println("no deliveries in the dataBase!!!!!");
        } else {

            for (Delivery delivery : blService.getAllDeliveries().values()) {
                printDelivery(delivery.getDeliveryID());
            }

        }

        System.out.println("1) Arrange a delivery");
        System.out.println("2) select a delivery");
        System.out.println("3) Return\n");
    }

    public static void PrintDeliveryView(int delivery_id) {


        Delivery delivery = blService.getDelivery(delivery_id);
        if (delivery == null) {
            System.out.println("Error : no delivery with such id");
            return;
        }

        printDelivery(delivery_id);


        System.out.println("1) Print Documents");
        System.out.println("2) Return");

    }

    private static void printDelivery(int delivery_id) {

        Delivery delivery = blService.getDelivery(delivery_id);

        Map<Integer, String> tables_info = new HashMap<>();
        Map<Integer, List<String>> columns_names = new HashMap<>();
        Map<Integer, List<List<String>>> rows_data = new HashMap<>();

        int available_space = blService.getTruck(delivery.getTruckSerialNumber()).getMaxAllowedWeight() - delivery.getTruckWeight();

        String table_name = "Delivery #" + delivery.getDeliveryID();
        List<String> table_headers = Arrays.asList("DATE", "SOURCE", "AVAILABLE SPACE");
        List<List<String>> table_rows = Arrays.asList(
                Arrays.asList(new SimpleDateFormat("dd/MM/yyyy").format(delivery.getDate()) + "  " + delivery.getShiftTime(), delivery.getSource(), String.valueOf(available_space))
        );

        tables_info.put(0, table_name);
        columns_names.put(0, table_headers);
        rows_data.put(0, table_rows);


        String destinations_table_name = "DESTINATIONS";
        List<String> delivery_headers = Arrays.asList("ADDRESS", "CONTACT NAME", "PHONE NUMBER");
        List<List<String>> delivery_rows = new LinkedList<>();


        for (String destination : delivery.getDestinations()) {

            Address des = blService.getAddress(destination);
            delivery_rows.add(Arrays.asList(destination, des.getContactName(), des.getPhoneNumber()));
        }


        tables_info.put(1, destinations_table_name);
        columns_names.put(1, delivery_headers);
        rows_data.put(1, delivery_rows);


        System.out.println(objectToComplexTableString(tables_info, columns_names, rows_data));


    }


    public static void border() {
        System.out.println("--------------------------------");
    }

    public static void printDocuments(int delivery_id) {

        for (Document document : blService.getDelivery(delivery_id).getDocuments().values()) {
            Map<Integer, String> tables_info = new HashMap<>();
            Map<Integer, List<String>> columns_names = new HashMap<>();
            Map<Integer, List<List<String>>> rows_data = new HashMap<>();

            Delivery delivery = blService.getDelivery(delivery_id);
            int weight = blService.getDocProductsWeight(document.getDeliveryGoods());
            String table_name = "Document #" + document.getDocumentID();
            List<String> table_headers = Arrays.asList("SOURCE", "DESTINATION", "PRODUCTS WEIGHT");
            List<List<String>> table_rows = Arrays.asList(
                    Arrays.asList(delivery.getSource(), document.getDestination(), String.valueOf(weight))
            );

            tables_info.put(0, table_name);
            columns_names.put(0, table_headers);
            rows_data.put(0, table_rows);


            String products_title = "PRODUCTS";
            List<String> products_headers = Arrays.asList("NAME", "AMOUNT", "TOTAL WEIGHT");
            List<List<String>> products_rows = new LinkedList<>();

            for (Map.Entry<String, Integer> product_entry : document.getDeliveryGoods().entrySet()) {
                Product product = blService.getProduct(product_entry.getKey());
                String product_total_weight = String.valueOf(product_entry.getValue() * product.getWeight());
                products_rows.add(Arrays.asList(product.getName(), String.valueOf(product_entry.getValue()), product_total_weight));
            }

            if (products_rows.isEmpty()) {
                products_rows.add(Arrays.asList("", ""));
            }
            tables_info.put(1, products_title);
            columns_names.put(1, products_headers);
            rows_data.put(1, products_rows);

            System.out.println(objectToComplexTableString(tables_info, columns_names, rows_data));
        }

    }

    public static void printAddresses(List<String> available_addresses) {


        if (available_addresses == null || available_addresses.isEmpty()) {
            System.out.println("Error : didnt find any address!!!!!");
        } else {
            List<String> headersList = Arrays.asList("LOCATION", "CONTACT NAME", "PHONE NUMBER");
            List<List<String>> rowsList = new LinkedList<>();

            for (String location : available_addresses) {
                Address address = blService.getAddress(location);
                List<String> address_details = Arrays.asList(location, address.getContactName(), address.getPhoneNumber());
                rowsList.add(address_details);
            }

            String tableString = objectToTableString(headersList, rowsList, 10);
            System.out.println(tableString);
        }

    }


//------------------------------------ Table Prints ---------------------------------//

    private static void printWorkerTable(int worker_id) {
        Worker worker = blService.getWorker(worker_id);
        List<String> headersList = Arrays.asList("ID", "NAME", "JOB");
        List<List<String>> rowsList = new LinkedList<>();
        List<String> worker_details = Arrays.asList(String.valueOf(worker.getId()), worker.getName(), worker.getType().toString());
        rowsList.add(worker_details);
        String tableString = objectToTableString(headersList, rowsList, 10);
        System.out.println(tableString);
    }

    private static void printShift(int shift_id) {

        Shift shift = blService.getShift(shift_id);

        Map<Integer, String> tables_info = new HashMap<>();
        Map<Integer, List<String>> columns_names = new HashMap<>();
        Map<Integer, List<List<String>>> rows_data = new HashMap<>();


        String table_name = "SHIFT #" + shift.getShiftId();
        List<String> table_headers = Arrays.asList("ADDRESS", "DATE", "TIME", "BOSS");
        List<List<String>> table_rows = Arrays.asList(
                Arrays.asList(shift.getAddress().getLocation(), new SimpleDateFormat("dd/MM/yyyy").format(shift.getShiftDate()), shift.getShiftTime().toString(), shift.getBoss().getName())
        );

        tables_info.put(0, table_name);
        columns_names.put(0, table_headers);
        rows_data.put(0, table_rows);


        String working_name = "WORKING TEAM";
        List<String> working_headers = Arrays.asList("NAME", "JOB");
        List<List<String>> working_rows = new LinkedList<>();


        for (List<Integer> workers_id : shift.getWorkingTeam().values()) {
            for (Integer worker_id : workers_id) {
                Worker worker = blService.getWorker(worker_id);
                working_rows.add(Arrays.asList(worker.getName(), worker.getType().toString()));

            }
        }

        if (working_rows.isEmpty()) {
            working_rows.add(Arrays.asList("", ""));
        }

        tables_info.put(1, working_name);
        columns_names.put(1, working_headers);
        rows_data.put(1, working_rows);

        System.out.println(objectToComplexTableString(tables_info, columns_names, rows_data));


    }

    // Table printing functions

    private static String objectToTableString(List<String> columns_names, List<List<String>> rows_data,
                                              int additional_space) {


        if (rows_data == null || columns_names == null || rows_data.isEmpty() || rows_data.get(0).size() != columns_names.size())
            return "Error empty table !!!";


        Integer[] widths = new Integer[rows_data.get(0).size()];

        int k = 0;
        for (String column_name : columns_names) {
            widths[k] = column_name.length() + additional_space;

            k++;
        }


        for (List<String> row_data : rows_data) {
            int field_num = 0;
            for (String field : row_data) {
                if (field.length() > widths[field_num]) {
                    widths[field_num] = field.length();
                }

                field_num++;
            }
        }

        int sum = columns_names.size() + 1;

        for (Integer width : widths) {
            sum = sum + width;
        }


        Board board = new Board(sum);   // sum = width of all + number of columns + 1
        Table table = new Table(board, sum, columns_names, rows_data);

        List<Integer> colAlignList = new LinkedList<>();

        for (int i = 0; i < columns_names.size(); i++)
            colAlignList.add(Block.DATA_CENTER);

        table.setColAlignsList(colAlignList);
        List<Integer> colWidthsListEdited = Arrays.asList(widths);
        table.setGridMode(Table.GRID_FULL).setColWidthsList(colWidthsListEdited);
        Block tableBlock = table.tableToBlocks();
        board.setInitialBlock(tableBlock);
        board.build();
        return board.getPreview();

    }


    public static void printSuperItems() {

        List<String> headersList = Arrays.asList("PRODUCT NAME", "SupplierID", "id_supplier", "PRICE");
        List<List<String>> rowsList = new LinkedList<>();

        LinkedList<InterfaceContract> Contract = blService.GetContract();
        for (InterfaceContract Con : Contract
        ) {
            for (Map.Entry<Integer, String> e : Con.ProductIDVendor_Name.entrySet()) {
                int P = e.getKey();
                String N = e.getValue();
                for (Map.Entry<Integer, Double> entry : Con.productIDVendor_Price.entrySet()) {
                    int p = entry.getKey();
                    Double price = entry.getValue();

                    if (P == p) {
                        List<String> product_details = Arrays.asList(N, String.valueOf(Con.Suplaier_ID), String.valueOf(p), String.valueOf(price));
                        rowsList.add(product_details);

                    }
                }
            }
        }

        String tableString = objectToTableString(headersList, rowsList, 10);
        System.out.println(tableString);
    }

    public static void printAllSuppliers() {
       /* LinkedList<InterfaceSupplier> suppliers =blService.GetSupliers();
        for (InterfaceSupplier Sup : suppliers
        ) {
            System.out.print("\nname: " + Sup.Name + "\n" +
                    "Id: " + Sup.ID + "\n" +
                    "Payment with: " + Sup.Payments + "\n" +
                    "Bank: " + Sup.Bank + "\n" +
                    "Branch: " + Sup.Branch + "\n" +
                    "Bank number: " + Sup.BankNumber + "\n");
            System.out.println("Contacts:");
            for (Map.Entry<Integer,String> i:Sup.ContactsID_Name.entrySet()
            ) {
                System.out.println("name: " + i.getValue());
                System.out.println("ID: " + i.getKey());
                for (Map.Entry<Integer,Integer> e:Sup.ContactsID_number.entrySet()
                ) {
                    if (i.getKey().intValue()==e.getKey().intValue()) {
                        System.out.println("number: " + e.getValue() + "\n");
                    }
                }
            }
        }

        */

        LinkedList<InterfaceSupplier> suppliers = blService.GetSupliers();
        for (InterfaceSupplier Sup : suppliers
        ) {

            Map<Integer, String> tables_info = new HashMap<>();
            Map<Integer, List<String>> columns_names = new HashMap<>();
            Map<Integer, List<List<String>>> rows_data = new HashMap<>();


            String table_name = "Supplier ID # " + Sup.ID;

            List<String> table_headers = Arrays.asList("NAME", "Address", "BANK", "BRANCH", "BANK NUMBER");
            List<List<String>> table_rows = Arrays.asList(
                    Arrays.asList(Sup.Name, Sup.Address, Sup.Bank, Sup.Branch, String.valueOf(Sup.BankNumber))
            );

            tables_info.put(0, table_name);
            columns_names.put(0, table_headers);
            rows_data.put(0, table_rows);


            String contact_title = "CONTACTS";
            List<String> contact_headers = Arrays.asList("ID", "NAME", "NUMBER");
            List<List<String>> contact_rows = new LinkedList<>();

            for (Map.Entry<Integer, String> i : Sup.ContactsID_Name.entrySet()
            ) {
                for (Map.Entry<Integer, Integer> e : Sup.ContactsID_number.entrySet()
                ) {
                    if (i.getKey().intValue() == e.getKey().intValue()) {
                        contact_rows.add(Arrays.asList(String.valueOf(i.getKey()), i.getValue(), String.valueOf(e.getValue())));
                    }
                }
            }

            if (contact_rows.isEmpty()) {
                contact_rows.add(Arrays.asList("", ""));
            }
            tables_info.put(1, contact_title);
            columns_names.put(1, contact_headers);
            rows_data.put(1, contact_rows);

            System.out.println(objectToComplexTableString(tables_info, columns_names, rows_data));

        }


    }

    private static String objectToComplexTableString
            (Map<Integer, String> tables_info, Map<Integer, List<String>> columns_names, Map<Integer, List<List<String>>> rows_data) {

// tables_info <tableNumber,tableName>
// columns_names <tableNumber , tableColumnsNames>
// rows_data <tableNumber, tableRows>

        if (rows_data == null || columns_names == null || rows_data.isEmpty())
            throw new IllegalArgumentException();


        Map<Integer, Integer[]> widths = new HashMap<>();

        // we need now to fill for each table its width , first we assume that the column name is the longest


        for (Map.Entry<Integer, List<String>> table : columns_names.entrySet()) {
            int table_number = table.getKey();
            widths.put(table_number, new Integer[columns_names.get(table_number).size()]);

            int k = 0;
            for (String column_name : columns_names.get(table_number)) {
                widths.get(table_number)[k] = column_name.length();

                k++;
            }
        }

        // we need now to actually find the appropriate length for each column for each table

        for (Map.Entry<Integer, List<List<String>>> table : rows_data.entrySet()) {
            int table_number = table.getKey();
            for (List<String> row_data : rows_data.get(table_number)) {
                int field_num = 0;
                for (String field : row_data) {
                    if (field.length() > widths.get(table_number)[field_num]) {
                        widths.get(table_number)[field_num] = field.length();
                    }

                    field_num++;
                }
            }
        }


        // now instead of having one sum which represents the width of a single table , we will need a sum array
        // because we have multiple tables

        Integer[] sums = new Integer[tables_info.keySet().size()];

        for (Map.Entry<Integer, List<String>> table : columns_names.entrySet()) {
            int table_number = table.getKey();
            sums[table_number] = table.getValue().size() + 1;

        }


        // add the width for each table to the sum
        //we also need to check an extreme case , is the table name longer than the total width of its columns ?


        for (Map.Entry<Integer, Integer[]> table_width : widths.entrySet()) {

            int total_width = 0;
            for (Integer column_width : table_width.getValue()) {
                total_width = total_width + column_width;
            }

            if (total_width >= tables_info.get(table_width.getKey()).length())
                sums[table_width.getKey()] = sums[table_width.getKey()] + total_width;
            else
                sums[table_width.getKey()] = sums[table_width.getKey()] + tables_info.get(table_width.getKey()).length();
        }

        // now we chose the biggest width in all tables to determinate the length for the rest

        int biggest_sum = Collections.max(Arrays.asList(sums));

        // create the board

        Board board = new Board(biggest_sum);   // sum = width of all + number of columns + 1


        List<Integer> tmp = new LinkedList<>();

        // now we need to create the tables with this width

        for (Integer table_number : tables_info.keySet()) {


            List<String> columns = columns_names.get(table_number);
            List<List<String>> rows = rows_data.get(table_number);

            List<Integer> columns_widths = new LinkedList<>();
            int total_width = 0;
            for (Integer width : widths.get(table_number)) {
                columns_widths.add(width);
                total_width = total_width + width;
            }

            int desired = total_width + columns.size() + 1;

            // if less then distribute the empty space between the columns
            if (desired < biggest_sum) {
                int result = (biggest_sum - desired) / columns_names.get(table_number).size();
                for (int i = 0; i < columns_widths.size(); i++) {
                    columns_widths.set(i, columns_widths.get(i) + result);
                }

                int remains = biggest_sum - (desired + result * columns_names.get(table_number).size());
                while (remains != 0) {
                    int min_width = Collections.min(columns_widths);
                    columns_widths.set(columns_widths.indexOf(min_width), min_width + 1);
                    remains--;
                }

            }

            List<Integer> colAlignList = new LinkedList<>();

            for (int i = 0; i < columns_names.get(table_number).size(); i++)
                colAlignList.add(Block.DATA_CENTER);


            Table table = new Table(board, biggest_sum, columns, rows, columns_widths);
            table.setGridMode(Table.GRID_FULL);
            table.setColAlignsList(colAlignList);


            int get_index = 0, append_index = 0;


            // create block from table
            Block block = new Block(board, biggest_sum - 2, 1, tables_info.get(table_number)).setDataAlign(Block.DATA_CENTER);

            append_index = block.getIndex();
            get_index = append_index + columns.size() + 1;

            tmp.add(get_index);

            if (table_number == 0) {
                board.setInitialBlock(block);
            } else {
                int z = tmp.remove(0);
                board.getBlock(z).setBelowBlock(block);

            }

            board.appendTableTo(append_index, Board.APPEND_BELOW, table);


        }

        return board.invalidate().build().getPreview();


    }

    public static void printWarnings(UserType userType) {

        List<String> user_warnings = blService.getWarnings(userType);

        if (!user_warnings.isEmpty()) {
            for (String warning_string : user_warnings) {
                System.out.println(warning_string);
            }
        }

    }
}
