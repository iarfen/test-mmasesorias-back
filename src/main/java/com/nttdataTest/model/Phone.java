package com.nttdataTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "phones")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Phone {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long id;

    @Column
    public Long number;

    @Column
    public Long cityCode;

    @Column
    public Long countryCode;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    public User user;
}
