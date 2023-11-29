package jdbc.mapper;

import jdbc.pojo.Jdbc;

import java.util.List;

public interface JdbcMapper {
    //插入一条数据
    int insert(Jdbc jdbc);
    //删除一条数据
    int deleteById(Long id);
    //删除所有数据
    int deleteAll();
    //查询一条数据
    Jdbc selectById(Long id);
    //查询所有数据
    List<Jdbc> selectAll();
}
