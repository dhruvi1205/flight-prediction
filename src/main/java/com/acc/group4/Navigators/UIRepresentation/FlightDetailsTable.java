package com.acc.group4.Navigators.UIRepresentation;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

import com.acc.group4.Navigators.pojo.FlightDetails;

public class FlightDetailsTable {
    private static DefaultTableModel tableModel;

    public static void showFlights(ArrayList<FlightDetails> flightDetails) {
        JFrame f = new JFrame("FlyHigh");
        
        // Clear existing entries in the table
        if (tableModel != null) {
            tableModel.setRowCount(0);
        }

        String data[][] = new String[flightDetails.size()][4];
        int i = 0;
        for (FlightDetails flightDet : flightDetails) {
            data[i][0] = flightDet.getFlightName();
            data[i][1] = flightDet.getFlightPrice();
            data[i][2] = flightDet.getTimeToReach();
            data[i][3] = flightDet.getStops();
            i++;
        }
        String column[] = { "Flight Names", "Price", "Time", "Stops" };
        
        // Use DefaultTableModel to manipulate the JTable
        tableModel = new DefaultTableModel(data, column);
        
        JTable jt = new JTable(tableModel);
        jt.setBounds(30, 40, 200, 300);
        JScrollPane sp = new JScrollPane(jt);
        f.add(sp);
        f.setSize(300, 400);
        f.setVisible(true);
    }
}
