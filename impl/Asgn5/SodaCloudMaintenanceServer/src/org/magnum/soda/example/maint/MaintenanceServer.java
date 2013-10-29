
package org.magnum.soda.example.maint;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.magnum.soda.Soda;
import org.magnum.soda.example.object.MaintenanceReport;
import org.magnum.soda.example.object.MaintenanceReports;
import org.magnum.soda.example.object.MaintenanceReportsImpl;
import org.magnum.soda.example.object.User;
import org.magnum.soda.example.object.Users;
import org.magnum.soda.example.object.UsersImpl;
import org.magnum.soda.protocol.java.NativeJavaProtocol;
import org.magnum.soda.server.wamp.ServerSodaLauncher;
import org.magnum.soda.server.wamp.ServerSodaListener;

public class MaintenanceServer implements ServerSodaListener {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerSodaLauncher launcher = new ServerSodaLauncher();
		launcher.launch(new NativeJavaProtocol(), 8081, new MaintenanceServer());

	}

	@Override
	public void started(Soda soda) {
		User u1 = new User();
		u1.setUsername_("alice@gmail.com");
		u1.setPwd_("aaaaa");
		u1.setName_("Alice");
		u1.setPhone_("540-200-1234");
		User u2 = new User();
		u2.setUsername_("bob@gmail.com");
		u2.setPwd_("bbbbb");
		u2.setName_("Bob");
		u2.setPhone_("540-200-5678");
		Users userManager = new UsersImpl();
		userManager.addUser(u1);
		userManager.addUser(u2);
		soda.bind(userManager, Users.SVC_NAME);
		
		
		MaintenanceReports reports = new MaintenanceReportsImpl();

		MaintenanceReport t1= new MaintenanceReport();
		t1.setId(UUID.randomUUID().toString());
		t1.setTitle("Machine#123 went down.");
		t1.setContents("It doesn't work.");
		t1.setCreatorId("alice@gmail.com");
		Calendar cal = Calendar.getInstance();
    	Date createTime = cal.getTime();
    	t1.setCreateTime_(createTime);
    	reports.addReport(t1);
		
		MaintenanceReport t2= new MaintenanceReport();
		t2.setId(UUID.randomUUID().toString());
		t2.setTitle("Replace the battery.");
		t2.setContents("The battery is replaced.");
		t2.setCreatorId("bob@gmail.com");
    	t2.setCreateTime_(createTime);
    	reports.addReport(t2);
    	
    	reports.addFollower(t1, u2.getUsername_());
    	reports.addFollower(t2, u1.getUsername_());
    	
		soda.bind(reports, MaintenanceReports.SVC_NAME);
		
		
		


	}

}
