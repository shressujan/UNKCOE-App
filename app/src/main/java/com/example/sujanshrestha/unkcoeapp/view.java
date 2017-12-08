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
import android.view.Gravity;
import android.util.TypedValue;
import android.widget.Toast;

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

public class view extends Activity implements Methods {
    public int rows;
    private static int columns = 9;
    public Connection connection;
    public TableRow tr;
    public String message = null;
    public boolean connectionReady = false;
    public  String[][] rowData = null;
    public boolean isDeleted = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Opens app in portrait mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.view_layout);
        connection = MainActivity.conn;
        Log.w("Connection", connection.toString());

        // this is the Asynctask which is used to process
        GenerateReport report =  new GenerateReport();
        //run background to reduce the load on the app process
        report.execute("");

    }


    /**
     * This method returns the total number of profiles in the inventory record
     * @return
     */
    public int getProfileCount()
    {
        //Getting the the number of rows in the report table
        int row = 0;
        try {
            if(connection!= null) {
                Statement stmt1 = connection.createStatement();
                String varSQL1 = "SELECT COUNT(*) FROM UNKCOEInventory.InventoryReport";
                ResultSet rs = null;
                try {
                    rs = stmt1.executeQuery(varSQL1);
                } catch (SQLException e) {
                    Log.d("Connection", e.getMessage());
                }
                catch (Exception ex)
                {
                    Log.w("Connection", ex.getMessage());
                }
                rs.next();
                row = rs.getInt(1);
                rs.close();
                stmt1.close();
                return row;
            }
        } catch (Exception ex) {
            Log.i("Connection", ex.getMessage());
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
                        Delete del = new Delete();
                        del.execute("");
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

    private  class Delete extends AsyncTask<String, String, String> {
        String message = "";
        Boolean isDeleted = false;
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected String doInBackground(String... strings) {
            String varSQL1 = "(?,?)";
            String call;
            call = "{CALL UNKCOEInventory.DeleteRecord" + varSQL1 + "}";
            CallableStatement stmt = null;
            try {
                stmt = MainActivity.conn.prepareCall(call);
//                            stmt.setString(1, SerialNum);  //How do I GET The Serial Num here???????????????????????
                stmt.registerOutParameter(2, Types.VARCHAR);
                stmt.execute();
                isDeleted = true;
                message = stmt.getString(2);
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return message;
        }
        @Override
        protected void onPostExecute(String s) {
            if (isDeleted) {
                Toast.makeText(view.this, s, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.this, view.class);
                startActivity(intent);
                finish();
            }
        }
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

    public void addData( String[][] rowData){
        TableLayout table =  (TableLayout) findViewById(R.id.TheDatabaseTable);
        int dps = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
        int dpsDevice = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
        int dpsHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        for (int i = 1; i < rows; i++)
        {

            /** Create a TableRow dynamically **/
            tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT));

            /** Creating a TextView to add to the row **/
            TextView serialNum = new TextView(this);
            serialNum.setText(rowData[i][0]);
            serialNum.setWidth(dps);
            serialNum.setHeight(dpsHeight);
            serialNum.setTextColor(Color.BLACK);
            serialNum.setBackgroundColor(Color.WHITE);
            serialNum.setGravity(Gravity.CENTER);
            serialNum.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(serialNum);  // Adding textView to tablerow.

            /** Creating another textview **/

            TextView user = new TextView(this);
            user.setText(rowData[i][1]);
            user.setWidth(dps);
            user.setHeight(dpsHeight);
            user.setTextColor(Color.BLACK);
            user.setBackgroundColor(Color.WHITE);
            user.setGravity(Gravity.CENTER);
            user.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(user);  // Adding textView to tablerow.

            /** Creating another textview **/

            TextView admin_UserName = new TextView(this);
            admin_UserName.setText(rowData[i][2]);
            admin_UserName.setHeight(dpsHeight);
            admin_UserName.setWidth(dps);
            admin_UserName.setTextColor(Color.BLACK);
            admin_UserName.setBackgroundColor(Color.WHITE);
            admin_UserName.setGravity(Gravity.CENTER);
            admin_UserName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(admin_UserName);  // Adding textView to tablerow.

            /** Creating another textview **/

            TextView admin_Password = new TextView(this);
            admin_Password.setText(rowData[i][3]);
            admin_Password.setHeight(dpsHeight);
            admin_Password.setWidth(dps);
            admin_Password.setTextColor(Color.BLACK);
            admin_Password.setBackgroundColor(Color.WHITE);
            admin_Password.setGravity(Gravity.CENTER);
            admin_Password.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(admin_Password);  // Adding textView to tablerow.

            /** Creating another textview **/

            TextView location = new TextView(this);
            location.setText(rowData[i][4]);
            location.setWidth(dps);
            location.setTextColor(Color.BLACK);
            location.setHeight(dpsHeight);
            location.setBackgroundColor(Color.WHITE);
            location.setGravity(Gravity.CENTER);
            location.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(location);  // Adding textView to tablerow.

            /** Creating another textview **/

            TextView computer_Type = new TextView(this);
            computer_Type.setText(rowData[i][5]);
            computer_Type.setWidth(dps);
            computer_Type.setTextColor(Color.BLACK);
            computer_Type.setHeight(dpsHeight);
            computer_Type.setBackgroundColor(Color.WHITE);
            computer_Type.setGravity(Gravity.CENTER);
            computer_Type.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(computer_Type);  // Adding textView to tablerow.

            /** Creating another textview **/

            TextView computer_Name = new TextView(this);
            computer_Name.setText(rowData[i][6]);
            computer_Name.setWidth(dps);
            computer_Name.setTextColor(Color.BLACK);
            computer_Name.setHeight(dpsHeight);
            computer_Name.setBackgroundColor(Color.WHITE);
            computer_Name.setGravity(Gravity.CENTER);
            computer_Name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(computer_Name);  // Adding textView to tablerow.

            /** Creating another textview **/

            TextView status = new TextView(this);
            status.setText(rowData[i][7]);
            status.setWidth(dps);
            status.setHeight(dpsHeight);
            status.setTextColor(Color.BLACK);
            status.setBackgroundColor(Color.WHITE);
            status.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            status.setGravity(Gravity.CENTER);
            tr.addView(status);  // Adding textView to tablerow.

            /** Creating another textview **/

            TextView devices = new TextView(this);
            devices.setText(rowData[i][8]);
            devices.setWidth(dpsDevice);
            devices.setHeight(dpsHeight);
            devices.setTextColor(Color.BLACK);
            devices.setBackgroundColor(Color.WHITE);
            devices.setGravity(Gravity.CENTER);
            devices.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(devices);  // Adding textView to tablerow.

            // Add the TableRow to the TableLayout
            table.addView(tr);
        }
    }

    private class GenerateReport extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {


        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                rows = getProfileCount();
                Log.d("Connection", rows + "");
                //Getting actual data from the database
                try {
                    Statement stmt = connection.createStatement();
                    String varSQL1 = "SELECT * FROM UNKCOEInventory.InventoryReport";
                    ResultSet rs = stmt.executeQuery(varSQL1);
                    ResultSetMetaData rsMeta = rs.getMetaData();
                    int columns = rsMeta.getColumnCount();
                    rowData = new String[rows][9];
                    String Name = "";
                    int b = 0;
                    while (rs.next()) {
                        for (int c = 1; c <= columns; c++) {
                            Name += rs.getString(c) + " ";
                            rowData[b][c - 1] = Name;
                            Name = "";
                        }
                        b++;
                    }
                }
                catch (SQLException ex) {
                    Log.e("Connection", ex.getMessage());
                }
                connectionReady = true;
                message = "Report query successfull!!";
            }
            catch (Exception e)
            {

            }

            return message;
        }

        @Override
        protected void onPostExecute(String s) {
            if(connectionReady) {
                addData(rowData);
            }
        }
    }
}