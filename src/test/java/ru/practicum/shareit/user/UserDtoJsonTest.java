package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class UserDtoJsonTest {
    @Autowired
    private JacksonTester<RequestUserDto> jsonRequest;
    @Autowired
    private JacksonTester<ResponseUserDto> jsonResponse;

    private final long userId = 10L;
    private final String userName = "userUser";
    private final String userMail = "user@mail.com";
    private final RequestUserDto requestUserDto = RequestUserDto.builder().name(userName).email(userMail).build();
    private final ResponseUserDto responseUserDto = ResponseUserDto.builder().id(userId).name(userName)
            .email(userMail).build();

    @Test
    void testResponseUserDto() throws Exception {
        JsonContent<ResponseUserDto> result = jsonResponse.write(responseUserDto);

        assertThat(result).extractingJsonPathNumberValue("$.id", Long.class).isEqualTo(10);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(responseUserDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(responseUserDto.getEmail());
    }

    @Test
    void testRequestUserDto() throws Exception {
        String jsonString = "{\"name\": \"userUser\", " +
                            "\"email\": \"user@mail.com\"}";

        RequestUserDto result = jsonRequest.parseObject(jsonString);
        assertThat(result).isEqualTo(requestUserDto);
    }

}