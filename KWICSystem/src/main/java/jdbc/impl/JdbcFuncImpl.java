package jdbc.impl;

import input.Input;
import input.impl.InputImpl;
import jdbc.JdbcFunc;
import jdbc.mapper.JdbcMapper;
import jdbc.pojo.Jdbc;
import jdbc.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import output.Output;
import output.impl.OutputImpl;
import shift.CircularShifter;
import shift.impl.CircularShifterImpl;
import sort.Alphabetizer;
import sort.impl.AlphabetizerImpl;

import java.util.List;

public class JdbcFuncImpl implements JdbcFunc {
    private final int SelectAll = 100;
    private final int Insert = 101;
    private final int DeleteById = 102;
    private final int DeleteAll = 103;
    private final int KWIC = 104;
    private final int Return = 105;

    private final String CMD_PROMPT = "***请根据序号选择数据库操作***\n" +
            "@1.查询当前数据库中所有语句\n" +
            "@2.插入一条语句(输入一条语句)\n" +
            "@3.删除一条语句(输入id)\n" +
            "@4.清空数据库内容\n" +
            "@5.对当前数据库内容执行KWIC操作\n" +
            "@6.返回上一级菜单\n" +
            "$请输入序号：";

    private final String INPUT_PROMPT = "$请输入：";
    private final String FILE_INPUT_PROMPT = "$请输入一个英文语句：";

    @Override
    public int parseCmd(String cmd) {
        int cmdCode = -1;
        if("1".equals(cmd)) {
            //查询当前数据库中所有语句
            cmdCode = SelectAll;
        }
        else if ("2".equals(cmd)) {
            //插入一条语句
            cmdCode = Insert;
        }
        else if ("3".equals(cmd)) {
            //删除一条语句
            cmdCode = DeleteById;
        }
        else if ("4".equals(cmd)) {
            //清空数据库内容
            cmdCode = DeleteAll;
        }
        else if ("5".equals(cmd)) {
            //对当前数据库内容执行KWIC操作
            cmdCode = KWIC;
        }
        else if ("6".equals(cmd)) {
            //返回上一级菜单
            cmdCode = Return;
        }
        // 返回命令码
        return cmdCode;
    }

    @Override
    public void JdbcService() {
        String line = null;
        String lineUserInput = null;
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

        while (true) {
            System.out.print(CMD_PROMPT);
            // 读取输入，input中的方法
            line = input.readLine();
            // 根据用户输入的命令，执行相应的操作
            switch (parseCmd(line)) {
                case SelectAll:
                    // 查询当前数据库中所有语句
                    SelectAllFunc();
                    break;
                case Insert:
                    // 插入一条语句
                    System.out.print(FILE_INPUT_PROMPT);
                    lineUserInput = input.readLine();
                    InsertFunc(lineUserInput);
                    break;
                case DeleteById:
                    // 删除一条语句
                    System.out.print(INPUT_PROMPT);
                    lineUserInput = input.readLine();
                    DeleteByIdFunc(lineUserInput);
                    break;
                case DeleteAll:
                    // 清空数据库内容
                    DeleteAllFunc();
                    break;
                case KWIC:
                    // 对当前数据库内容执行KWIC操作
                    KWICFunc();
                    break;
                case Return:
                    // 返回上一级菜单
                    return;
                default:
                    System.out.println("%输入错误，请重新输入！%");
            }
        }
    }

    @Override
    public void SelectAllFunc() {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        JdbcMapper mapper = sqlSession.getMapper(JdbcMapper.class);
        List<Jdbc> jdbcs = mapper.selectAll();
        if (jdbcs.size() == 0) {
            System.out.println("%当前数据库为空！%");
        }
        else {
            System.out.println("~当前数据库中所有语句如下：");
            //jdbcs.forEach(jdbc -> System.out.println(jdbc));
            for (Jdbc jdbc : jdbcs) {
                System.out.println(jdbc);
            }
        }
    }

    @Override
    public void InsertFunc(String lineUserInput) {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 面向接口获取接口的代理对象
        JdbcMapper mapper = sqlSession.getMapper(JdbcMapper.class);
        Jdbc jdbc = new Jdbc(null, lineUserInput);
        int count = mapper.insert(jdbc);
        if (count == 1) {
            System.out.println("%插入成功！%");
        }
        else {
            System.out.println("%插入失败！%");
        }
        sqlSession.commit();
    }

    @Override
    public void DeleteByIdFunc(String lineUserInput) {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        JdbcMapper mapper = sqlSession.getMapper(JdbcMapper.class);
        // 将字符串转换为Long类型
        Long id = Long.parseLong(lineUserInput);
        int count = mapper.deleteById(id);
        if (count == 1) {
            System.out.println("%删除成功！%");
        }
        else {
            System.out.println("%删除失败！%");
        }
        sqlSession.commit();
    }

    @Override
    public void DeleteAllFunc() {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        JdbcMapper mapper = sqlSession.getMapper(JdbcMapper.class);
        int count = mapper.deleteAll();
        if (count >= 0) {
            System.out.println("%清空成功！%");
        }
        else {
            System.out.println("%清空失败！%");
        }
        sqlSession.commit();
    }

    @Override
    public void KWICFunc() {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        JdbcMapper mapper = sqlSession.getMapper(JdbcMapper.class);
        List<Jdbc> jdbcs = mapper.selectAll();
        if (jdbcs.size() == 0) {
            System.out.println("%当前数据库为空！%");
        }
        else {
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

            // 将数据库所有语句进行KWIC操作
            for (Jdbc jdbc : jdbcs) {
                //调用setup方法读取改行，这一步将所有的循环移位存储到shifter创建的LineStorageImpl对象中
                shifter.setup(jdbc.getLine());
            }

            // 接下来将shifter中LineStorageImpl对象中的内容进行排序，排序后的结果存放在alphabetizer创建的LineStorageImpl对象中
            alphabetizer.alpha(shifter);

            // 将排序后的结果输出到控制台
            output.WritetoDatebase(alphabetizer);

            //清除shifter中的内容
            shifter.clear();
        }
    }
}
