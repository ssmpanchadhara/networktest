import java.util.*;
import java.util.Scanner;

class Device{
	public String type;
	public int Strength;
	public String Name;
	public boolean visited;
	Device(String type, String Name){
		this.type = type;
		this.Name = Name;
		this.Strength = 5;
		this.visited = false;
	}
	public void setStrength(int Strength){
		this.Strength = Strength;
	}
	public void addBlockedDevice(String Name){

	}
}

class Network{
	public HashMap<String,ArrayList<Device>> Connections;
	public HashMap<String,Device> Devices;
	public ArrayList<String> deviceTypes;

	Network(ArrayList deviceTypes){
		this.deviceTypes = new ArrayList<String>();
		this.deviceTypes = deviceTypes;//computer,repeater
		this.Connections = new HashMap<String,ArrayList<Device>>();
		this.Devices = new HashMap<String,Device>();
	}

	public Boolean checkDuplicateName(String Name){
		if(Devices.containsKey(Name))
			return true;
		return false;
	}

	public String addDevice(String type,String Name){
		if(!deviceTypes.contains(type)){
			return "Error: Invalid command syntax.";
		}
		if(checkDuplicateName(Name)){
			return "Error: That name already exists.";
		}
		Device d = new Device(type,Name);
		Devices.put(Name,d);
		Connections.put(d.Name,new ArrayList<Device>());
		return "Successfully added "+Name;
	}

	public String setStrength(String Name, String Strength){
		if(!Devices.containsKey(Name)){
			return "Error: Device doesnot exist.";
		}
		Device d = Devices.get(Name);
		int strength;
		if(d.type.equals("Repeater")){
			return "Error: Cannot add Strength to repeater."; 
		}
		try{ 
            strength = Integer.parseInt(Strength); 
		}  
        catch (NumberFormatException e){ 
            return "Error: Invalid command syntax."; 
        } 
		d.setStrength(strength);
		return "Successfully defined strength."; 
	}

	public String connectDevices(String d1,String d2){
		if(d1.equals(d2)){
			return "Error: Cannot connect device to itself.";
		}
		if(!Devices.containsKey(d1) || !Devices.containsKey(d2)){
			return "Error: Device doesnot exist.";
		}
		ArrayList<Device> connected2d1 = Connections.get(d1);
		ArrayList<Device> connected2d2 = Connections.get(d2);
		for(int i=0;i<connected2d1.size();i++){
			if(connected2d1.get(i).Name.equals(d2)){
				return "Error: Devices are already connected.";
			}
		}
		connected2d1.add(Devices.get(d2));
		connected2d2.add(Devices.get(d1));
		return "Successfully connected.";
	}
	public String getRoute(String d1,String d2){
		if(!Devices.containsKey(d1) || !Devices.containsKey(d2)){
			return "Error: Device doesnot exist.";
		}
		Device device1 = Devices.get(d1);
		Device device2 = Devices.get(d2);
		if(device1.type.equals("REPEATER") || device2.type.equals("REPEATER")){
			return "Error: Route cannot be calculated with a repeater.";
		}
		String route = constructRoute(d1,d2,"",device1.Strength);
		for (String name : Devices.keySet()){
            Device d = Devices.get(name);
			d.visited = false;
		}
          
		if(route.length()>0){
			return route;
		}
		return "Error: Route not found!";
	}

	public String constructRoute(String source, String destination, String route, Integer Strength){
		Device device1 = Devices.get(source);
		Device device2 = Devices.get(destination);
		if(device1.visited){
			if(source.equals(destination)){
				return destination;
			}
			return "";
		}
		if(Strength <= 0){
			if(source.equals(destination)){
				return destination;
			}
			return "";
		}
		if(source.equals(destination)){
			return destination;
		}
		ArrayList<String> node = new ArrayList<String>();
		for(int i=0;i<Connections.get(source).size();i++){
			if(Connections.get(source).get(i).type.equals("REPEATER")){
				Strength = Strength*2;
			}
			else{
				Strength = Strength - 1;
			}
			node.add(constructRoute(Connections.get(source).get(i).Name,destination,"",Strength));
			Connections.get(source).get(i).visited = true;
		}
		for(int i=0;i<node.size();i++){
		    if(node.get(i).indexOf(destination)>=0){
		        return source+"->"+node.get(i);
		    }
		}
		return "";
	}
}

public class MyClass{
	public static void main(String[] args){
		System.out.println("To Create a Network, enter Device Types Used in the Network as csv's"); 
		ArrayList<String> deviceTypes = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		String ip = sc.nextLine();
		for(int i=0;i<ip.split(",").length;i++){
			deviceTypes.add(ip.split(",")[i]);
		}
		Network mynetwork = new Network(deviceTypes);
		String repeat = "y";
		while(repeat.equals("y")){
			String command = sc.nextLine();
			System.out.print(command+" : ");
			String[] commandArgs = command.split(" ");	
			switch(commandArgs[0]) {
  				case "ADD":
    				if(commandArgs.length<3){
						System.out.println("Error: Enter Appropriate Command");
						break;
					}
					System.out.println(mynetwork.addDevice(commandArgs[1],commandArgs[2]));
    				break; 

  				case "SET_DEVICE_STRENGTH":
    				if(commandArgs.length<3){
						System.out.println("Error: Enter Appropriate Command");
						break;
					}
					System.out.println(mynetwork.setStrength(commandArgs[1],commandArgs[2]));
    				break;
				case "CONNECT":
    				if(commandArgs.length<3){
						System.out.println("Error: Enter Appropriate Command");
						break;
					}
					System.out.println(mynetwork.connectDevices(commandArgs[1],commandArgs[2]));
    					break;
				case "INFO_ROUTE":
    				if(commandArgs.length<3){
						System.out.println("Error: Enter Appropriate Command");
						break;
					}
					System.out.println(mynetwork.getRoute(commandArgs[1],commandArgs[2]));
    					break;
  				default:
    					break;
			}
			if(sc.nextLine().equals("y")){
				repeat = "n";
			}
		}
	}
}
