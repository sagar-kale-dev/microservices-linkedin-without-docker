package co.in.sagarkale.linkedin.notification_service.client;

import co.in.sagarkale.linkedin.notification_service.advice.ApiResponse;
import co.in.sagarkale.linkedin.notification_service.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "connections-service", path = "/connections")
public interface ConnectionsClient {

    @GetMapping("/connection/first-degree")
    ApiResponse<List<PersonDto>> getAllFirstDegreeConn(@RequestHeader("X-User-Id") Long userId);

}
