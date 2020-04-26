import java.util.Scanner;
import BusinessLayer.BLObjects.*;

public class main {

    public static void main(String[] args) {
        
    	Scanner scan=new Scanner(System.in);
    	int selected=0;
    	String name="";
    	String id="";
    	String license="";
    	Data data=new Data();
    	//get the user's choice
    	while(selected !=8) {
    		menu();
    		selected=scan.nextInt();  
    		switch(selected) {
    		
    		//print drivers data
    		case(4):
    			System.out.println("Enter driver name: ");
    			name=scan.next();
    			System.out.println("Enter driver id: ");
    			id=scan.next();
    			System.out.println("Enter driver license: ");
    			license=scan.next();
    			Driver driver=new Driver(name,id,license);
    			data.addDriver(driver);
    			System.out.println("Done ");
    			break;
    			
    			//add address
    		case(5):
    			System.out.println("Enter you'r location: ");
				name=scan.next();
				Location location=new Location(name);
				System.out.println("Enter contactname: ");
				String contactname=scan.next();
				System.out.println("Enter phoneNumber: ");
				String phonenumber=scan.next();
				Adress address=new Adress(location,contactname,phonenumber); 
				data.addAdress(address);
				System.out.println("Done ");
    			break;
    			
    			//add truck
    		case(6):
    			System.out.println("Enter serialNumber: ");
				String serialNumber =scan.next();
				System.out.println("Enter model: ");
				String model =scan.next();
				System.out.println("Enter weight: ");
				int weight =scan.nextInt();
				System.out.println("Enter max Allowed Weight: ");
				int maxAllowedWeight =scan.nextInt();
				Truck truck=new Truck(serialNumber,model,weight,maxAllowedWeight);
				data.addTruck(truck);
				System.out.println("Done ");
    			break;
    			
    			//add product
    		case(7):
    			System.out.println("Enter name: ");
				String name1 =scan.next();
				System.out.println("Enter CN : ");
				String CN =scan.next();
				System.out.println("Enter weight: ");
				int weight1 =scan.nextInt();
				System.out.println("Enter amount: ");
				int amount =scan.nextInt();
				System.out.println("Enter Address detailes: ");
				System.out.println("Enter you'r location: ");
				name=scan.next();
				Location location1=new Location(name);
				System.out.println("Enter contactname: ");
				String contactname1=scan.next();
				System.out.println("Enter phoneNumber: ");
				String phonenumber1=scan.next();
				Adress address1=new Adress(location1,contactname1,phonenumber1); 
				Product product=new Product(name1,CN,weight1,amount);
				data.addProduct(product, address1.getLocation().getLocation());
				System.out.println("Done ");
    			break;
    			
    			
    			//get drivers data
    		case(1):
    		        for(Driver d : data.getDrivers()){
    		        	System.out.println("Driver name : " +d.getName());
    		        	System.out.println("Drivers ID : " +d.getId());
    		        	System.out.println("Available : " +d.isAvailable());
    		        	System.out.println("License : " +d.getLicense());
    		        } 
    			break;
    			
    			//get trucks data
    		case(2):
    			for(Truck t : data.getTrucks()) {
    				
    				System.out.println("Truck serial number : " +t.getSerialNumber());
		        	System.out.println("Model : " + t.getModel() );
		        	System.out.println("Weight : "  + t.getWeight());
		        	System.out.println("Max allowed weight : "+t.getMaxAllowedWeight());
    	        }
    			
    			break;
    			
    			
    			//get addresses data
    		case(3):
    			for(Adress a : data.getAdresses()) {
    				
    				System.out.println("Adress's location : " + a.getLocation().getLocation());
		        	System.out.println("Contact name : "+ a.getContactName() );
		        	System.out.println( "Phone number : " + a.getPhoneNumber());
    	        }
    			break;
    			
    			//Exit
    		case(8):
    			break;
    		
    		
    			default:
    				break;
    		
    		}
    	}
    }
    	
    private static void menu() {
    	 System.out.println("choose Section :");
         System.out.println("1)printDriversData");
         System.out.println("2)printTrucksData");
         System.out.println("3)printAdresses");
         System.out.println("4)addDriver");
         System.out.println("5)addAdress");
         System.out.println("6)addTruck");
         System.out.println("7)addProduct");
         System.out.println("8)Exit");
    }
}
