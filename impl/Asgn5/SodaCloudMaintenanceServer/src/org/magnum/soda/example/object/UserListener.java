
package org.magnum.soda.example.object;

import org.magnum.soda.proxy.SodaAsync;

public interface UserListener {

	@SodaAsync
	public void userAdded(User u);
	
	@SodaAsync
	public void notifyFollowers(MaintenanceReport u);
	
}
