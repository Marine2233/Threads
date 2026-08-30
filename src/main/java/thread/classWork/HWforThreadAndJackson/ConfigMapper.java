package thread.classWork.HWforThreadAndJackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class ConfigMapper {

    private ConfigMapper(){}

    public static ObjectMapper getMapper(){
        ObjectMapper mapper =
                JsonMapper.
                        builder().
                        enable(SerializationFeature.INDENT_OUTPUT).
                        enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES).
                        build();
        return mapper;
    }
}
