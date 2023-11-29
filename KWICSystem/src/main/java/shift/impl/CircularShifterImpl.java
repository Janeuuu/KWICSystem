package shift.impl;

import line.LineStorage;
import line.impl.LineStorageImpl;
import shift.CircularShifter;

import java.util.List;

// 循环移位类，实现了对输入的一行进行循环移位的功能，并将结果存储在LineStorageImpl类中
public class CircularShifterImpl implements CircularShifter {

    private LineStorage shifts_ = new LineStorageImpl();

    // 处理命令行输入（一行）
    @Override
    public void setup(String line) {
        // 将输入的一行按空格分割成单词
        String[] words = line.split("\\s+");

        // iterate through all words of the current line
        // 对于当前行的所有单词，进行循环，每次循环，将第一个单词放到最后一个单词的后面，即循环左移一位，直到循环一周，即第一个单词又回到了原来的位置，结束循环
        for(int j = 0; j < words.length; j++){

            // add a new empty line for the current shift
            // 为当前循环创建一个新的空行，准备在这个空行中存放此次循环移位的结果
            shifts_.addEmptyLine();

            // add all words of the current shift
            // 对所读取的行中的所有单词进行循环，每次循环，将当前单词添加到当前行的最后一个单词的后面，即循环左移一位
            for(int k = j; k < (words.length + j); k++)

                // add current word to the last line
                // index is the remainder of dividing k and line.length
                // 在最后一行中每次添加一个单词，添加的单词是words[k % words.length]
                shifts_.addWord(words[k % words.length], shifts_.getLineCount() - 1);

        }
    }

    // 处理文件输入，将文件的每一行作为参数调用setup(String line)方法
    @Override
    public void setup(List<String> fileLines) {
        if (fileLines == null) {
            System.out.println("读取文件失败！");
        }
        for (String line : fileLines) {
            setup(line);
        }
    }


    @Override
    public char getChar(int position, int word, int line) {
        return shifts_.getChar(position, word, line);
    }

    @Override
    public int getCharCount(int word, int line) {
        return shifts_.getCharCount(word, line);
    }

    @Override
    public String getWord(int word, int line) {
        return shifts_.getWord(word, line);
    }

    @Override
    public int getWordCount(int line) {
        return shifts_.getWordCount(line);
    }

    @Override
    public String[] getLine(int line) {
        return shifts_.getLine(line);
    }

    @Override
    public String getLineAsString(int line) {
        return shifts_.getLineAsString(line);
    }

    @Override
    public int getLineCount() {
        return shifts_.getLineCount();
    }

    @Override
    public void clear() {
        shifts_.clear();
    }
}