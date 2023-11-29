package output.impl;

import output.Output;
import sort.Alphabetizer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputImpl implements Output {

    /**
     * 打印运行结果
     * @param alphabetizer
     */
    @Override
    public List<String> print(Alphabetizer alphabetizer) {
        List<String> res = new ArrayList<>();

        System.out.println("~处理结果为:");
        for(int i = 0; i < alphabetizer.getLineCount(); i++) {
            res.add(alphabetizer.getLineAsString(i));
            System.out.println(alphabetizer.getLineAsString(i));
        }

        return res;
    }

    public void WritetoDatebase(Alphabetizer alphabetizer) {
        System.out.println("~处理结果为:");
        for(int i = 0; i < alphabetizer.getLineCount(); i++) {
            System.out.println(alphabetizer.getLineAsString(i));
        }
    }

    /**
     * 写入运行结果到文件
     * @param alphabetizer
     */
    @Override
    public void write(Alphabetizer alphabetizer, String fileName) {
//        File writename = new File("output.txt");
        File writename = new File(fileName);
        try {
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            for(int i = 0; i < alphabetizer.getLineCount(); i++) {
                out.write(alphabetizer.getLineAsString(i) + "\r\n");
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("~处理结果保存路径为：" + writename.getAbsolutePath());
    }
}
