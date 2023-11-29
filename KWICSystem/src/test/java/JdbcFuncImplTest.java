import jdbc.JdbcFunc;
import jdbc.impl.JdbcFuncImpl;
import org.junit.Test;

public class JdbcFuncImplTest {
    @Test
    public void testJdbcService() {
        JdbcFunc jdbcFuncImpl = new JdbcFuncImpl();
        jdbcFuncImpl.JdbcService();
    }
}
