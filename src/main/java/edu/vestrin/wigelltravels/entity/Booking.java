package edu.vestrin.wigelltravels.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Destination destination;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "number_of_weeks", nullable = false)
    private int numOfWeeks;

    @Column(name = "hotel_name", nullable = false, length = 50)
    private String hotelName;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 50)
    private String country;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPriceSEK;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPricePLN;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false)
    private BookingStatus status;

    @Column(name = "booked_at")
    private LocalDateTime bookedAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    public Booking() {
    }

    public Booking(Customer customer, Destination destination, LocalDate departureDate, int numOfWeeks, String hotelName,
                   String city, String country, BigDecimal totalPriceSEK, BigDecimal totalPricePLN) {
        this.customer = customer;
        this.destination = destination;
        this.departureDate = departureDate;
        this.numOfWeeks = numOfWeeks;
        this.hotelName = hotelName;
        this.city = city;
        this.country = country;
        this.totalPriceSEK = totalPriceSEK;
        this.totalPricePLN = totalPricePLN;
        this.status = BookingStatus.CONFIRMED;
    }

    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public int getNumOfWeeks() {
        return numOfWeeks;
    }

    public void setNumOfWeeks(int numOfWeeks) {
        this.numOfWeeks = numOfWeeks;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BigDecimal getTotalPriceSEK() {
        return totalPriceSEK;
    }

    public void setTotalPriceSEK(BigDecimal totalPriceSEK) {
        this.totalPriceSEK = totalPriceSEK;
    }

    public BigDecimal getTotalPricePLN() {
        return totalPricePLN;
    }

    public void setTotalPricePLN(BigDecimal totalPricePLN) {
        this.totalPricePLN = totalPricePLN;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    @SuppressWarnings("unused")
    private void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.bookedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onModified() {
        this.modifiedAt = LocalDateTime.now();
    }
}
