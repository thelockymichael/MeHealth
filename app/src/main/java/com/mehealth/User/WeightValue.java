package com.mehealth.User;

import java.util.Date;
import java.util.Objects;

public class WeightValue {
    private int weight;
    private Date date;

    public WeightValue(int weight, Date date) {
        this.weight = weight;
        this.date = date;
    }

    public int getWeight() {
        return weight;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeightValue that = (WeightValue) o;
        return weight == that.weight &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, date);
    }
}
