/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bso_dm_2017;

//import static bso.Main.aBlocTimeSearch;
//import static bso.Main.clusteringTime;
import static bso.Main.getCpuTime;
import static bso.Main.getSystemTime;
import static bso.Main.getUserTime;
import static bso.Main.waktRvrai;
import bso.SatData;
import bso.Solution;
import bso.SwarmClassic;
import bsoHybridations.BeeSearchSpaceClusteringR;

import bsoHybridations.ClassificationR;
import bsoHybridations.FrequentItemsetMiningVerticallyR;
import bsoHybridations.RegressionR;
import bsoHybridations.SSL_R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.awt.List;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXToggleButton;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static toolsToManipulateSolutions.UsefulMethods.flip;

/**
 * FXML Controller class
 *
 * @author kadero
 */
public class FirstController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Pane AlgoPane;
    
    @FXML
    private JFXCheckBox BSO_Classic_id;

    @FXML
    private JFXCheckBox BSO_Clustering_id;

    @FXML
    private JFXCheckBox BSO_Regression_id;

    @FXML
    private JFXCheckBox BSO_SSL_id;

    @FXML
    private JFXCheckBox BSO_Classification_id;

    @FXML
    private JFXCheckBox BSO_FIM_id;
    
    @FXML
    private JFXButton AlgoOk;
    
    @FXML
    private VBox BSOFrame;
   
    @FXML
    private HBox HBOXinstances;
    
    @FXML
    private HBox HBOXresultats;
    
    @FXML
    private JFXTabPane tabs;

    @FXML
    private JFXComboBox<String> heuristic_init;

    @FXML
    private TextField MaxIter;

    @FXML
    private TextField SearchIter;

    @FXML
    private TextField NbrBees;

    @FXML
    private TextField Flip;

    @FXML
    private TextField Chance;

    @FXML
    private TextField instances_path;

    @FXML
    private JFXButton parcourir_instances;

    @FXML
    private TextField results_path;

    @FXML
    private JFXButton parcourir_results;

    @FXML
    private TextField nbExecutionTextField;

    @FXML
    private JFXToggleButton timeoutToggleButton;

    @FXML
    private TextField timeOutTextField;

    @FXML
    private ProgressBar progress;

    @FXML
    private Label progress_label;

    @FXML
    private JFXButton startrun;

    @FXML
    private JFXButton stop_aquisition;
    
    @FXML
    private Button id_afficher;

    @FXML
    private TextField data_path;

    @FXML
    private JFXButton parcourir_data;

    @FXML
    private JFXButton parcourir_model;

    @FXML
    private JFXButton importButton;

    @FXML
    private JFXButton saveButton;

    @FXML
    private TextArea resultTextArea;

    @FXML
    private JFXSpinner resultTextSpinner;

    @FXML
    private TabPane resultsTabPane;

    @FXML
    private PieChart pie;

    @FXML
    private Tab modelTab;

    @FXML
    private AnchorPane evaluationPane;

    @FXML
    private Tab predictionTab;

    @FXML
    private AnchorPane predictionPane;

    @FXML
    private JFXSpinner updatingChartSpinner;

    @FXML
    private HBox ajustementHBox;

    @FXML
    private JFXSlider timeQuality;

    @FXML
    private JFXSlider threshold;

    @FXML
    private JFXComboBox<?> algorithmeComboBox;

    @FXML
    private JFXToggleButton crossValidation;

    @FXML
    private HBox pourcentageHBox;

    @FXML
    private JFXSlider pourcentage;

    @FXML
    private HBox foldsHBox;

    @FXML
    private TextField numFolds;

    @FXML
    private JFXToggleButton featureSelection;

    @FXML
    private JFXToggleButton featureSelectionMode;

    @FXML
    private HBox autoFeaturesHBox;

    @FXML
    private JFXComboBox<?> featureSelectionAlgorithm;

    @FXML
    private HBox manualFeaturesListHBox;

    @FXML
    private JFXButton start_apprentissage;

    @FXML
    private JFXButton stop_apprentissage;

    @FXML
    private JFXNodesList saveNodesList;

    @FXML
    private JFXNodesList importNodesList;

    @FXML
    private JFXButton predictButton;

    @FXML
    private JFXButton evalButton;
    
     @FXML
    private JFXButton ViewGraphiButtonid;
     
     @FXML
    private JFXButton AfficheGraphhId;
     
     @FXML
    private JFXButton Annuler_Exec;

    @FXML
    private BarChart<String,Number> chartTempsId;

    @FXML
    private BarChart<String,Number> ChartQualiteId;
    
      @FXML
    private TextArea SmacResultsId;

     @FXML
     private JFXButton AfficheResultSmacId;
     
     @FXML
     private JFXButton arreterID;


    @FXML
    void cancelRunning(ActionEvent event) {

    }

    @FXML
    void closeWindow(ActionEvent event) {

    }

    @FXML
    void evaluationDataFilesChooser(ActionEvent event) {

    }

 

    @FXML
    void learnerParamsChange(ActionEvent event) {

    }

    @FXML
    void minimizeWindow(ActionEvent event) {

    }

    
    @FXML
    void resultsFilesChooser(ActionEvent event) {

    }
  

    @FXML
    void validationToggleChanged(ActionEvent event) {

    }
    
    @FXML
    void ViewSmacResult(ActionEvent event) {
        
        
                 
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run()  {
                    FileReader flux= null;
                    BufferedReader input= null;
                    String str;
                    String str2=""; //je rajoute un string qui va servir a concatener les info
                
                try{
                    
                    flux= new FileReader ("benchmarks/Test.txt");
                    
                    input= new BufferedReader( flux);
                    
                    System.out.println("jmen sors comme une grande !");
                     
                    
                    while((str=input.readLine())!=null)
                    {
                        str2+=str+"\n"; // je concatene les info
                         
                    }
                    SmacResultsId.setText(str2);
                } catch (IOException e1)
                {
                    System.out.println("Impossible d'ouvrir le fichier : " +e1.toString());
                }
                finally{
            try {
                flux.close();
            } catch (IOException ex) {
                System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
            }
            
             }
                
            }
        }, 0, 5, TimeUnit.SECONDS);

    }

   
    @FXML
    void ViewGraphFunction1(ActionEvent event) {
        
          
            
             XYChart.Series series1 = new XYChart.Series();
            //series1.setName("Temps");       
            series1.getData().add(new XYChart.Data("BSO", TimeBSO));
            series1.getData().add(new XYChart.Data("BSOCA",TimeBSOCA));
            series1.getData().add(new XYChart.Data("BSOCE", TimeBSOCE));
            series1.getData().add(new XYChart.Data("BSOCAV", TimeBSOCAV));
            series1.getData().add(new XYChart.Data("BSOCEV",TimeBSOCEV)); 
             chartTempsId.getData().add(series1);
             
            XYChart.Series series2 = new XYChart.Series();
           // series2.setName("Qualité");
            series2.getData().add(new XYChart.Data("BSO", QualitéBSO));
            series2.getData().add(new XYChart.Data("BSOCA", QualitéBSOCA));
            series2.getData().add(new XYChart.Data("BSOCE", QualitéBSOCE));
            series2.getData().add(new XYChart.Data("BSOCAV", QualitéBSOCAV));
            series2.getData().add(new XYChart.Data("BSOCEV", QualitéBSOCEV)); 
             ChartQualiteId.getData().add(series2);
             //AfficheGraphhId.setVisible(false);
             
        

    }
    
    @FXML
    void ViewGraphFunction(ActionEvent event) throws IOException {
        
           
          /*  final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            final CategoryAxis xAxis1 = new CategoryAxis();
            final NumberAxis yAxis1 = new NumberAxis();
            final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
            final BarChart<String, Number> bc1 =new BarChart<String, Number>(xAxis1, yAxis1);
            
            
        bc.setTitle("Temps");
        xAxis.setLabel("Algorithme");       
        yAxis.setLabel("Temps");
 
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Temps");       
        series1.getData().add(new XYChart.Data("BSO", TimeBSO));
        series1.getData().add(new XYChart.Data("BSOCA",TimeBSOCA));
        series1.getData().add(new XYChart.Data("BSOCE", TimeBSOCE));
        series1.getData().add(new XYChart.Data("BSOCAV", TimeBSOCAV));
        series1.getData().add(new XYChart.Data("BSOCEV",TimeBSOCEV));    
        
        bc1.setTitle("Qualité");
        xAxis1.setLabel("Algorithme");
        yAxis1.setLabel("Qualité");
       
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Qualité");
        series2.getData().add(new XYChart.Data("BSO", QualitéBSO));
        series2.getData().add(new XYChart.Data("BSOCA", QualitéBSOCA));
        series2.getData().add(new XYChart.Data("BSOCE", QualitéBSOCE));
        series2.getData().add(new XYChart.Data("BSOCAV", QualitéBSOCAV));
        series2.getData().add(new XYChart.Data("BSOCEV", QualitéBSOCEV));  */
      /*  XYChart.Series series2 = new XYChart.Series();
        series2.setName("2004");
        series2.getData().add(new XYChart.Data(austria, 57401.85));
        series2.getData().add(new XYChart.Data(brazil, 41941.19));
        series2.getData().add(new XYChart.Data(france, 45263.37));
        series2.getData().add(new XYChart.Data(italy, 117320.16));
        series2.getData().add(new XYChart.Data(usa, 14845.27));  
        
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("2005");
        series3.getData().add(new XYChart.Data(austria, 45000.65));
        series3.getData().add(new XYChart.Data(brazil, 44835.76));
        series3.getData().add(new XYChart.Data(france, 18722.18));
        series3.getData().add(new XYChart.Data(italy, 17557.31));
        series3.getData().add(new XYChart.Data(usa, 92633.68));  */
        
        /*  chartTempsId.getData().add(series2);
          Scene scene  = new Scene(bc1,800,600);
          
          bc1.getData().addAll(series2);
          stage.setScene(scene);
          stage.show();*/
    }
    
      @FXML
    void show_results(ActionEvent event) throws IOException {
                   Stage home = (Stage)((Node)event.getSource()).getScene().getWindow();
                   Parent root = FXMLLoader.load(getClass().getResource("FXMLResultsView.fxml"));
                    Scene scene = new Scene(root);
               

                  home.hide(); //optional
                  home.setScene(scene);
                   home.show();

    }
    ////// parcourir files ana zedtha
           
    java.util.List<File> files;
    @FXML public void instancesFilesChooser() {
       
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Instance SAT","*.cnf"));

        files = fileChooser.showOpenMultipleDialog(Bso_DM_2017.stage);
        String res = "";
        for (File file :files) {
            
            res = res.concat(file.toString());
            System.out.println(file.toString());
        }
      
        // System.out.println(res);
       String res1= res.replace("\\","/"); 
      
        instances_path.setText(res1);
        
       ///System.out.println( res1);

        
    }

    //////////
    
      public static long aBlocTimeSearch = 0;
	public static long clusteringTime = 0;    
	public static long readFileByR = 0;//une variable qui mesure le temmps total prit par le R pour lire le fichier sol.csv.
	public static int best=0;
	public static Solution solutionDepart=null;//quand bsoClassic demarre elle mait la solution initiale ici,et elle sera utilisee pae toutes
	                                           //les autres strategies.
 	public static long waktRnewMethod=0;
	public static long waktRoldMethod=0;
	public static double waktRvrai=0; //c le temps calcule par R lui meme.
	////////////////////////////////////////////////excelFile
	static XSSFWorkbook workbook=null;
	static Row  rowCourant=null,rowCourantTime=null;
	static int []tabQuality=new int [7]; 
	static int []tabTemps=new int[5];
	static int indiceParcourDesTables=0;
	static int rowcourant=1;
	static int createdRow=0;//la methode fusion quand elle cree un row elle mais son num ici pour que la methode write ne l'ecrase pas.
        public static String actualDirectory="C:/Users/Vi Incorporated/Desktop/ici";
        static public ArrayList<resultat> ensemebleresultat= new ArrayList();
        static int index=0;
        static long TimeBSO;
        static int QualitéBSO;
        static long TimeBSOCA;
        static int QualitéBSOCA;
        static long TimeBSOCE;
        static int QualitéBSOCE;
        static long TimeBSOCAV;
        static int QualitéBSOCAV;
        static long TimeBSOCEV;
        static int QualitéBSOCEV;
        static long TimeBSOCLS;
        static long TimeBSOFIM;
        final CategoryAxis xAxis1 = new CategoryAxis();
        final NumberAxis yAxis1 = new NumberAxis();
        ExecutorService Executor;
        public static int minimumQuality=100,maximumQuality=0;

    
    ///////
    
    
    @FXML
    void startRun(ActionEvent event) {
        
        
        
        
     
        
              //  initialiseExcelFile();
        
       
         //System.out.println(chance);
      
                 /*
                int chance = 3;
		int flip = 5;
		int heuristic = 5;
		int maxIter = 3;
		int nBees = 2;
		int nbSearchIter = 2;
         */
         
         
       
    //   SatData satData;
      
       
       
         
         //boucle qui liste le contenu d'un repertoire en instances.
		//File folder = new File(instances_path.getText());
		//File[] listOfFiles = folder.listFiles();

		
		 

		
		        		
	   //bso
                   
           
                         ////////////// paramètres de BSO
                        int heuristic=0;
                        String chance=Chance.getText();
                        String flip=Flip.getText();
                        String iter=MaxIter.getText();
                        String nbr=NbrBees.getText();
                        String search=SearchIter.getText();
                        String heuri=heuristic_init.getValue();
                        
        if(!Chance.getText().equals("")&& !Flip.getText().equals("")&&!MaxIter.getText().equals("")
                &&!NbrBees.getText().equals("")&&!SearchIter.getText().equals(""))
        {

                        //////casting of String to int
                        int chanc=Integer.parseInt(chance);
                        int itr=Integer.parseInt(iter);
                        int flp=Integer.parseInt(flip);
                        int Nbr=Integer.parseInt(nbr);
                        int searchItr=Integer.parseInt(search);
                       // int heuris=Integer.parseInt(heuri);

                        if(heuri=="john1") heuristic=1;
                        else if(heuri=="john2a") heuristic=2;
                        else if(heuri=="john2b") heuristic=3;
                        else if(heuri=="recherche locale") heuristic=4;
                        else if(heuri=="aleatoire") heuristic=5;
                       System.out.println(chanc+"/////"+flp+"//////"+itr+"/////"+Nbr+"//////"+searchItr+"/////"+heuristic);
                        //////
                        
                        
                       //satData = new SatData(new File(instances_path.getText()));
                        bso.SatData satData = new bso.SatData(new File("benchmarks/maxcut-140-630-07-1.cnf"));/// Ceci juste pour les tests

                        indiceParcourDesTables=0;
                        
                        
	
		
              if(BSO_Classic_id.isSelected()) RunBso(satData, heuristic, chanc, flp, itr, Nbr, searchItr);
                 
 		//C2 in R
			 
               if(BSO_Clustering_id.isSelected())  RunClustering(satData, heuristic, chanc, flp, itr, Nbr, searchItr);
			
			
                 //C1-With-C3 in R
                
              if(BSO_Regression_id.isSelected()) RunRegression(satData, heuristic, chanc, flp, itr, Nbr, searchItr);
                     
		
		//C2-With-C3 in R
                         

              if(BSO_SSL_id.isSelected()) RunSSL(satData, heuristic, chanc, flp, itr, Nbr, searchItr);
		 
               // Run classification
               
              if(BSO_Classification_id.isSelected()) RunClassification(satData, heuristic, chanc, flp, itr, Nbr, searchItr);
    
              /// Run FIM strategie
              
              if(BSO_FIM_id.isSelected()) RunFIM(satData, heuristic, chanc, flp, itr, Nbr, searchItr);
        }else
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez remplir tout les paramètres ");

            alert.showAndWait();
        }
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       // FirstDialog();
        setParams();
       /* BSOFrame.setVisible(false);
        HBOXinstances.setVisible(false);
        HBOXresultats.setVisible(false);*/
        

      
    }    
    
    @FXML public void AnnulerExecution() {
        Executor.shutdownNow();
    }
  
    
     private void setParams() {
        
        heuristic_init.getItems().addAll(
            "aleatoire",
            "john1",
            "john2a",
            "john2b",
            "recherche locale"
        );
        heuristic_init.getSelectionModel().select("aleatoire");
    }
  
 
          
     
       
     
                
                
                
                	////////////////////////////////////////////////mesure temps
		public static long getCpuTime( ) {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
		return bean.isCurrentThreadCpuTimeSupported( ) ?
		bean.getCurrentThreadCpuTime( ) : 0L;
		}

		/** Get user time in nanoseconds. */
		public static long getUserTime( ) {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
		return bean.isCurrentThreadCpuTimeSupported( ) ?
		bean.getCurrentThreadUserTime( ) : 0L;
		}

		/** Get system time in nanoseconds. */
		public static long getSystemTime( ) {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
		return bean.isCurrentThreadCpuTimeSupported( ) ?
		(bean.getCurrentThreadCpuTime( ) - bean.getCurrentThreadUserTime( )) : 0L;
		}
                ///////////////////////////////////////////////////////////
                
                
                
                
                
       
            
            
           
            
            //////////////// Run Bso classic
            
           
            
            public void RunBso(bso.SatData satData, int heuristic, int Chance, int Flip, int MaxIter,int NbrBees, int SearchIter )
            {
                   
                     satData = new SatData(new File("benchmarks/maxcut-140-630-0.7-1.cnf"));
			long startCpuTimeNano = getCpuTime( );
			SwarmClassic swarmClassic = new SwarmClassic(satData, heuristic,MaxIter, SearchIter, NbrBees, Flip, Chance);
			swarmClassic.bso();
			long taskCpuTimeNano    = getCpuTime( ) ;
			System.out.println(((taskCpuTimeNano-startCpuTimeNano)/1000000+(waktRvrai*1000)) + " ms ");
			System.out.println("best= "+best);
                 
                    
                        addToResultats("BSO",FirstController.best ,((taskCpuTimeNano-startCpuTimeNano)/1000000) );
                       // addToResultats("BSO",FirstController.best ,((t2-t1)/1000000) );

            }
            
       
            
            
            ////////////////// Run C2 in R
            
            public void RunClustering(SatData satData, int heuristic, int Chance, int Flip, int MaxIter,int NbrBees, int SearchIter)
            {
                
                         Dialog<Dialogue> dialogC2 = new Dialog<>();
                                    dialogC2.setTitle("C2");
                                    dialogC2.setHeaderText("Entrer les paramètres suivants pour la stratégie C2");
                                    dialogC2.setResizable(true);
                                    
                                     Label label21 = new Label("Nombre de clusters: ");
                                     //Label label22 = new Label("NbrToCluster: ");
                                     
                                     TextField text21 = new TextField();
                                     //TextField text22 = new TextField();
                                     
                                     GridPane grid2 = new GridPane();
                                     grid2.setHgap(10);
                                     grid2.setVgap(10);
                                     grid2.setPadding(new Insets(20, 150, 10, 10));
                                     grid2.add(label21, 1, 1);
                                     grid2.add(text21, 2, 1);
                                    // grid2.add(label22, 1, 2);
                                    // grid2.add(text22, 2, 2);
                                     
                                    dialogC2.getDialogPane().setContent(grid2);
                                     
                                    ButtonType buttonTypeOk2 = new ButtonType("Okay", ButtonData.OK_DONE);
                        
                                    dialogC2.getDialogPane().getButtonTypes().add(buttonTypeOk2);
                                    dialogC2.setResultConverter(new Callback<ButtonType, Dialogue>() {

                                        @Override
                                        public Dialogue call(ButtonType c) {
                                            if (c == buttonTypeOk2) {
                                                return new Dialogue(Integer.parseInt(text21.getText()));
                                            }
                                            return null;
                                        }
                                    });
                                    Optional<Dialogue> result2 = dialogC2.showAndWait();
                                         if (result2.isPresent()) {
                                        System.out.println(text21.getText());
                                      //  System.out.println(text22.getText());
                                    }
                                     int hmc2=Integer.parseInt(text21.getText());
                                     //int ntc2=Integer.parseInt(text22.getText());

                   
                        int howManyClusterC2=hmc2;
                        //int nbrToClusterC2=ntc2;
                       
                        
                        
                        /// New method
                        
                         satData = new SatData(new File("benchmarks/scpcyc09_maxsat (2304V).cnf"));
			System.out.println("nbr clauses: " + satData.getClauseCount());
			System.out.println("How many clusters ? (>=1)");
			/*howManyCluster=sc.nextInt();
			while(howManyCluster<1){
				System.out.println("How many clusters ? (>=1)");
				howManyCluster=sc.nextInt();
			}*/
			long startCpuTimeNano2 = getCpuTime( );
			BeeSearchSpaceClusteringR clusterEachBeeSearchSpaceR = new BeeSearchSpaceClusteringR(satData, heuristic,MaxIter, SearchIter, NbrBees, Flip, Chance,10,hmc2);
			clusterEachBeeSearchSpaceR.bso();
			long taskCpuTimeNano2    = getCpuTime( ) ;
			System.out.println(((taskCpuTimeNano2-startCpuTimeNano2)/1000000+(waktRvrai*1000)) + " ms ");
			waktRvrai=0;
			System.out.println("best= "+best);
                    
			 
            }
            
            
            ///////////Run C1 with C3 in R
            public void RunRegression(SatData satData, int heuristic, int Chance, int Flip, int MaxIter,int NbrBees, int SearchIter)
            {
                
                            Dialog<Dialogue> dialogC13 = new Dialog<>();
                                    dialogC13.setTitle("C13");
                                    dialogC13.setHeaderText("Entrer les paramètres suivants pour la stratégie C13");
                                    dialogC13.setResizable(true);
                                    
                                     Label label21 = new Label("Nombre de clusters: ");
                                     Label label22 = new Label("NbrToCluster: ");
                                     Label label33 = new Label("Nombre de parties de solution");
                                     TextField text21 = new TextField();
                                     TextField text22 = new TextField();
                                     TextField text33 = new TextField();
                                     
                                     GridPane grid13 = new GridPane();
                                     grid13.setHgap(10);
                                     grid13.setVgap(10);
                                     grid13.setPadding(new Insets(20, 150, 10, 10));
                                     grid13.add(label21, 1, 1);
                                     grid13.add(text21, 2, 1);
                                     grid13.add(label22, 1, 2);
                                     grid13.add(text22, 2, 2);
                                     grid13.add(label33, 1, 3);
                                     grid13.add(text33, 2, 3);
                                     
                                    dialogC13.getDialogPane().setContent(grid13);
                                     
                                    ButtonType buttonTypeOk13 = new ButtonType("Okay", ButtonData.OK_DONE);
                        
                                    dialogC13.getDialogPane().getButtonTypes().add(buttonTypeOk13);
                                    dialogC13.setResultConverter(new Callback<ButtonType, Dialogue>() {

                                        @Override
                                        public Dialogue call(ButtonType c) {
                                            if (c == buttonTypeOk13) {
                                                return new Dialogue(Integer.parseInt(text21.getText()), Integer.parseInt(text22.getText()));
                                            }
                                            return null;
                                        }
                                    });
                                    Optional<Dialogue> result2 = dialogC13.showAndWait();
                                         if (result2.isPresent()) {
                                        System.out.println(text21.getText());
                                        System.out.println(text22.getText());
                                    }
                                     int hmc13=Integer.parseInt(text21.getText());
                                     int ntc13=Integer.parseInt(text22.getText());
                                     int pcps13=Integer.parseInt(text33.getText());

                
                
                        int howManyClusterC13=hmc13;
                        int nbrToClusterC13=ntc13;
                        int parCombienDePartieDiviserLaSolution_13=pcps13;
                        
                        
                        
                        long startCpuTimeNano8 = getCpuTime( );
			satData = new SatData(new File("benchmarks/scpcyc09_maxsat (2304V).cnf"));
			System.out.println("nbr clauses: " + satData.getClauseCount()+"  optimum:  "+satData.getOptimum());
			RegressionR swarmRegression = new RegressionR(satData, heuristic,MaxIter, SearchIter, NbrBees, Flip, Chance,1,"C:/Users/adel/Desktop/java/bso2015_with_DM/allBeesSolutionsForClassification.csv","C:/Users/adel/Desktop/java/bso2015_with_DM");
			swarmRegression.bso();
			long taskCpuTimeNano8    = getCpuTime( ) ;
			System.out.println(((taskCpuTimeNano8-startCpuTimeNano8)/1000000+(waktRvrai*1000))+ " ms ");
			waktRvrai=0;
                        
                       

            }
            
            /////////////////// RUn C2 with C3 
            public void RunSSL(SatData satData, int heuristic, int Chance, int Flip, int MaxIter,int NbrBees, int SearchIter)
            {
                        Dialog<Dialogue> dialogC23 = new Dialog<>();
                                    dialogC23.setTitle("C23");
                                    dialogC23.setHeaderText("Entrer les paramètres suivants pour la stratégie C23");
                                    dialogC23.setResizable(true);
                                    
                                     Label label21 = new Label("Nombre de clusters: ");
                                     Label label22 = new Label("NbrToCluster: ");
                                     Label label33 = new Label("Nombre de parties de solution");
                                     TextField text21 = new TextField();
                                     TextField text22 = new TextField();
                                     TextField text33 = new TextField();
                                     
                                     GridPane grid23 = new GridPane();
                                     grid23.setHgap(10);
                                     grid23.setVgap(10);
                                     grid23.setPadding(new Insets(20, 150, 10, 10));
                                     grid23.add(label21, 1, 1);
                                     grid23.add(text21, 2, 1);
                                     grid23.add(label22, 1, 2);
                                     grid23.add(text22, 2, 2);
                                     grid23.add(label33, 1, 3);
                                     grid23.add(text33, 2, 3);
                                     
                                    dialogC23.getDialogPane().setContent(grid23);
                                     
                                    ButtonType buttonTypeOk23 = new ButtonType("Okay", ButtonData.OK_DONE);
                        
                                    dialogC23.getDialogPane().getButtonTypes().add(buttonTypeOk23);
                                    dialogC23.setResultConverter(new Callback<ButtonType, Dialogue>() {

                                        @Override
                                        public Dialogue call(ButtonType c) {
                                            if (c == buttonTypeOk23) {
                                                return new Dialogue(Integer.parseInt(text21.getText()), Integer.parseInt(text22.getText()));
                                            }
                                            return null;
                                        }
                                    });
                                    Optional<Dialogue> result2 = dialogC23.showAndWait();
                                         if (result2.isPresent()) {
                                        System.out.println(text21.getText());
                                        System.out.println(text22.getText());
                                    }
                                     int hmc23=Integer.parseInt(text21.getText());
                                     int ntc23=Integer.parseInt(text22.getText());
                                     int pcps23=Integer.parseInt(text33.getText());

                
                
                        int howManyClusterC23=hmc23;
                        int nbrToClusterC23=ntc23;
                        int parCombienDePartieDiviserLaSolution_23=pcps23;
                        
                        
                        
                            
                        long startCpuTimeNano9 = getCpuTime( );
			satData = new SatData(new File("benchmarks/jnh/jnh203.cnf"));
			System.out.println("nbr clauses: " + satData.getClauseCount()+"  optimum:  "+satData.getOptimum());
			SSL_R swarmSSL = new SSL_R(satData, heuristic,MaxIter, SearchIter, NbrBees, Flip, Chance,"C:/Users/adel/Desktop/java/bso2015_with_DM/X.csv","C:/Users/adel/Desktop/java/bso2015_with_DM/Y.csv","C:/Users/adel/Desktop/java/bso2015_with_DM/X_U.csv");
			swarmSSL.bso();
			long taskCpuTimeNano9    = getCpuTime( ) ;
			System.out.println(((taskCpuTimeNano9-startCpuTimeNano9)/1000000+(waktRvrai*1000))+ " ms ");
			waktRvrai=0;
                        
                         

            }
            
            // Run FIM strategie
            
            public void RunFIM(SatData satData, int heuristic, int Chance, int Flip, int MaxIter,int NbrBees, int SearchIter)
            {
                        
                
                    Dialog<Dialogue> dialogFIM = new Dialog<>();
                                    dialogFIM.setTitle("C2");
                                    dialogFIM.setHeaderText("Entrer les paramètres suivants pour la stratégie BSO_FIM");
                                    dialogFIM.setResizable(true);
                                    
                                     Label label21 = new Label("Combien d'itération pour extraire les règles: ");
                                     Label label22 = new Label("le support: ");
                                     
                                     TextField text21 = new TextField();
                                     TextField text22 = new TextField();
                                     
                                     GridPane grid2 = new GridPane();
                                     grid2.setHgap(10);
                                     grid2.setVgap(10);
                                     grid2.setPadding(new Insets(20, 150, 10, 10));
                                     grid2.add(label21, 1, 1);
                                     grid2.add(text21, 2, 1);
                                     grid2.add(label22, 1, 2);
                                     grid2.add(text22, 2, 2);
                                     
                                    dialogFIM.getDialogPane().setContent(grid2);
                                     
                                    ButtonType buttonTypeOk2 = new ButtonType("Okay", ButtonData.OK_DONE);
                        
                                    dialogFIM.getDialogPane().getButtonTypes().add(buttonTypeOk2);
                                     dialogFIM.setResultConverter(new Callback<ButtonType, Dialogue>() {
                                  
                                        @Override
                                        public Dialogue call(ButtonType c) {
                                            if (c == buttonTypeOk2) {
                                              
                                                return new Dialogue(Integer.parseInt(text21.getText()), Double.parseDouble(text22.getText()));
                                            }
                                            return null;
                                        }
                                    });
                                     Optional<Dialogue> result2 = dialogFIM.showAndWait();
                                          if (result2.isPresent()) {
                                        System.out.println(text21.getText());
                                           System.out.println(text22.getText());
                                    }
                                     int EFAHMI=Integer.parseInt(text21.getText());
                                     double supp=Double.parseDouble(text22.getText());  
                  
                        int EFAHMIterations=EFAHMI;
                        double  support=supp;
                             
                
                if(EFAHMIterations<MaxIter && support<1 && support>0)
                {
                
                       
                      satData = new SatData(new File("benchmarks/maxcut-140-630-0.7-1.cnf"));
			System.out.println("nbr clauses: " + satData.getClauseCount());
			long startCpuTimeNano7 = getCpuTime( );
			FrequentItemsetMiningVerticallyR swarmFim = new FrequentItemsetMiningVerticallyR(satData, heuristic,MaxIter, SearchIter, NbrBees, Flip, Chance,support,1,10,EFAHMIterations,"C:/Users/Vi Incorporated/Documents/NetBeansProjects/Bso_DM_2017");
			swarmFim.bso();
			long taskCpuTimeNano7    = getCpuTime( ) ;
			System.out.println(((taskCpuTimeNano7-startCpuTimeNano7)/1000000+(waktRvrai*1000))+ " ms ");
                        addToResultats("FIM",FirstController.best ,((taskCpuTimeNano7-startCpuTimeNano7)/1000000+(long)(waktRvrai*1000)));
                        waktRvrai=0;
                }
                else
                {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erreur");
                    if (EFAHMIterations>MaxIter )
                    {
                         alert.setContentText("Le parametre Combien d'itération pour extraire les règles doit etre inferieur a MaxIter");
                    }
                    if (support>1)
                    {
                    alert.setContentText("La parametre support doit etre inferieur a  1");
                    }
                    if (EFAHMIterations>MaxIter && support>1)
                    {
                        alert.setContentText("Veuillez verifier les parametres");
                    }

                    alert.showAndWait();
                }
                
            }
            
            
            //Run Classification strategie
             public void RunClassification(SatData satData, int heuristic, int Chance, int Flip, int MaxIter,int NbrBees, int SearchIter)
            {
                
                
                
                
                
                
                            Dialog<Dialogue> dialogC2 = new Dialog<>();
                                    dialogC2.setTitle("C2");
                                    dialogC2.setHeaderText("Entrer les paramètres suivants pour la stratégie Classification");
                                    dialogC2.setResizable(true);
                                    
                                     Label label21 = new Label("Aprés combien d'iérations: ");
                                     //Label label22 = new Label("NbrToCluster: ");
                                     
                                     TextField text21 = new TextField();
                                     //TextField text22 = new TextField();
                                     
                                     GridPane grid2 = new GridPane();
                                     grid2.setHgap(10);
                                     grid2.setVgap(10);
                                     grid2.setPadding(new Insets(20, 150, 10, 10));
                                     grid2.add(label21, 1, 1);
                                     grid2.add(text21, 2, 1);
                                    // grid2.add(label22, 1, 2);
                                    // grid2.add(text22, 2, 2);
                                     
                                    dialogC2.getDialogPane().setContent(grid2);
                                     
                                    ButtonType buttonTypeOk2 = new ButtonType("Okay", ButtonData.OK_DONE);
                        
                                    dialogC2.getDialogPane().getButtonTypes().add(buttonTypeOk2);
                                    dialogC2.setResultConverter(new Callback<ButtonType, Dialogue>() {

                                        @Override
                                        public Dialogue call(ButtonType c) {
                                            if (c == buttonTypeOk2) {
                                                return new Dialogue(Integer.parseInt(text21.getText()));
                                            }
                                            return null;
                                        }
                                    });
                                    Optional<Dialogue> result2 = dialogC2.showAndWait();
                                         if (result2.isPresent()) {
                                        System.out.println(text21.getText());
                                      //  System.out.println(text22.getText());
                                    }
                                     int AHMI=Integer.parseInt(text21.getText());
                                     //int ntc2=Integer.parseInt(text22.getText());

                   
                        int AHMIterations=AHMI;
                        //int nbrToClusterC2=ntc2;
                       
                        
                
                
                         long startCpuTimeNano6 = getCpuTime( );
			satData = new SatData(new File("benchmarks/maxcut-140-630-0.7-1.cnf"));
			System.out.println("nbr clauses: " + satData.getClauseCount()+"  optimum:  "+satData.getOptimum());
			ClassificationR swarmClassification = new ClassificationR(satData, heuristic,MaxIter, SearchIter, NbrBees, Flip, Chance,AHMIterations,"C:/Users/Vi Incorporated/Documents/NetBeansProjects/Bso_DM_2017/allBeesSolutionsForClassification.csv","C:/Users/Vi Incorporated/Documents/NetBeansProjects/Bso_DM_2017");
			swarmClassification.bso();
			long taskCpuTimeNano6    = getCpuTime( ) ;
			System.out.println(((taskCpuTimeNano6-startCpuTimeNano6)/1000000+(waktRvrai*1000))+ " ms ");
			
                
                
                
                         addToResultats("Classification",FirstController.best ,((taskCpuTimeNano6-startCpuTimeNano6)/1000000+(long)(waktRvrai*1000)));
                        
                        waktRvrai=0;
                        
                        
                        
                        
                        
            }
            
      
            
            
            public void addToResultats(String strategie, int qualite, long temps)
            {
                resultat res= new resultat(strategie, qualite, temps);
                FirstController.ensemebleresultat.add(index,res);
                index++;
 
            }
            
        
    
}
