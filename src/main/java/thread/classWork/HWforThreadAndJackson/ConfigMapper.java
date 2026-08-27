package thread.classWork.HWforThreadAndJackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class ConfigMapper {
    private ObjectMapper mapper = JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT).build();;

    private ConfigMapper(){}

    public  ObjectMapper getMapper(){
        return mapper;
    }
}
