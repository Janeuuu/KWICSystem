package jdbc;

public interface JdbcFunc {
    void JdbcService();
    void SelectAllFunc();
    void InsertFunc(String lineUserInput);
    void DeleteByIdFunc(String lineUserInput);
    void DeleteAllFunc();
    void KWICFunc();
    int parseCmd(String cmd);
}
