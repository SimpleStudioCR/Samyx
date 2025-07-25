package snn.soluciones.com.models.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "c_terminales", uniqueConstraints = {@UniqueConstraint(columnNames = {"sucursal_id", "terminal"})})
public class CTerminal {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(length = 3)
  private int terminal;
  
  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  @JoinColumn(name = "sucursal_id")
  private CSucursal sucursal;
  
  @Column(name = "nombre_terminal", length = 200)
  private String nombreTerminal;
  
  @Column(name = "consecutivo_fe", length = 20)
  private Long consecutivoFe;
  
  @Column(name = "consecutivo_te", length = 20)
  private Long consecutivoTe;
  
  @Column(name = "consecutivo_nd", length = 20)
  private Long consecutivoNd;
  
  @Column(name = "consecutivo_nc", length = 20)
  private Long consecutivoNc;
  
  @Column(name = "consecutivo_cce", length = 20)
  private Long consecutivoCCE;
  
  @Column(name = "consecutivo_cpce", length = 20)
  private Long consecutivoCPCE;
  
  @Column(name = "consecutivo_rce", length = 20)
  private Long consecutivoRCE;
  
  @Column(name = "consecutivo_fee", length = 20)
  private Long consecutivoFEE;
  
  @Column(name = "consecutivo_fec", length = 20)
  private Long consecutivoFEC;
  
  @Column(length = 1)
  private int status;

}
