package com.jnicram.notificationsgrpcclient;

import com.jnicram.notification.NotificationServiceGrpc;
import com.jnicram.notification.PingRequest;
import com.jnicram.notification.PingResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.stream.IntStream;

public class GrpcPingClient {

  public static void main(String[] args) {
    ManagedChannel channel = ClientHelper.getGrpcChannel();
    NotificationServiceGrpc.NotificationServiceBlockingStub stub = NotificationServiceGrpc.newBlockingStub(channel);

    IntStream.rangeClosed(1, 5)
        .mapToObj(i -> PingRequest.newBuilder()
            .setEchoMessage("TEST - " + i)
            .build())
        .forEach(request -> {
          try {
            Thread.sleep(10_000);
            PingResponse response = stub.ping(request);
            System.out.println("Response received from server:\n" + response);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        });

    channel.shutdown();
  }
}
