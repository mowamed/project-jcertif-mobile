package com.jcertif.android.service;

import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.jcertif.android.Application;
import com.jcertif.android.net.RestClient;

/**
 * The localService
 * This service will do real network access asynchronously
 * @author mouhamed_diouf
 *
 */
public class JCertifLocalService extends Service {
	// Binder given to clients
	private final IBinder mBinder = new LocalBinder();

	/**
	 * Class used for the client Binder. Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		public JCertifLocalService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return JCertifLocalService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(Application.NAME, this.getClass().getSimpleName()
				+ " binding ...");
		return mBinder;
	}

	public String getSpeakersData() throws Exception {
        return callFacade(Application.SPEAKER_URL);
	}

    public String getEventsData() throws Exception {
        return callFacade(Application.EVENT_URL);
	}

    private String callFacade(String url) throws Exception {
        String responseString;RestClient client = new RestClient();

        try {
            client.execute(url).get(180, TimeUnit.SECONDS);
            responseString = client.getResponse();
            Log.i(Application.NAME, responseString);
        } catch (Exception e) {
            Log.e(Application.NAME, "LocalService : " + e.getMessage());
            throw e;
        }
        return responseString;
    }


}
