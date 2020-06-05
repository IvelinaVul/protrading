package com.trading.protrading.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "conditions")
public class Condition {

    @Id
    @GeneratedValue
    private Long id;
    private double assetPrice;
    private String predicate;
    @OneToOne
    @JoinColumn(nullable = false, referencedColumnName = "id", name = "ruleId")
    private Rule rule;
}
