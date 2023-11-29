import jdbc.mapper.JdbcMapper;
import jdbc.pojo.Jdbc;
import jdbc.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class JdbcMapperTest {
    @Test
    public void testInsert() {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // 面向接口获取接口的代理对象
        JdbcMapper mapper = sqlSession.getMapper(JdbcMapper.class);
        Jdbc jdbc = new Jdbc(null, "ok world");
        int count = mapper.insert(jdbc);
        System.out.println(count);
        sqlSession.commit();
    }

    @Test
    public void testSelectAll(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        JdbcMapper mapper = sqlSession.getMapper(JdbcMapper.class);
        List<Jdbc> jdbcs = mapper.selectAll();
        jdbcs.forEach(jdbc -> System.out.println(jdbc));
    }

    @Test
    public void testSelectById(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        JdbcMapper mapper = sqlSession.getMapper(JdbcMapper.class);
        Jdbc jdbc = mapper.selectById(1L);
        System.out.println(jdbc);
    }

    @Test
    public void testDeleteById(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        JdbcMapper mapper = sqlSession.getMapper(JdbcMapper.class);
        int count = mapper.deleteById(1L);
        System.out.println(count);
        sqlSession.commit();
    }

    @Test

    public void testDeleteAll(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        JdbcMapper mapper = sqlSession.getMapper(JdbcMapper.class);
        int count = mapper.deleteAll();
        System.out.println(count);
        sqlSession.commit();
    }
}
