package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User {

  @Id
  private String id;
  private String name;
  private String email;
  private String documentType;
  private String documentNumber;
  private String password;
  private String image;
  private List<String> cardIds = new ArrayList<>();
  private List<Double> ingresos = new ArrayList<>();
  private List<Double> egresos = new ArrayList<>();
  private List<FinancialMovement> financialMovements = new ArrayList<>();

  public User() {
  }

  public User(String name, String image, String email, String dni, String password, String documentType,
      String documentNumber) {
    this.name = name;
    this.email = email;
    this.documentType = documentType;
    this.documentNumber = documentNumber;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    this.password = passwordEncoder.encode(password);

    if (image != null && !image.isEmpty()) {
      this.image = image;
    } else {
      this.image = "https://res.cloudinary.com/dylweuvjp/image/upload/v1691007184/vecqghtxym5pp8dwtmks.jpg";
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDocumentType() {
    return documentType;
  }

  public void setDocumentType(String documentType) {
    this.documentType = documentType;
  }

  public String getDocumentNumber() {
    return documentNumber;
  }

  public void setDocumentNumber(String documentNumber) {
    this.documentNumber = documentNumber;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<String> getCardIds() {
    return cardIds;
  }

  public void setCardIds(List<String> cardIds) {
    this.cardIds = cardIds;
  }

  public List<Double> getIngresos() {
    return ingresos;
  }

  public void setIngresos(List<Double> ingresos) {
    this.ingresos = ingresos;
  }

  public List<Double> getEgresos() {
    return egresos;
  }

  public void setEgresos(List<Double> egresos) {
    this.egresos = egresos;
  }

  public List<FinancialMovement> getFinancialMovements() {
    return financialMovements;
  }

  public void setFinancialMovements(List<FinancialMovement> financialMovements) {
    this.financialMovements = financialMovements;
  }

  public void addCardId(String cardId) {
    this.cardIds.add(cardId);
  }

  public void removeCardId(String cardId) {
    this.cardIds.remove(cardId);
  }

  public void addIngreso(Double monto) {
    if (ingresos == null) {
      ingresos = new ArrayList<>();
    }
    ingresos.add(monto);
  }

  public void addEgreso(Double monto) {
    if (egresos == null) {
      egresos = new ArrayList<>();
    }
    egresos.add(monto);
  }

  public void addFinancialMovement(FinancialMovement movement) {
    this.financialMovements.add(movement);
  }

  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", image='" + image + '\'' +
        ", documentType='" + documentType + '\'' +
        ", documentNumber='" + documentNumber + '\'' +
        ", ingresos='" + ingresos + '\'' +
        ", egresos='" + egresos + '\'' +
        ", financialMovements='" + financialMovements + '\'' +
        '}';
  }
}
