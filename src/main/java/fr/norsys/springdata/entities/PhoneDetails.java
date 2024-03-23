package fr.norsys.springdata.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@ToString(exclude = "phone")
@Table(name = "phone_details")
public class PhoneDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String provider;
    private String technology;
    private String frequency;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phone_id")
    private Phone phone;

}
