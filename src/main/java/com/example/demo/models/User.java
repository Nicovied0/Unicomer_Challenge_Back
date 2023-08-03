package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
  // Otros atributos que desees agregar
  // Lista de ObjectIds para las relaciones
  private List<String> cardIds = new ArrayList<>();

  // Constructor vacío
  public User() {
  }

  // Constructor con parámetros
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

  // Getters y Setters para el atributo "name"
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  // Getters y Setters para el atributo "image"
  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  // Getters y Setters para el atributo "email"
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  // Getters y Setters para el atributo "documentType"
  public String getDocumentType() {
    return documentType;
  }

  public void setDocumentType(String documentType) {
    this.documentType = documentType;
  }

  // Getters y Setters para el atributo "documentNumber"
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

  // Métodos para agregar o eliminar IDs de tarjetas mediante sus ObjectIds
  public void addCardId(String cardId) {
    this.cardIds.add(cardId);
  }

  public void removeCardId(String cardId) {
    this.cardIds.remove(cardId);
  }

  // Getters y Setters para la lista de IDs de tarjetas
  public List<String> getCardIds() {
    return cardIds;
  }

  public void setCardIds(List<String> cardIds) {
    this.cardIds = cardIds;
  }

  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        "image='" + image + '\'' +
        ", documentType='" + documentType + '\'' +
        ", documentNumber='" + documentNumber + '\'' +
        '}';
  }

}
