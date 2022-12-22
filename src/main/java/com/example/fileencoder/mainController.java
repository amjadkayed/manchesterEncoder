package com.example.fileencoder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class mainController implements Initializable {
    XYChart.Series seriesForInput ;
    XYChart.Series seriesForManchester ;
    XYChart.Series seriesForDifferentialManchester ;

    @FXML
    private LineChart<Number, Number> differentialManchesterLineChart;

    @FXML
    private RadioButton fromFileRadioButton;

    @FXML
    private LineChart<Number, Number> inputLineChart;

    @FXML
    private Button loadButton;

    @FXML
    private LineChart<Number, Number> manchesterLineChart;

    @FXML
    private RadioButton manualInputRadioButton;

    @FXML
    private CategoryAxis xAxis1;

    @FXML
    private CategoryAxis xAxis2;

    @FXML
    private CategoryAxis xAxis3;

    @FXML
    private TextField inputTextField;

    @FXML
    private Button nextPhaseButton;

    @FXML
    private Button previousPhaseButton;

    @FXML
    private Label InputLabel;

    @FXML
    private NumberAxis yAxis1;

    @FXML
    private NumberAxis yAxis2;

    @FXML
    private NumberAxis yAxis3;


    @FXML
    private TextField maxBytesTextField;

    @FXML
    private Label selectedFileLabel;

    private int maxBytesInPhase = 1 ;

    private ArrayList<Boolean> invertedArray;

    byte[] data ;
    String inputData ;
    int phaseNumber ;
    @FXML
    private Label invalidLabel;
    int start  ,end ;
    boolean inverted ;
    void setCharts(int num ,int x  ){
        // for the input chart
        seriesForInput.getData().add(new XYChart.Data(Integer.toString(x),num));
        seriesForInput.getData().add(new XYChart.Data(Integer.toString(x+1),num));
        // for the manchester chart
        if(num==1){
            seriesForManchester.getData().add(new XYChart.Data(Double.toString(x),-1 ));
            seriesForManchester.getData().add(new XYChart.Data(Double.toString(x+0.5),-1 ));
            seriesForManchester.getData().add(new XYChart.Data(Double.toString(x+0.5),1 ));
            seriesForManchester.getData().add(new XYChart.Data(Double.toString(x+1),1 ));
        }
        else {
            seriesForManchester.getData().add(new XYChart.Data(Double.toString(x),1 ));
            seriesForManchester.getData().add(new XYChart.Data(Double.toString(x+0.5),1 ));
            seriesForManchester.getData().add(new XYChart.Data(Double.toString(x+0.5),-1 ));
            seriesForManchester.getData().add(new XYChart.Data(Double.toString(x+1),-1 ));
        }
        // for differential manchester

        if(num==1){
            if(inverted){
                seriesForDifferentialManchester.getData().add(new XYChart.Data(Double.toString(x),-1 ));
                seriesForDifferentialManchester.getData().add(new XYChart.Data(Double.toString(x+0.5),-1 ));
                seriesForDifferentialManchester.getData().add(new XYChart.Data(Double.toString(x+0.5),1 ));
                seriesForDifferentialManchester.getData().add(new XYChart.Data(Double.toString(x+1),1 ));
            }
            else {
                seriesForDifferentialManchester.getData().add(new XYChart.Data(Double.toString(x),1 ));
                seriesForDifferentialManchester.getData().add(new XYChart.Data(Double.toString(x+0.5),1 ));
                seriesForDifferentialManchester.getData().add(new XYChart.Data(Double.toString(x+0.5),-1 ));
                seriesForDifferentialManchester.getData().add(new XYChart.Data(Double.toString(x+1),-1 ));
            }
            inverted = !inverted;
        }
        else {
            if(inverted){
                seriesForDifferentialManchester.getData().add(new XYChart.Data(Double.toString(x),1 ));
                seriesForDifferentialManchester.getData().add(new XYChart.Data(Double.toString(x+0.5),1 ));
                seriesForDifferentialManchester.getData().add(new XYChart.Data(Double.toString(x+0.5),-1 ));
                seriesForDifferentialManchester.getData().add(new XYChart.Data(Double.toString(x+1),-1 ));

            }
            else {
                seriesForDifferentialManchester.getData().add(new XYChart.Data(Double.toString(x), -1));
                seriesForDifferentialManchester.getData().add(new XYChart.Data(Double.toString(x + 0.5), -1));
                seriesForDifferentialManchester.getData().add(new XYChart.Data(Double.toString(x + 0.5), 1));
                seriesForDifferentialManchester.getData().add(new XYChart.Data(Double.toString(x + 1), 1));
            }

        }
    }
    void errorPage(){
        Alert alert = new Alert(Alert.AlertType.ERROR, "please choose a valid number between 1 - 10", ButtonType.OK);
        alert.showAndWait();
    }
    @FXML
    void loadButtonClicked(ActionEvent event) throws IOException {


        String tempString = maxBytesTextField.getText();
        for(int i =0 ; i < tempString.length();i++){
            if(tempString.charAt(i)<'0' || tempString.charAt(i)>'9'){
                errorPage();
                return ;
            }
        }
        if(tempString.length()>2){
            errorPage();
            return;
        }
        maxBytesInPhase  = Integer.parseInt(tempString) ;
        phaseNumber = 0 ;
        inverted = false ;
        previousPhaseButton.setVisible(false);



        inputLineChart.getData().clear();
        manchesterLineChart.getData().clear();
        differentialManchesterLineChart.getData().clear();


        seriesForInput= new XYChart.Series();
        seriesForManchester= new XYChart.Series();
        seriesForDifferentialManchester= new XYChart.Series();

        if(fromFileRadioButton.isSelected()){
            FileChooser file_chooser = new FileChooser();
            FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Audio Files","*.wav");
            file_chooser.getExtensionFilters().add(fileExtensions);

            File file = file_chooser.showOpenDialog(main.getStage());

            if (file == null) {
                return ;
            }

            selectedFileLabel.setText(file.getName());

            File f = new File(file.getPath());
            data = new byte[(int) f.length()];
            if(data.length > maxBytesInPhase ){
                nextPhaseButton.setVisible(true);
            }
            else {
                nextPhaseButton.setVisible(false);
            }
            FileInputStream in = new FileInputStream(file);
            in.read(data);
            in.close();
            int x =  0 ;
            start = 0 ;
            end = maxBytesInPhase ;
            for(int i =start ; i < end ; i ++ ){
                Byte temp = data[i];
                for(int q = 0 ;q<8 ;q++){

                    int num = (temp&(128>>q))==0 ?-1 : 1 ;
                    //System.out.print(num==1 ? 1 : 0 );

                    setCharts(num , x);
                    x++ ;
                }
                System.out.println();
            }

        }
        else if(manualInputRadioButton.isSelected()){
            invalidLabel.setVisible(false );
            inputData  = inputTextField.getText();
            boolean valid = true ;
            if(inputTextField.getText()==null || inputTextField.getText().equals("")){
                valid = false;
            }
            for(int i =0 ; i < inputData.length();i++){
                if(inputTextField.getText().charAt(i)!= '1' && inputTextField.getText().charAt(i) != '0' ){
                    valid = false ;
                }
            }
            if(!valid){
                invalidLabel.setVisible(true );
                return ;
            }
            if(inputData.length() >maxBytesInPhase*8 ){
                nextPhaseButton.setVisible(true);
            }
            selectedFileLabel.setText("Manual Input Data : " + inputData );
            invalidLabel.setVisible(false );

            for(int i =0 ; i < Math.min(inputData.length(),maxBytesInPhase*8);i++){
                setCharts((inputData.charAt(i)=='1'?1:-1),i);
            }
        }
        inputLineChart.getData().add(seriesForInput);
        manchesterLineChart.getData().add(seriesForManchester);
        differentialManchesterLineChart.getData().add(seriesForDifferentialManchester);
    }

    @FXML
    void manualIsSelected(ActionEvent event) {
        inputTextField.setVisible(true);
        InputLabel.setVisible(true);
    }

    @FXML
    void fromFileSelected(ActionEvent event) {
        inputTextField.setVisible(false);
        InputLabel.setVisible(false);
    }


    @FXML
    void nextPhaseButtonClicked(ActionEvent event) {
        phaseNumber ++ ;

        inputLineChart.getData().clear();
        manchesterLineChart.getData().clear();
        differentialManchesterLineChart.getData().clear();


        seriesForInput= new XYChart.Series();
        seriesForManchester= new XYChart.Series();
        seriesForDifferentialManchester= new XYChart.Series();

        start = phaseNumber*maxBytesInPhase;
        end   =  (phaseNumber+1)*maxBytesInPhase;;
        previousPhaseButton.setVisible(true);
        if(fromFileRadioButton.isSelected()){
            int x =  0 ;

            for(int i =start ; i < Math.min(end,data.length) ; i ++ ){
                Byte temp = data[i];
                for(int q = 0 ;q<8 ;q++){

                    int num = (temp&(128>>q))==0 ?-1 : 1 ;
                    setCharts(num , x);
                    x++ ;
                }
            }
        }
        else if(manualInputRadioButton.isSelected()){
            if(end*8>=inputData.length()){
                nextPhaseButton.setVisible(false );
            }
            for(int i =start*8 , x = 0 ; i < Math.min(end*8,inputData.length());i++,x++){

                //System.out.print(i + " " + end+" " +inputData.length());
                //System.out.println(i);
                setCharts((inputData.charAt(i)=='1'?1:-1),x);
            }
        }
        invertedArray.add(inverted);
        inputLineChart.getData().add(seriesForInput);
        manchesterLineChart.getData().add(seriesForManchester);
        differentialManchesterLineChart.getData().add(seriesForDifferentialManchester);
    }

    @FXML
    void previousPhaseButtonClicked(ActionEvent event) {
        nextPhaseButton.setVisible(true);
        phaseNumber -- ;
        inverted = invertedArray.get(phaseNumber);
        invertedArray.remove(phaseNumber);

        inputLineChart.getData().clear();
        manchesterLineChart.getData().clear();
        differentialManchesterLineChart.getData().clear();


        seriesForInput= new XYChart.Series();
        seriesForManchester= new XYChart.Series();
        seriesForDifferentialManchester= new XYChart.Series();

        start = phaseNumber*maxBytesInPhase;
        end   =  (phaseNumber+1)*maxBytesInPhase;;
        previousPhaseButton.setVisible(true);
        if(fromFileRadioButton.isSelected()){
            int x =  0 ;

            for(int i =start ; i < Math.min(end,data.length) ; i ++ ){
                Byte temp = data[i];
                for(int q = 0 ;q<8 ;q++){

                    int num = (temp&(128>>q))==0 ?-1 : 1 ;
                    setCharts(num , x);
                    x++ ;
                }
            }
        }
        else if(manualInputRadioButton.isSelected()){
            if(end*8>=inputData.length()){
                nextPhaseButton.setVisible(false );
            }
            for(int i =start*8 , x = 0 ; i < Math.min(end*8,inputData.length());i++,x++){

               // System.out.print(i + " " + end+" " +inputData.length());
                //System.out.println(i);
                setCharts((inputData.charAt(i)=='1'?1:-1),x);
            }
        }
        invertedArray.add(inverted);
        inputLineChart.getData().add(seriesForInput);
        manchesterLineChart.getData().add(seriesForManchester);
        differentialManchesterLineChart.getData().add(seriesForDifferentialManchester);

        if(phaseNumber==0){
            previousPhaseButton.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        phaseNumber = 1;
        invertedArray = new ArrayList<Boolean>();
        previousPhaseButton.setVisible(false);
        nextPhaseButton.setVisible(false);
        invertedArray.add(false);
        inputTextField.setVisible(false);
        InputLabel.setVisible(false);
        invalidLabel.setVisible(false );

        final ToggleGroup group = new ToggleGroup();

        fromFileRadioButton.setToggleGroup(group);
        fromFileRadioButton.setSelected(true);

        manualInputRadioButton.setToggleGroup(group);
    }
}
