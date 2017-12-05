package com.example.sujanshrestha.unkcoeapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import android.graphics.Typeface;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.sql.ResultSetMetaData;

/**
 * Created by Sujan Shrestha on 9/12/17.
 */

public class view extends Activity {
    public int rows;
    private static int columns = 9;
    public Connection connection;
    public TableRow tr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Opens app in portrait mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.view_layout);


//        addData();

        // this is the Asynctask which is used to process
        GenerateReport report =  new GenerateReport();
        //run background to reduce the load on the app process
        report.execute("");

    }


    /**
     * This method returns the total number of profiles in the inventory record
     * @param table
     * @param conn
     * @return
     */
    public int getProfileCount(String table, Connection conn)
    {
        int count = 0;
        String query = "SELECT COUNT(*) FROM UNKCOEInventory.InventoryReport";
        Log.e("Connection", query);
        Statement stmt = null;

            if(conn != null)
            {
                try {
                    stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    { count++;}
                    rs.close();
                    stmt.close();
                    return count;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Log.e("Connection","NULL Connection");
            }

        return 0;
    }

    /**
     * Edit button onClick event
     * @param view
     */
    public void onClickEdit(final View view)
    {

    addClickEffect(view);
        new AlertDialog.Builder(this)
                .setTitle("Edit Record")
                .setMessage("Are you sure you want to Edit this record?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(view.this , edit.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    /**
     * Delete button onClick event
     * @param view
     */
    public void onClickDelete(View view) {
        addClickEffect(view);
        //Creating the alert dialog box
        new AlertDialog.Builder(this)
                .setTitle("Delete Record")
                .setMessage("Are you sure you want to Delete this record?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String varSQL1 = "(?,?)";
                        String call;
                        call = "{CALL UNKCOEInventory.DeleteRecord" + varSQL1 + "}";
                        CallableStatement stmt = null;
                        try {
                            stmt = MainActivity.conn.prepareCall(call);
//                            stmt.setString(1, SerialNum);  //How do I GET The Serial Num here???????????????????????
                            stmt.registerOutParameter(2, Types.VARCHAR);
                            stmt.execute();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing when user clicks cancel
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    /**
     * This method is run when the back button is pressed
     * @param view
     */

    public void onClickBack(View view)
    {
        addClickEffect(view);
        Intent  intent = new Intent(this, homePage.class);
        startActivity(intent);
    }


    @SuppressLint("NewApi")
    public void addClickEffect(View view)
    {
        Drawable drawableNormal = view.getBackground();

        Drawable drawablePressed = view.getBackground().getConstantState().newDrawable();
        drawablePressed.mutate();
        drawablePressed.setColorFilter(Color.argb(100, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);

        StateListDrawable listDrawable = new StateListDrawable();
        listDrawable.addState(new int[] {android.R.attr.state_pressed}, drawablePressed);
        listDrawable.addState(new int[] {}, drawableNormal);
        view.setBackground(listDrawable);
    }

    /** This function add the data to the table **/

//    public void addData(){
//        TableLayout table =  (TableLayout) findViewById(R.id.TheDatabaseTable);
//
//        for (int i = 0; i < rows; i++)
//        {
//            /** Create a TableRow dynamically **/
//            tr = new TableRow(this);
//            tr.setLayoutParams(new LayoutParams(
//                    LayoutParams.WRAP_CONTENT,
//                    LayoutParams.MATCH_PARENT));
//
//            /** Creating a TextView to add to the row **/
//            TextView serialNum = new TextView(this);
//            serialNum.setText("data");
//            serialNum.setWidth(Integer.parseInt("130dp"));
//            serialNum.setTextSize(Float.parseFloat("15dp"));
//            serialNum.setTextColor(Color.BLACK);
//            serialNum.setBackgroundColor(Color.WHITE);
//            serialNum.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//            tr.addView(serialNum);  // Adding textView to tablerow.
//
//            /** Creating another textview **/
//
//            TextView user = new TextView(this);
//            user.setText("data");
//            user.setWidth(Integer.parseInt("130dp"));
//            user.setTextSize(Float.parseFloat("15dp"));
//            user.setTextColor(Color.BLACK);
//            user.setBackgroundColor(Color.WHITE);
//            user.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//            tr.addView(user);  // Adding textView to tablerow.
//
//            /** Creating another textview **/
//
//            TextView admin_UserName = new TextView(this);
//            admin_UserName.setText("data");
//            admin_UserName.setWidth(Integer.parseInt("130dp"));
//            admin_UserName.setTextSize(Float.parseFloat("15dp"));
//            admin_UserName.setTextColor(Color.BLACK);
//            admin_UserName.setBackgroundColor(Color.WHITE);
//            admin_UserName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//            tr.addView(admin_UserName);  // Adding textView to tablerow.
//
//            /** Creating another textview **/
//
//            TextView admin_Password = new TextView(this);
//            admin_Password.setText("data");
//            admin_Password.setWidth(Integer.parseInt("130dp"));
//            admin_Password.setTextSize(Float.parseFloat("15dp"));
//            admin_Password.setTextColor(Color.BLACK);
//            admin_Password.setBackgroundColor(Color.WHITE);
//            admin_Password.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//            tr.addView(admin_Password);  // Adding textView to tablerow.
//
//            /** Creating another textview **/
//
//            TextView location = new TextView(this);
//            location.setText("data");
//            location.setWidth(Integer.parseInt("130dp"));
//            location.setTextSize(Float.parseFloat("15dp"));
//            location.setTextColor(Color.BLACK);
//            location.setBackgroundColor(Color.WHITE);
//            location.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//            tr.addView(location);  // Adding textView to tablerow.
//
//            /** Creating another textview **/
//
//            TextView computer_Type = new TextView(this);
//            computer_Type.setText("data");
//            computer_Type.setWidth(Integer.parseInt("130dp"));
//            computer_Type.setTextSize(Float.parseFloat("15dp"));
//            computer_Type.setTextColor(Color.BLACK);
//            computer_Type.setBackgroundColor(Color.WHITE);
//            computer_Type.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//            tr.addView(computer_Type);  // Adding textView to tablerow.
//
//            /** Creating another textview **/
//
//            TextView computer_Name = new TextView(this);
//            computer_Name.setText("data");
//            computer_Name.setWidth(Integer.parseInt("130dp"));
//            computer_Name.setTextSize(Float.parseFloat("15dp"));
//            computer_Name.setTextColor(Color.BLACK);
//            computer_Name.setBackgroundColor(Color.WHITE);
//            computer_Name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//            tr.addView(computer_Name);  // Adding textView to tablerow.
//
//            /** Creating another textview **/
//
//            TextView status = new TextView(this);
//            status.setText("data");
//            status.setWidth(Integer.parseInt("130dp"));
//            status.setTextSize(Float.parseFloat("15dp"));
//            status.setTextColor(Color.BLACK);
//            status.setBackgroundColor(Color.WHITE);
//            status.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//            tr.addView(status);  // Adding textView to tablerow.
//
//            /** Creating another textview **/
//
//            TextView devices = new TextView(this);
//            devices.setText("data");
//            devices.setWidth(Integer.parseInt("130dp"));
//            devices.setTextSize(Float.parseFloat("15dp"));
//            devices.setTextColor(Color.BLACK);
//            devices.setBackgroundColor(Color.WHITE);
//            devices.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//            tr.addView(status);  // Adding textView to tablerow.
//
//            // Add the TableRow to the TableLayout
//            table.addView(tr, new TableLayout.LayoutParams(
//                    LayoutParams.FILL_PARENT,
//                    LayoutParams.WRAP_CONTENT));
//        }
//    }

    private class GenerateReport extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            connection = MainActivity.conn;
            rows = getProfileCount("UNKCOEInventory.InventoryDatabase", connection);
            Log.d("Connection", rows+"");


        }
        @Override
        protected String doInBackground(String... strings) {
//            TableLayout table =  (TableLayout) findViewById(R.id.TheDatabaseTable);
//
//            for(int i = 0; i < rows; i++)
//            {
//                TableRow tr = new TableRow();
//            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }
}
