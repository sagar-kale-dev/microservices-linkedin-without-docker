package co.in.sagarkale.linkedin.posts_service.client;

import co.in.sagarkale.linkedin.posts_service.advice.ApiResponse;
import co.in.sagarkale.linkedin.posts_service.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "connections-service", path = "/connections")
public interface ConnectionsClient {

    @GetMapping("/connection/first-degree")
    ApiResponse<List<PersonDto>> getAllFirstDegreeConn();

}
