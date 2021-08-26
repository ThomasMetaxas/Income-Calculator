package application;

/**
 * @author: Thomas Metaxas
 * @date: Jeudi, 11 Juin 2020
 * @description: Programme permettant de calculer le revenu annuel incluant les dépenses et avec ceux donnés calculer combien d'heures de travail son nécessaire pour payer un produit.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

public class TImeCostController implements Initializable {

    @FXML
    private Label annualSalary;

    @FXML
    private ComboBox<String> salaryTypeCB;

    @FXML
    private Label hourlySalaryText;
    
    @FXML
    private Label hoursPerWeekText;
    
    @FXML
    private Label weekPerYearText;
    
    @FXML
    private TextField weekYears;

    @FXML
    private TextField weekHours;

    @FXML
    private ComboBox<String> currencyCB;

    @FXML
    private TextField hourly;
    
    @FXML
    private ComboBox<String> incomeTaxCB;
    
    @FXML
    private TextField customIncomeTax;
    
    @FXML
    private Label customIncomeTaxText;
    
    @FXML
    private ComboBox<String> countryCB;
    
    @FXML
    private Label annualSalaryTax;
    
    @FXML
    private Label taxAmount;
    
    @FXML
    private ComboBox<String> rentPaymentRateCB;
    
    @FXML
    private TextField rentRate;
    
    @FXML
    private Label annualRentText;
    
    @FXML
    private Label incomeRentText;
    
    @FXML
    private ComboBox<String>otherPaymentRateCB;
    
    @FXML
    private TextField otherPayment;
    
    @FXML
    private Label annualOtherPaymentText;
    
    @FXML
    private Label finalIncomeText;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button restoreButton;
    
    @FXML
    private Button resetButton;
    
    @FXML
    private TextField productName;
    
    @FXML
    private TextField productPrice;
    
    @FXML
    private TextField productQuantity;
    
    @FXML
    private TextField salesTax;
    
    @FXML
    private Label subTotalText;
    
    @FXML
    private Label totalText;
    
    @FXML
    private Label workTimeText;
    
    public static String currency;
    public static String currencySymbol;
    public static String salaryType;
    public static Double hourlyIncome;
    public static Double hoursPerWeek;
    public static Double weeksPerYear;
    public static Double yearlyIncome;
    public static String incomeTaxType;
    public static Double customIncomeTaxRate;
    public static String country;
    public static Double incomeTax;
    public static Double taxIncome;
    public static String rentPeriod;
    public static Double rentPrice;
    public static Double yearlyRent;
    public static Double rentIncome;
    public static String otherPaymentRate;
    public static Double otherPaymentPrice;
    public static Double yearlyOtherPayment;
    public static Double finalIncome;
    
    public static String name;
    public static int quantity;
    public static Double price;
    public static Double tax;
    public static Double subTotal;
    public static Double total;
    public static Double workTime;
    public static Double hourlyIncomeTax;
    
    Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
    
    private ObservableList<String> currencyList = (ObservableList<String>) FXCollections.observableArrayList("Dollar Américain ($)", "Dollar Canadien ($)", "Dollar Australien ($)", "Euro (€)", "Livre Britannique (£)", "Yuan Chinois (¥)", "Yen Japonnais (¥)", "Rouble Russe (₽)", "Peso Mexicain ($)", "Autre ($)");
    private ObservableList<String> salaryTypeList = (ObservableList<String>) FXCollections.observableArrayList("Salaire horaire", "Salaire annuel");
    private ObservableList<String> incomeTaxTypeList = (ObservableList<String>) FXCollections.observableArrayList("% Simple", "% Avancée");
    private ObservableList<String> countryList = (ObservableList<String>) FXCollections.observableArrayList("Canada", "États-Unis");
    private ObservableList<String> paymentRate = (ObservableList<String>) FXCollections.observableArrayList("Par jour", "Par semaine", "Par mois", "Par année");
    
    public static Product productData = new Product(name, price, quantity, tax);
    
    /**
     * initialize() définit la liste d'objet pour les ComboBox et appelle sur la fonction loadSave() pour chargé les données sauvegarder par l'utilisateur.
     */
    
    public void initialize(URL location, ResourceBundle resources) { //Charger les données dans les ComboBox
    	
		currencyCB.setItems(currencyList);
		salaryTypeCB.setItems(salaryTypeList);
		incomeTaxCB.setItems(incomeTaxTypeList);
		rentPaymentRateCB.setItems(paymentRate);
		otherPaymentRateCB.setItems(paymentRate);
		
		loadSave(); //Charger les données sauvegarder quand l'utilisateur ouvre la session
		
    }
    
    /**
     * @param event Détermine la monnaie utilisé par l'utilisateur basé sur son choix dans le ComboBox currencyCB.
     */
    
   @FXML
   void chooseCurrency(ActionEvent event) { //Définir la monnaie et son symbol
    	
    	currency = currencyCB.getValue();
    	currencySymbol = currency.substring(currency.length() - 2);
    	currencySymbol = currencySymbol.substring(0, currencySymbol.length() - 1);
    	findYearlyIncome();
    	
    }

   /**
    * @param event Détermine si l'utilisateur a un salaire horaire ou annuel basé sur son choix dans le ComboBoc salaryTypeCB.
    */
   
    @FXML
    void chooseSalaryType(ActionEvent event) { //Choisir si le salaire est horaire ou annuel

    	salaryType = salaryTypeCB.getValue();
    	
    	if (salaryType == "Salaire annuel") {
    		hourlySalaryText.setText("Salaire annuel");
    		annualSalary.setText("Salaire horaire:");
    	} else {
    		hourlySalaryText.setText("Salaire horaire");
    		annualSalary.setText("Salaire annuel:");
    	}
    	
    }
    
    /**
     * @param event Détermine le salaire horaire ou annuel de l'utilisateur basé sur leur réponse a la question précedente et les données entrés. La fonction est déclenché après que l'utilisateur relache la clé du clavier.
     */
    
    @FXML
    void setHourly(KeyEvent event) { //Déterminer le salaire horaire/annuel
    	
    	number(hourly);
    	if (salaryType == "Salaire annuel") {
    		yearlyIncome = Double.parseDouble(hourly.getText());
    		findHourlyIncome();
    	} else {
    		hourlyIncome = Double.parseDouble(hourly.getText());
    		findYearlyIncome();
    	}
    	
    }
    
    /**
     * @param event Détermine le nombre d'heures que l'utilisateur travaille par semaine basé sur les données entrés par l'utilisateur.
     */
    
    @FXML
    void setWeekHours(KeyEvent event) { //Choisir le nombre d'heures de travail par semaine
    	
    	number(weekHours);
    	hoursPerWeek = Double.parseDouble(weekHours.getText());
    	if (salaryType == "Salaire annuel") {
    		findHourlyIncome();
    	} else {
    		findYearlyIncome();
    	}
    	
    }
    
    /**
     * @param event Détermine le nombre de semaine par année durant lesquelles l'utilisateur est payé basé sur son information entré.
     */
    
    @FXML
    void setWeekYears(KeyEvent event) { //Choisir le nombre de semaines payés par année
    	
    	number(weekYears);
    	if (Double.parseDouble(weekYears.getText()) >= 53) {
    		weekYears.setText(null);
    	}
    	weeksPerYear = Double.parseDouble(weekYears.getText());
    	if (salaryType == "Salaire annuel") {
    		findHourlyIncome();
    	} else {
    		findYearlyIncome();
    	}
    	
    }

    /**
     * findYearlIncome() calcule le salaire annuel de l'utilisateur si l'option "Salaire horaire" a été choisit dans la fonction chooseSalaryType(). Cette fonction utilise toutes les données précedentes pour le salaire.
     */
    
    @FXML
    void findYearlyIncome() { //Calculer le salaire annuel
    	
    	if ((hourlyIncome != null) && (hoursPerWeek != null) && (weeksPerYear != null)) {
    		yearlyIncome = hourlyIncome*hoursPerWeek*weeksPerYear;
    		annualSalary.setText("Salaire annuel (avant impôts): " + yearlyIncome + currencySymbol);
    	}
    	
    }
    
    /**
     * findHourlyIncome() calcule le salaire annuel de l'utilisateur si l'option "Salaire annuel" a été choisit dans la fonction chooseSalaryType(). Cette fonction utilise toutes les données précedentes pour le salaire.
     */
    
    @FXML
    void findHourlyIncome() { //Calculer le salaire horaire
    	
    	if ((yearlyIncome != null) && (hoursPerWeek != null) && (weeksPerYear != null)) {
    		hourlyIncome = yearlyIncome/weeksPerYear/hoursPerWeek;
    		annualSalary.setText("Salaire horaire: " + hourlyIncome + currencySymbol);
    	}
    	
    }
    
    /**
     * @param event Définir le type d'impot sur le revenu a partir du ComboBox incomeTaxCB, soit "% Simple" un pourcentage fixe ce qui fait apparaitre un TextField pour la prochaine étape, ou "% Avancée" le vrai système d'impot.
     */
    
    @FXML
    void chooseIncomeTax(ActionEvent event) { //Choisir le type d'impot sure le revenu
    	
    	incomeTaxType = incomeTaxCB.getValue();
    	
    	if (incomeTaxType.equals("% Simple")) {
    		customIncomeTaxText.setText("Impot (%)");
    		customIncomeTax.setVisible(true);
    		countryCB.setVisible(false);
  
    	} else {
    		customIncomeTaxText.setText("Pays");
    		customIncomeTax.setVisible(false);
    		countryCB.setVisible(true);
    		countryCB.setItems(null);
    		countryCB.setItems(countryList);
    	}
    	
    }
    
    /**
     * @param event Définir le pourcentage fixe d'impot sur le revenu si l'option "% Simple" a été selectionné dans la fonction chooseIncomeTax().
     */
    
    @FXML
    void setCustomIncomeTax(KeyEvent event) { //Choisir le % d'impot sur le revenu
    	
    	number(customIncomeTax);
    	customIncomeTaxRate = Double.parseDouble(customIncomeTax.getText());
    	findIncomeTax();
    	
    }
    
    /**
     * @param event Définir le pays de l'utilisateur si l'option "% Avancée" a été selectionné dans la fonction chooseIncomeTax(), pour que le système d'impot du pays selectionné, soit le Canada ou les États-Unis soit appliqué.
     */
    
    @FXML
    void chooseCountry(ActionEvent event) { //Choisir le pays
    	
    	country = countryCB.getValue();
    	findIncomeTax();
    	
    }
    
    /**
     * findIncomeTax() calcule le montant d'impot sur le revenu a payer basé sur l'information du salaire d'auparavant et le système d'impôt choisit.
     */
    
    @FXML
    void findIncomeTax() { //Calculer le montant d'impot sur le revenu
    	
    	if (incomeTaxType.equals("% Simple")) {
    	
    		incomeTax = yearlyIncome*(customIncomeTaxRate/100);
    		taxIncome = yearlyIncome + incomeTax;
    		
    	} else {
    		
    		if (country == "Canada") {
    			
    			if (yearlyIncome <= 48535) {
    				incomeTax = yearlyIncome*0.15;
    			} else if (yearlyIncome > 48535 && yearlyIncome <= 97069) {
    				incomeTax = 48535*0.15 + (yearlyIncome - 48535)*0.205;
    			} else if (yearlyIncome > 97069 && yearlyIncome <= 150473) {
    				incomeTax = 48535*0.15 + 48534*0.205 + (yearlyIncome - 97069)*0.26;
    			} else if (yearlyIncome > 150473 && yearlyIncome <= 214368) {
    				incomeTax = 48535*0.15 + 48534*0.205 + 53404*0.26 + (yearlyIncome - 150473)*0.29;
    			} else if (yearlyIncome > 214638) {
    				incomeTax = 48535*0.15 + 48534*0.205 + 53404*0.26 + 63895*0.29 + (yearlyIncome - 214638)*0.33;
    			}
    			taxIncome = yearlyIncome - incomeTax;
    			
    		} else if (country == "États-Unis") {
    			
    			if (yearlyIncome <= 9875) {
    				incomeTax = yearlyIncome*0.1;
    			} else if (yearlyIncome > 9875 && yearlyIncome <= 40125) {
    				incomeTax = 9875*0.1 + (yearlyIncome - 9875)*0.12;
    			} else if (yearlyIncome > 40125 && yearlyIncome <= 85525) {
    				incomeTax = 9875*0.1 + 30250*0.12 + (yearlyIncome - 40125)*0.22;
    			} else if (yearlyIncome > 85525 && yearlyIncome <= 163300) {
    				incomeTax = 9875*0.1 + 30250*0.12 + 45399*0.22 + (yearlyIncome - 85225)*0.24;
    			} else if (yearlyIncome > 163300 && yearlyIncome <= 207350) {
    				incomeTax = 9875*0.1 + 30250*0.12 + 45399*0.22 + 77775*0.24 + (yearlyIncome - 163300)*0.32;
    			} else if (yearlyIncome > 207350 && yearlyIncome <= 518400) {
    				incomeTax = 9875*0.1 + 30250*0.12 + 45399*0.22 + 77775*0.24 + 44050*32 + (yearlyIncome - 207350)*0.35;
    			} else if (yearlyIncome > 518400) {
    				incomeTax = 9875*0.1 + 30250*0.12 + 45399*0.22 + 77775*0.24 + 44050*32 + 311050*35 + (yearlyIncome - 518400)*0.37;
    			}
    			taxIncome = yearlyIncome - incomeTax;
    		}
    	
    	}
 
    	taxAmount.setText("Impôts a payer: " + incomeTax);
    	annualSalaryTax.setText("Salaire annuel (après impôts): " + taxIncome);
    	
    }
    
    /**
     * @param event Définit le taux de paiement du loyer de l'utilisateur avec rentPaymentRateCB, soit par jour, par semaine, par mois ou par année
     */
    
    @FXML
    void chooseRentPaymentRate(ActionEvent event) { //Choisir le taux de loyer
    	
    	rentPeriod = rentPaymentRateCB.getValue();
    	calculateRent();
    	
    }
    
    /**
     * @param event Détermine le prix du loyer de l'utilisateur avec les données entrées dans le TextField rentRate par l'utilisateur.
     */
    
    @FXML
    void setRentRate(KeyEvent event) { //Déterminer le prix du loyer
    	
    	number(rentRate);
    	rentPrice = Double.parseDouble(rentRate.getText());
    	calculateRent();
    	
    }
    
    /**
     * calculateRent() calcule le loyer annuel en prennant le prix du loyer de l'utilisateur et le multipliant dépendant du taux de paiement.
     */
    
    @FXML
    void calculateRent() { //Calculer le loyer annuel
    	
    	if (rentPeriod.equals("Par jour")) {
    		yearlyRent = rentPrice*7*52;
    	} else if (rentPeriod.equals("Par semaine")) {
    		yearlyRent = rentPrice*52;
    	} else if (rentPeriod.equals("Par mois")) {
    		yearlyRent = rentPrice*12;
    	} else if (rentPeriod.equals("Par année")) {
    		yearlyRent = rentPrice;
    	}
    	
    	rentIncome = taxIncome - yearlyRent;
    	
    	annualRentText.setText("Prix de loyer annuel: " + yearlyRent);
    	incomeRentText.setText("Salaire annuel (après loyer): " + rentIncome);
    	
    }
    
    /**
     * @param event Détermine le taux d'autres dépenses de l'utilisateur avec le ComboBox otherPaymentRateCB et les mêmes choix que le taux du loyer.
     */
    
    @FXML
    void chooseOtherPaymentRate(ActionEvent event) { //Choisir le taux d'autres dépenses
    	
    	otherPaymentRate = otherPaymentRateCB.getValue();
    	calculateOtherPayment();
    	
    }
    
    /**
     * @param event Déterminer le motant d'autres dépenses basés sur l'information entré par l'utilisateur dans otherPayment.
     */
    
    @FXML
    void setOtherPayment(KeyEvent event) { //Déterminer le montant des autres dépenses
    	
    	number(otherPayment);
    	otherPaymentPrice = Double.parseDouble(otherPayment.getText());
    	calculateOtherPayment();
    	
    }
    
    /**
     * calculateOtherPayment() calcule le prix annuel des autres dépenses en prennant le prix des dépenses de l'utilisateur et le multipliant dépendant du taux de paiement.
     */
    
    @FXML
    void calculateOtherPayment() { //Calculer les dépenses annuels
    	
    	if (otherPaymentRate.equals("Par jour")) {
    		yearlyOtherPayment = otherPaymentPrice*7*52;
    	} else if (otherPaymentRate.equals("Par semaine")) {
    		yearlyOtherPayment = otherPaymentPrice*52;
    	} else if (otherPaymentRate.equals("Par mois")) {
    		yearlyOtherPayment = otherPaymentPrice*12;
    	} else if (otherPaymentRate.equals("Par année")) {
    		yearlyOtherPayment = otherPaymentPrice;
    	}
    	
    	finalIncome = rentIncome - yearlyOtherPayment;
    	
    	annualOtherPaymentText.setText("Prix d'autres dépenses annuel: " + yearlyOtherPayment);
    	finalIncomeText.setText("Salaire annuel final: " + finalIncome);
    	
    }
    
    /**
     * @param event Détermine le nom du produit que l'utilisateur veut acheter avec l'information entré.
     */
    
    @FXML
    void setProductName(KeyEvent event) { //Déterminer le nom du produit
    	
    	name = productName.getText();
    	
    }
    
    /**
     * @param event Détermine le prix du produit choisis par l'utilisateur avec les données entrées.
     */
    
    @FXML
    void setProductPrice(KeyEvent event) { //Déterminer le prix du produit
    	
    	number(productPrice);
    	price = Double.parseDouble(productPrice.getText());
    	findSubTotal();
    	
    }
    
    /**
     * @param event Déterminer la quantité du produit avec les données entrées dans productQuantity.
     */
    
    @FXML
    void setProductQuantity(KeyEvent event) { //Déterminer la quantité de produit
    	
    	number(productQuantity);
    	quantity = Integer.parseInt(productQuantity.getText());
    	findSubTotal();
    	
    }
    
    /**
     * findSubTotal() calcule le sous total du produit en multipliant le prix par la quantité.
     */
    
    @FXML
    void findSubTotal() { //Calculer le sous total
    	
    	if ((name != null) && (price != null)) {
    		subTotal = price*quantity;
    		subTotalText.setText("Sous total: " + subTotal);
    		findTotal();
    	}
    	
    }
    
    /**
     * @param event Détermine le % de taxe de vente avec l'information entré dans salesTax.
     */
    
    @FXML
    void setSalesTax(KeyEvent event) { //Déterminer le % de taxe de vente
    	
    	number(salesTax);
    		tax = Double.parseDouble(salesTax.getText());
    		tax = tax/100 + 1;
    	findTotal();
    	
    }
    
    /**
     * finTotal() calcule le total du produit en multipliant le sous total fois la taxe de vente.
     */
    
    @FXML
    void findTotal() { //Calculer le prix total
    	
    	total = subTotal*tax;
    	totalText.setText("Total: " + total);
    	findWorkTime();
    	
    }
    
    /**
     * findWorkTime() calcule le nombre d'heures de travail nécessaire pour pouvoir payé le produit utilisant le salaire final annuel déterminer auparavant et toutes les autres données. 
     */
    
    @FXML
    void findWorkTime() { //Trouver le nombre d'heure de travaille nécessaire
    	
    	if (subTotal != null) {
    		
    		hourlyIncomeTax = taxIncome/weeksPerYear/hoursPerWeek;
    		workTime = total/hourlyIncomeTax;
    		workTimeText.setText("Heures de travail nécéssaire: " + workTime);
    		
    	}
    	
    }
    
    /**
     * @param text La fonction vérifie si les données entréés contiennent des charactères qui ne sont pas des numéros ou un point et les effaces.
     * La variable text est déterminé chaque fois que la fonction est appelé avec la valeur du texte entré dans les TextField.
     */
    
    @FXML
    private void number(TextField text) { //Seulement accepter des nombre
    	
    	text.textProperty().addListener((observable, oldValue, newValue)-> {
    		
    		if (!newValue.matches("^-?[0=9](\\.[0-9]+)?$")) {
    			text.setText(newValue.replaceAll("[^\\d*\\.\\-]",""));
    		}
    		
    	});
    	
    }
    
    /**
     * save() Sauvegarde les données de l'utilisateur sur son salaire annuel dans les préférences.
     */
    
    @FXML
    void save() { //Sauvegarder
    	
    	prefs.put("currencySymbolSave", currencySymbol);
    	prefs.put("salaryTypeSave", salaryType);
    	prefs.put("currencySave", currency);
    	prefs.putDouble("hourlyIncomeSave", hourlyIncome);
    	prefs.putDouble("hoursPerWeekSave", hoursPerWeek);
    	prefs.putDouble("weeksPerYearSave", weeksPerYear);
    	prefs.putDouble("yearlyIncomeSave", yearlyIncome);
    	
    	prefs.put("incomeTaxTypeSave", incomeTaxType);
    	if (incomeTaxType.equals("% Simple")) {
    		prefs.putDouble("customeIncomeTaxRateSave", customIncomeTaxRate);
    	} else {
    		prefs.put("countrySave", country);
    	}
    	prefs.putDouble("incomeTaxSave", incomeTax);
    	prefs.putDouble("taxIncomeSave", taxIncome);
    	
    	prefs.put("rentPeriodSave", rentPeriod);
    	prefs.putDouble("rentPriceSave", rentPrice);
    	prefs.putDouble("yearlyRentSave", yearlyRent);
    	prefs.putDouble("rentIncomeSave", rentIncome);
    	
    	prefs.put("otherPaymentRateSave", otherPaymentRate);
    	prefs.putDouble("otherPaymentPriceSave", otherPaymentPrice);
    	prefs.putDouble("yearlyOtherPaymentSave", yearlyOtherPayment);
    	prefs.putDouble("finalIncomeSave", finalIncome);
    	
    }
    
    /**
     * loadSave() Charge les données sauvegarder dans save() et ajoutent les données dans les TextFields Labels et ComboBoxs.
     */
    
    @FXML
    void loadSave() { //Charger la sauvegarde
    	
    	currency = prefs.get("currencySave", currency);
		currencySymbol = prefs.get("currencySymbolSave", currencySymbol);
		salaryType = prefs.get("salaryTypeSave", salaryType);
		hourlyIncome = prefs.getDouble("hourlyIncomeSave", 0);
		hoursPerWeek = prefs.getDouble("hoursPerWeekSave", 0);
		weeksPerYear = prefs.getDouble("weeksPerYearSave", 0);
		yearlyIncome = prefs.getDouble("yearlyIncomeSave", 0);
		
		incomeTaxType = prefs.get("incomeTaxTypeSave", incomeTaxType);
		if ("incomeTaxType".equals("% Simple")) {
			customIncomeTaxRate = prefs.getDouble("customIncomeTaxRateSave", 0);
    	} else {
    		country = prefs.get("countrySave", country);
    	}
		incomeTax = prefs.getDouble("incomeTaxSave", 0);
    	taxIncome = prefs.getDouble("taxIncomeSave", 0);
    	
    	rentPeriod = prefs.get("rentPeriodSave", rentPeriod);
    	rentPrice = prefs.getDouble("rentPriceSave", 0);
    	yearlyRent = prefs.getDouble("yearlyRentSave", 0);
    	rentIncome = prefs.getDouble("rentIncomeSave", 0);
    	
    	otherPaymentRate = prefs.get("otherPaymentRateSave", otherPaymentRate);
    	otherPaymentPrice = prefs.getDouble("otherPaymentPriceSave", 0);
    	yearlyOtherPayment = prefs.getDouble("yearlyOtherPaymentSave", 0);
    	finalIncome = prefs.getDouble("finalIncomeSave", 0);
		
		currencyCB.getSelectionModel().select(prefs.get("currencySave", currency));
		salaryTypeCB.getSelectionModel().select(prefs.get("salaryTypeSave", salaryType));
		incomeTaxCB.getSelectionModel().select(prefs.get("incomeTaxTypeSave", incomeTaxType));
		rentPaymentRateCB.getSelectionModel().select(prefs.get("rentPeriodSave", rentPeriod));
		otherPaymentRateCB.getSelectionModel().select(prefs.get("otherPaymentRateSave", otherPaymentRate));
		
		if (salaryType.equals("Salaire horaire")) {
			
			annualSalary.setText("Salaire annuel: " + yearlyIncome + currencySymbol);
			hourly.setText(hourlyIncome.toString());
			
		} else {
			
			annualSalary.setText("Salaire horaire: " + hourlyIncome + currencySymbol);
			hourly.setText(yearlyIncome.toString());
			
		}
		
		weekHours.setText(hoursPerWeek.toString());
		weekYears.setText(weeksPerYear.toString());
		
		if ("incomeTaxType".equals("% Simple")) {
			customIncomeTax.setVisible(true);
    		countryCB.setVisible(false);
    		customIncomeTax.setText(customIncomeTax.toString());
    	} else {
    		customIncomeTax.setVisible(false);
    		countryCB.setVisible(true);
    		countryCB.getSelectionModel().select(prefs.get("countrySave", country));
    	}
		
		taxAmount.setText("Impôts a payer: " + incomeTax);
		annualSalaryTax.setText("Salaire annuel (après impôts): " + taxIncome);
		
		rentRate.setText(rentPrice.toString());
		annualRentText.setText("Prix de loyer annuel: " + yearlyRent);
		incomeRentText.setText("Salaire annuel (après loyer): " + rentIncome);
		
		otherPayment.setText(otherPaymentPrice.toString());
		annualOtherPaymentText.setText("Prix d'autres dépenses annuel: " + yearlyOtherPayment);
		finalIncomeText.setText("Salaire annuel final: " + finalIncome);
    	
    }
    
    /**
     * Remet toutes les données sur le salaire annuel à 0 incluant les TextFields et ComboBoxs.
     */
    
    @FXML
    void reset() { //Remettre a 0
    	
    	currencySymbol = "";
    	salaryType = "";
    	currency = "";
    	hourlyIncome = 0.0;
		hoursPerWeek = 0.0;
		weeksPerYear = 0.0;
		yearlyIncome = 0.0;
		
		incomeTaxType = "";
		customIncomeTaxRate = 0.0;
    	country = "";
		incomeTax = 0.0;
    	taxIncome = 0.0;
    	
    	rentPeriod = "";
    	rentPrice = 0.0;
    	yearlyRent = 0.0;
    	rentIncome = 0.0;
    	
    	otherPaymentRate = "";
    	otherPaymentPrice = 0.0;
    	yearlyOtherPayment = 0.0;
    	finalIncome = 0.0;
    	
    	currencyCB.getSelectionModel().select(null);
		salaryTypeCB.getSelectionModel().select(null);
		incomeTaxCB.getSelectionModel().select(null);
		countryCB.getSelectionModel().select(null);
		rentPaymentRateCB.getSelectionModel().select(null);
		otherPaymentRateCB.getSelectionModel().select(null);
		
		annualSalary.setText("Salaire annuel: ");
		hourly.setText("");
		weekHours.setText("");
		weekYears.setText("");
		
		customIncomeTax.setVisible(true);
    	countryCB.setVisible(true);
    	customIncomeTax.setText("");
    	taxAmount.setText("Impôts a payer: ");
		annualSalaryTax.setText("Salaire annuel (après impôts): ");
		
		rentRate.setText("");
		annualRentText.setText("Prix de loyer annuel: ");
		incomeRentText.setText("Salaire annuel (après loyer): ");
		
		otherPayment.setText("");
		annualOtherPaymentText.setText("Prix d'autres dépenses annuel: ");
		finalIncomeText.setText("Salaire annuel final: ");
    	
    }
    
    /**
     * @return Trouve le chemin des fichiers.
     */
    
    public File getProductFilePath() { //Trouver le chemin des fichiers
    	
    	Preferences prefs = Preferences.userNodeForPackage(Main.class);
    	String filePath = prefs.get("filePath", null);
    	
    	if (filePath != null) {
    		return new File(filePath);
    	} else {
    		return null;
    	}
    	
    }
    
    /**
     * @param file Détermine le chemin du fichier XML.
     */
    
    public void setProductFilePath(File file) { //Donner un chemin
    	
    	Preferences prefs = Preferences.userNodeForPackage(Main.class);
    	
    	if (file != null) {
    		prefs.put("filePath", file.getPath());
    	} else {
    		prefs.remove("filePath");
    	}
    	
    }
    
    /**
     * @param file Convertit les données JavaFX à XML pour sauvegarder comme fichier XML et crée un alerte d'erreur si les données n'ont pas pu être sauvegarder.
     */
    
    public void saveProductDataToFile(File file) { //Convertir JavaFX a XML
    	
    	Product productData = new Product(name, price, quantity, tax);    	

    	try {
    		
    		JAXBContext context = JAXBContext.newInstance(Product.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    		productData = new Product(name, price, quantity, tax);
    		marshaller.marshal(productData, new FileOutputStream(file));
    		
    	} catch(Exception e) {
    		
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Erreur");
    		alert.setHeaderText("Les données n'ont pas été sauvegarder");
    		alert.setContentText("Les données ne pouvaient pas être sauvegardées dans le fichier: \n" + file.getPath());
    		alert.showAndWait();
    		
    	}
    	
    }
    
    /**
     * @param file Convertit les données du fichier XML ouvert a des données JavaFX puis les remets dans les TextFields, Labels et ComboBoxs. Une aletre d'erreur est crée si les données n'ont pas pu être retrouvées.
     */
    
    public void loadProductDataFromFile(File file) { //Convertir XML a JavaFX

    	try {
    		
    		JAXBContext context = JAXBContext.newInstance(Product.class);
    		Unmarshaller unmarshaller = context.createUnmarshaller();
    		Product productData = (Product) unmarshaller.unmarshal(file);
    		
    		name = productData.getName();
        	price = productData.getPrice();
        	quantity = productData.getQuantity();
        	
        	productName.setText(name);
        	productPrice.setText(price.toString());
        	productQuantity.setText(String.valueOf(quantity));

        	subTotal = price*quantity;
        	subTotalText.setText("Sous total: " + subTotal);
     
        	tax = (productData.getTax() - 1)*100;
        	salesTax.setText(tax.toString());
        	    
        	total = subTotal*tax;
        	totalText.setText("Total: " + total);
        	    	
        	hourlyIncomeTax = taxIncome/weeksPerYear/hoursPerWeek;
        	workTime = total/hourlyIncomeTax;
        	workTimeText.setText("Heures de travail nécéssaire: " + workTime);
    		
    	} catch(Exception e) {
    		
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Erreur");
    		alert.setHeaderText("Les données n'ont pas été trouvées");
    		alert.setContentText("Les données ne pouvaient pas être trouvées dans le fichier: \n" + file.getPath());
    		alert.showAndWait();
    		
    	}
    	
    }
    
    /**
     * Remettre toutes les données sur le produit a 0, incluant les TextFields.
     */
    
    @FXML
    private void newSession() { //Créer une nouvelle session
    	
    	name = null;
    	price = 0.0;
    	quantity = 0;
    	
    	productName.setText(name);
    	productPrice.setText("");
    	productQuantity.setText(String.valueOf(""));

    	subTotal = price*quantity;
    	subTotalText.setText("Sous total: " + subTotal);
 
    	tax = 0.0;
    	salesTax.setText("");
    	    
    	total = subTotal*tax;
    	totalText.setText("Total: " + total);
    	    	
    	hourlyIncomeTax = taxIncome/weeksPerYear/hoursPerWeek;
    	workTime = total/hourlyIncomeTax;
    	workTimeText.setText("Heures de travail nécéssaire: " + workTime);
    	
    	setProductFilePath(null);
    	
    }
    
    /**
     * openFile() Ouvre un fichier XML choisis puis appelle sur loadProductDataFromFile() pour pouvoir charger les données.
     */
    
    @FXML
    private void openFile() { //Ouvrir un fichier
    	
    	FileChooser fileChooser = new FileChooser();
    	FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
    	fileChooser.getExtensionFilters().add(extensionFilter);
    	
    	File file = fileChooser.showOpenDialog(null);
    	
    	if (file != null) {
    		
    		loadProductDataFromFile(file);
    		
    	}
    	
    }
    
    /**
     * saveFile() enregistre les données du produit sur le dernier fichier XML utilisé en appelant la fonction saveProductDataToFile(), si il n'y a pas de fichier XML précedant la fonction saveAs() est appelé pour en crée.
     */
    
    @FXML
    private void saveFile() { //Sauvegarder a un fichier
    	
    	File productFile = getProductFilePath();
    	if (productFile != null) {
    		saveProductDataToFile(productFile);
    	} else {
    		saveAs();
    	}
    	
    }
    
    /**
     * saveAs() Enregistre les données dans un nouveau fichier XML de nom et chemin choisit par l'utilisateur. La fonction saveProductDataToFile() est appelé pour sauvegarder les données.
     */
    
    @FXML
    private void saveAs() { //Enregistrer sous
    	
    	Product productData = new Product(name, price, quantity, tax);
    	
    	FileChooser fileChooser = new FileChooser();
    	FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
    	fileChooser.getExtensionFilters().add(extensionFilter);
    	
    	File file = fileChooser.showSaveDialog(null);
    	
    	if (file != null) {
    		
    		if (!file.getPath().endsWith(".xml")) {
    			
    			file = new File(file.getPath() + ".xml");
    			
    		}
    		
    		saveProductDataToFile(file);
    		
    	}
    	
    }
    
}
