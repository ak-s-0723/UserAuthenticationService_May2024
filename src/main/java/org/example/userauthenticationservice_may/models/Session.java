package org.example.userauthenticationservice_may.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Session extends BaseModel {
  @Enumerated(EnumType.ORDINAL)
 private SessionStatus sessionStatus;

 private String token;

 @ManyToOne
 private User user;
}
