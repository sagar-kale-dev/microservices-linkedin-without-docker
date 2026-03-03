package co.in.sagarkale.linkedin.connections_service.controller;

import co.in.sagarkale.linkedin.connections_service.dto.CreateConnReqDto;
import co.in.sagarkale.linkedin.connections_service.dto.PersonCreateReqDto;
import co.in.sagarkale.linkedin.connections_service.entity.Person;
import co.in.sagarkale.linkedin.connections_service.service.ConnectionsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/connection")
@RequiredArgsConstructor
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody PersonCreateReqDto personCreateReqDto){
        Person person = connectionsService.createPerson(personCreateReqDto);
        return ResponseEntity.ok(person);
    }

    @GetMapping("/first-degree")
    public ResponseEntity<List<Person>> getAllFirstDegreeConn(HttpServletRequest httpServletRequest){
        List<Person> personList = connectionsService.getFirstDegreeConnections();
        return ResponseEntity.ok(personList);
    }

    @PostMapping("/request/{userId}")
    public ResponseEntity<Boolean> sendConnectionRequest(@PathVariable Long userId) {
        return ResponseEntity.ok(connectionsService.sendConnectionRequest(userId));
    }

    @PostMapping("/accept/{userId}")
    public ResponseEntity<Boolean> acceptConnectionRequest(@PathVariable Long userId) {
        return ResponseEntity.ok(connectionsService.acceptConnectionRequest(userId));
    }

    @PostMapping("/reject/{userId}")
    public ResponseEntity<Boolean> rejectConnectionRequest(@PathVariable Long userId) {
        return ResponseEntity.ok(connectionsService.rejectConnectionRequest(userId));
    }
}
