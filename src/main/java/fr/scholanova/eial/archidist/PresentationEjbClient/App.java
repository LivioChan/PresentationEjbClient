package fr.scholanova.eial.archidist.PresentationEjbClient;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import fr.scholanova.eial.archidist.PresentationEjbInterface.CalculInterface;
import fr.scholanova.eial.archidist.PresentationEjbInterface.IEjbTestEtat;
import fr.scholanova.eial.archidist.PresentationEjbInterface.MangerInterface;
import fr.scholanova.eial.archidist.PresentationEjbInterface.Repas;

/**
 * @author Livio
 *
 */
public class App {
	
	public static void main( String[] args )
    {
		Properties props = new Properties();
		props.put(Context.PROVIDER_URL, "http-remoting://127.0.0.1:8080");
		props.put(Context.SECURITY_PRINCIPAL, "admin");
		props.put(Context.SECURITY_CREDENTIALS, "admin");
		props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		props.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.remote.client.InitialContextFactory");
		props.put("jboss.naming.client.ejb.contex", true);
		try {
			Context ctx = new InitialContext(props);
			CalculInterface proxy = (CalculInterface) ctx.lookup("PresentationEjbEAR-1.0/PresentationEjb-1.0/Calcul!fr.scholanova.eial.archidist.PresentationEjbInterface.CalculInterface");
			System.out.println(proxy.ajouter(3, 5));
			System.out.println(proxy.puissance(2, 3));
			MangerInterface proxy2 = (MangerInterface) ctx.lookup("PresentationEjbEAR-1.0/PresentationEjb-1.0/Manger!fr.scholanova.eial.archidist.PresentationEjbInterface.MangerInterface");
			Repas repas = proxy2.mangerDehors();
			System.out.println(repas.getNom() + " " +repas.getPrix() + " " +  repas.getDehors());
			

			
			// Test Ejb
			IEjbTestEtat proxySingleton = (IEjbTestEtat) ctx.lookup("PresentationEjbEAR-1.0/PresentationEjb-1.0/EjbSingleton!fr.scholanova.eial.archidist.PresentationEjbInterface.IEjbTestEtat");
			IEjbTestEtat proxyStateless = (IEjbTestEtat) ctx.lookup("PresentationEjbEAR-1.0/PresentationEjb-1.0/EjbStateless!fr.scholanova.eial.archidist.PresentationEjbInterface.IEjbTestEtat");
			IEjbTestEtat proxyStatefull = (IEjbTestEtat) ctx.lookup("PresentationEjbEAR-1.0/PresentationEjb-1.0/EjbStatefull!fr.scholanova.eial.archidist.PresentationEjbInterface.IEjbTestEtat");
			
			for( int i = 0 ; i < 10000 ;i++) {
				proxySingleton.increment();
				proxyStatefull.increment();
			}
			
			Thread t1 = new Thread(() ->  { 
				for(int i=0;i<10000;i++) {proxyStateless.increment();}
			});
			
			Thread t2= new Thread(() ->  { 
				for(int i=0;i<10000;i++) {proxyStateless.increment();}
			});
			
			Thread t3 = new Thread(() ->  { 
				for(int i=0;i<10000;i++) {proxyStateless.increment();}
			});
			
			Thread t4 = new Thread(() ->  { 
				for(int i=0;i<10000;i++) {proxyStateless.increment();}
			});
			
			t1.start();
			t2.start();
			t3.start();
			t4.start();
			Thread.sleep(10000);
			System.out.println("Statefull : " + proxyStatefull.getNb());
			System.out.println("Stateless : " + proxyStateless.getNb());
			System.out.println("Singleton : " + proxySingleton.getNb());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
