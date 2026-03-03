package co.in.sagarkale.linkedin.connections_service.service;

import co.in.sagarkale.linkedin.connections_service.dto.PersonCreateReqDto;
import co.in.sagarkale.linkedin.connections_service.entity.Person;

import java.util.List;

public interface ConnectionsService {
    public List<Person> getFirstDegreeConnections();
    public Person createPerson(PersonCreateReqDto personCreateReqDto);
    public Boolean sendConnectionRequest(Long receiverId);
    public Boolean acceptConnectionRequest(Long senderId);
    public Boolean rejectConnectionRequest(Long senderId);
}
