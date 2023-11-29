package kwic.impl;

import input.Input;
import input.impl.InputImpl;
import jdbc.JdbcFunc;
import jdbc.impl.JdbcFuncImpl;
import kwic.KWIC;
import output.Output;
import output.impl.OutputImpl;
import shift.CircularShifter;
import shift.impl.CircularShifterImpl;
import socket.impl.SocketServerImpl;
import sort.Alphabetizer;
import sort.impl.AlphabetizerImpl;

import java.io.IOException;
import java.util.List;

// KWICImpl类实现了KWIC接口
// 主控制模块，首先展现界面显示，再通过用户的不同选择，调用不同的模块
public class KWICImpl extends Thread implements KWIC {
    // 命令行提示
    private final String CMD_PROMPT = "*****请输入序号选择运行功能*****\n" +
            "@1、命令行输入(单行)\n" +
            "@2、文件输入(单行/多行)\n" +
            "@3、Socket输入(单行)\n" +
            "@4、数据库输入(单行/多行)\n" +
            "@5、消息中间件输入\n" +
            "@6、退出\n" +
            "$请输入序号：";

    private final String INPUT_PROMPT = "$请输入：";
    private final String FILE_INPUT_PROMPT = "$请输入输入文件存放路径(如D:\\input.txt)：";
    private final String FILE_OUTPUT_PROMPT = "$请输入输出文件存放路径(如D:\\output.txt)：";

    // 该参数为true时，表示程序仍处于功能选择模式，为false时，表示程序将处理用户输入的内容，处理结束后，再次进入功能选择模式变为true
    private boolean isCmdMode = true;

    // 不同的命令对应不同的命令码
    private final int CMD_ADD_LINE = 100;
    private final int FILE_ADD_LINES = 101;
    private final int SOCKET_ADD_LINES = 102;
    private final int JDBC_ADD_LINES = 103;
    private final int MSG_ADD_LINES = 104;
    private final int CMD_QUIT = 105;

    //选择运行模式
    private int parseCmd(String cmd) {
        int cmdCode = -1;
        if("1".equals(cmd)) {
            // 命令行输入
            cmdCode = CMD_ADD_LINE;
        }
        else if ("2".equals(cmd)) {
            // 文件输入
            cmdCode = FILE_ADD_LINES;
        }
        else if ("3".equals(cmd)) {
            // Socket输入
            cmdCode = SOCKET_ADD_LINES;
        }
        else if ("4".equals(cmd)) {
            // 数据库输入
            cmdCode = JDBC_ADD_LINES;
        }
        else if ("5".equals(cmd)) {
            // 消息中间件输入
            cmdCode = MSG_ADD_LINES;
        }
        else if ("6".equals(cmd)) {
            // 退出
            cmdCode = CMD_QUIT;
        }
        // 返回命令码
        return cmdCode;
    }

    @Override
    public void execute() {
        String line = null;
        int cmdCode = -1;

        // initialize all variables
        // input reader
        // 负责读取输入的对象
        Input input = new InputImpl();

        // circular shifter
        // 负责移位的对象
        CircularShifter shifter = new CircularShifterImpl();

        // alphabetizer
        // 负责排序的对象
        Alphabetizer alphabetizer = new AlphabetizerImpl();

        // line printer
        // 负责输出的对象
        Output output = new OutputImpl();

        System.out.println("KWIC索引系统1.0");

        // 读取输入,直到输入q退出
        while (!"q".equals(line)) {
            if (isCmdMode) {
                // 如果仍为true，表示程序处于功能选择模式，显示命令行提示
                System.out.print(CMD_PROMPT);
            } else {
                // 如果为false，表示程序将处理用户输入的内容，显示输入提示
                if (cmdCode == CMD_ADD_LINE) {
                    System.out.print(INPUT_PROMPT);
                } else if (cmdCode == FILE_ADD_LINES) {
                    System.out.print(FILE_INPUT_PROMPT);
                }
            }
            // 读取输入，input中的方法
            line = input.readLine();

            if (isCmdMode) {
                // 如果仍为true，表示程序处于功能选择模式，解析用户的输入，得到命令码
                cmdCode = parseCmd(line);
                switch (cmdCode) {
                    // 获取命令码后将isCmdMode置为false，表示程序将处理用户输入的内容
                    case CMD_ADD_LINE:
                        isCmdMode = false;
                        break;
                    case FILE_ADD_LINES:
                        isCmdMode = false;
                        break;
                    case SOCKET_ADD_LINES:
                        // 如果选择Socket输入，则启动SocketServerImpl
                        try {
                            Thread scThread = new SocketServerImpl(8080);
                            scThread.run();
                            //清除shifter中的内容
                            shifter.clear();
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case JDBC_ADD_LINES:
                        // 如果选择数据库输入，则启动JDBCFuncImpl
                        JdbcFuncImpl jdbcFunc = new JdbcFuncImpl();
                        jdbcFunc.JdbcService();
                        break;
                    case MSG_ADD_LINES:
                        // 如果选择消息中间件输入，则启动MSGServerImpl

                        break;
                    case CMD_QUIT:
                        // 如果选择退出，则退出程序
                        System.out.println("%程序已退出%");
                        System.exit(0);
                        break;

                    default:
                        // 如果输入的序号不正确，则提示用户重新输入
                        System.out.println("%输入有误，请重新输入%");
                        break;
                }
            } else {
                if (cmdCode == CMD_ADD_LINE) {
                    // 如果是命令行输入，则直接调用setup方法读取改行，这一步将所有的循环移位存储到shifter创建的LineStorageImpl对象中
                    shifter.setup(line);
                } else if (cmdCode == FILE_ADD_LINES) {
                    // 如果是文件输入，则调用readFile方法根据用户输入的路径读取文件，这一步将所有的循环移位存储到shifter创建的LineStorageImpl对象中
                    List<String> fileLines = input.readFile(line);
                    shifter.setup(fileLines);
                }
                if (shifter.getLineCount() == 0) {
                    // 如果输入的内容为空，则提示用户重新输入
                    System.out.println("%输入内容为空，请重新选择运行功能%");
                } else {
                    // 接下来将shifter中LineStorageImpl对象中的内容进行排序，排序后的结果存放在alphabetizer创建的LineStorageImpl对象中
                    alphabetizer.alpha(shifter);

                    // 输出排序后的结果
                    if (cmdCode == CMD_ADD_LINE) {
                        // 如果是命令行输入，则直接输出
                        output.print(alphabetizer);
                        //清除shifter中的内容
                        shifter.clear();
                    } else if (cmdCode == FILE_ADD_LINES) {
                        // 由用户来指定输出文件的路径
                        System.out.print(FILE_OUTPUT_PROMPT);
                        String outputPath = input.readLine();
                        // 如果是文件输入，则输出到文件
                        output.write(alphabetizer, outputPath);
                        //清除shifter中的内容
                        shifter.clear();
                    }
                }
                // 处理完用户输入的内容后，将isCmdMode置为true，表示程序重新回到功能选择模式
                isCmdMode = true;
            }
        }
    }

    public void run() {
        execute();
    }
}
