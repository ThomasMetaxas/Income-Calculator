package application;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//Sauvegarder et restorer les données a partir du fichier XML

@XmlRootElement
public class Product {
	
	private String name;
	private Double price;
    private int quantity;
    private Double tax;
    
    Product () {
    	
    }
    
    Product(String name, Double price, int quantity, Double tax) {
    	
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.tax = tax;
        
    }
    
    @XmlElement
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @XmlElement
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    @XmlElement
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    @XmlElement
    public Double getTax() {
        return tax;
    }
    
    public void setTax(Double tax) {
        this.tax = tax;
    }
	
}
