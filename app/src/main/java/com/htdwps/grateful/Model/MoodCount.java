package com.htdwps.grateful.Model;

/**
 * Created by HTDWPS on 6/28/18.
 */
public class MoodCount {

    // Find if the child name for mood exists, than do transaction, else do a new POJO for it set to value 1
    private String moodName;
    private int valueCount;

    public MoodCount() {
    }

    public MoodCount(String moodName) {
        this.moodName = moodName;
        this.valueCount = 1;
    }

    public MoodCount(String moodName, int valueCount) {
        this.moodName = moodName;
        this.valueCount = valueCount;
    }

    public String getMoodName() {
        return moodName;
    }

    public void setMoodName(String moodName) {
        this.moodName = moodName;
    }

    public int getValueCount() {
        return valueCount;
    }

    public void setValueCount(int valueCount) {
        this.valueCount = valueCount;
    }
}
