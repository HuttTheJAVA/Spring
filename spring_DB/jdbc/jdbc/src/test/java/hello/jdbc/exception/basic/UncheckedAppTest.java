package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class UncheckedAppTest {

    @Test
    void Unchecked(){
        Controller controller = new Controller();
        assertThatThrownBy(()->controller.request()).isInstanceOf(Exception.class);
    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
            //e.printStackTrace();
            log.info("ex", e);
        }
    }
    static class NetworkClient{
        void call(){
            throw new RuntimeConnectionException("연결 실패");
        }
    }

    static class Controller{
        Service service = new Service();
        public void request(){
            service.logic();
        }
    }

    static class Service{
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();
        public void logic(){
            repository.call();
            networkClient.call();
        }
    }

    static class RuntimeSQLException extends RuntimeException{
        public RuntimeSQLException(){

        }

        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }

    static class RuntimeConnectionException extends RuntimeException{
        public RuntimeConnectionException(String message) {
            super(message);
        }
    }
    static class Repository{
        void call(){
            try{
                runSQL();
            }catch (SQLException e){
                throw new RuntimeSQLException(e);
            }
        }

        private void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }



}
