package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Mateusz on 08.12.2016.
 */
public final class ElementInitializer {

    public static void setIpAddress(Label ipAddressLabel){
        try {
            ipAddressLabel.setText(Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void setComboBoxItems(ComboBox comboBox, List<Object[]> itemList){
        ObservableList<String> tracksList = FXCollections.observableArrayList();
        for (Object result : itemList) {
            tracksList.add(String.valueOf(result));
        }
        comboBox.setItems(tracksList);
    }

}
