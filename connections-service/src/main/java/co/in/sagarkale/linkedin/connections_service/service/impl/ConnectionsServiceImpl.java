package co.in.sagarkale.linkedin.connections_service.service.impl;

import co.in.sagarkale.linkedin.connections_service.auth.UserContextHolder;
import co.in.sagarkale.linkedin.connections_service.dto.PersonCreateReqDto;
import co.in.sagarkale.linkedin.connections_service.entity.Person;
import co.in.sagarkale.linkedin.connections_service.event.AcceptConnectionRequestEvent;
import co.in.sagarkale.linkedin.connections_service.event.SendConnectionRequestEvent;
import co.in.sagarkale.linkedin.connections_service.repository.ConnectionRepository;
import co.in.sagarkale.linkedin.connections_service.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsServiceImpl implements ConnectionsService {

    private final ConnectionRepository connectionRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendRequestKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptRequestKafkaTemplate;


    @Override
    public List<Person> getFirstDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        return connectionRepository.getFirstDegreeConnections(userId);
    }

    @Override
    public Person createPerson(PersonCreateReqDto personCreateReqDto) {
        Optional<Person> personOptional = connectionRepository.findByUserId(personCreateReqDto.getUserId());
        if(personOptional.isPresent()){
            throw new RuntimeException("User already exists with id: "+personCreateReqDto.getUserId());
        }

        Person person = modelMapper.map(personCreateReqDto, Person.class);
        return connectionRepository.save(person);
    }

   public Boolean sendConnectionRequest(Long receiverId) {
        Long senderId = UserContextHolder.getCurrentUserId();
        log.info("Trying to send connection request, sender: {}, reciever: {}", senderId, receiverId);

        if(senderId.equals(receiverId)) {
            throw new RuntimeException("Both sender and receiver are the same");
        }

        boolean alreadySentRequest = connectionRepository.connectionRequestExists(senderId, receiverId);
        if (alreadySentRequest) {
            throw new RuntimeException("Connection request already exists, cannot send again");
        }

        boolean alreadyConnected = connectionRepository.alreadyConnected(senderId, receiverId);
        if(alreadyConnected) {
            throw new RuntimeException("Already connected users, cannot add connection request");
        }

        log.info("Successfully sent the connection request");
       connectionRepository.addConnectionRequest(senderId, receiverId);

        SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        sendRequestKafkaTemplate.send("send-connection-request-topic", sendConnectionRequestEvent);

        return true;
    }


    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        boolean connectionRequestExists = connectionRepository.connectionRequestExists(senderId, receiverId);
        if (!connectionRequestExists) {
            throw new RuntimeException("No connection request exists to accept");
        }

        connectionRepository.acceptConnectionRequest(senderId, receiverId);
        log.info("Successfully accepted the connection request, sender: {}, receiver: {}", senderId, receiverId);

        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        acceptRequestKafkaTemplate.send("accept-connection-request-topic", acceptConnectionRequestEvent);
        return true;
    }

    public Boolean rejectConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        boolean connectionRequestExists = connectionRepository.connectionRequestExists(senderId, receiverId);
        if (!connectionRequestExists) {
            throw new RuntimeException("No connection request exists, cannot delete");
        }

        connectionRepository.rejectConnectionRequest(senderId, receiverId);
        return true;
    }
}
