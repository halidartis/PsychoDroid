package droid.psycho.com.psychodroid;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class ReadSMS {
    Boolean SMSReadFinished = false;

    public ReadSMS() {

        // public static final String INBOX = "content://sms/inbox";
        // public static final String SENT = "content://sms/sent";
        // public static final String DRAFT = "content://sms/draft";

    }
//Deneme
    public void getSMSMessages(Context context, int intervalHour, String FileName) {
        String str = "";
        long currentDateInMillis = System.currentTimeMillis();
        long yesterdayInMillis = currentDateInMillis - intervalHour * 3600000;


        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/sent"), new String[]{"_id", "address", "date", "body"}, null, null, null);

        cursor.moveToFirst();

        str += "MessageBody: " + cursor.getString(3) + " Address: " + cursor.getString(1) + "\n"; //first message.
        while (cursor.moveToNext()) { //check following messages.
            String date = cursor.getString(cursor.getColumnIndex("date")); //mesajın alınma-gönderilme tarihi
            Long timestamp = Long.parseLong(date);

            if (timestamp > yesterdayInMillis) {
                Log.e("AHA", "MESAJIN TARIHI: " + date);
                String address = cursor.getString(1);
                String body = cursor.getString(3);
                Log.e("AHA", "body: " + body);
                Log.e("AHA", "MessageBody: " + body + " Address: " + address);
                str += "MessageBody: " + body + " Address: " + address + "\n";


            } else {
                Log.e("AHA","buraya giriyo mu?");
                break;
            }


        }
        writeToFile("keremdurak", str, context);


    }






    private void writeToFile(String filename, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename + ".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    protected  String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("keremdurak.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


}



