/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.main.fragment</li>
 * <li>22 mai 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * <li>Produit par MSE.</li>
 *
 /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br> 
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 *  Belongs to <strong>Mathias Seguy</strong></br>
 ****************************************************************************************************************</br>
 * This code is free for any usage except training and can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * 
 * *****************************************************************************************************************</br>
 *  Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 *  Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br> 
 *  Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 *  <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */
package com.jcertif.android.ui.view.main.fragment;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 * This class aims to make callBack from MainFragment to the Activity using it
 */
public interface MainFragmentCallBack {
	/**
	 * Show the Events fragment(s)
	 */
	public void showEvents();
	/**
	 * Show the Speakers fragment(s) 
	 */
	public void showSpeakers();
	/**
	 * Show the Calendar fragment(s)
	 */
	public void showCalendar();
	/**
	 * Show the Agenda fragment(s)
	 */
	public void showAgenda();
	/**
	 * Show the Info fragment(s)
	 */
	public void showInfo();
	/**
	 * Show the fragment using all the available space
	 */
	public void fillSpace(Boolean force);
}
