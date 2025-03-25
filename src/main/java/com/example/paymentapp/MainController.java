package com.example.paymentapp;

import com.sun.source.doctree.SummaryTree;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableListValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

import javafx.scene.paint.Color;

import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.ResourceBundle;

//
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainController implements Initializable {
    @FXML
    // THIS TEXT IS USED AS A LOG OF OPERATIONS
    Text StatusLog;

    @FXML
    // FIELD TO INSERT HOUR OF APPOINTMENT
    TextField HourField;

    @FXML
    // FIELD TO INSERT NAME OF PATIENT
    TextField NameField;

    @FXML
    // FIELD TO INSERT CASH PAYMENT AMOUNT
    TextField CashField;

    @FXML
    // FIELD TO INSERT CARD PAYMENT AMOUNT
    TextField CardField;

    @FXML
    // TABLE WITH ALL DETAILS
    TableView<PaymentData> PatientsTable;
    @FXML
    TableColumn<PaymentData, String> HourCol;
    @FXML
    TableColumn<PaymentData, String> NameCol;
    @FXML
    TableColumn<PaymentData, Double> CashCol;
    @FXML
    TableColumn<PaymentData, Double> CardCol;
    @FXML
    TableColumn<PaymentData, Double> TotalCol;

    private double SumCash;
    private double SumCard;
    private double SumTotal;

    // TO MOŻNA WYRZUCIC
    ArrayList<PaymentData> PatientData = new ArrayList<>();

    @FXML
    Text sumCash;
    @FXML
    Text sumCard;
    @FXML
    Text sumTotal;

    @FXML
    ImageView Logo;

    Image logo;
    LocalDate DateOfSave = LocalDate.now();;

    int InputError = 0;

    // LIST WITH ALL PATIENTS AND THEIR'S DEATAILS (OBSERVED BY TABLEVIEW)
    ObservableList<PaymentData> allPayments = FXCollections.observableArrayList( );
    SortedList<PaymentData> sortedPatients = new SortedList<>(allPayments);

    public double takeDouble(TextField field){
        double amount;
        try{
            if(field.getText().equals("")){
                amount = 0;
            } else {
                amount = Double.parseDouble(field.getText());
                if(amount < 0) amount = 0;
            }
        } catch(Exception e){
            InputError++;
            amount = 0;
        }
        return amount;
    }

    public String takeString(TextField field){
        String text;
        if(field.getText().equals("")) {
            InputError++;
        }
        text = field.getText();
        return text;
    }

    public void vAddRecord(ActionEvent actionEvent) {
        InputError = 0;
        System.out.println("> START: vAddRecord");

        String hour = takeString(HourField);
        String name = takeString(NameField);
        double cashamount = takeDouble(CashField);
        double cardamount = takeDouble(CardField);


        if(InputError > 0 || (cashamount == 0 && cardamount == 0)){
            System.out.println("> ERROR OCCURED: vAddRecord");
            StatusLog.setFill(Color.RED);
            StatusLog.setText("Wprowadzone dane są niepoprawne lub niepełne!");

        } else {
            PaymentData paymentData = new PaymentData(name, hour, cashamount, cardamount);

            // DODANIE ELEMENTU DO TABLICY
            allPayments.add(paymentData);
            SumCash += cashamount;
            SumCard += cardamount;
            SumTotal += cashamount+cardamount;
            sumCash.setText(SumCash + "");
            sumCard.setText(SumCard + "");
            sumTotal.setText(SumTotal + "");
            HourField.clear();
            NameField.clear();
            CashField.clear();
            CardField.clear();
            //

            StatusLog.setFill(Color.GREEN);
            StatusLog.setText("Wpis został poprawine dodany!");
            PatientsTable.sort();
            PatientsTable.refresh();

        }

        System.out.println("> END: vAddRecord");
    }

    public void vSaveToFile(ActionEvent actionEvent) throws IOException {
        System.out.println("> START: Saving File");

        // DODAWANIE DO PLIKU
        saveToFile();
        createExcelFile();
        //
        for(int i=0; i<allPayments.size(); i++){
            System.out.println(allPayments.get(i).toString());
        }

        StatusLog.setFill(Color.GREEN);
        StatusLog.setText("Wpisy zostały zapisane do pliku!");
        System.out.println("> END: Saving File");
        getXSSColor("12");
    }

    public void saveToExcel() throws IOException {
        String filepath = "Zestawienie123.xlsx";
        File myfile = new File(filepath);
        Workbook wb = new HSSFWorkbook();

        Sheet sheet = wb.createSheet("Zestawienie1");
        FileOutputStream fos = new FileOutputStream(myfile);
        wb.write(fos);
        fos.close();
        System.out.println("> END: Saving File");



    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        HourCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentHour"));
        NameCol.setCellValueFactory(new PropertyValueFactory<>("NameAndSurname"));
        CashCol.setCellValueFactory(new PropertyValueFactory<>("Cash"));
        CardCol.setCellValueFactory(new PropertyValueFactory<>("Card"));
        TotalCol.setCellValueFactory(new PropertyValueFactory<>("Total"));

        logo = new Image("logo1.png");
        Logo.setImage(logo);


        PatientsTable.setItems(allPayments);
        HourCol.setComparator(new HourComparator());
        PatientsTable.getSortOrder().add(HourCol);
        PatientsTable.sort();
        editData();
/*
        PatientsTable.setItems(allPayments);
        PatientsTable.getSortOrder().add(HourCol);
        editData();
        */
    }


    public void saveToFile(){
        String data = "GODZINA, IMIĘ I NAZWISKO, GOTÓWKA, KARTA, SUMA\n";
        for(int i=0; i<allPayments.size(); i++){
            data += allPayments.get(i).toString();
            data += "\n";

        }
        // Utworzenie obiektu File
        //LocalDate DateOfSave = LocalDate.now();
        String name = "Zestawienie " + DateOfSave + ".txt";
        File file = new File(name);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(data);
            System.out.println("Dane zostały zapisane do pliku.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public XSSFColor getXSSColor(String hex_code){
        byte[] rgbB = null; // get byte array from hex string
        try {
            rgbB = Hex.decodeHex(hex_code);
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
        XSSFColor color = new XSSFColor(rgbB, null);
        return color;
    }

    private void createExcelFile() throws IOException {
        // Tworzymy nowy workbook (Excel)
        Workbook workbook = new XSSFWorkbook();

        // Tworzymy arkusz
        String name = "Zestawienie " + DateOfSave;
        Sheet sheet = workbook.createSheet(name);


        CellStyle headerstyle = workbook.createCellStyle();
        headerstyle.setAlignment(HorizontalAlignment.CENTER);
        headerstyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        headerstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerstyle.setBorderBottom(BorderStyle.THIN);

        CellStyle evenstyle = workbook.createCellStyle();
        evenstyle.setAlignment(HorizontalAlignment.CENTER);
        //evenstyle.setFillForegroundColor(color);
        evenstyle.setFillForegroundColor(getXSSColor("D9D9D9"));
        evenstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle oddstyle = workbook.createCellStyle();
        oddstyle.setAlignment(HorizontalAlignment.CENTER);
        oddstyle.setFillForegroundColor(getXSSColor("F2F2F2"));
        oddstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

//FURPS + MOSCOW

        // Tworzymy wiersz w arkuszu
        Row row = sheet.createRow(0);
        // Tworzymy komórki w wierszu
        Cell cell1 = row.createCell(0);
        cell1.setCellValue("Godzina wizyty");
        sheet.setColumnWidth(0, 14*256);
        cell1.setCellStyle(headerstyle);

        // dodaj tutaj co

        Cell cell2 = row.createCell(1);
        cell2.setCellValue("Imię i nazwisko pacjenta");
        sheet.setColumnWidth(1, 20*256);
        cell2.setCellStyle(headerstyle);

        Cell cell3 = row.createCell(2);
        cell3.setCellValue("Płatność gotówką");
        sheet.setColumnWidth(2, 15*256);
        cell3.setCellStyle(headerstyle);

        Cell cell4 = row.createCell(3);
        cell4.setCellValue("Płatność kartą");
        sheet.setColumnWidth(3, 12*256);
        cell4.setCellStyle(headerstyle);

        Cell cell5 = row.createCell(4);
        cell5.setCellValue("Suma płatności");
        sheet.setColumnWidth(4, 13*256);
        cell5.setCellStyle(headerstyle);


        for(int i=0; i<allPayments.size(); i++){
            Row newrow = sheet.createRow(i+1);
            newrow.createCell(0).setCellValue(allPayments.get(i).getAppointmentHour());
            newrow.createCell(1).setCellValue(allPayments.get(i).getNameAndSurname());
            newrow.createCell(2).setCellValue(allPayments.get(i).getCash());
            newrow.createCell(3).setCellValue(allPayments.get(i).getCard());
            newrow.createCell(4).setCellValue((allPayments.get(i).getTotal()).doubleValue());
            if(i%2 == 0){
                for(int j=0; j<5; j++) newrow.getCell(j).setCellStyle(evenstyle);
            } else {
                for(int j=0; j<5; j++) newrow.getCell(j).setCellStyle(oddstyle);
            }

        }

        // Tworzymy plik na dysku
        File file = new File(name+".xlsx");

        // Zapisujemy dane do pliku
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Plik Excel zapisany pomyślnie!");
    }

    public void editData(){
        HourCol.setCellFactory(TextFieldTableCell.<PaymentData>forTableColumn());
        HourCol.setOnEditCommit(event ->{
            PaymentData person = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(!event.getNewValue().equals("")){
                person.setAppointmentHour(event.getNewValue());
            }
            System.out.println("> TABLE RECORD EDITED: at row " + (event.getTablePosition().getRow() + 1) + " new value: " + event.getNewValue());
            PatientsTable.sort();
            PatientsTable.refresh();
        });
        NameCol.setCellFactory(TextFieldTableCell.<PaymentData>forTableColumn());
        NameCol.setOnEditCommit(event ->{
            PaymentData person = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(!event.getNewValue().equals("")){
                person.setNameAndSurname(event.getNewValue());
            }
            System.out.println("> TABLE RECORD EDITED: at row " + (event.getTablePosition().getRow() + 1) + " new value: " + event.getNewValue());
            PatientsTable.refresh();
        });


        CashCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        CashCol.setOnEditCommit(event ->{
            PaymentData person = event.getTableView().getItems().get(event.getTablePosition().getRow());
            try{
                double newamount = Double.parseDouble(String.valueOf(event.getNewValue()));
                SumCash -= person.getCash();
                SumTotal -= person.getCash();
                person.setCash(newamount);
                SumCash += person.getCash();
                SumTotal += person.getCash();
                sumCash.setText(SumCash + "");
                sumTotal.setText(SumTotal + "");
            } catch (Exception e){
                System.out.println("> EROOR IN EDITING RECORD!");
            }
            System.out.println("> TABLE RECORD EDITED: at row " + (event.getTablePosition().getRow() + 1) + " new value: " + event.getNewValue());
            PatientsTable.refresh();
        });

        CardCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        CardCol.setOnEditCommit(event ->{
            PaymentData person = event.getTableView().getItems().get(event.getTablePosition().getRow());
            try{
                double newamount = Double.parseDouble(String.valueOf(event.getNewValue()));
                SumCard -= person.getCard();
                SumTotal -= person.getCard();
                person.setCard(newamount);
                SumCard += person.getCard();
                SumTotal += person.getCard();
                sumCard.setText(SumCard + "");
                sumTotal.setText(SumTotal + "");
            } catch (Exception e){
                System.out.println("> EROOR IN EDITING RECORD!");
            }
            System.out.println("> TABLE RECORD EDITED: at row " + (event.getTablePosition().getRow() + 1) + " new value: " + event.getNewValue());
            PatientsTable.refresh();
        });

        TotalCol.setCellValueFactory(cellData -> cellData.getValue().getTotal().asObject());
    }

}



