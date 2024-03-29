package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class UncheckedTest {

    @Test
    void unchecked_catch(){
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unchecked_throw(){
        Service service = new Service();
        assertThatThrownBy(()->service.callThrow()).isInstanceOf(MyUncheckedException.class);
    }

    static class MyUncheckedException extends RuntimeException{
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    static class Repository{
        public void call(){
            throw new MyUncheckedException("ex");
        }
    }

    static class Service{

        Repository repository = new Repository();

        public void callCatch(){
            try {
                repository.call();
            }catch (MyUncheckedException e){
                log.info("예외 처리, message={}", e.getMessage(), e);
            }
        }

        /**
         * MyUncheckedException는 언체크 예외라 메소드에 throws 생략함.
         */
        public void callThrow(){
            repository.call();
        }
    }

}
