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
import android.os.Bundle;
import android.view.View;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 * Created by Sujan Shrestha on 9/12/17.
 */

public class view extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Opens app in portrait mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.view_layout);
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
        String query = "Select * FROM " + table + "Report";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs =  stmt.executeQuery(query);
            if(rs.next())
            {
                count++;
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return count;
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
}
