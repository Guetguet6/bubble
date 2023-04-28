package ca.uqac.bubble.Calendrier;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.core.app.NotificationCompat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import ca.uqac.bubble.MainActivity;
import ca.uqac.bubble.MyDatabaseHelper;
import ca.uqac.bubble.R;

public class NotificationScheduler extends BroadcastReceiver {
    private static final String CHANNEL_ID = "channel_id";
    private static final String CHANNEL_NAME = "channel_name";
    private static final String CHANNEL_DESCRIPTION = "channel_description";
    private static final int NOTIFICATION_ID = 1;


    @Override
    public void onReceive(Context context, Intent intent) {

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                MyDatabaseHelper.EventEntry.COLUMN_NAME_NAME,
                MyDatabaseHelper.EventEntry.COLUMN_NAME_DATETIME
        };

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());

        LocalDate endDate = LocalDate.now().plusDays(1);

        String[] selectionArgs = {
                endDate.format(dateFormatter)
        };

        String selection = MyDatabaseHelper.EventEntry.COLUMN_NAME_DATE + " = ? ";

        Cursor cursor = db.query(
                MyDatabaseHelper.EventEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int nbEvent = cursor.getCount();
                showNotification(context, nbEvent);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
    }


    private void showNotification(Context context, int nbEvent) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription(CHANNEL_DESCRIPTION);
        notificationManager.createNotificationChannel(notificationChannel);

        Notification builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(nbEvent + " sessions de prévues demain")
                .setSmallIcon(R.mipmap.ic_logo_app)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(Notification.CATEGORY_STATUS)
                .build();

        notificationManager.notify(NOTIFICATION_ID, builder);
    }
}
