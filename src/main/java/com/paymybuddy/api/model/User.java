package com.paymybuddy.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name="app_user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable=false, length = 50)
	private String username;
	
	@Column(nullable=false, length = 100)
	private String email;
	
	@Column(name="deleted_at", columnDefinition = "TIMESTAMP(0)")
	private LocalDateTime deletedAt;
	
	@Column(nullable=false, precision = 10, scale = 2)
	private BigDecimal balance;
	
	@Column(nullable=false, length = 255)
	private String password;
	
	@Column(name="active_email", unique=true, length = 100)
	private String activeEmail;
	
	@OneToMany(mappedBy="sender") //default lazy
	private List<Transfer> sentTransfers;
	
	@OneToMany(mappedBy="receiver")
	private List<Transfer> receivedTransfers;
	
	@ManyToMany
	@JoinTable(
			name="user_beneficiary",
			joinColumns = 
				@JoinColumn(name="user_id", referencedColumnName = "id"),
			inverseJoinColumns = 
				@JoinColumn(name="beneficiary_id", referencedColumnName = "id")
			)
	private Set<User> beneficiaries = new HashSet<>();
	
	protected User() {}

	private User(Integer id, String username, String email, LocalDateTime deletedAt, BigDecimal balance, String password, String activeEmail) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.deletedAt = deletedAt;
		this.balance = balance;
		this.password = password;
		this.activeEmail = activeEmail;
	}
	
	public static User forInitialData(Integer id, String username, String email, LocalDateTime deletedAt, BigDecimal balance, String password, String activeEmail) {
		return new User(id, username, email, deletedAt, balance, password, activeEmail);
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}
	
	public void setDeletedAt(LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getActiveEmail() {
		return activeEmail;
	}
	
	public void setActiveEmail(String activeEmail) {
		this.activeEmail = activeEmail;
	}

	public List<Transfer> getSentTransfers() {
		return sentTransfers;
	}

	public void setSentTransfers(List<Transfer> sentTransfers) {
		this.sentTransfers = sentTransfers;
	}

	public List<Transfer> getReceivedTransfers() {
		return receivedTransfers;
	}

	public void setReceivedTransfers(List<Transfer> receivedTransfers) {
		this.receivedTransfers = receivedTransfers;
	}
	
	public void addReceivedTransfer(Transfer transfer) {
		this.receivedTransfers.add(transfer);
	}

	public Set<User> getBeneficiaries() {
		return beneficiaries;
	}
	
	public void addBeneficiary(User beneficiary) {
		this.beneficiaries.add(beneficiary);
	}
	
	public void removeBeneficiary(User beneficiary) {
		this.beneficiaries.remove(beneficiary);
	}
	
	//valeurs calculées pour les colonnes générées initialement par MySQL
	//https://www.baeldung.com/jpa-entity-lifecycle-events
	@PrePersist
	@PreUpdate
	public void defineActiveEmail() {
		if (this.deletedAt == null) {
			this.activeEmail = email;
		} else {
			this.activeEmail = null;
		}
	}
}
