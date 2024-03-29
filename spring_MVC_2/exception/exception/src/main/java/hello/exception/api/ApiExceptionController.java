package hello.exception.api;

import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class ApiExceptionController {

    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1(){
        throw new BadRequestException();
    }

    @GetMapping("/api/default-handler-ex")
    public String defaultException(@RequestParam(name="data") Integer data){ //스프링 부트 3.2부터는 name을 필수적으로 명시
        return "ok";
    }

    @GetMapping("/api/response-status-ex2")
    public String responseStatusEx2(){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
    }

    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id){
        if(id.equals("ex")){
            throw new RuntimeException("잘못된 사용자");
        }
        if(id.equals("bad")){
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if(id.equals("user-ex")){
            throw new UserException("사용자 오류");
        }
        return new MemberDto(id,"hello "+id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String memberId;
        private String name;
    }
}
