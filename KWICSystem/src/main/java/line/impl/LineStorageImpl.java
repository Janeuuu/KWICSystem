package line.impl;

import line.Line;
import line.LineStorage;

import java.util.ArrayList;

// 存储所有行的类
public class LineStorageImpl implements LineStorage {

    private ArrayList<Line> lines_ = new ArrayList<>();

    //
    @Override
    public void setChar(char c, int position, int word, int line) {
        // get the specified line
        Line current_line = lines_.get(line);

        current_line.setChar(c, position, word);
    }

    @Override
    public char getChar(int position, int word, int line) {

        return (lines_.get(line).getChar(position, word));
    }

    @Override
    public void addChar(char c, int word, int line) {
        Line current_line = lines_.get(line);

        current_line.addChar(c, word);
    }

    @Override
    public void deleteChar(int position, int word, int line) {
        Line current_line = lines_.get(line);

        current_line.deleteChar(position, word);
    }

    @Override
    public int getCharCount(int word, int line) {
        return (lines_.get(line).getCharCount(word));
    }

    @Override
    public void setWord(char[] chars, int word, int line) {
        setWord(new String(chars), word, line);
    }

    @Override
    public void setWord(String chars, int word, int line) {
        Line current_line = lines_.get(line);

        // replace the old word with the new one
        current_line.setWord(chars, word);
    }

    @Override
    public String getWord(int word, int line) {

        return (lines_.get(line).getWord(word));
    }

    // 添加单词，本身单词是以char[]的形式存储的，这里将char[]转换成String，再调用addWord(String chars, int line)方法
    @Override
    public void addWord(char[] chars, int line) {
        addWord(new String(chars), line);
    }

    // 添加单词
    @Override
    public void addWord(String chars, int line) {
        //获取指定行
        Line current_line = lines_.get(line);

        // add the new word
        // 在指定行中添加单词，这里针对的是一行，因此调用LineImpl中的addWord(String word)方法
        current_line.addWord(chars);
    }

    @Override
    public void addEmptyWord(int line) {
        Line current_line = lines_.get(line);

        // add the new word
        current_line.addEmptyWord();
    }

    @Override
    public void deleteWord(int word, int line) {
        Line current_line = lines_.get(line);

        // delete the specified word
        current_line.deleteWord(word);
    }

    @Override
    public int getWordCount(int line) {

        return (lines_.get(line)).getWordCount();
    }

    @Override
    public void setLine(char[][] words, int line) {
        String[] tmp = new String[words.length];
        for(int i = 0; i < words.length; i++)
            tmp[i] = new String(words[i]);

        setLine(tmp, line);
    }

    @Override
    public void setLine(String[] words, int line) {
        Line new_line = new LineImpl();

        for (int i = 0; i < words.length; i++) {
            new_line.addWord(words[i]);
        }

        lines_.set(line, new_line);
    }

    @Override
    public String[] getLine(int line) {

        Line current_line = lines_.get(line);

        // create the String[] representation of the line
        String[] tmp = new String[current_line.getWordCount()];
        for(int i = 0; i < tmp.length; i++)
            tmp[i] = current_line.getWord(i);

        return tmp;
    }

    @Override
    public String getLineAsString(int line) {

        Line current_line = lines_.get(line);

        // calculate the length of the line
        // 获取当前行的单词数
        int size = current_line.getWordCount();

        // 获取当前行的所有单词的长度
        int length = 0;
        for(int i = 0; i < size; i++)
            length += getWord(i, line).length();

        // add the length of space characters delimiting the words
        // 添加空格的长度，即单词之间的空格
        length += size - 1;

        // initialize the char[]
        // 初始化char[]
        char[] tmp = new char[length];

        // create the String representation of the line
        // 将当前行的所有单词存放到tmp中
        int count = 0;
        for(int i = 0; i < size; i++){
            getWord(i, line).getChars(0, getWord(i, line).length(), tmp, count);
            count += getWord(i, line).length();
            if(i != (size - 1))
                tmp[count++] = ' ';
        }

        return new String(tmp);
    }

    @Override
    public void addLine(char[][] words) {
        String[] tmp = new String[words.length];
        for(int i = 0; i < words.length; i++)
            tmp[i] = new String(words[i]);

        addLine(tmp);
    }

    @Override
    public void addLine(String[] words) {
        Line current_line = new LineImpl();

        // add words
        for(int i = 0; i < words.length; i++)
            current_line.addWord(words[i]);

        // add the new line at the end
        lines_.add(current_line);
    }

    // 添加空行
    @Override
    public void addEmptyLine() {
        Line current_line = new LineImpl();

        // add the new line at the end
        // 在最后添加空的一行，准备将该次移位的结果存放在这一行中
        lines_.add(current_line);
    }

    @Override
    public void deleteLine(int line) {
        lines_.remove(line);
    }

    // 获取当前行数，主要是为了在CircularShifterImpl中使用，获取到最后一行的行数，然后在最后一行中添加单词
    @Override
    public int getLineCount() {
        return lines_.size();
    }

    @Override
    public void clear() {
        lines_.clear();
    }

}
