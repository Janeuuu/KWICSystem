package shift;

import java.util.List;

public interface CircularShifter {
    void setup(String line);
    void setup(List<String> fileLines);
    char getChar(int position, int word, int line);
    int getCharCount(int word, int line);
    String getWord(int word, int line);
    int getWordCount(int line);
    String[] getLine(int line);
    String getLineAsString(int line);
    int getLineCount();
    //清空所有行
    void clear();
}
