package org.magnum.soda.example.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.magnum.soda.Callback;
import org.magnum.soda.Soda;
import org.magnum.soda.SodaQuery;
import org.magnum.soda.ctx.SodaQR;
import org.magnum.soda.example.maint.MaintenanceServer;
import org.magnum.soda.proxy.SodaAsync;


public class MaintenanceReportsImpl implements MaintenanceReports {
	
	private List<MaintenanceListener> listeners_ = new LinkedList<MaintenanceListener>();
	private List<MaintenanceReport> reports_ = new LinkedList<MaintenanceReport>();

	private Map<String, List<String>> followers_ = new HashMap<String, List<String>>();
	private Map<String, List<UserListener>> followerlisteners_ = new HashMap<String, List<UserListener>>();

	@Override
	@SodaAsync
	public void addReport(MaintenanceReport r) {

		System.out.println("add :" + r.getContents() + " :"
				+ r.getCreatorId());
		reports_.add(r);

		for (MaintenanceListener l : listeners_) {
			l.reportAdded(r);
		}
	}

	@Override
	@SodaAsync
	public void modifyReport(MaintenanceReport r) {

		boolean success = false;
		System.out.println("modify :" + r.getContents() + " :"
				+ r.getCreatorId());

		Iterator<MaintenanceReport> it = reports_.iterator();
		while (it.hasNext()) {
			MaintenanceReport temp = it.next();
			if (temp.getId().equals(r.getId())) {
				temp.setImageData(r.getImageData());
				temp.setContents(r.getContents());
				success = true;
				break;
			}
		}
		if (success) {
			for (MaintenanceListener l : listeners_) {
				l.reportchanged(r);

			}
			if(followerlisteners_.containsKey(r.getId()))
			{
				for (UserListener l : followerlisteners_.get(r.getId())) {
					l.notifyFollowers(r);
				}
			}
		}
	}
	
	@Override
	@SodaAsync
	public void deleteReport(String id) {
		
		Iterator<MaintenanceReport> it = reports_.iterator();
		while (it.hasNext()) {
			MaintenanceReport r = it.next();
			if (r.getId().equals(id)) {
				System.out.println("find Report to delete");
				it.remove();
				break;
			}
		}
		
		if(followerlisteners_.containsKey(id))
		{
			for (UserListener l : followerlisteners_.get(id)) {
				//l.notifyFollowers(r);
			}
			followerlisteners_.remove(id);		
		}
	}

	public void bindQRContext(Soda s, MaintenanceReport r) {
		SodaQR qr = SodaQR.create(r.getContents());
		s.bind(r).to(qr);
	}

	@Override
	@SodaAsync
	public void getReports(Callback<List<MaintenanceReport>> callback) {
		callback.handle(reports_);
	}

	@Override
	public List<MaintenanceReport> getReports() {
		return reports_;
	}

	@Override
	public void addListener(MaintenanceListener l) {
		listeners_.add(l);
	}

	@Override
	public void removeListener(MaintenanceListener l) {
		listeners_.remove(l);
	}

	@Override
	@SodaAsync
	public void getReports(Callback<List<MaintenanceReport>> callback, Soda s,
			byte[] b) {

		SodaQR _objQR = SodaQR.fromImageData(b);
		SodaQuery<MaintenanceReport> _objSQ = s.find(MaintenanceReport.class,
				_objQR);

		callback.handle(_objSQ.getList_());

	}

	@Override
	@SodaAsync
	public void getReports(String username,
			Callback<List<MaintenanceReport>> callback) {

		List<MaintenanceReport> queryresult = new LinkedList<MaintenanceReport>();
		
		for(MaintenanceReport r:reports_){
			if(r.getCreatorId().equals(username)) {
				queryresult.add(r);
			}
		}

		callback.handle(queryresult);

	}
	@Override
	public MaintenanceReport getReportsById(String id) {
				
		for(MaintenanceReport r:reports_){
			if(r.getId().equals(id))
				return r;
		}
		return null;
	}
	
	@Override
	public void addFollower(MaintenanceReport r, String u) {

		List<String> lusr = null;
		if (followers_.containsKey(r.getId())) {
			lusr = followers_.get(r.getId());
		} else {
			lusr = new ArrayList<String>();
		}
		lusr.add(u);
		followers_.put(r.getId(), lusr);
		if (followerlisteners_.containsKey(r.getId())) {
			for (UserListener l : followerlisteners_.get(r.getId())) {
				l.notifyFollowers(r);
			}
		}
	}

	@Override
	public void removeFollower(MaintenanceReport r, String u) {
		
		followers_.remove(r.getId());
		if(followers_.containsKey(r.getId()))
		{
			List<String> lusr=followers_.get(r.getId());
			if(lusr.contains(u))
				lusr.remove(u);
		}
		if (followerlisteners_.containsKey(r.getId())) {
			for (UserListener l : followerlisteners_.get(r.getId())) {
				l.notifyFollowers(r);
			}
		}
	}

	@Override
	public void addFollowerListener(String id, UserListener l) {

		List<UserListener> lusr = null;
		if (followerlisteners_.containsKey(id)) {
			lusr = followerlisteners_.get(id);
		} else {
			lusr = new ArrayList<UserListener>();
		}
		lusr.add(l);
		followerlisteners_.put(id, lusr);

	}

	@Override
	public void removeFollowerListener(String id, UserListener l) {
		if (followerlisteners_.containsKey(id)) {
			List<UserListener> lu = followerlisteners_.get(id);
			//Iterator itr=lu.iterator();
			if (lu.contains(l))
				lu.remove(l);
			
		}
	}

	@Override
	public List<String> getFollowers(String r) {
		if(followers_.containsKey(r))
			return followers_.get(r);
		
		return null;
	}

	@Override
	public List<MaintenanceReport> getFollowing(String user) {
		System.out.println("inside getfollowing.");
		List<MaintenanceReport> result=new ArrayList<MaintenanceReport>();
		Iterator<String> itr=followers_.keySet().iterator();
		while(itr.hasNext())
		{
			String i=itr.next();
			System.out.println("UUID:" + i);
			Iterator<String> users=followers_.get(i).iterator();
			while(users.hasNext())
			{	
				if(users.next().equals(user)){
					System.out.println("User:" + user);
					result.add(getReportsById(i));
				}
				
			}
		}
				
		return null;
	}


}
