package br.ufrn.imd.investbankapi.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "assets")
public class Asset implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true, length = 6)
    private String code;

    @Column(nullable = false, unique = false, length = 30)
    private String name;

    // @Column(nullable = false, precision = 2)
    // private BigDecimal price;

    @Column(nullable = false, length = 30)
    private String type;

    public Asset() {}

    public Asset(String code, String name, String type) {
        this.code = code;
        this.name = name;
        // this.price = price;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    // public BigDecimal getPrice() {
    //     return price;
    // }
    // public void setPrice(BigDecimal price) {
    //     this.price = price;
    // }

    
}
