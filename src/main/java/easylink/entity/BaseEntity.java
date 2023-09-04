package easylink.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@SuppressWarnings("serial")	
@MappedSuperclass				// to use this as base class for other entities
@EntityListeners(AuditingEntityListener.class)		// to allow injecting audting information into entity
@JsonInclude(Include.NON_NULL)						// to ignore null fields
@JsonIgnoreProperties(ignoreUnknown = true)			// ignore unrecognized properties during deserialization
public class BaseEntity implements Serializable {

	protected Integer id;
	
	@CreatedBy
	protected Integer createdUser;
	
	@CreatedDate
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	protected Date createdDate;
	
	@LastModifiedBy
	protected Integer modifiedUser;
	
	@LastModifiedDate
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	protected Date modifiedDate;

//	@PrePersist		// replaced by spring annotation @CreatedDate, etc...
//	public void onPrePersist() {
//		if (description == null)
//			description = resource + ":" + action + (condition == null? "" : ":" +condition);
//	}
//	
//	@PreUpdate
//	public void onPreUpdate() {
//		if (description == null)
//			description = resource + ":" + action + (condition == null? "" : ":" +condition);
//	}
	
	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "created_user")
	public Integer getCreatedUser() {
		return this.createdUser;
	}

	public void setCreatedUser(Integer createdUser) {
		this.createdUser = createdUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", length = 19)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "modified_user")
	public Integer getModifiedUser() {
		return this.modifiedUser;
	}

	public void setModifiedUser(Integer modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_date", length = 19)
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
