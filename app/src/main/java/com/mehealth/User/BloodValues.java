package com.mehealth.User;

import java.util.ArrayList;
import java.util.List;

public class BloodValues {
    private static final BloodValues ourInstance = new BloodValues();
    private List<HighBloodPressure> bloodvalues;

    public static BloodValues getInstance() {

        return ourInstance;
    }

    private BloodValues() {
        bloodvalues = new ArrayList<HighBloodPressure>();
        bloodvalues.add(new HighBloodPressure(0,120,80));
        bloodvalues.add(new HighBloodPressure(1,98,54));
        bloodvalues.add(new HighBloodPressure(2,101,58));
        bloodvalues.add(new HighBloodPressure(3,102,61));
        bloodvalues.add(new HighBloodPressure(4,103,64));
        bloodvalues.add(new HighBloodPressure(5,104,66));
        bloodvalues.add(new HighBloodPressure(6,105,68));
        bloodvalues.add(new HighBloodPressure(7,106,70));
        bloodvalues.add(new HighBloodPressure(8,107,71));
        bloodvalues.add(new HighBloodPressure(9,108,72));
        bloodvalues.add(new HighBloodPressure(10,109,73));
        bloodvalues.add(new HighBloodPressure(11,111,74));
        bloodvalues.add(new HighBloodPressure(12,114,75));
        bloodvalues.add(new HighBloodPressure(13,117,76));
        bloodvalues.add(new HighBloodPressure(14,119,77));
        bloodvalues.add(new HighBloodPressure(15,120,78));
        bloodvalues.add(new HighBloodPressure(16,120,78));
        bloodvalues.add(new HighBloodPressure(17,120,78));
        bloodvalues.add(new HighBloodPressure(18,120,80));

    }
    public List<HighBloodPressure> getBloodvalues() {
        return bloodvalues;
    }


}
