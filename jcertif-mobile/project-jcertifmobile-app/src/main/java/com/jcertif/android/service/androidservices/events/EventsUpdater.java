/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.service.androidservices.events</li>
 * <li>30 mai 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * <li>Produit par MSE.</li>
 */
package com.jcertif.android.service.androidservices.events;

import java.util.List;

import android.os.Handler;
import android.util.Log;

import com.jcertif.android.JCApplication;
import com.jcertif.android.R;
import com.jcertif.android.com.net.RestClient;
import com.jcertif.android.com.net.RestClient.RequestMethod;
import com.jcertif.android.com.parsing.jackson.service.EventsController;
import com.jcertif.android.dao.ormlight.EventProvider;
import com.jcertif.android.service.androidservices.UpdaterService;
import com.jcertif.android.service.androidservices.UpdaterServiceElementIntf;
import com.jcertif.android.transverse.model.Event;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class EventsUpdater implements UpdaterServiceElementIntf {
	/**
	 * The updaterService parent
	 */
	Handler parent;
	

	/**
	 * @param parent
	 */
	public EventsUpdater(Handler parent) {
		super();
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.service.androidservices.UpdaterServiceElementIntf#onUpdate()
	 */
	@Override
	public void onUpdate() {
		Log.w("onUpdate Events", "onUpdate Events called");
		// Demander la liste des speakers (chaque speaker est complet)
		try {
			String speakersJson = getEventsList();
			// reconstruire les données
			EventsController evc = new EventsController(speakersJson);
			Log.i("onUpdate", speakersJson);
			evc.init();
			List<Event> events = evc.findAll();
			// Enregistrer chacun en BD
			EventProvider evp = new EventProvider();
			evp.deleteAllAndSaveAllEvents(events);			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//The threatment is over, callBack the parent to tell it
			parent.sendEmptyMessage(UpdaterService.TREATMENT_OVER);
			Log.w("onUpdate Events", "onUpdate Events finished");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.service.androidservices.UpdaterServiceElementIntf#getName()
	 */
	@Override
	public String getName() {
		return JCApplication.getInstance().getApplicationContext().getString(R.string.eventUpdater);
	}

	/**
	 * @return the Http response of the web services
	 * @throws Exception
	 */
	private String getEventsList() throws Exception {
		String responseString = null;
		String url = JCApplication.getInstance().getUrlFactory().getEventUrl();

		RestClient client = new RestClient(url);
		try {
			client.Execute(RequestMethod.GET);
			responseString = client.getResponse();
			Log.i(this.getClass().getSimpleName(), responseString);
		} catch (Exception e) {
			Log.d(this.getClass().getSimpleName(), "LocalServiceBinder : json " + responseString);
			throw e;
		}
		return responseString;
	}

}
