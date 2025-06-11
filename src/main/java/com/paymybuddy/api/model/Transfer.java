package com.paymybuddy.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.paymybuddy.api.constants.TransferSettings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name="transfer")
public class Transfer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JsonBackReference
	@ManyToOne //https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#associations
	@JoinColumn(name="sender", referencedColumnName = "id", nullable=false)
	private User sender;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="receiver", referencedColumnName = "id", nullable=false)
	private User receiver;

	@Column(nullable=false, length = 255)
	private String description;

	@Column(nullable=false, precision = 10, scale = 2)
	private BigDecimal amount;
	
	@Column(nullable=false, precision = 10, scale = 3)
	@ColumnDefault("0.005")
	private BigDecimal fees = TransferSettings.TRANSFER_FEES;

	@Column(name="total_amount", precision=10, scale =2, insertable=false, updatable=false)
	private BigDecimal totalAmount;

	@Column(nullable = false, columnDefinition = "TIMESTAMP(0)")
	private LocalDateTime date = LocalDateTime.now();

	protected Transfer() {} // no class can accidentally use the default constructor

	private Transfer(User sender, User receiver, String description, BigDecimal amount) {
		this.sender = sender;
		this.receiver = receiver;
		this.description = description;
		this.amount = amount;
	}
	
	public static Transfer forInitialData(User sender, User receiver, String description, BigDecimal amount) {
		return new Transfer (sender, receiver, description, amount);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getFees() {
		return fees;
	}

	public void setFees(BigDecimal fees) {
		this.fees = fees;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	//valeurs calculées pour les colonnes générées initialement par MySQL
	// évite le NPE. La valeur est calculée avant l'insertion et est donc en mémoire dans l'instance
	//https://www.baeldung.com/jpa-entity-lifecycle-events
	@PrePersist
	public void calculateTotalAmount() {
		if (amount != null && fees != null) {
			this.totalAmount = amount.add(amount.multiply(fees));
		}
	}
}
