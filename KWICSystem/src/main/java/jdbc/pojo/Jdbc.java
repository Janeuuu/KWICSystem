package jdbc.pojo;

import java.util.Objects;

// 数据库表当中的字段应该和pojo类的属性一一对应。
// kwic数据库对应对象
public class Jdbc {
    private Long id;
    private String line;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jdbc jdbc = (Jdbc) o;
        return Objects.equals(id, jdbc.id) && Objects.equals(line, jdbc.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line);
    }

    @Override
    public String toString() {
        return "Jdbc{" +
                "id=" + id +
                ", line='" + line + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public Jdbc(Long id, String line) {
        this.id = id;
        this.line = line;
    }

    public Jdbc() {
    }
}
