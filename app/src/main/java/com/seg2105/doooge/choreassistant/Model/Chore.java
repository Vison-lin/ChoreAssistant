package com.seg2105.doooge.choreassistant.Model;


import com.seg2105.doooge.choreassistant.Tool.IdentificationUtility;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dustin on 11/25/17.
 */

public class Chore implements Serializable {

    private String description;
    private String choreName;

    private List<Responsibility> responsibilities;

    private Long timeInMillis;
    private String choreIdentification;
    

    public Chore() {
        //Keep for Firebase only
    }


    public Chore(String choreName, String description, long timeInMillis) throws NoSuchAlgorithmException {
        this.choreName = choreName;
        this.description = description;
        this.timeInMillis = timeInMillis;

        responsibilities = new ArrayList<Responsibility>();

        choreIdentification = IdentificationUtility.generateIdentification(choreName, String.valueOf(timeInMillis), description);

    }

    public boolean hasResponsibilities() {
        return responsibilities.size() > 0;
    }

    public boolean addResponsibility(Responsibility responsibility) {
        boolean added = false;
        if (responsibilities.contains(responsibility)) {
            return false;
        }

        Chore existingChore = responsibility.getChore();
        boolean isNewChore = existingChore != null && !this.equals(existingChore);
        if (isNewChore) {
            responsibility.setChore(this);
        } else {
            responsibilities.add(responsibility);
        }
        added = true;
        return added;
    }

    public boolean removeResponsibility(Responsibility responsibility) {
        boolean removed = false;

        if (!this.equals(responsibility.getChore())) {
            responsibilities.remove(responsibility);
            removed = true;
        }
        return removed;
    }

    public List<Responsibility> getResponsibilities() {
        List<Responsibility> newResponsibilities = responsibilities;
        return newResponsibilities;
    }

    public int numberOfResponsibilities() {
        return responsibilities.size();
    }


    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChoreName() {
        return choreName;
    }

    public void setChoreName(String choreName) {
        this.choreName = choreName;
    }


    public String getChoreIdentification() {
        return choreIdentification;
    }

}
