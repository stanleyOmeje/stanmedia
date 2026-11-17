package com.stan.account.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.time.Instant;

/**
 * Configuration for an entity that runs a wallet scheme
 */
@Entity
@Table(name = "ac_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "other_name")
    private String otherName;

    @Column(name = "alias")
    private String alias;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "phone_country_code", nullable = false)
    private String phoneCountryCode;

    @Column(name = "date_of_birth")
    private Instant dateOfBirth;

    @Column(name = "bvn")
    private String bvn;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "cba_tenant")
    private String cbaTenant;

    @Column(name = "gender")
    private String gender;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Profile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Profile firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Profile lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Profile fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOtherName() {
        return this.otherName;
    }

    public Profile otherName(String otherName) {
        this.setOtherName(otherName);
        return this;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getAlias() {
        return this.alias;
    }

    public Profile alias(String alias) {
        this.setAlias(alias);
        return this;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getEmail() {
        return this.email;
    }

    public Profile email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public Profile phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneCountryCode() {
        return this.phoneCountryCode;
    }

    public Profile phoneCountryCode(String phoneCountryCode) {
        this.setPhoneCountryCode(phoneCountryCode);
        return this;
    }

    public void setPhoneCountryCode(String phoneCountryCode) {
        this.phoneCountryCode = phoneCountryCode;
    }

    public Instant getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Profile dateOfBirth(Instant dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBvn() {
        return this.bvn;
    }

    public Profile bvn(String bvn) {
        this.setBvn(bvn);
        return this;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getStreet() {
        return this.street;
    }

    public Profile street(String street) {
        this.setStreet(street);
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return this.city;
    }

    public Profile city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Profile postalCode(String postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Profile active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Profile creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getCbaTenant() {
        return this.cbaTenant;
    }

    public Profile cbaTenant(String cbaTenant) {
        this.setCbaTenant(cbaTenant);
        return this;
    }

    public void setCbaTenant(String cbaTenant) {
        this.cbaTenant = cbaTenant;
    }

    public String getGender() {
        return this.gender;
    }

    public Profile gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profile)) {
            return false;
        }
        return id != null && id.equals(((Profile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Profile{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", otherName='" + getOtherName() + "'" +
            ", alias='" + getAlias() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", phoneCountryCode='" + getPhoneCountryCode() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", bvn='" + getBvn() + "'" +
            ", street='" + getStreet() + "'" +
            ", city='" + getCity() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", active='" + getActive() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", cbaTenant='" + getCbaTenant() + "'" +
            ", gender='" + getGender() + "'" +
            "}";
    }
}
