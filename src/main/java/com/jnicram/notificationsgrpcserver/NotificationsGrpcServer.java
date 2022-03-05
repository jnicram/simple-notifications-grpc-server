package com.jnicram.notificationsgrpcserver;

import com.jnicram.notification.Message;
import com.jnicram.notification.Message.Status;
import com.jnicram.notification.NotificationServiceGrpc;
import com.jnicram.notification.NotifyRequest;
import com.jnicram.notification.PingRequest;
import com.jnicram.notification.PingResponse;
import io.grpc.stub.StreamObserver;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
public class NotificationsGrpcServer extends NotificationServiceGrpc.NotificationServiceImplBase {

  @Override
  public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
    log.info("[PING] Payload received : " + request.getEchoMessage());
    PingResponse response = PingResponse.newBuilder()
        .setEchoMessage(request.getEchoMessage())
        .setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void notify(NotifyRequest request, StreamObserver<Message> responseObserver) {
    log.info("[NOTIFY] Payload received : " + request.getUsername());

    int index  = 100;
    IntStream.rangeClosed(1, index)
        .mapToObj(i -> Message.newBuilder()
            .setMessage("Test message: " + i)
            .setStatus(Status.STATUS_B)
            .setData(i, "Some data " + i)
            .build())
        .forEach(responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
