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
    @JoinColumn(name = "travel_id")
    private Travel travel;

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
    @Column(name = "travel_status", nullable = false)
    private TravelStatus status;

    @Column(name = "booked_at")
    private LocalDateTime bookedAt;

    protected Booking() {
    }

    public Booking(Customer customer, Travel travel, LocalDate departureDate, int numOfWeeks, String hotelName,
                   String city, String country, BigDecimal totalPriceSEK, BigDecimal totalPricePLN) {
        this.customer = customer;
        this.travel = travel;
        this.departureDate = departureDate;
        this.numOfWeeks = numOfWeeks;
        this.hotelName = hotelName;
        this.city = city;
        this.country = country;
        this.totalPriceSEK = totalPriceSEK;
        this.totalPricePLN = totalPricePLN;
        this.status = TravelStatus.CONFIRMED;
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

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
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

    public TravelStatus getStatus() {
        return status;
    }

    public void setStatus(TravelStatus status) {
        this.status = status;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.bookedAt = LocalDateTime.now();
    }
}
