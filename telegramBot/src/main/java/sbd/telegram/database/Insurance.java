package sbd.telegram.database;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Insurance {
    private String insuranceType;
    private String monthlyPrice;
    private String payoutPercentage;

    public Insurance() {
        this.insuranceType = null;
        this.monthlyPrice = null;
        this.payoutPercentage = null;
    }
}
