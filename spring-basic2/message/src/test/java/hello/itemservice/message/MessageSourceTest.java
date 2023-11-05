package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource messageSource;

    @Test
    public void helloMessage() throws Exception {
        //given
        String result = messageSource.getMessage("hello", null, null);
        //when

        //then
        assertThat(result).isEqualTo("안녕");
    }

    @Test
    public void notFoundMessageCode() throws Exception {
        //given

        //when

        //then
        assertThatThrownBy(() -> messageSource.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    public void notFoundMessageCodeDefaultMessage() throws Exception {
        //given

        //when
        // 메시지를 찾지못하면 default meassge를 찾아서 준다.
        String result = messageSource.getMessage("no_code", null, "기본 메시지", null);
        //then
        assertThat(result).isEqualTo("기본 메시지");
    }

    @Test
    public void argumentMessage() throws Exception {
        //given

        //when
        String message = messageSource.getMessage("hello.name", new Object[]{"Spring"}, null);
        assertThat(message).isEqualTo("안녕 Spring");
        //then
    }

    @Test
    public void defaultLang() throws Exception {
        //given

        //when

        //then
        assertThat(messageSource.getMessage("hello", null, null)).isEqualTo("안녕");
        assertThat(messageSource.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
    }
    
    @Test
    public void enLang() throws Exception {
        assertThat(messageSource.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
        //given
        
        //when
        
        //then
    }
}
