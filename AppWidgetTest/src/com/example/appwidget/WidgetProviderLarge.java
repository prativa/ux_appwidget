package com.example.appwidget;

import java.util.ArrayList;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

public class WidgetProviderLarge extends AppWidgetProvider {
	public static int count;
	public static String ACTION_WIDGET_CLICK_NEXT = "Action_nextbtn";
	public static String ACTION_WIDGET_CLICK_PREV = "Action_prevbtn";


	/**
	 * Called in response to the ACTION_APPWIDGET_UPDATE broadcast when this
	 * AppWidget provider is being asked to provide RemoteViews for a set of
	 * AppWidgets. Override this method to implement your own AppWidget
	 * functionality.
	 */

	public void onUpdate(Context context,
			android.appwidget.AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		int appWidgetId = INVALID_APPWIDGET_ID;
		if (appWidgetIds != null) {
			int N = appWidgetIds.length;
			if (N == 1) {
				appWidgetId = appWidgetIds[0];
			}
		}

		Intent intent = new Intent(context, UpdateWidgetService.class);
		intent.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
		intent.setAction("DO NOTHING ACTION");
		context.startService(intent);

	}

	/**
	 * static class does not need instantiation UpdateWidgetService is a Service
	 * that identifies the App Widgets , instantiates AppWidgetManager and calls
	 * updateAppWidget() to update the widget values
	 */
	public static class UpdateWidgetService extends IntentService {

		public UpdateWidgetService() {
			super("UpdateWidgetService");

		}

		@Override
		protected void onHandleIntent(Intent intent) {
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(this);

			int incomingAppWidgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID,
					INVALID_APPWIDGET_ID);
			if (incomingAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				Log.i("prativa test widget", intent.getAction());
				updateOneAppWidget(appWidgetManager, incomingAppWidgetId,
						intent.getAction());
			}
		}

		/**
		 * For the random passcode app widget with the provided ID, updates its
		 * display with a new passcode, and registers click handling for its
		 * buttons.
		 */
		private void updateOneAppWidget(AppWidgetManager appWidgetManager,
				int appWidgetId, String whichButton) {

			ArrayList<String> listOfQuedMessage = fetchMotivationalQuotes();
			Log.i("Test arraylist", listOfQuedMessage.size() + "");
			int maxCount = listOfQuedMessage.size();
			int minCount = 0;
			RemoteViews views = new RemoteViews(this.getPackageName(),
					R.layout.layout_appwidget_large);

			bindQuoteToTextView(views, listOfQuedMessage.get(count));
			btnNxtClick(views, appWidgetId);
			btPrevClick(views, appWidgetId);

			if (whichButton.equals(ACTION_WIDGET_CLICK_PREV)) {

				if (count > minCount && count < maxCount) {
					count--;
					bindQuoteToTextView(views, listOfQuedMessage.get(count));

				}
			} else if (whichButton.equals(ACTION_WIDGET_CLICK_NEXT)) {

				if (count < maxCount - 1) {
					count++;
					bindQuoteToTextView(views, listOfQuedMessage.get(count));

				}

			}
			appWidgetManager.updateAppWidget(appWidgetId, views);

		}

		public void bindQuoteToTextView(RemoteViews views, String message) {

			views.setTextViewText(R.id.txt_messageContent, message);

		}

		private void btnNxtClick(RemoteViews views, int appWidgetId) {
			Intent btnNextIntent = new Intent(this, this.getClass());
			btnNextIntent.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
			btnNextIntent.setAction(ACTION_WIDGET_CLICK_NEXT);
			PendingIntent btnNextPendingIntent = PendingIntent.getService(this,
					0, btnNextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			views.setOnClickPendingIntent(R.id.btnNext, btnNextPendingIntent);

		}

		private void btPrevClick(RemoteViews views, int appWidgetId) {
			Intent btnPrevIntent = new Intent(this, this.getClass());
			btnPrevIntent.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
			btnPrevIntent.setAction(ACTION_WIDGET_CLICK_PREV);
			PendingIntent btnNextPendingIntent = PendingIntent.getService(this,
					0, btnPrevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			views.setOnClickPendingIntent(R.id.btnPrev, btnNextPendingIntent);

		}

		private ArrayList<String> fetchMotivationalQuotes() {
			ArrayList<String> motivationalQuoteList = new ArrayList<String>();
			motivationalQuoteList
					.add("It's not the load that breaks you down, it's the way you carry it");
			motivationalQuoteList
					.add("Of course motivation is not permanent. But then, neither is bathing; but it is something you should do on a regular basis.");
			motivationalQuoteList
					.add("Do you want to know who you are? Don't ask. Act! Action will delineate and define you");
			motivationalQuoteList
					.add("Nothing in the world is ever completely wrong. Even a stopped clock is right twice a day");
			motivationalQuoteList
					.add("When all is said and done, more is said than done");
			motivationalQuoteList
					.add("You'll never get ahead of anyone as long as you try to get even with him.");
			motivationalQuoteList
					.add("When someone tells me no, it doesnt mean I cant do it, it simply means I can't do it with them.");
			motivationalQuoteList
					.add("The way to get started is to quit talking and begin doing");
			motivationalQuoteList
					.add("You're never as good as everyone tells you when you win, and you're never as bad as they say when you lose.");

			return motivationalQuoteList;
		}

	}

}
