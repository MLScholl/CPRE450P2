import AuctionApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;

public class AuctionServer {
	public static void main(String args[]) {
	// create and initialize the ORB
	ORB orb = ORB.init(args, null);
	// get reference to rootPOA & activate the POAManager
	POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
	rootpoa.the_POAManager().activate();
	// create servant and register it with the ORB
	AuctionImpl auctionImpl = new AuctionImpl();
	auctionImpl.setORB(orb);
	// get object reference from the servant
	org.omg.CORBA.Object ref = rootpoa.servant_to_reference(auctionImpl);
	Auction href = AuctionHelper.narrow(ref);
	// get the root naming context. NameService invokes the name service
	org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
	// Use NamingContextExt which is part of the Interoperable
	// Naming Service (INS) specification.
	NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	// bind the Object Reference in Naming
	String name = "Hello";
	NameComponent path[] = ncRef.to_name( name );
	ncRef.rebind(path, href);
	System.out.println("AuctionServer ready and waiting ...");
	// wait for invocations from clients
	orb.run();
	}
}
