package com.example.paymentapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class TableViewController implements Initializable {
    @FXML
    TableColumn<PaymentData, String> NameCol;
    @FXML
    TableColumn<PaymentData, Double> CashCol;
    @FXML
    TableColumn<PaymentData, Double> CardCol;
    @FXML
    TableColumn<PaymentData, Double> TotalCol;
    @FXML
    TableView<PaymentData> PatientsTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NameCol.setCellValueFactory(new PropertyValueFactory<PaymentData, String>("NameAndSurname"));
        CashCol.setCellValueFactory(new PropertyValueFactory<PaymentData, Double>("Cash"));
        CardCol.setCellValueFactory(new PropertyValueFactory<PaymentData, Double>("Card"));
        TotalCol.setCellValueFactory(new PropertyValueFactory<PaymentData, Double>("Total"));
    }

    public void insertData(PaymentData pd) {
        PatientsTable.getItems().add(pd);
    }
}
