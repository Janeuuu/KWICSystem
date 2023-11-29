package input.impl;

import input.Input;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// 负责输入的模块，实现了Input接口
public class InputImpl implements Input {
    // 从控制台读取输入，使用Scanner类，标准输入流System.in
    private Scanner scanner = new Scanner(System.in);

    // 读取一行
    @Override
    public String readLine(){
        return scanner.nextLine();
    }

    // 读取文件
    @Override
    public List<String> readFile(String fileName) {
        List<String> textFile = new ArrayList<>();
        Scanner input = null;
        try
        {
            input = new Scanner(new FileInputStream(fileName));

            while(input.hasNextLine())
            {
                String line = input.nextLine();

                textFile.add(line);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        }
        finally
        {
            if (input != null) {
                input.close();
            }
        }

        return textFile;
    }

}
