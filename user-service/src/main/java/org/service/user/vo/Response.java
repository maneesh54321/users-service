package org.service.user.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class Response {
    @NonNull
    private String message;

    @NonNull
    private HttpStatus status;

    private Map<String, Object> data;

    public void addData(String key, Object value){
        if(data == null){
            data = new HashMap<>();
        }
        data.put(key, value);
    }

    public Integer getStatus(){
        return status.value();
    }
}
