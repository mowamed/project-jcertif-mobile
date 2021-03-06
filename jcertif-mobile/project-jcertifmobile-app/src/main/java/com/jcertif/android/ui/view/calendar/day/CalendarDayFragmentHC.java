/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.calendar</li>
 * <li>5 juin 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * Projet : JCertif Africa 2012 Project
 */
package com.jcertif.android.ui.view.calendar.day;

import java.util.Calendar;
import java.util.List;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcertif.android.R;
import com.jcertif.android.service.business.stardevents.StaredEventsService;
import com.jcertif.android.transverse.model.Event;
import com.jcertif.android.ui.view.main.MainActivityHC;

import de.akquinet.android.androlog.Log;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to display within a generic fragment a calendar of events
 */
public class CalendarDayFragmentHC extends Fragment {
	/******************************************************************************************/
	/** Attribute **************************************************************************/
	/******************************************************************************************/

	/**
	 * The calendarAdpater that manages the data
	 */
	CalendarDayAdapter adapter;

	/**
	 * The inflater
	 */
	LayoutInflater inflater;
	/**
	 * The callBack
	 */
	CalendarDayCallBack callBack;
	/**
	 * The star
	 */
	Drawable star = null;
	/**
	 * The empty star
	 */
	Drawable emptyStar = null;

	/******************************************************************************************/
	/** LifeCycle management **************************************************************************/
	/******************************************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		star = (Drawable) getResources().getDrawable(R.drawable.star);
		emptyStar = (Drawable) getResources().getDrawable(R.drawable.empty_star);
		View view = inflater.inflate(R.layout.calendar, container, false);
		// Add the listener to change the day
		Button btnNext = (Button) view.findViewById(R.id.nextday);
		Button btnPrevious = (Button) view.findViewById(R.id.previousday);
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeDay(1);
			}
		});
		btnPrevious.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeDay(-1);
			}
		});
		// insure the fragment not being destroyed when activity destroyed because else memory leaks
		// is generated and null pointerExceptions too (when rotating the device)
		setRetainInstance(true);
		return view;
	}

	/**
	 * @param step
	 */
	private void changeDay(int step) {
		adapter.changeDay(step);
		updateGui();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// Define header title
				getActivity().getActionBar().setTitle(getResources().getString(R.string.sessions_list_htitle));

		if (adapter.eventType == CalendarDayAdapter.ALL_EVENTS) {
			getActivity().getActionBar().setTitle(getResources().getString(R.string.calendar_htitle));
		} else if (adapter.eventType == CalendarDayAdapter.STARED_EVENTS) {
			getActivity().getActionBar().setTitle(getResources().getString(R.string.agenda_htitle));
		} else {
			// neutral choice
			getActivity().getActionBar().setTitle(getResources().getString(R.string.calendar_htitle));
		}
		// ensure the panel is displayed using the whole space
		// update the current shown fragment
		updateGui();
		super.onResume();
	}
	
	/**
	 * To be sure that the callBack is instantiate
	 */
	public CalendarDayCallBack getCallBack() {
		if (callBack == null) {
			callBack = (CalendarDayCallBack) ((MainActivityHC) getActivity()).getFragmentSwitcher();
		}
		return callBack;
	}

	/******************************************************************************************/
	/** GUI management **************************************************************************/
	/******************************************************************************************/
	/**
	 * Called when a new adapter is set to the calendar
	 */
	void updateGui() {
		View view = getView();
		if (null != view) {
			// build the view:Display the day
			((TextView) view.findViewById(R.id.calendar_day)).setText(adapter.formatTitleDate(adapter.dayToDisplay));
			// and then the events
			buildDay(view, adapter.getDayToDisplay());
		}
	}

	/**
	 * @param view
	 * @param cal
	 */
	private void buildDay(View view, Calendar cal) {
		// Find the layout
		RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.calendarRelativeLayout);
		// Find the layout
		RelativeLayout layoutHour = (RelativeLayout) view.findViewById(R.id.calendarHourRelativeLayout);
		// insure the layout is empty
		layout.removeAllViewsInLayout();
		// Build the calendar day used to displayed
		Calendar time = Calendar.getInstance();
		time.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		time.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		time.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		time.set(Calendar.HOUR_OF_DAY, 7);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		// the current list of events of a period to display
		List<Event> events = adapter.events;
		int hourCellId = 0;
		int lastHourCellId = 0;
		LinearLayout evtCell;
		Boolean isFirstCell = true;
		// Now browse the time
		while (time.get(Calendar.HOUR_OF_DAY) <= CalendarDayAdapter.LAST_HOUR_OF_DAY) {
			// Ajouter les heures dans le panel principal
			hourCellId = createHourCell(layout, time, lastHourCellId, isFirstCell, false);
			// Ajouter des heures dans le layout des heures
			createHourCell(layoutHour, time, lastHourCellId, isFirstCell, true);
			lastHourCellId = hourCellId;
			isFirstCell = false;
			// increment the time
			time.set(Calendar.MINUTE, time.get(Calendar.MINUTE) + CalendarDayAdapter.ROW_CURRENT_SIZE);
		}
		for (final Event event : events) {
			// ajouter les events
			evtCell = (LinearLayout) inflater.inflate(R.layout.calendar_event_cell, null);
			evtCell.setId(10000 * event.id);
			((TextView) evtCell.findViewById(R.id.calendar_evtStartTime)).setText(adapter.getStartHour(event));
			((TextView) evtCell.findViewById(R.id.calendar_evtSpeakerName)).setText(adapter.getSpeakerName(event));
			((TextView) evtCell.findViewById(R.id.calendar_evtName)).setText(event.name);
			((TextView) evtCell.findViewById(R.id.calendar_evtSubject)).setText(event.description);
			((TextView) evtCell.findViewById(R.id.calendar_evtRoom)).setText(event.room);
			evtCell.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getCallBack().showSelectedEvent(event.id, false);
				}
			});
			// manage the stared event status
			final ImageView starView = ((ImageView) evtCell.findViewById(R.id.star_cal));
			if (StaredEventsService.instance.isStared(event.id)) {
				starView.setBackgroundDrawable(star);
			} else {
				starView.setBackgroundDrawable(emptyStar);
			}
			// add the listener
			starView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					changeEventStartState(starView, event.id);
				}
			});

			// Build the relative layout parameter
			float dimWidth = getActivity().getResources().getDimension(R.dimen.calendar_cell_width);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) dimWidth,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_TOP, adapter.getTopAlign(event));
			params.addRule(RelativeLayout.ALIGN_BOTTOM, adapter.getBottomAlign(event));
			// now fix the level android:layout_toRightOf
			params.addRule(RelativeLayout.RIGHT_OF, adapter.getLeftAlign(event));
			layout.addView(evtCell, params);
		}
		// Ajouter les evenements
		Log.d("CalendarDayFragment:buildDay", "build finished");
	}

	/**
	 * @param layout
	 * @param time
	 * @param lastHourCellId
	 * @param isFirstCell
	 * @return
	 */
	public int createHourCell(RelativeLayout layout, Calendar time, int lastHourCellId, Boolean isFirstCell,
			Boolean visible) {
		TextView hourCell;
		int hourCellId;
		hourCell = (TextView) inflater.inflate(R.layout.calendar_hour_cell, null);
		hourCellId = adapter.getCellId(time);
		hourCell.setId(hourCellId);

		// Build the relative layout parameter
		RelativeLayout.LayoutParams params;
		float dimHeight = getActivity().getResources().getDimension(R.dimen.calendar_cell_height);
		if (!visible) {
			params = new RelativeLayout.LayoutParams(1, (int) dimHeight);
			hourCell.setText("");
		} else {
			params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) dimHeight);
			hourCell.setText(adapter.formatDate(time));
		}
		if (isFirstCell) {
			// align to top
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		} else {
			// align to previous
			params.addRule(RelativeLayout.BELOW, lastHourCellId);
		}

		layout.addView(hourCell, params);
		return hourCellId;
	}

	/******************************************************************************************/
	/** Getters Setters **************************************************************************/
	/******************************************************************************************/

	/**
	 * @return the adapter
	 */
	public final CalendarDayAdapter getAdapter() {
		return adapter;
	}

	/**
	 * @param adapter
	 *            the adapter to set
	 */
	public final void setAdapter(CalendarDayAdapter adapter) {
		Log.w("CalendarDayFragment" + this.hashCode() + ": setAdapter", "adapter: " + adapter + " this.adapter "
				+ this.adapter);
		this.adapter = adapter;
		// make this link bidirectionnal
		this.adapter.setCalendarHC(this);
		// then update the gui
	}

	/**
	 * Change the state of the event from stared to unstared or from unstared to stared
	 * 
	 * @param imageView
	 *            the holder that holds the view
	 * @param eventId
	 *            the event id of the event
	 */
	private void changeEventStartState(ImageView imageView, int eventId) {
		StaredEventsService service = StaredEventsService.instance;
		if (service.isStared(eventId)) {
			// first change the stared status of the event
			service.staredEventsStatusChanged(eventId, false);
			// then update the gui
			imageView.setBackgroundDrawable(emptyStar);
		} else {
			// first change the stared status of the event
			service.staredEventsStatusChanged(eventId, true);
			// then update the gui
			imageView.setBackgroundDrawable(star);
		}
	}

}
