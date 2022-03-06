package com.jnicram.notificationsgrpcserver;

import com.jnicram.notification.NotifyResponse;
import com.jnicram.notification.NotifyResponse.Status;
import com.jnicram.notification.NotificationServiceGrpc;
import com.jnicram.notification.NotifyRequest;
import com.jnicram.notification.PingRequest;
import com.jnicram.notification.PingResponse;
import io.grpc.stub.StreamObserver;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
public class NotificationsGrpcServer extends NotificationServiceGrpc.NotificationServiceImplBase {

  @Override
  public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
    log.info("[ping] Payload received : " + request.getEchoMessage());
    PingResponse response = PingResponse.newBuilder()
        .setEchoMessage(request.getEchoMessage())
        .setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void serverSideStreaming(NotifyRequest request, StreamObserver<NotifyResponse> responseObserver) {
    log.info("[serverSideStreaming] Payload received : " + request.getUsername());

    sendSimpleData(responseObserver);
    responseObserver.onCompleted();
  }

  @Override
  public StreamObserver<NotifyRequest> bidirectionalStreaming(StreamObserver<NotifyResponse> responseObserver) {
    return new StreamObserver<>() {

      @Override
      public void onNext(NotifyRequest request) {
        log.info("[bidirectionalStreaming] Payload received : " + request.getUsername());
        sendSimpleData(responseObserver);
      }

      @Override
      public void onCompleted() {
        responseObserver.onCompleted();
      }

      @Override
      public void onError(Throwable error) {
        log.error("[bidirectionalStreaming] error: " + error.getMessage(), error);
      }
    };
  }

  private void sendSimpleData(StreamObserver<NotifyResponse> responseObserver) {
    IntStream.rangeClosed(1, 100)
        .mapToObj(i -> NotifyResponse.newBuilder()
            .setMessage("Test message: " + i)
            .setStatus(Status.STATUS_B)
            .addAllData(Arrays.asList("A", "B", "C", "D"))
            .build())
        .forEach(request -> {
          try {
            Thread.sleep(10_000);
            responseObserver.onNext(request);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        });
  }
}
