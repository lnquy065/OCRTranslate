package com.bitstudio.aztranslate.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;

import com.bitstudio.aztranslate.LocalDatabase.TranslationHistoryDatabaseHelper;
import com.bitstudio.aztranslate.MainActivity;
import com.bitstudio.aztranslate.R;
import com.bitstudio.aztranslate.Setting;
import com.bitstudio.aztranslate.models.BookmarkWord;
import com.bitstudio.aztranslate.models.TranslationHistory;

import java.util.Timer;
import java.util.TimerTask;

import static com.bitstudio.aztranslate.Setting.Notification.TIME_DELAY;

public class BookMarkService extends Service {

    NotificationManager notificationManager;
    private TranslationHistoryDatabaseHelper translationHistoryDatabaseHelper;

    NotifyTimerTask notifyTask = new NotifyTimerTask();
    Timer notifyTimer = new Timer();



    public BookMarkService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        translationHistoryDatabaseHelper = new TranslationHistoryDatabaseHelper(this, null);

        notifyTimer.schedule(notifyTask, TIME_DELAY, TIME_DELAY);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private BookmarkWord getBookMark() {
            return translationHistoryDatabaseHelper.getBookmarkWordRandomly();
    }


    private void showNotification(BookmarkWord b) {
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Notification noti  = new Notification.Builder(this)
                .setContentTitle( b.getWord())
                .setContentText(b.getWordTranslated())
                .setSmallIcon(R.drawable.logo_ocr)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();

        notificationManager.notify(0, noti);
    }

    class NotifyTimerTask extends TimerTask {

        public void run() {

            showNotification(getBookMark());
        }
    }
}
