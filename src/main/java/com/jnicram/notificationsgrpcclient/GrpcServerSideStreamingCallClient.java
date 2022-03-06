package com.jnicram.notificationsgrpcclient;

import com.jnicram.notification.NotificationServiceGrpc;
import com.jnicram.notification.NotifyRequest;
import com.jnicram.notification.NotifyResponse;
import io.grpc.ManagedChannel;
import java.util.Iterator;

public class GrpcServerSideStreamingCallClient {

  public static void main(String[] args) {
    ManagedChannel channel = ClientHelper.getGrpcChannel();
    NotificationServiceGrpc.NotificationServiceBlockingStub stub = NotificationServiceGrpc.newBlockingStub(channel);

    NotifyRequest notifyRequest = NotifyRequest.newBuilder().setUsername("username").build();
    Iterator<NotifyResponse> responseIterator = stub.serverSideStreaming(notifyRequest);
    while (responseIterator.hasNext()) {
      NotifyResponse response = responseIterator.next();
      System.out.println("Response received from server:\n" + response);
    }

    channel.shutdown();
  }
}
