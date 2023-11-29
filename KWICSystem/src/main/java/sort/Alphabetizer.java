package sort;

import shift.CircularShifter;

public interface Alphabetizer {
    void alpha(CircularShifter shifter);
    String[] getLine(int line);
    String getLineAsString(int line);
    int getLineCount();
}
