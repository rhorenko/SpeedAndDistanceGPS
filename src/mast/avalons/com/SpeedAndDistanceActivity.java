package mast.avalons.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SpeedAndDistanceActivity extends Activity  implements LocationListener{
	SQLiteDatabase mDatabase;
	Cursor mCursor;	
	final String tag = "log";
    TextView DataText;
    String TimeNowtmp;
    int N, sec, stroka=0;
    Location[] mass = new Location[10000];
    double latitude, longitude, altitude, accuracy, averspeed,maxaccspeed = 0;
    long time;
    int onoff=0;    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);             
        Button start = (Button) findViewById(R.id.start);
        Button stop = (Button) findViewById(R.id.stop);
        Button copyDB2SD = (Button) findViewById(R.id.copyDB2SD);
        DataText = (TextView) findViewById(R.id.data);
        DataText.setText(
				"Широта: "	+ latitude  + 
				"\nДолгота: " + longitude +
				"\nВысота"     + altitude + 
				//"\nТочность"+accuracy + 
				"\nСредняя скорость: " + averspeed + 
				"\nМаксимально точная (для GPS) скорость в данный момент: " + maxaccspeed + "\n");          
        start.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) 
            		{
                    	startwrite();
                    }
                });
       
        stop.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) 
            		{
            			stopwrite();
                    }
            	});
        
        copyDB2SD.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) 
            		{
            			copyDB2SD();
                    	DataText.setText(DataText.getText()+"\nWrited to DB!!!");
                    }
            	});        
    }
    

    public void startwrite()
    {
    	onoff=1;
    	return;
    }
    public void stopwrite()
    {
    	onoff=0;
    	return;
    }
    
    		           
       
    protected void copyDB2SD(){
    	File file1 = new File(Environment.getDataDirectory().getPath()+"/data/mast.avalons.com/databases/db.db");
    	File folder = new File(Environment.getExternalStorageDirectory () + "/DB");
    	folder.mkdir();
    	File file2 = new File(Environment.getExternalStorageDirectory () + "/DB/db"+System.currentTimeMillis()+".db");
    	try {
    		
			copyFile(file1,file2);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    	try {
    		
    		//deleteDatabase("db.db");
    		
    	} catch (Exception e) {
    		
    		e.printStackTrace();
		}
    };
    
    private static void copyFile(File sourceFile, File destFile)
            throws IOException {
    if (!sourceFile.exists()) {
    	 return;
    }
    if (!destFile.exists()) {
            destFile.createNewFile();
    }
    FileChannel source = null;
    FileChannel destination = null;
    source = new FileInputStream(sourceFile).getChannel();
    destination = new FileOutputStream(destFile).getChannel();
    if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
    }
    if (source != null) {
            source.close();
    }
    if (destination != null) {
            destination.close();
    }

}
    public void onLocationChanged(Location location) {
    	mass[stroka] = location;
	    stroka++;
	     
    	latitude =location.getLatitude();
    	longitude=location.getLongitude();
    	altitude =location.getAltitude();
    	accuracy =location.getAccuracy();
    	averspeed=location.distanceTo(mass[0]);
    	maxaccspeed=location.distanceTo(mass[stroka-2]);
    	Log.d(tag,"Широта: "	+ latitude  + 
				"\nДолгота: " + longitude +
				"\nВысота"     + altitude + 
				//"\nТочность"+accuracy + 
				"\nСредняя скорость: " + averspeed + 
				"\nМаксимально точная (для GPS) скорость в данный момент: " + maxaccspeed + "\n");
    	DataText.setText(
				"Широта: "	+ latitude  + 
				"\nДолгота: " + longitude +
				"\nВысота"     + altitude + 
				//"\nТочность"+accuracy + 
				"\nСредняя скорость: " + averspeed + 
				"\nМаксимально точная (для GPS) скорость в данный момент: " + maxaccspeed + "\n");
    	//if (onoff==1){
    	ContentValues values = new ContentValues(2);
        values.put(DbHelper.LAT, Double.toString(latitude));
        values.put(DbHelper.LON, Double.toString(longitude));
        values.put(DbHelper.ALT, Double.toString(altitude));
        values.put(DbHelper.ACC, Double.toString(accuracy));
        values.put(DbHelper.TIME, Long.toString(time));
        getContentResolver().insert(Provider.CONTENT_URI, values); 
         		
	    //}
    	
    }

	public void onProviderDisabled(String provider) {}

	public void onProviderEnabled(String provider) {}

	public void onStatusChanged(String provider, int status, Bundle extras) {}

}